package com.github.captain_miao.recyclerviewutils.listener;

/**
 * @author YanLu
 * @since 15/11/1
 */
public interface RefreshRecyclerViewListener {

    /**
     * Called when pull to refresh.
     */
    void onRefresh( );

    /**
     * Called when scroll to recyclerView end.
     */
    void onLoadMore(final int pagination, final int pageSize);
}
