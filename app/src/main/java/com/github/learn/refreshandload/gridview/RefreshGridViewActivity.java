package com.github.learn.refreshandload.gridview;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.captain_miao.recyclerviewutils.common.BaseLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.common.LayoutManagers;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.github.learn.base.BaseActivity;
import com.github.learn.refreshandload.R;
import com.github.learn.refreshandload.adapter.SimpleAdapter;

import java.util.ArrayList;

import static com.github.learn.refreshandload.adapter.SimpleAdapter.getRandomColor;


public class RefreshGridViewActivity extends BaseActivity implements View.OnClickListener, RefreshRecyclerViewListener {

    private SimpleAdapter mAdapter;
    private WrapperRecyclerView mRecyclerView;
    private View mRecyclerViewHeader;
    private TextView mTvHeader;

    private void addHeaderView() {
        if (mAdapter.getHeaderSize() > 0) {
            Toast.makeText(this, "already has a header view", Toast.LENGTH_LONG).show();
        } else {
            mRecyclerViewHeader = LayoutInflater.from(this).inflate(R.layout.recycler_view_header, null, false);
            mRecyclerViewHeader.findViewById(R.id.btn_header_change_color).setOnClickListener(this);
            mTvHeader = (TextView) mRecyclerViewHeader.findViewById(R.id.tv_header);
            mAdapter.addHeaderView(mRecyclerViewHeader, true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_header_change_color:
                mTvHeader.setTextColor(getRandomColor());
                break;
            case R.id.btn_footer_change_color:
                //  mTvFooter.setTextColor(getRandomColor());
                break;
        }
    }


    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.ac_refresh_recycler_view);
        mRecyclerView = (WrapperRecyclerView) findViewById(R.id.recycler_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mAdapter = new SimpleAdapter(new ArrayList<String>());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.isHeaderView(position)) {
                    return layoutManager.getSpanCount();
                } else if (mAdapter.isContentView(position)) {
                    return 1;
                } else {
                    return layoutManager.getSpanCount();
                }
            }
        });
        //LayoutManagers.grid(3).create(this)
        mRecyclerView.setLayoutManager(layoutManager);
        addHeaderView();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreFooterView(new BaseLoadMoreFooterView(this) {
            @Override
            public int getLoadMoreLayoutResource() {
                return R.layout.list_load_more;
            }
        });
        mRecyclerView.setRecyclerViewListener(this);
        mRecyclerView.disableLoadMore();
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.autoRefresh();
            }
        });

    }

    @Override
    public void onRefresh() {
        mAdapter.append(mAdapter.getItemCount() + "", false);
        mAdapter.append(mAdapter.getItemCount() + "", false);
        mAdapter.append(mAdapter.getItemCount() + "", false);
        mAdapter.append(mAdapter.getItemCount() + "", false);
        mAdapter.append(mAdapter.getItemCount() + "", false);
        mAdapter.append(mAdapter.getItemCount() + "", false);
        mAdapter.append(mAdapter.getItemCount() + "", false);
        mAdapter.append(mAdapter.getItemCount() + "", false);
        mAdapter.append(mAdapter.getItemCount() + "", false);
        mAdapter.append(mAdapter.getItemCount() + "", false);
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
