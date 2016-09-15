package com.github.learn.refreshandload;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.github.captain_miao.recyclerviewutils.BaseWrapperRecyclerAdapter;
import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.learn.base.BaseRecyclerActivity;
import com.github.learn.model.TextModel;
import com.github.learn.refreshandload.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;


public class RefreshRecyclerActivity extends BaseRecyclerActivity<TextModel> {
    private static final String TAG = "RefreshRecyclerActivity";

    private View mEmptyView;
    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        //addHeaderView();
        //addFooterView();
        mEmptyView = getLayoutInflater().inflate(R.layout.recycler_empty_view, null);
        mWrapperRecyclerView.setEmptyView(mEmptyView);

        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RefreshRecyclerActivity.this, "onClick EmptyView", Toast.LENGTH_SHORT).show();
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
    protected BaseWrapperRecyclerAdapter<TextModel> getWrapperRecyclerAdapter() {
        return  mAdapter != null ? mAdapter : ( mAdapter = new SimpleAdapter(new ArrayList<TextModel>()));
    }

    @Override
    public boolean enablePullToLoadMore() {
        return false;
    }

    @Override
    protected void loadData() {
        new AsyncTask<Boolean, Boolean, List<TextModel>>() {
            @Override
            protected List<TextModel> doInBackground(Boolean... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mCurrPage > 1 && mAdapter.getItemCount() > 50) {
                    return new ArrayList<>();
                } else {
                    return new ArrayList<TextModel>() {{
                        for (int i = 0; i < 10; i++) {
                            add(new TextModel(mCurrPage + " page -> " + i));
                        }
                    }};
                }
            }

            @Override
            protected void onPostExecute(List<TextModel> items) {
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

}
