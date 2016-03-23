package com.github.learn.permission;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.captain_miao.recyclerviewutils.BaseLoadMoreRecyclerAdapter;
import com.github.captain_miao.recyclerviewutils.common.ClickableViewHolder;
import com.github.captain_miao.recyclerviewutils.listener.OnRecyclerItemClickListener;
import com.github.learn.refreshandload.R;

import java.util.List;

/**
 * @author YanLu
 * @since 15/3/18
 */
public class PermissionAdapter extends BaseLoadMoreRecyclerAdapter<PermissionEntity, PermissionAdapter.ItemViewHolder>  implements OnRecyclerItemClickListener {

    private OnRecyclerItemClickListener itemClickListener;

    public PermissionAdapter(List<PermissionEntity> items, OnRecyclerItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
        appendToList(items);
    }


    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.permission_item_view, parent, false);

        return new PermissionAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(PermissionAdapter.ItemViewHolder vh, int position) {
        PermissionEntity entity = getItem(position);
        vh.mTvName.setText(entity.permissionName);
        vh.mTvLevel.setText(entity.permissionLevel);
        vh.mIvStatus.setImageResource(entity.getStatusIcon());
        vh.mTvStatus.setText(entity.permissionStatus.getStatus());
    }

    @Override
    public void onClick(View v, int position) {
        if(itemClickListener != null){
            itemClickListener.onClick(v, position);
        }
    }


    public class ItemViewHolder extends ClickableViewHolder{
        public TextView mTvName;
        public TextView mTvStatus;
        public TextView mTvLevel;
        public ImageView mIvStatus;

        public ItemViewHolder(View view) {
            super(view);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mTvLevel = (TextView) view.findViewById(R.id.tv_level);
            mIvStatus = (ImageView) view.findViewById(R.id.iv_status);
            mTvStatus = (TextView) view.findViewById(R.id.tv_status);
            setOnRecyclerItemClickListener(PermissionAdapter.this);
            addOnItemViewClickListener();
        }
    }

}
