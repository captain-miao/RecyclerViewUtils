package com.github.captain_miao.recyclerviewutils.listener;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


/**
 * @author YanLu
 * @since 16/4/23
 */
public abstract class StaggeredGridWithRecyclerOnScrollListener extends RecyclerOnScrollListener {
    public static String TAG = StaggeredGridWithRecyclerOnScrollListener.class.getSimpleName();

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 1;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private StaggeredGridLayoutManager mLayoutManager;

    public abstract void onLoadMore(int pagination, int pageSize);

    public StaggeredGridWithRecyclerOnScrollListener(StaggeredGridLayoutManager layoutManager, int pagination, int pageSize) {
        this.mLayoutManager = layoutManager;
        this.pagination = pagination;
        this.pageSize = pageSize;
    }
    public StaggeredGridWithRecyclerOnScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!isLoading()) {
            visibleItemCount = mLayoutManager.getChildCount();
            totalItemCount = mLayoutManager.getItemCount();
            int[] firstVisibleItems = null;
            firstVisibleItems = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
            if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                pastVisibleItems = firstVisibleItems[0];
            }

            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                // End has been reached
                loading = true;
                pagination++;
                onLoadMore(pagination, pageSize);
            }
        }
    }

    public boolean checkCanBePulledDown() {
        boolean allViewAreOverScreen = true;
        int[] positions = mLayoutManager.findFirstVisibleItemPositions(null);
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] == 0) {
                return true;
            }
            if (positions[i] != -1) {
                allViewAreOverScreen = false;
            }
        }
        if (allViewAreOverScreen) {
            positions = mLayoutManager.findFirstVisibleItemPositions(null);
            for (int i = 0; i < positions.length; i++) {
                if (positions[i] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
