package com.zysm.curtain.uidemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;

/**
 * Created by Curtain on 2017/3/19.
 */

public class MRecyclerViewAdapter extends RecyclerView.Adapter<MRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;

    private String[] mTitles = null;

    public MRecyclerViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mTitles = new String[20];
        for (int i = 0; i < 20; ++i) {
            mTitles[i] = "item-" + i;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_tv.setText(mTitles[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_tv;
        public ViewHolder(View itemView) {
            super(itemView);
            item_tv = (TextView)itemView.findViewById(R.id.item_tv);
        }
    }
}
