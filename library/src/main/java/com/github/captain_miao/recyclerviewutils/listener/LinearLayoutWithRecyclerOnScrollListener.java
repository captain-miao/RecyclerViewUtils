package com.github.captain_miao.recyclerviewutils.listener;

/*
 * Copyright (C) 2015 Jorge Castillo Pérez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * modify from https://github.com/JorgeCastilloPrz/Mirage
 */


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author Jorge Castillo Pérez
 *
 * modify at 2015/08/23
 */
public abstract class LinearLayoutWithRecyclerOnScrollListener extends RecyclerOnScrollListener {
    public static String TAG = LinearLayoutWithRecyclerOnScrollListener.class.getSimpleName();


    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLinearLayoutManager;

    public abstract void onLoadMore(int pagination, int pageSize);


    public LinearLayoutWithRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager, int pagination, int pageSize) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.pagination = pagination;
        this.pageSize = pageSize;
    }

    public LinearLayoutWithRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!isLoading()) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            //totalItemCount > visibleItemCount load more
            if (loadMoreEnable && !loading && totalItemCount > visibleItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                loading = true;
                pagination++;
                onLoadMore(pagination, pageSize);
            }
        }
    }


    public boolean checkCanDoRefresh() {
        int position = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (position == 0) {
            return true;
        } else if (position == -1) {
            position = mLinearLayoutManager.findFirstVisibleItemPosition();
            return position == 0;
        }
        //int firstPos = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        return false;
    }


}
