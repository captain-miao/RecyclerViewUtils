package com.github.learn.staggeredgrid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.captain_miao.recyclerviewutils.common.DefaultLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.github.learn.refreshandload.R;

import java.util.ArrayList;


public class StaggeredGridRecyclerActivity extends AppCompatActivity implements RefreshRecyclerViewListener {

    private StaggeredGridAdapter mAdapter;
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
        final StaggeredGridLayoutManager LayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mWrapperRecyclerView.setLayoutManager(LayoutManager);
        mAdapter = new StaggeredGridAdapter(new ArrayList<String>());
        mAdapter.setLoadMoreFooterView(new DefaultLoadMoreFooterView(this));
        mWrapperRecyclerView.setAdapter(mAdapter);

        // TODO: 16/4/23 it's a bug
        //addHeaderView();
        //addFooterView();
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
            mAdapter.add(images[i]);
        }
    }
    private void initMockData() {
        initMockData(10);
    }


    private String[] images = new String[]{
            "http://ww1.sinaimg.cn/small/7a8aed7bjw1f2sm0ns82hj20f00l8tb9.jpg",
            "http://ww4.sinaimg.cn/large/7a8aed7bjw1f2tpr3im0mj20f00l6q4o.jpg",
            "http://ww4.sinaimg.cn/small/610dc034jw1f2uyg3nvq7j20gy0p6myx.jpg",
            "http://ww2.sinaimg.cn/large/7a8aed7bjw1f2w0qujoecj20f00kzjtt.jpg",
            "http://ww3.sinaimg.cn/small/7a8aed7bjw1f2x7vxkj0uj20d10mi42r.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f2zwrqkmwoj20f00lg0v7.jpg",
            "http://ww2.sinaimg.cn/small/7a8aed7bjw1f30sgi3jf0j20iz0sg40a.jpg",
            "http://ww4.sinaimg.cn/small/7a8aed7bjw1f32d0cumhkj20ey0mitbx.jpg",
            "http://ww2.sinaimg.cn/large/610dc034gw1f35cxyferej20dw0i2789.jpg",
            "http://ww2.sinaimg.cn/small/7a8aed7bjw1f340c8jrk4j20j60srgpf.jpg"
    };


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
                    initMockData(5);
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
