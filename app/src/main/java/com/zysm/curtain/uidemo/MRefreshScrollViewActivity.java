package com.zysm.curtain.uidemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import com.zysm.curtain.mwidgetgroup.mrefreshscrollview.PullToRefreshBase;
import com.zysm.curtain.mwidgetgroup.mrefreshscrollview.PullToRefreshScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MRefreshScrollViewActivity extends AppCompatActivity {

    @BindView(R.id.pull_to_refresh_scroll_view)
    PullToRefreshScrollView pullToRefreshScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrefresh_scroll_view);
        ButterKnife.bind(this);

        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
            }
        });

        pullToRefreshScrollView.setPullToRefreshOverScrollEnabled(false);
        pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//设置仅仅可以下拉，默认是上拉，下拉都可以刷新
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Do some stuff here

            // Call onRefreshComplete when the list has been refreshed.
            pullToRefreshScrollView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }


}
