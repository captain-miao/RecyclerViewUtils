package com.dahuo.learn.swiperefreshandload;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.dahuo.learn.swiperefreshandload.adapter.SimpleAdapter;
import com.dahuo.library.swiperefresh.BaseLoadMoreRecyclerAdapter;
import com.dahuo.library.swiperefresh.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;


public class GridViewActivity extends AppCompatActivity {

    private SimpleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView =  (RecyclerView) findViewById(R.id.recycler_view);

        // 网格布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //加载更多 占领 整个一行
                switch (mAdapter.getItemViewType(position)) {
                    case BaseLoadMoreRecyclerAdapter.TYPE_FOOTER:
                        return 3;//number of columns of the grid
                    case BaseLoadMoreRecyclerAdapter.TYPE_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }

        });


        mRecyclerView.setLayoutManager(layoutManager);
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
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
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
                            mAdapter.setHasFooter(false);
                            //mAdapter.remove(mAdapter.getItemCount() - 1);
                            mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
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

    ArrayList<String> values = new ArrayList<String>() {{
        add("Android");
        add("iPhone");
        add("WindowsMobile");
        add("Blackberry");
    }};


}
