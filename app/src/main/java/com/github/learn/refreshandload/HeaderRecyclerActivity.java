package com.github.learn.refreshandload;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.captain_miao.recyclerviewutils.RefreshRecyclerView;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.github.learn.refreshandload.adapter.SimpleHeaderAdapter;

import java.security.SecureRandom;
import java.util.ArrayList;


public class HeaderRecyclerActivity extends AppCompatActivity implements View.OnClickListener, RefreshRecyclerViewListener {

    private SimpleHeaderAdapter mAdapter;
    private RefreshRecyclerView mRefreshRecyclerView;
    private TextView mTvHeader;
    private TextView mTvFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_refresh_recycler_view);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        View mRecyclerViewHeader = LayoutInflater.from(this).inflate(R.layout.recycler_view_header, null);
        View mRecyclerViewFooter = LayoutInflater.from(this).inflate(R.layout.recycler_view_footer, null);
        mRecyclerViewHeader.findViewById(R.id.btn_header_change_color).setOnClickListener(this);
        mRecyclerViewFooter.findViewById(R.id.btn_footer_change_color).setOnClickListener(this);
        mTvHeader = (TextView) mRecyclerViewHeader.findViewById(R.id.tv_header);
        mTvFooter = (TextView) mRecyclerViewFooter.findViewById(R.id.tv_footer);
        mAdapter = new SimpleHeaderAdapter(new ArrayList<String>());
        mAdapter.addHeaderView(mRecyclerViewHeader, false);
        mAdapter.addFooterView(mRecyclerViewFooter, false);


        mRefreshRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRefreshRecyclerView.setLayoutManager(linearLayoutManager);
        mRefreshRecyclerView.setAdapter(mAdapter);

        mRefreshRecyclerView.setRecyclerViewListener(this);
        mRefreshRecyclerView.disableLoadMore();
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
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_header_change_color:
                mTvHeader.setTextColor(getRandomColor());
                break;
            case R.id.btn_footer_change_color:
                mTvFooter.setTextColor(getRandomColor());
                break;
        }
    }


    private int getRandomColor() {
      SecureRandom rgen = new SecureRandom();
      return Color.HSVToColor(150, new float[]{
              rgen.nextInt(359), 1, 1
      });
    }


    @Override
    public void onRefresh() {
        mRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshRecyclerView.refreshComplete();
                if (mAdapter.getItemCount() < 15) {
                    mAdapter.clear();
                    initMockData();
                } else {
                    mAdapter.clear();
                    initMockData(5);
                }
                mAdapter.hideFooterView();
                mAdapter.notifyDataSetChanged();
                mRefreshRecyclerView.getRecyclerView().scrollToPosition(0);
            }
        }, 500);
    }

    @Override
    public void onLoadMore(int pagination, int pageSize) {

    }
}
