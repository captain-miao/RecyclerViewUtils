package com.github.captain_miao.recyclerviewutils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.captain_miao.recyclerviewutils.common.BaseLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.common.UnRecyclableViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YanLu
 * @since 16/3/30
 */
public abstract class BaseWrapperRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{
    private static final String TAG = "BaseRvAdapter";

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

    //load more footer view
    private BaseLoadMoreFooterView mLoadMoreFooterView;
    private boolean showLoadMoreView;//is show load more view

    protected List<T> mItemList = new ArrayList<>();


    //Content itemViewHolder
    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);
    //Content itemViewHolder
    public abstract void onBindItemViewHolder(final VH holder, int position);

    //Content itemViewViewType
    public int getContentViewType(int dataListIndex) {
        return 0;
    }


    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(isContentViewType(viewType)){
            return onCreateItemViewHolder(parent, viewType - CONTENT_VIEW_TYPE_OFFSET);
        } else if (isHeaderViewType(viewType)) {
            return mHeaderViews.get(viewType - HEADER_VIEW_TYPE_OFFSET);
        } else if(isFooterViewType(viewType)){
            return mFooterViews.get(viewType - FOOTER_VIEW_TYPE_OFFSET);
        } else if (isFooterLoadMoreViewType(viewType)) {//the bottom load more view
            return new UnRecyclableViewHolder(mLoadMoreFooterView);
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (isContentView(position)) {
            onBindItemViewHolder((VH) holder, position);
        }
    }


    @Override
    public final int getItemViewType(int position) {
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


    @Override
    @SuppressWarnings("unchecked")
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (!isContentView(position)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
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

    public BaseLoadMoreFooterView getLoadMoreFooterView() {
        return mLoadMoreFooterView;
    }

    public void setLoadMoreFooterView(BaseLoadMoreFooterView mLoadMoreFooterView) {
        this.mLoadMoreFooterView = mLoadMoreFooterView;
    }

    public int getHeaderSize() {
        return mHeaderSize;
    }

    public int getFooterSize() {
        return mFooterSize;
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
        return mItemList;
    }


    public void addAll(List<T> list, boolean notifyDataChange) {
        if (list == null || list.isEmpty()) {
            return;
        }
        mItemList.addAll(list);
        if(notifyDataChange) {
            try {
                notifyItemRangeInserted(mHeaderSize + getBasicItemCount(), list.size());
            } catch (Exception e) {
                Log.w(TAG, "notifyItemRangeInserted failure");
                e.printStackTrace();
                notifyDataSetChanged();
            }
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
        mItemList.add(t);
        if(notifyDataChange) {
            try {
                notifyItemInserted(mHeaderSize + getBasicItemCount());
            } catch (Exception e) {
                Log.w(TAG, "notifyItemInserted failure");
                e.printStackTrace();
                notifyDataSetChanged();
            }
        }
    }
    public void add(T t) {
        add(t, true);
    }

    public void appendToList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        addAll(list);
        try {
            notifyItemRangeInserted(mHeaderSize + getBasicItemCount(), list.size());
        } catch (Exception e) {
            Log.w(TAG, "notifyItemRangeInserted failure");
            e.printStackTrace();
            notifyDataSetChanged();
        }
    }

    public void append(T item) {
        append(item, true);
    }
    public void append(T item, boolean notifyDataChange) {
        if (item == null) {
            return;
        }
        add(item, notifyDataChange);
    }

    public void appendToTop(T item) {
        appendToTop(item, true);
    }
    public void appendToTop(T item, boolean notifyDataChange) {
        if (item == null) {
            return;
        }
        mItemList.add(0, item);
        if(notifyDataChange) {
            try {
                notifyItemInserted(mHeaderSize);
            } catch (Exception e) {
                Log.w(TAG, "notifyItemInserted failure");
                e.printStackTrace();
                notifyDataSetChanged();
            }
        }
    }

    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        mItemList.addAll(0, list);
        try {
            notifyItemRangeInserted(mHeaderSize, list.size());
        } catch (Exception e) {
            Log.w(TAG, "notifyItemRangeInserted failure");
            e.printStackTrace();
            notifyDataSetChanged();
        }
    }


    public T remove(int dataListIndex, boolean notifyDataChange) {
        if (dataListIndex >= 0 && dataListIndex < mItemList.size()) {
            T t = mItemList.remove(dataListIndex);
            if (notifyDataChange) {
                try {
                    notifyItemRemoved(mHeaderSize + dataListIndex);
                } catch (Exception e) {
                    Log.w(TAG, "notifyItemRemoved failure");
                    e.printStackTrace();
                    notifyDataSetChanged();
                }
            }
            return t;
        } else {
            return null;
        }
    }

    public T remove(int dataListIndex) {
        return remove(dataListIndex, true);
    }

    public boolean remove(T data, boolean notifyDataChange) {
        int position = mItemList.indexOf(data);
        remove(position, notifyDataChange);
        return position >= 0;
    }

    public boolean remove(T data) {
        return remove(data, true);
    }


    public void update(int dataListIndex, boolean notifyDataChange) {
        if (dataListIndex >= 0 && dataListIndex < mItemList.size()) {
            if (notifyDataChange) {
                try {
                    notifyItemChanged(mHeaderSize + dataListIndex);
                } catch (Exception e) {
                    Log.w(TAG, "notifyItemChanged failure");
                    e.printStackTrace();
                    notifyDataSetChanged();
                }
            }
        }
    }

    public boolean update(T data, boolean notifyDataChange) {
        int position = mItemList.indexOf(data);
        update(position, notifyDataChange);
        return position >= 0;
    }

    public boolean update(T data) {
        return update(data, true);
    }

    public void clear(boolean notifyDataChange) {
        mItemList.clear();
        if(notifyDataChange) {
            notifyDataSetChanged();
        }
    }
    public void clear() {
        clear(true);
    }

    protected int getBasicItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemCount() {
        return mHeaderSize + mFooterSize + getBasicItemCount() + (showLoadMoreView ? 1 : 0);
    }

    public T getItem(int position) {
        if(isContentView(position)){
            return mItemList.get(position - mHeaderSize);
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
        if(mLoadMoreFooterView == null){
            throw new IllegalArgumentException("mLoadMoreFooterView is null, you can call setLoadMoreFooterView()");
        }
        mLoadMoreFooterView.showLoading();
        if(showLoadMoreView) {
            //notifyItemChanged(getItemCount());
        } else {
            this.showLoadMoreView = true;
            try {
                notifyItemInserted(getItemCount());
            } catch (Exception e) {
                Log.w(TAG, "notifyItemChanged failure");
                e.printStackTrace();
                notifyDataSetChanged();
            }
        }
    }

    public void showNoMoreDataView(){
        if(mLoadMoreFooterView == null){
            throw new IllegalArgumentException("mLoadMoreFooterView is null, you can call setLoadMoreFooterView()");
        }
        mLoadMoreFooterView.showNoMoreData();
        if(showLoadMoreView) {
            //notifyItemChanged(getItemCount());
        } else {
            this.showLoadMoreView = true;
            try {
                notifyItemInserted(getItemCount());
            } catch (Exception e) {
                Log.w(TAG, "notifyItemChanged failure");
                e.printStackTrace();
                notifyDataSetChanged();
            }
        }
    }

    public void hideFooterView(){
        if(mLoadMoreFooterView == null){
            throw new IllegalArgumentException("mLoadMoreFooterView is null, must call setLoadMoreFooterView()");
        }
        if(showLoadMoreView) {
            this.showLoadMoreView = false;
            //for java.lang.IllegalStateException: Added View has RecyclerView as parent but view is not a real child.
            //https://github.com/captain-miao/RecyclerViewUtils/issues/3
            try {
                notifyItemRemoved(mHeaderSize + mFooterSize + getBasicItemCount() + 1);
            } catch (Exception e) {
                notifyDataSetChanged();
                Log.w(TAG, "notifyItemChanged failure");
                e.printStackTrace();
            }
        }
    }

    public boolean isShowingLoadMoreView(){
        return showLoadMoreView;
    }

}
