package com.github.captain_miao.recyclerviewutils.listener;

import android.view.View;

/**
 * @author YanLu
 * @since 15/11/1
 */
public interface RefreshRecyclerViewListener {
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    void onClick(View v, int position);

    /**
     * Called when pull to refresh.
     */
    void onRefresh( );

    /**
     * Called when scroll to recyclerView end.
     */
    void onLoadMore(final int pagination, final int pageSize);
}
