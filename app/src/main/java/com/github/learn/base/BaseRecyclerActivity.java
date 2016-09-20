package com.github.learn.base;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.captain_miao.recyclerviewutils.BaseWrapperRecyclerAdapter;
import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.captain_miao.recyclerviewutils.common.DefaultLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.common.LayoutManagers;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;

import java.util.List;

public abstract class BaseRecyclerActivity<T> extends BaseActivity implements RefreshRecyclerViewListener {
    private static String TAG = BaseRecyclerActivity.class.getSimpleName();

    protected WrapperRecyclerView mWrapperRecyclerView;
    protected BaseWrapperRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> mAdapter;

    protected boolean mIsRefresh = false;
    protected int      mCurrPage = 1;

    //Recycler 绑定 Adapter
    protected abstract int getLayoutResID();
    protected abstract WrapperRecyclerView getRecyclerView();
    protected abstract BaseWrapperRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> getWrapperRecyclerAdapter();

    //加载数据 getCurrPage() 拿到当前页数
    protected abstract void loadData();

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(getLayoutResID());
        initRecyclerView();
        if(enablePullToRefresh()) {
            autoRefresh();
        }
    }

    protected void initRecyclerView() {
        checkParent();
        mWrapperRecyclerView = getRecyclerView();
        mAdapter = getWrapperRecyclerAdapter();
        mAdapter.setLoadMoreFooterView(new DefaultLoadMoreFooterView(this));

        mWrapperRecyclerView.setLayoutManager(getLayoutManager());
        mWrapperRecyclerView.setRecyclerViewListener(this);
        mWrapperRecyclerView.setAdapter(mAdapter);
        if (enablePullToRefresh()) {
            mWrapperRecyclerView.enableRefresh();
        } else {
            mWrapperRecyclerView.disableRefresh();
        }
        if (enablePullToLoadMore()) {
            mWrapperRecyclerView.enableLoadMore();
        } else {
            mWrapperRecyclerView.disableLoadMore();
        }
    }

    public void autoRefresh(){
        mWrapperRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mWrapperRecyclerView.autoRefresh(true, 500);
            }
        });
    }


    //Recycler 绑定 LayoutManager
    protected RecyclerView.LayoutManager getLayoutManager(){
        return LayoutManagers.linear().create(this);
        //return new LinearLayoutManager(this);
    }

    public boolean enablePullToRefresh(){
        return true;
    }

    public boolean enablePullToLoadMore(){
        return true;
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh begin ====");
        this.mIsRefresh = true;
        if(enablePullToLoadMore()) {
            mWrapperRecyclerView.enableLoadMore();
        }
        mAdapter.hideFooterView();
        mCurrPage = 1;
        loadData( );
    }

    public void onLoadMore(int pagination, int pageSize) {
        Log.d(TAG, "pagination: " + mCurrPage);
        mWrapperRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mIsRefresh = false;
                mAdapter.showLoadMoreView();
                loadData();
            }
        });
    }


    public void refreshData(List<T> items) {
        mAdapter.clear(false);
        addMoreData(items);
    }

    public void addMoreData(List<T> items) {
        mAdapter.addAll(items, true);
    }


    public void showDataEmptyView() {
        //
    }

    public void hideDataEmptyView() {
        //
    }


    public void showLoadMoreView() {
        mWrapperRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mWrapperRecyclerView.showLoadMoreView();
            }
        });
    }


    public void showNoMoreDataView() {
        mWrapperRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mWrapperRecyclerView.disableLoadMore();
                mWrapperRecyclerView.showNoMoreDataView();
            }
        });
    }


    public void hideLoadMoreView() {
        mWrapperRecyclerView.hideFooterView();
    }


    public void onRefreshComplete() {
        mWrapperRecyclerView.refreshComplete();
    }
    public void onLoadMoreComplete() {
        mWrapperRecyclerView.loadMoreComplete();
    }


    protected boolean isDataEmpty() {
        return mAdapter == null || mAdapter.getItemCount() == 0;
    }

    private void checkParent(){
        if(getWrapperRecyclerAdapter() == null){
            throw new IllegalArgumentException("getWrapperRecyclerAdapter() is null");
        }
        if(getLayoutManager() == null){
            throw new IllegalArgumentException("getLayoutManager() is null");
        }
    }
}
