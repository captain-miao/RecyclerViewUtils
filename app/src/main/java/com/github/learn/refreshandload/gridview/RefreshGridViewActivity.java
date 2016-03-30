package com.github.learn.refreshandload.gridview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;

import com.github.captain_miao.recyclerviewutils.BaseLoadMoreRecyclerAdapter;
import com.github.captain_miao.recyclerviewutils.RefreshRecyclerView;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.github.learn.refreshandload.R;
import com.github.learn.refreshandload.adapter.SimpleAdapter;

import java.util.ArrayList;


public class RefreshGridViewActivity extends AppCompatActivity {

    private SimpleAdapter mAdapter;
    private RefreshRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_refresh_recycler_view);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);

        // 网格布局管理器
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
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
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setRecyclerViewListener(new RefreshRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.appendToTop(mAdapter.getItemCount() + "");
                mAdapter.notifyItemRangeInserted(0, 10);
                mRecyclerView.refreshComplete();
                mRecyclerView.getRecyclerView().scrollToPosition(0);
            }

            @Override
            public void onLoadMore(final int pagination, int pageSize) {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.showLoadMoreView();
                    }
                });


                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int position = mAdapter.getItemCount();
                        if (mAdapter.getItemCount() > 50) {
                            mAdapter.showNoMoreDataView();
                        } else {
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.loadMoreComplete();
                        }
                        //java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
                        //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - 5, 5);
                        //mRecyclerView.scrollToPosition(position);

                    }
                }, 1500);
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
