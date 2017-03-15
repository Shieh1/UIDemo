package com.zysm.curtain.mwidgetgroup.mrefreshlistview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.zysm.curtain.mwidgetgroup.R;
import com.zysm.curtain.mwidgetgroup.util.DensityUtils;
import com.zysm.curtain.mwidgetgroup.util.LogUtils;

/**
 * Time:2017/3/15 13:57
 * Created by Curtain.
 */

public class MRefreshListView extends ListView implements AbsListView.OnScrollListener{

    /**modify*/

    private float mDownRowX;
    private float mDownRowY;

    /**swipeList*/

    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;

    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = -1;

    private SwipeMenuLayout mTouchView;
    private int mTouchPosition;
    private float mDownX;//按下时候的相对控件的x坐标
    private float mDownY;//按下时候的相对控件的y坐标
    private int mTouchState;
    private int mDirection = 1;//swipe from right to left by default
    private OnSwipeListener mOnSwipeListener;
    private SwipeMenuCreator mMenuCreator;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private OnMenuStateChangeListener mOnMenuStateChangeListener;
    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;

    /**refreshList*/
    private Context mContext;
    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private IMListViewListener mListViewListener;

    /**头部布局*/
    private MListViewHeader headView;

    // 头部布局容器，用于控制计算高度，显示和隐藏
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // 头部布局的高度

    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.

    /**底部布局*/
    private MListViewFooter mFooterView;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.

    /**是否侧滑*/
    private boolean mSideEnable = false;

    public MRefreshListView(Context context) {
       this(context,null);
    }

    public MRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置滑动监听
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        //初始化头布局
        initHeaderView(context);

        //初始化为尾布局
        initFooterView(context);
    }


    /**初始化头部布局*/
    private void initHeaderView(Context context){
        headView = new MListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) headView
                .findViewById(R.id.m_listview_header_content);
        mHeaderTimeView = (TextView) headView
                .findViewById(R.id.m_listview_header_time);
        addHeaderView(headView);
        // init header height
        headView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }else{
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }

    /**初始化为尾布局*/
    private void initFooterView(Context context){
        mFooterView = new MListViewFooter(context);

    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // 确保底部view只被添加了一次.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView,null,true);
            this.setFooterDividersEnabled(false);
        }
        super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
            @Override
            public void createMenu(SwipeMenu menu) {
                if (mMenuCreator != null) {
                    mMenuCreator.create(menu);
                }
            }

            @Override
            public void onItemClick(SwipeMenuView view, SwipeMenu menu,
                                    int index) {
                boolean flag = false;
                if (mOnMenuItemClickListener != null) {
                    flag = mOnMenuItemClickListener.onMenuItemClick(
                            view.getPosition(), menu, index);
                }
                if (mTouchView != null && !flag) {
                    mTouchView.smoothCloseMenu();
                }
            }
        });
    }

    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }


    /**
     * 打开或者关闭侧滑菜单
     *
     * @param enable
     */
    private int MAX_Y = 5;
    private int MAX_X = 3;
    public void setSideMenuEnable(boolean enable) {
        mSideEnable = enable;
        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        mTouchState = TOUCH_STATE_NONE;
    }

    /**
     * 打开或者关闭下拉刷新
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 打开或者关闭上拉加载更多
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(MListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setState(MListViewFooter.STATE_NORMAL);
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        headView.setVisiableHeight((int) delta
                + headView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (headView.getVisiableHeight() > mHeaderViewHeight) {
                headView.setState(MListViewHeader.STATE_READY);
            } else {
                headView.setState(MListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }


    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = headView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
                // more.
                mFooterView.setState(MListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(MListViewFooter.STATE_NORMAL);
            }
        }
        mFooterView.setBottomMargin(height);

        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
                    SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(MListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //在拦截处处理，在滑动设置了点击事件的地方也能swip，点击时又不能影响原来的点击事件
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                boolean handled = super.onInterceptTouchEvent(ev);
                mTouchState = TOUCH_STATE_NONE;
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                //只在空的时候赋值 以免每次触摸都赋值，会有多个open状态
                if (view instanceof SwipeMenuLayout) {
                    //如果有打开了 就拦截.
                    if (mTouchView != null && mTouchView.isOpen() && !inRangeOfView(mTouchView.getMenuView(), ev)) {
                        return true;
                    }
                    mTouchView = (SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);
                }
                //如果摸在另外个view
                if (mTouchView != null && mTouchView.isOpen() && view != mTouchView) {
                    handled = true;
                }

                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                return handled;
            case MotionEvent.ACTION_MOVE:

                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (Math.abs(dy) > MAX_Y || Math.abs(dx) > MAX_X) {
                    //每次拦截的down都把触摸状态设置成了TOUCH_STATE_NONE 只有返回true才会走onTouchEvent 所以写在这里就够了
                    if (mTouchState == TOUCH_STATE_NONE) {
                        if (Math.abs(dy) > MAX_Y) {
                            mTouchState = TOUCH_STATE_Y;
                        } else if (dx > MAX_X) {
                            mTouchState = TOUCH_STATE_X;
                            if (mOnSwipeListener != null) {
                                mOnSwipeListener.onSwipeStart(mTouchPosition);
                            }
                        }
                    }
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private float lastRowX;//记录上次的位置，用于move不松开时判断如何滑动
    private float lastRowY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
            return super.onTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownRowX = ev.getRawX();
                mDownRowY = ev.getRawY();
                lastRowX = mDownRowX;
                lastRowY = mDownRowY;
                LogUtils.d("mDownRowX="+mDownRowX);
                LogUtils.d("mDownRowY="+mDownRowY);
                mLastY = ev.getRawY();
                if(mSideEnable) {
                    int oldPos = mTouchPosition;
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    mTouchState = TOUCH_STATE_NONE;

                    mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

                    if (mTouchPosition == oldPos && mTouchView != null
                            && mTouchView.isOpen()) {
                        mTouchState = TOUCH_STATE_X;
                        mTouchView.onSwipe(ev);
                        return true;
                    }

                    View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                    if (mTouchView != null && mTouchView.isOpen()) {
                        mTouchView.smoothCloseMenu();
                        mTouchView = null;
                        // return super.onTouchEvent(ev);
                        // try to cancel the touch event
                        MotionEvent cancelEvent = MotionEvent.obtain(ev);
                        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                        onTouchEvent(cancelEvent);
                        if (mOnMenuStateChangeListener != null) {
                            mOnMenuStateChangeListener.onMenuClose(oldPos);
                        }
                        return true;
                    }
                    if (view instanceof SwipeMenuLayout) {
                        mTouchView = (SwipeMenuLayout) view;
                        mTouchView.setSwipeDirection(mDirection);
                    }
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                float divy = ev.getRawY() - mDownRowY;
                float divx = ev.getRawX() - mDownRowX;
//                LogUtils.d("divy = " + divy);
//                LogUtils.d("divx = " + divx);
                if( ev.getRawY() > lastRowY){
                    //下拉的幅度大于左右拉的幅度，表示是下拉刷新
                    LogUtils.d("down");
                }else if( ev.getRawY() < lastRowY){
                    //表示是上拉
                    LogUtils.d("up");
                }else if( ev.getRawX() < lastRowX){
                    //向左滑动
                    LogUtils.d("left");
                }else if( ev.getRawX() > lastRowX){
                    //向右滑动
                    LogUtils.d("right");
                }

                    final float deltaY = ev.getRawY() - mLastY;
                    mLastY = ev.getRawY();
                    if (getFirstVisiblePosition() == 0
                            && (headView.getVisiableHeight() > 0 || deltaY > 0)) {
                        // the first item is showing, header has shown or pull down.
                        updateHeaderHeight(deltaY / OFFSET_RADIO);
                        invokeOnScrolling();
                    } else if (getLastVisiblePosition() == mTotalItemCount - 1
                            && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                        // last item, already pulled up or want to pull up.
                        updateFooterHeight(-deltaY / OFFSET_RADIO);
                    }

                if(mSideEnable){
                    //有些可能有header,要减去header再判断
                    mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY()) - getHeaderViewsCount();
                    //如果滑动了一下没完全展现，就收回去，这时候mTouchView已经赋值，再滑动另外一个不可以swip的view
                    //会导致mTouchView swip 。 所以要用位置判断是否滑动的是一个view
                    if (!mTouchView.getSwipEnable() || mTouchPosition != mTouchView.getPosition()) {
                        break;
                    }
                    float dy = Math.abs((ev.getY() - mDownY));
                    float dx = Math.abs((ev.getX() - mDownX));
                    if (mTouchState == TOUCH_STATE_X) {
                        if (mTouchView != null) {
                            mTouchView.onSwipe(ev);
                        }
                        getSelector().setState(new int[]{0});
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.onTouchEvent(ev);
                        return true;
                    } else if (mTouchState == TOUCH_STATE_NONE) {
                        if (Math.abs(dy) > MAX_Y) {
                            mTouchState = TOUCH_STATE_Y;
                        } else if (dx > MAX_X) {
                            mTouchState = TOUCH_STATE_X;
                            if (mOnSwipeListener != null) {
                                mOnSwipeListener.onSwipeStart(mTouchPosition);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mSideEnable){
                    if (mTouchState == TOUCH_STATE_X) {
                        if (mTouchView != null) {
                            boolean isBeforeOpen = mTouchView.isOpen();
                            mTouchView.onSwipe(ev);
                            boolean isAfterOpen = mTouchView.isOpen();
                            if (isBeforeOpen != isAfterOpen && mOnMenuStateChangeListener != null) {
                                if (isAfterOpen) {
                                    mOnMenuStateChangeListener.onMenuOpen(mTouchPosition);
                                } else {
                                    mOnMenuStateChangeListener.onMenuClose(mTouchPosition);
                                }
                            }
                            if (!isAfterOpen) {
                                mTouchPosition = -1;
                                mTouchView = null;
                            }
                        }
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeEnd(mTouchPosition);
                        }
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.onTouchEvent(ev);
                        return true;
                    }
                }
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh
                            && headView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        headView.setState(MListViewHeader.STATE_REFRESHING);
                        if (mListViewListener != null) {
                            mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }
                if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    if (mEnablePullLoad
                            && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                headView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    public void setMListViewListener(IMListViewListener l) {
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IMListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }

    public void smoothOpenMenu(int position) {
        if (position >= getFirstVisiblePosition()
                && position <= getLastVisiblePosition()) {
            View view = getChildAt(position - getFirstVisiblePosition());
            if (view instanceof SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.setSwipeDirection(mDirection);
                mTouchView.smoothOpenMenu();
            }
        }
    }

    public void smoothCloseMenu(){
        if (mTouchView != null && mTouchView.isOpen()) {
            mTouchView.smoothCloseMenu();
        }
    }

    public void setMenuCreator(SwipeMenuCreator menuCreator) {
        this.mMenuCreator = menuCreator;
    }

    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
    }

    public void setOnMenuStateChangeListener(OnMenuStateChangeListener onMenuStateChangeListener) {
        mOnMenuStateChangeListener = onMenuStateChangeListener;
    }

    public static interface OnMenuItemClickListener {
        boolean onMenuItemClick(int position, SwipeMenu menu, int index);
    }

    public static interface OnSwipeListener {
        void onSwipeStart(int position);

        void onSwipeEnd(int position);
    }

    public static interface OnMenuStateChangeListener {
        void onMenuOpen(int position);

        void onMenuClose(int position);
    }

    public void setSwipeDirection(int direction) {
        mDirection = direction;
    }

    /**
     * 判断点击事件是否在某个view内
     *
     * @param view
     * @param ev
     * @return
     */
    public static boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }
}
