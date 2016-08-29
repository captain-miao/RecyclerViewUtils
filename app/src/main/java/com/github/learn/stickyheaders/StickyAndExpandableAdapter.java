package com.github.learn.stickyheaders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.captain_miao.recyclerviewutils.stickyandexpandable.StickyAndExpandableRecyclerHeadersAdapter;
import com.github.learn.refreshandload.R;


/**
 * @author YanLu
 * @since 16/3/28
 */
public class StickyAndExpandableAdapter
        extends StickyAndExpandableRecyclerHeadersAdapter<DetectionModel, StickyAndExpandableAdapter.ItemViewHolder, StickyAndExpandableAdapter.HeaderItemViewHolder> {
    private static final String TAG = "VehicleDetectionAdapter";

    public StickyAndExpandableAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }


    @Override
    public int getContentViewType(int dataListIndex) {
        if(dataListIndex >= 0 && mItemList.size() > 0 && dataListIndex < mItemList.size()){
            DetectionModel model = mItemList.get(dataListIndex);
            return model.isParentItem() ? 1 : 0;
        } else {
            return 0;
        }
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_sticky_and_header, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_sticky_header_hide, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder holder, int position) {
        DetectionModel vo = getItem(position);
        holder.mTvTitle.setText(vo.title);
        holder.mTvValue.setText(vo.value);
        if (vo.isQualified) {
            holder.mTvValue.setTextColor(Color.parseColor("#999999"));
        } else {
            holder.mTvValue.setTextColor(Color.parseColor("#FF0000"));
        }
    }


    //Sticky Headers
    @Override
    public long getHeaderId(int position) {
        DetectionModel vo = getItem(position);
        return vo.getHeaderId();
    }

    @Override
    public HeaderItemViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.header_view_sticky_header, parent, false);
        return new HeaderItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderItemViewHolder holder, int position) {
        DetectionModel vo = getItem(position);
        holder.mTvTitle.setText(vo.category);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvTitle;
        public TextView mTvValue;

        public ItemViewHolder(View view) {
            super(view);
            mTvTitle = (TextView) view.findViewById(R.id.detail_title);
            mTvValue = (TextView) view.findViewById(R.id.detail_value);
        }
    }


    public class HeaderItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvTitle;

        public HeaderItemViewHolder(View view) {
            super(view);
            mTvTitle = (TextView) view.findViewById(R.id.detail_title);
        }
    }
}
