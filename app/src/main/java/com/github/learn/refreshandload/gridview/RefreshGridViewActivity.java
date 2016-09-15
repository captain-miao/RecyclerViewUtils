package com.github.learn.refreshandload.gridview;

import android.os.Bundle;
import android.view.MenuItem;

import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.captain_miao.recyclerviewutils.common.BaseLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.common.LayoutManagers;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.github.learn.base.BaseActivity;
import com.github.learn.refreshandload.R;
import com.github.learn.refreshandload.adapter.SimpleAdapter;

import java.util.ArrayList;


public class RefreshGridViewActivity extends BaseActivity {

    private SimpleAdapter mAdapter;
    private WrapperRecyclerView mRecyclerView;

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.ac_refresh_recycler_view);


        mRecyclerView = (WrapperRecyclerView) findViewById(R.id.recycler_view);

        //final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(LayoutManagers.grid(3).create(this));
        mAdapter = new SimpleAdapter(values);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreFooterView(new BaseLoadMoreFooterView(this) {
            @Override
            public int getLoadMoreLayoutResource() {
                return R.layout.list_load_more;
            }
        });
        mRecyclerView.setRecyclerViewListener(new RefreshRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
                mAdapter.appendToTop(mAdapter.getItemCount() + "", false);
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
