package com.dahuo.learn.swiperefreshandload.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dahuo.learn.swiperefreshandload.R;
import com.dahuo.library.swiperefresh.BaseLoadMoreRecyclerAdapter;

import java.util.List;

/**
 * @author YanLu
 * @since 15/9/15
 */
public class SimpleAdapter extends BaseLoadMoreRecyclerAdapter<String, SimpleAdapter.ItemViewHolder> {

    public SimpleAdapter(List<String> items) {
        appendToList(items);
    }


    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new SimpleAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(SimpleAdapter.ItemViewHolder vh, int position) {
        vh.mTvContent.setText(getItem(position));
    }



    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvContent;

        public ItemViewHolder(View view) {
            super(view);
            mTvContent = (TextView) view.findViewById(R.id.tv_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvContent.getText();
        }
    }
}
