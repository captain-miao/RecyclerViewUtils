package com.github.learn.refreshandload.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.captain_miao.recyclerviewutils.BaseHeaderRecyclerAdapter;
import com.github.captain_miao.recyclerviewutils.common.ClickableViewHolder;
import com.github.captain_miao.recyclerviewutils.listener.OnRecyclerItemClickListener;
import com.github.learn.refreshandload.R;

import java.util.List;

/**
 * @author YanLu
 * @since 15/9/15
 */
public class SimpleHeaderAdapter extends BaseHeaderRecyclerAdapter<String, RecyclerView.ViewHolder> implements OnRecyclerItemClickListener {

    public SimpleHeaderAdapter(List<String> items) {
        appendToList(items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new SimpleHeaderAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) vh;
            viewHolder.mTvContent.setText(getItem(position));
        }
    }

    @Override
    public void onClick(View v, int position) {
        switch (v.getId()){
            case R.id.tv_content:
                Toast.makeText(v.getContext(), "on click " + position, Toast.LENGTH_SHORT).show();
                break;
            default:
                //mock click todo  last item
                int index = (int) getItemId(position);
                remove(index);
                //notifyItemRemoved(position);
        }
    }


    public class ItemViewHolder extends ClickableViewHolder{
        public TextView mTvContent;

        public ItemViewHolder(View view) {
            super(view);
            mTvContent = (TextView) view.findViewById(R.id.tv_content);
            setOnRecyclerItemClickListener(SimpleHeaderAdapter.this);
            //view.setOnClickListener(this);
            //mTvContent.setOnClickListener(this);
            addOnItemViewClickListener();
            addOnViewClickListener(mTvContent);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvContent.getText();
        }
    }


    //自定义
    public int getFooterLayoutResource() {
        return R.layout.list_load_more;
    }
//    public int getFooterLoadingShowStringResource() {
//        return com.github.captain_miao.recyclerviewutils.R.string.app_loading_more;//loading_more
//    }
//    public int getFooterNoMoreDataShowStringResource() {
//        return com.github.captain_miao.recyclerviewutils.R.string.app_no_more_data;//loading_more
//    }
}
