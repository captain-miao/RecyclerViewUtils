package com.github.learn.staggeredgrid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.captain_miao.recyclerviewutils.BaseWrapperRecyclerAdapter;
import com.github.captain_miao.recyclerviewutils.common.ClickableViewHolder;
import com.github.captain_miao.recyclerviewutils.listener.OnRecyclerItemClickListener;
import com.github.learn.refreshandload.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author YanLu
 * @since 16/4/23
 */
public class StaggeredGridAdapter extends BaseWrapperRecyclerAdapter<String, RecyclerView.ViewHolder> implements OnRecyclerItemClickListener {

    public StaggeredGridAdapter(List<String> items) {
        appendToList(items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staggered_grid_item, parent, false);

        return new StaggeredGridAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) vh;
            Picasso.with(viewHolder.itemView.getContext()).load(getItem(position)).into(viewHolder.mIvImage);
        }
    }

    @Override
    public void onClick(View v, int position) {
        switch (v.getId()){
            //case R.id.tv_content:
            //    Toast.makeText(v.getContext(), "on click " + position, Toast.LENGTH_SHORT).show();
            //    break;
            default:
                Toast.makeText(v.getContext(), "on click " + position, Toast.LENGTH_SHORT).show();
                //mock click todo  last item
                //int CIndex = (int) getItemId(position);
                //remove(CIndex);
                //notifyItemRemoved(position);
        }
    }


    public class ItemViewHolder extends ClickableViewHolder{
        public ImageView mIvImage;

        public ItemViewHolder(View view) {
            super(view);
            mIvImage = (ImageView) view.findViewById(R.id.iv_image);
            setOnRecyclerItemClickListener(StaggeredGridAdapter.this);
            //view.setOnClickListener(this);
            //mTvContent.setOnClickListener(this);
            addOnItemViewClickListener();
            addOnViewClickListener(mIvImage);
        }
    }
}
