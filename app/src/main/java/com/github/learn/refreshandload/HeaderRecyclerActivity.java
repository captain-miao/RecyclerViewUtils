package com.github.learn.refreshandload;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.captain_miao.recyclerviewutils.common.DefaultLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.common.LayoutManagers;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.github.learn.app.AppConstants;
import com.github.learn.refreshandload.adapter.SimpleWrapperAdapter;

import java.security.SecureRandom;
import java.util.ArrayList;


public class HeaderRecyclerActivity extends AppCompatActivity implements View.OnClickListener, RefreshRecyclerViewListener {

    private SimpleWrapperAdapter mAdapter;
    private WrapperRecyclerView mWrapperRecyclerView;
    private View mRecyclerViewHeader;
    private TextView mTvHeader;
    private View mRecyclerViewFooter;
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
        mAdapter = new SimpleWrapperAdapter(new ArrayList<String>());
        mAdapter.setLoadMoreFooterView(new DefaultLoadMoreFooterView(this));
        addHeaderView();
        addFooterView();

        mWrapperRecyclerView = (WrapperRecyclerView) findViewById(R.id.recycler_view);
        if(getIntent() != null && getIntent().getBooleanExtra(AppConstants.KEY_BOOLEAN, false)) {
            mWrapperRecyclerView.setLayoutManager(LayoutManagers.grid(3).create(this));
        } else {
            mWrapperRecyclerView.setLayoutManager(LayoutManagers.linear().create(this));
        }
        mWrapperRecyclerView.setAdapter(mAdapter);

        mWrapperRecyclerView.setRecyclerViewListener(this);
        mWrapperRecyclerView.disableLoadMore();
        mWrapperRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mWrapperRecyclerView.autoRefresh();
            }
        });
    }

    @Override
    @Deprecated
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_recycler_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_header:
                addHeaderView();
                return true;
            case R.id.action_del_header:
                removeHeaderView();
                return true;
            case R.id.action_add_footer:
                addFooterView();
                return true;
            case R.id.action_del_footer:
                removeFooterView();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addHeaderView(){
        if(mAdapter.getHeaderSize() > 0) {
            Toast.makeText(this, "already has a header view", Toast.LENGTH_LONG).show();
        } else {
            mRecyclerViewHeader = LayoutInflater.from(this).inflate(R.layout.recycler_view_header, null);
            mRecyclerViewHeader.findViewById(R.id.btn_header_change_color).setOnClickListener(this);
            mTvHeader = (TextView) mRecyclerViewHeader.findViewById(R.id.tv_header);
            mAdapter.addHeaderView(mRecyclerViewHeader, true);
        }
    }
    private void removeHeaderView(){
        mAdapter.removeHeaderView(mRecyclerViewHeader, true);
    }

    private void addFooterView(){
        if(mAdapter.getFooterSize() > 0) {
            Toast.makeText(this, "already has a footer view", Toast.LENGTH_LONG).show();
        } else {
            mRecyclerViewFooter = LayoutInflater.from(this).inflate(R.layout.recycler_view_footer, null);
            mRecyclerViewFooter.findViewById(R.id.btn_footer_change_color).setOnClickListener(this);
            mTvFooter = (TextView) mRecyclerViewFooter.findViewById(R.id.tv_footer);
            mAdapter.addFooterView(mRecyclerViewFooter, true);
        }
    }
    private void removeFooterView(){
        mAdapter.removeFooterView(mRecyclerViewFooter, true);
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
        mWrapperRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWrapperRecyclerView.refreshComplete();
                if (mAdapter.getItemCount() < 15) {
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
    public void onLoadMore(int pagination, int pageSize) {

    }
}
