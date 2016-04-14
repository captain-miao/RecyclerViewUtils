package com.github.learn.refreshandload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.captain_miao.recyclerviewutils.common.DefaultLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.github.learn.refreshandload.adapter.SimpleAdapter;

import java.util.ArrayList;


public class RefreshRecyclerActivity extends AppCompatActivity implements RefreshRecyclerViewListener {

    private SimpleAdapter mAdapter;
    private WrapperRecyclerView mWrapperRecyclerView;
    private final int MAX_ITEM_COUNT = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_refresh_recycler_view);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mWrapperRecyclerView = (WrapperRecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mWrapperRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SimpleAdapter(new ArrayList<String>());
        mAdapter.setLoadMoreFooterView(new DefaultLoadMoreFooterView(this));
        mWrapperRecyclerView.setAdapter(mAdapter);
        //mAdapter.setHasMoreData(true);
        addHeaderView();
        addFooterView();
        mWrapperRecyclerView.setRecyclerViewListener(this);

        mWrapperRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mWrapperRecyclerView.autoRefresh();
            }
        });
    }

    private void addHeaderView() {
        View mRecyclerViewHeader = LayoutInflater.from(this).inflate(R.layout.recycler_view_header, null);
        mRecyclerViewHeader.findViewById(R.id.btn_header_change_color).setVisibility(View.GONE);
        mAdapter.addHeaderView(mRecyclerViewHeader, true);
    }

    private void addFooterView() {
        View mRecyclerViewHeader = LayoutInflater.from(this).inflate(R.layout.recycler_view_footer, null);
        mRecyclerViewHeader.findViewById(R.id.btn_footer_change_color).setVisibility(View.GONE);
        mAdapter.addFooterView(mRecyclerViewHeader, true);
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
    public void onRefresh() {
        mWrapperRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWrapperRecyclerView.refreshComplete();
                if(mAdapter.getItemCount() < 15) {
                    mAdapter.clear();
                    initMockData();
                } else {
                    mAdapter.clear();
                    initMockData(5);
                }
                mAdapter.hideFooterView();
                mAdapter.notifyDataSetChanged();
                mWrapperRecyclerView.getRecyclerView().scrollToPosition(0);
            }
        }, 500);
    }

    @Override
    public void onLoadMore(final int pagination, int pageSize) {
        mWrapperRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mAdapter.getItemCount() < MAX_ITEM_COUNT) {
                    mAdapter.showLoadMoreView();
                } else {
                    mAdapter.showNoMoreDataView();
                }

            }
        });


        mWrapperRecyclerView.postDelayed(new Runnable() {
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
                mWrapperRecyclerView.loadMoreComplete();
            }
        }, 1500);
    }
}
