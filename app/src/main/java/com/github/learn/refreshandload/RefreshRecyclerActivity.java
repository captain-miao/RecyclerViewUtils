package com.github.learn.refreshandload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.github.captain_miao.recyclerviewutils.RefreshRecyclerView;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.github.learn.refreshandload.adapter.SimpleAdapter;

import java.util.ArrayList;


public class RefreshRecyclerActivity extends AppCompatActivity implements RefreshRecyclerViewListener {

    private SimpleAdapter mAdapter;
    private RefreshRecyclerView mRefreshRecyclerView;
    private final int MAX_ITEM_COUNT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_refresh_recycler_view);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mRefreshRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRefreshRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SimpleAdapter(new ArrayList<String>());
        //initMockData();
        //mAdapter.setHasMoreData(false);
        //mAdapter.setHasFooter(false);
        mRefreshRecyclerView.setAdapter(mAdapter);
        //mAdapter.setHasMoreData(true);

        mRefreshRecyclerView.setRecyclerViewListener(this);
        mRefreshRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshRecyclerView.autoRefresh();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initMockData(int count){
        for (int i = 0; i < count; i++) {
            mAdapter.appendToTop("1 page -> " + mAdapter.getItemCount() + "");
        }
    }
    private void initMockData() {
        initMockData(25);
    }


    @Override
    public void onClick(View v, int position) {

    }

    @Override
    public void onRefresh() {
        mRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshRecyclerView.refreshComplete();
                if(mAdapter.getItemCount() < 15) {
                    mAdapter.clear();
                    initMockData();
                } else {
                    mAdapter.clear();
                    initMockData(5);
                }
                mAdapter.hideFooterView();
                mAdapter.notifyDataSetChanged();
                mRefreshRecyclerView.getRecyclerView().scrollToPosition(0);
                mAdapter.setHasMoreData(true);
            }
        }, 500);
    }

    @Override
    public void onLoadMore(final int pagination, int pageSize) {
        mRefreshRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mAdapter.getItemCount() < MAX_ITEM_COUNT) {
                    mAdapter.showLoadMoreView();
                } else {
                    mAdapter.showNoMoreDataView();
                }

            }
        });


        mRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {

                //int position = mAdapter.getItemCount();
                if (mAdapter.getItemCount() >= MAX_ITEM_COUNT) {
                    mAdapter.showNoMoreDataView();
                } else {
                    mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                    mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                    mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                    mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                    mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                    mAdapter.notifyDataSetChanged();
                    mAdapter.hideFooterView();
                }
                //java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
                //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - 5, 5);
                //mRefreshRecyclerView.scrollToPosition(position);
                mRefreshRecyclerView.loadMoreComplete();

            }
        }, 1500);
    }
}
