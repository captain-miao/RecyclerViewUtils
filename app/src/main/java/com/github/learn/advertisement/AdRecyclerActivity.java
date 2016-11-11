package com.github.learn.advertisement;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.github.captain_miao.recyclerviewutils.BaseWrapperRecyclerAdapter;
import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.learn.base.BaseRecyclerActivity;
import com.github.learn.refreshandload.R;
import com.github.learn.refreshandload.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;


public class AdRecyclerActivity extends BaseRecyclerActivity<String> {
    private static final String TAG = "AdRecyclerActivity";

    private View mEmptyView;
    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        mEmptyView = getLayoutInflater().inflate(R.layout.recycler_empty_view, null);
        mWrapperRecyclerView.setEmptyView(mEmptyView);

        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdRecyclerActivity.this, "onClick EmptyView", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.ac_refresh_recycler_view;
    }

    @Override
    protected WrapperRecyclerView getRecyclerView() {
        return mWrapperRecyclerView != null ? mWrapperRecyclerView
                : (mWrapperRecyclerView = (WrapperRecyclerView) findViewById(R.id.recycler_view));
    }

    @Override
    protected BaseWrapperRecyclerAdapter<String, ? extends RecyclerView.ViewHolder> getWrapperRecyclerAdapter() {
        return  mAdapter != null ? mAdapter : ( mAdapter = new SimpleAdapter(new ArrayList<String>()));
    }

    @Override
    public boolean enablePullToLoadMore() {
        return false;
    }

    @Override
    protected void loadData() {
        new AsyncTask<Boolean, Boolean, List<String>>() {
            @Override
            protected List<String> doInBackground(Boolean... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mCurrPage > 1 && mAdapter.getItemCount() > 50) {
                    return new ArrayList<>();
                } else {
                    return new ArrayList<String>() {{
                        for (int i = 0; i < 10; i++) {
                            add(mCurrPage + " page -> " + i);
                        }
                    }};
                }
            }

            @Override
            protected void onPostExecute(List<String> items) {
                if(items != null && items.size() > 0) {
                    // 加载完数据 页数+1
                    mCurrPage++;
                    if (mIsRefresh) {
                        refreshData(items);
                        onRefreshComplete();
                    } else {
                        addMoreData(items);
                        onLoadMoreComplete();
                    }
                } else {
                    if (mIsRefresh) {
                        onRefreshComplete();
                        hideLoadMoreView();
                    } else {
                        onLoadMoreComplete();
                        showNoMoreDataView();
                    }
                }
            }
        }.execute();
    }

}
