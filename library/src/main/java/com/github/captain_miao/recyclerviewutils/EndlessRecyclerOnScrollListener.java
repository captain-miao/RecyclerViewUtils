package com.github.captain_miao.recyclerviewutils;

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
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private boolean loading = false;
    //list到达 最后一个item的时候 触发加载
    private int visibleThreshold = 1;
    // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLinearLayoutManager;


    //分页加载
    private int pageSize = 15;  //查询数量
    private int pagination = 1; //查询页码
    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager, int pagination, int pageSize) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.pagination = pagination;
        this.pageSize = pageSize;
    }
    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(!isLoading()) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            //totalItemCount > visibleItemCount 超过一个页面才有加载更多
            if (!loading && totalItemCount > visibleItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                loading = true;
                pagination++;
                onLoadMore(pagination, pageSize);
            }
        }
    }

    public boolean checkCanBePulledDown() {
        int firstPos = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        return firstPos <= 0;
    }


    public void loadComplete() {
        loading = false;
    }

    public synchronized boolean isLoading() {
        return loading;
    }

    public int getPagination() {
        return pagination;
    }

    public void setPagination(int pagination) {
        this.pagination = pagination;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public abstract void onLoadMore(int pagination, int pageSize);


}
