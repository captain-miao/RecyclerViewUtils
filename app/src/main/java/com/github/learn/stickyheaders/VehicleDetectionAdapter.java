package com.github.learn.stickyheaders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.captain_miao.recyclerviewutils.common.ClickableViewHolder;
import com.github.captain_miao.recyclerviewutils.listener.OnRecyclerItemClickListener;
import com.github.learn.refreshandload.R;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.LinkedList;
import java.util.List;


/**
 * @author YanLu
 * @since 16/3/28
 */
public class VehicleDetectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
                                    implements StickyRecyclerHeadersAdapter<VehicleDetectionAdapter.HeaderItemViewHolder> {
    private static final String TAG = "VehicleDetectionAdapter";

    //数据相关
    private final List<DetectionVo> mList = new LinkedList<DetectionVo>();


    public VehicleDetectionAdapter() {
        setHasStableIds(true);
    }


    public DetectionVo getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addData(DetectionVo data) {
        int pos = getItemCount();
        mList.add(data);
        notifyItemInserted(pos);
    }

    public void addAllData(List<DetectionVo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    //item view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_sticky_header, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DetectionVo vo = getItem(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.mTvTitle.setText(vo.title);
        itemViewHolder.mTvValue.setText(vo.value);
        if (vo.isQualified) {
            itemViewHolder.mTvValue.setTextColor(Color.parseColor("#999999"));
        } else {
            itemViewHolder.mTvValue.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //Sticky Headers
    @Override
    public long getHeaderId(int position) {
        DetectionVo vo = getItem(position);
        return vo.categoryId;
    }

    @Override
    public HeaderItemViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.header_view_sticky_header, parent, false);
        return new HeaderItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderItemViewHolder holder, int position) {
        DetectionVo vo = getItem(position);
        holder.mTvTitle.setText(vo.category);
    }

    public class ItemViewHolder extends ClickableViewHolder implements OnRecyclerItemClickListener {
        public TextView mTvTitle;
        public TextView mTvValue;

        public ItemViewHolder(View view) {
            super(view);
            mTvTitle = (TextView) view.findViewById(R.id.detail_title);
            mTvValue = (TextView) view.findViewById(R.id.detail_value);
            addOnItemViewClickListener();
            setOnRecyclerItemClickListener(this);
        }

        @Override
        public void onClick(View v, int position) {
            Toast.makeText(v.getContext(), "on click " + position, Toast.LENGTH_SHORT).show();
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
