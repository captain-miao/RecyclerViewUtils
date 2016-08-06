package com.github.captain_miao.recyclerviewutils.stickyandexpandable;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.captain_miao.recyclerviewutils.BaseWrapperRecyclerAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.List;
import java.util.Map;

/**
 * @author YanLu
 * @since 16/7/30
 */
public abstract class StickyAndExpandableRecyclerHeadersAdapter<T, VH extends RecyclerView.ViewHolder,  HEADER extends RecyclerView.ViewHolder>
        extends BaseWrapperRecyclerAdapter<T, VH> implements StickyRecyclerHeadersAdapter<HEADER> {

    private Map<? extends StickyHeaderItem, List<T>> mDataMap;

    private StickyAndExpandableRecyclerHeadersAdapter() {
    }

    public StickyAndExpandableRecyclerHeadersAdapter(final RecyclerView recyclerView, Map<? extends StickyHeaderItem, List<T>> dataMap) {
        this.mDataMap = dataMap;

        initStickyHeader(recyclerView);
    }


    private void initStickyHeader(final RecyclerView recyclerView) {

        final StickyRecyclerHeadersDecoration headersDecoration = new StickyRecyclerHeadersDecoration(this);
        recyclerView.addItemDecoration(headersDecoration);

        StickyRecyclerHeadersTouchListener headersTouchListener =
                new StickyRecyclerHeadersTouchListener(recyclerView, headersDecoration);

        recyclerView.addOnItemTouchListener(headersTouchListener);

        headersTouchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        StickyHeaderItem wrapper = getStickyHeaderWrapper(headerId);
                        List<T>  childItemList = getChildItemList(wrapper);
                        if (childItemList != null) {
                            int childListItemCount = childItemList.size();
                            if (wrapper.isExpanded()) {
                                for (int i = childListItemCount - 1; i >= 0; i--) {
                                    mItemList.remove(position + i + 1);
                                }

                                notifyItemRangeRemoved(position + 1, childListItemCount);
                            } else {
                                for (int i = 0; i < childListItemCount; i++) {
                                    mItemList.add(position + i + 1, childItemList.get(i));
                                }

                                notifyItemRangeInserted(position + 1, childListItemCount);
                            }
                        }
                        wrapper.setExpanded(!wrapper.isExpanded());
                    }
                });

        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecoration.invalidateHeaders();
            }
        });

    }

    public List<T> getChildItemList(StickyHeaderItem wrapper){
        return mDataMap != null ? mDataMap.get(wrapper) : null;
    }

    public StickyHeaderItem getStickyHeaderWrapper(long headerId){

        StickyHeaderItem headerWrapper = null;
        for(StickyHeaderItem wrapper: mDataMap.keySet()){
            if(wrapper.getHeaderId() == headerId){
                headerWrapper = wrapper;
                break;
            }
        }

        return headerWrapper;
    }

}