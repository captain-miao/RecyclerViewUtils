package com.github.learn.refreshandload.adapter;

import com.github.captain_miao.recyclerviewutils.BaseWrapperRecyclerAdapter;
import com.github.learn.model.TextModel;

import java.util.List;

/**
 * @author YanLu
 * @since 15/9/15
 */
public class SimpleAdapter extends BaseWrapperRecyclerAdapter<TextModel> {

    public SimpleAdapter(List<TextModel> items) {
        appendToList(items);
    }


//    @Override
//    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item, parent, false);
//
//        return new SimpleAdapter.ItemViewHolder(view);
//    }
//
//
//    @Override
//    public void onBindItemViewHolder(SimpleAdapter.ItemViewHolder vh, int position) {
//        vh.mTvContent.setText(getItem(position));
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position, List payloads) {
//        if(payloads != null && payloads.size() > 0 && vh instanceof UniqueViewHolder){
//            for(Object o : payloads){
//                if(o != null && o instanceof Integer) {
//                    ((UniqueViewHolder) vh).dataBinding.setVariable(BR.txtColor, (Integer) o);
//                }
//            }
//        } else {
//            super.onBindViewHolder(vh, position);
//        }
//    }
//
//    @Override
//    public void onClick(View v, int position) {
//        switch (v.getId()){
//            case R.id.tv_content:
//                notifyItemChanged(position, getRandomColor());
//                break;
//            default:
//                Toast.makeText(v.getContext(), "on click " + position, Toast.LENGTH_SHORT).show();
//                //mock click
//                remove(getItem(position));
//        }
//    }
//
//
//    public class ItemViewHolder extends ClickableViewHolder{
//        public TextView mTvContent;
//
//        public ItemViewHolder(View view) {
//            super(view);
//            mTvContent = (TextView) view.findViewById(R.id.tv_content);
//            setOnRecyclerItemClickListener(SimpleAdapter.this);
//            addOnItemViewClickListener();
//            addOnViewClickListener(mTvContent);
//        }
//    }



}
