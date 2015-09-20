package com.dahuo.learn.swiperefreshandload;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dahuo.learn.swiperefreshandload.adapter.SimpleAdapter;
import com.dahuo.library.swiperefreshandloadmore.refreshView.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private SimpleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView =  (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SimpleAdapter(values);
        mAdapter.setHasMoreData(false);
        mAdapter.setHasFooter(false);
        mRecyclerView.setAdapter(mAdapter);


        //设置加载圈圈的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.line_color_run_speed_13);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.appendToTop(mAdapter.getItemCount() + "");
                        mAdapter.appendToTop(mAdapter.getItemCount() + "");
                        mAdapter.appendToTop(mAdapter.getItemCount() + "");
                        mAdapter.appendToTop(mAdapter.getItemCount() + "");
                        mAdapter.appendToTop(mAdapter.getItemCount() + "");
                        mAdapter.notifyItemRangeInserted(0, 5);
                    }
                }, 1000);//1秒
            }
        });


        mAdapter.setHasMoreData(true);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                mAdapter.setHasFooter(true);
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int position = mAdapter.getItemCount();
                        if (mAdapter.getItemCount() > 50) {
                            mAdapter.setHasMoreDataAndFooter(false, true);
                        } else {
                            mAdapter.append("" +  mAdapter.getItemCount());
                            mAdapter.append("" +  mAdapter.getItemCount());
                            mAdapter.append("" +  mAdapter.getItemCount());
                            mAdapter.append("" +  mAdapter.getItemCount());
                            mAdapter.append("" +  mAdapter.getItemCount());
                        }
                        mAdapter.notifyDataSetChanged();
                        //java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
                        //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - 5, 5);
                        mRecyclerView.scrollToPosition(position);
                    }
                }, 2000);//2秒
            }
        });
    }




    ArrayList<String> values = new ArrayList<String>() {{
        add("Android");
        add("iPhone");
        add("WindowsMobile");
        add("Blackberry");
    }};


}
