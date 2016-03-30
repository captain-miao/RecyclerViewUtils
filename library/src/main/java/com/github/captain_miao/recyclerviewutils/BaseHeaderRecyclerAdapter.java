package com.github.captain_miao.recyclerviewutils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author YanLu
 * @since 16/3/30
 */
public abstract class BaseHeaderRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{
    private static final String TAG = "BaseHeaderRecyclerAdapter";

    public static final int NO_POSITION = -1;
    public static final long NO_ID = -1;
    public static final int INVALID_TYPE = -1;
    private static final int VIEW_TYPE_MAX_COUNT = 1000;//header or footer max view type :1000
    private static final int HEADER_VIEW_TYPE_OFFSET = 0;
    private static final int FOOTER_VIEW_TYPE_OFFSET = HEADER_VIEW_TYPE_OFFSET + VIEW_TYPE_MAX_COUNT;
    private static final int FOOTER_LOAD_MORE_VIEW_TYPE = FOOTER_VIEW_TYPE_OFFSET + 1;//the bottom view
    private static final int CONTENT_VIEW_TYPE_OFFSET = FOOTER_LOAD_MORE_VIEW_TYPE + VIEW_TYPE_MAX_COUNT;

    //header view and footer view can't be recycled
    private List<RecyclerView.ViewHolder> mHeaderViews = new ArrayList<>();
    //store the ViewHolder for remove header view
    private Map<View, RecyclerView.ViewHolder> mHeaderViewHolderMap = new HashMap<>();
    private int mHeaderSize;

    //header view and footer view can't be recycled
    private List<RecyclerView.ViewHolder> mFooterViews = new ArrayList<>();
    //store the ViewHolder for remove footer view
    private Map<View, RecyclerView.ViewHolder> mFooterViewHolderMap = new HashMap<>();
    private int mFooterSize;



    private boolean showLoadMoreView;//has load more view
    private boolean hasMoreData;//load more display message

    protected final List<T> mList = new LinkedList<T>();


    //Content itemViewHolder
    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);
    //Content itemViewHolder
    public abstract void onBindItemViewHolder(final VH holder, int position);

    //Content itemViewViewType
    public int getContentViewType(int dataListIndex) {
        return 0;
    }

    public int getFooterLayoutResource() {
        return R.layout.item_view_load_more;//default
    }
    public int getFooterLoadingShowStringResource() {
        return R.string.app_loading_more;//loading_more
    }
    public int getFooterNoMoreDataShowStringResource() {
        return R.string.app_no_more_data;//loading_more
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public final View mProgressView;

        public final TextView mTextView;
        public FooterViewHolder(View view) {
            super(view);
            mProgressView = view.findViewById(R.id.progress_view);
            mTextView = (TextView) view.findViewById(R.id.tv_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(isContentViewType(viewType)){
            return onCreateItemViewHolder(parent, viewType);
        } else if (isHeaderViewType(viewType)) {
            return mHeaderViews.get(viewType - HEADER_VIEW_TYPE_OFFSET);
        } else if(isFooterViewType(viewType)){
            return mFooterViews.get(viewType - FOOTER_VIEW_TYPE_OFFSET);
        } else if (isFooterLoadMoreViewType(viewType)) {//the bottom load more view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(getFooterLayoutResource(), parent, false);
            return new FooterViewHolder(view);
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            //没有更多数据
            if (hasMoreData) {
                ((FooterViewHolder) holder).mProgressView.setVisibility(View.GONE);
                ((FooterViewHolder) holder).mProgressView.setVisibility(View.VISIBLE);
                ((FooterViewHolder) holder).mTextView.setText(getFooterLoadingShowStringResource());
            } else {
                ((FooterViewHolder) holder).mProgressView.setVisibility(View.VISIBLE);
                ((FooterViewHolder) holder).mProgressView.setVisibility(View.GONE);
                ((FooterViewHolder) holder).mTextView.setText(getFooterNoMoreDataShowStringResource());
            }
        } else {
            onBindItemViewHolder((VH) holder, position);
        }
    }


    @Override
    final public int getItemViewType(int position) {
        //header view type
        if(mHeaderSize > 0 && position < mHeaderSize) {
            return HEADER_VIEW_TYPE_OFFSET + position;//header view has different viewType
        } else if(position >= mHeaderSize && position < getBasicItemCount() + mHeaderSize) {
            //content view type
            int contentViewType = getContentViewType(position - mHeaderSize);
            if(contentViewType >= 0) {
                return CONTENT_VIEW_TYPE_OFFSET + contentViewType;
            } else {
                throw new IllegalArgumentException("contentViewType must >= 0");
            }
        } else if(mFooterSize > 0 && position >= (getBasicItemCount() + mHeaderSize)
                                    && position < (getBasicItemCount() + mHeaderSize + mFooterSize)){
            //footer view type
            return FOOTER_VIEW_TYPE_OFFSET + (position - mHeaderSize - getBasicItemCount());//footer view has different viewType
        } else if (showLoadMoreView && position == (getBasicItemCount() + mHeaderSize + mFooterSize)) {
            //load more  view type
            return FOOTER_LOAD_MORE_VIEW_TYPE;
        }
        return INVALID_TYPE;
    }

    public void addHeaderView(View view, boolean notifyDataChange) {
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) { };
        viewHolder.setIsRecyclable(false);
        mHeaderViewHolderMap.put(view, viewHolder);
        mHeaderViews.add(viewHolder);
        mHeaderSize = mHeaderViews.size();
        if(notifyDataChange) {
            notifyItemInserted(mHeaderSize - 1);
        }
    }
    public void addHeaderView(View view) {
        addHeaderView(view, true);
    }

    public void removeHeaderView(View view, boolean notifyDataChange) {
        RecyclerView.ViewHolder viewHolder = mHeaderViewHolderMap.get(view);
        if(mHeaderViews.remove(viewHolder)) {
            mHeaderSize = mHeaderViews.size();
            mHeaderViewHolderMap.remove(view);
            if(notifyDataChange) {
                notifyDataSetChanged();
            }
        }
    }
    public void removeHeaderView(View view) {
        removeHeaderView(view, true);
    }

    public void addFooterView(View view, boolean notifyDataChange) {
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) { };
        viewHolder.setIsRecyclable(false);
        mFooterViewHolderMap.put(view, viewHolder);
        mFooterViews.add(viewHolder);
        mFooterSize = mFooterViews.size();
        if(notifyDataChange) {
            notifyItemInserted(mFooterSize - 1);
        }
    }

    public void addFooterView(View view) {
        addFooterView(view, true);
    }

    public void removeFooterView(View view, boolean notifyDataChange) {
        RecyclerView.ViewHolder viewHolder = mFooterViewHolderMap.get(view);
        if(viewHolder != null && mFooterViews.remove(viewHolder)) {
            mFooterSize = mFooterViews.size();
            mFooterViewHolderMap.remove(view);
            if(notifyDataChange) {
                notifyDataSetChanged();
            }
        }
    }
    public void removeFooterView(View view) {
        removeFooterView(view, true);
    }

    public boolean isHeaderViewType(int viewType) {
        return viewType >= 0 && viewType < FOOTER_VIEW_TYPE_OFFSET;
    }

    public boolean isFooterViewType(int viewType) {
        return viewType >= 0 && viewType >= FOOTER_VIEW_TYPE_OFFSET && viewType < FOOTER_LOAD_MORE_VIEW_TYPE;
    }

    public boolean isFooterLoadMoreViewType(int viewType) {
        return  viewType >= 0 && viewType == FOOTER_LOAD_MORE_VIEW_TYPE;
    }

    public boolean isContentViewType(int viewType) {
        return  viewType >= 0 && viewType >= CONTENT_VIEW_TYPE_OFFSET;
    }


    public boolean isHeaderView(int position) {
        return isHeaderViewType(getItemViewType(position));
    }

    public boolean isFooterView(int position) {
        return isFooterViewType(getItemViewType(position));
    }
    public boolean isContentView(int position) {
        return isContentViewType(getItemViewType(position));
    }

    public List<T> getList() {
        return mList;
    }


    public void addAll(List<T> list, boolean notifyDataChange) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        if(notifyDataChange) {
            notifyDataSetChanged();
        }
    }
    public void addAll(List<T> list){
        addAll(list, true);
    }

    public void add(T t, boolean notifyDataChange) {
        if (t == null) {
            return;
        }
        mList.add(t);
        if(notifyDataChange) {
            notifyDataSetChanged();
        }
    }
    public void add(T t) {
        add(t, true);
    }

    public void appendToList(List<T> list) {
        addAll(list);
    }

    public void append(T t) {
        add(t);
    }

    public void appendToTop(T item) {
        if (item == null) {
            return;
        }
        mList.add(0, item);
        notifyDataSetChanged();
    }

    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(0, list);
        notifyDataSetChanged();
    }


    public T remove(int dataListIndex, boolean notifyDataChange) {
        if (dataListIndex >= 0 && dataListIndex < mList.size()) {
            T t = mList.remove(dataListIndex);
            if(notifyDataChange) {
                notifyItemRemoved(mHeaderSize + dataListIndex);
            }
            //notifyDataSetChanged();
            return t;
        } else {
            return null;
        }
    }

    public T remove(int dataListIndex) {
        return remove(dataListIndex, true);
    }

    public boolean remove(T data, boolean notifyDataChange) {
        boolean ret =  mList.remove(data);
        if(ret && notifyDataChange){
            notifyDataSetChanged();
        }

        return ret;
    }
    public boolean remove(T data) {
        return remove(data, true);
    }

    public void clear(boolean notifyDataChange) {
        mList.clear();
        if(notifyDataChange) {
            notifyDataSetChanged();
        }
    }
    public void clear() {
        clear(true);
    }

    protected int getBasicItemCount() {
        return mList.size();
    }

    @Override
    public int getItemCount() {
        return mHeaderSize + mFooterSize + getBasicItemCount() + (showLoadMoreView ? 1 : 0);
    }

    public T getItem(int position) {
        if(isContentView(position)){
            return mList.get(position - mHeaderSize);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        //content view return dataListIndex
        if(isContentView(position)) {
            return position - mHeaderSize;
        } else {
            return NO_POSITION;
        }
    }


    public void showLoadMoreView(){
        this.hasMoreData = true;
        if(showLoadMoreView) {
            notifyItemChanged(getItemCount());
        } else {
            this.showLoadMoreView = true;
            notifyItemInserted(getBasicItemCount());
        }
    }

    public void showNoMoreDataView(){
        this.hasMoreData = false;
        if(showLoadMoreView) {
            notifyItemChanged(getItemCount());
        } else {
            this.showLoadMoreView = true;
            notifyItemInserted(getBasicItemCount());
        }
    }

    public void hideFooterView(){
        if(showLoadMoreView) {
            this.showLoadMoreView = false;
            notifyItemRemoved(getItemCount());
        }
    }

}
