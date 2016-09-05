package com.github.captain_miao.recyclerviewutils.stickyandexpandable;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.captain_miao.recyclerviewutils.BaseWrapperRecyclerAdapter;
import com.timehop.stickyheadersrecyclerview.StickyAndExpandableHeadersTouchListener;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

/**
 * @author YanLu
 * @since 16/7/30
 */
public abstract class StickyAndExpandableRecyclerHeadersAdapter<T extends StickyHeaderItem, VH extends RecyclerView.ViewHolder,  HEADER extends RecyclerView.ViewHolder>
        extends BaseWrapperRecyclerAdapter<T, VH> implements StickyRecyclerHeadersAdapter<HEADER> {

    private StickyAndExpandableRecyclerHeadersAdapter() {
    }

    public StickyAndExpandableRecyclerHeadersAdapter(final RecyclerView recyclerView) {
        initStickyHeader(recyclerView);
    }


    private void initStickyHeader(final RecyclerView recyclerView) {

        final StickyRecyclerHeadersDecoration headersDecoration = new StickyRecyclerHeadersDecoration(this);
        recyclerView.addItemDecoration(headersDecoration);

        StickyAndExpandableHeadersTouchListener headersTouchListener =
                new StickyAndExpandableHeadersTouchListener(recyclerView, headersDecoration);

        recyclerView.addOnItemTouchListener(headersTouchListener);

        headersTouchListener.setOnHeaderClickListener(
                new StickyAndExpandableHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        T item = getItem(position);
                        if(item != null && item.isParentItem()){
                            List<T>  childItemList = item.getChildItemList();

                            if (childItemList != null) {
                                int childListItemCount = childItemList.size();
                                if (item.isExpanded()) {
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
                            item.setExpanded(!item.isExpanded());
                        }
                    }
                });

        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecoration.invalidateHeaders();
            }
        });

    }
}