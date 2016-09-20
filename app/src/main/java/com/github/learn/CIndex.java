package com.github.learn;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.captain_miao.recyclerviewutils.BaseWrapperRecyclerAdapter;
import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.learn.app.AppConstants;
import com.github.learn.base.BaseRecyclerActivity;
import com.github.learn.databinding.DataBindingRecyclerActivity;
import com.github.learn.expandable.ExpandableRecyclerActivity;
import com.github.learn.index.IndexRecyclerActivity;
import com.github.learn.refreshandload.HeaderRecyclerActivity;
import com.github.learn.refreshandload.MainActivity;
import com.github.learn.refreshandload.R;
import com.github.learn.refreshandload.RefreshRecyclerActivity;
import com.github.learn.refreshandload.adapter.SimpleAdapter;
import com.github.learn.refreshandload.gridview.GridViewActivity;
import com.github.learn.refreshandload.gridview.RefreshGridViewActivity;
import com.github.learn.staggeredgrid.StaggeredGridRecyclerActivity;
import com.github.learn.stickyheaders.StickyHeadersActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hesk on 2016/9/15.
 */

public class CIndex extends BaseRecyclerActivity<String> {

    private LinkedHashMap<String, Class> data = new LinkedHashMap<>();
    private ArrayList<Class> o = new ArrayList<>();

    public boolean isShowHomeAsUp() {
        return false;
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        ArrayList<String> items = new ArrayList<>();
        initList();
        for (Map.Entry<String, Class> entry : data.entrySet()) {
            String key = entry.getKey();
            items.add(key);
            o.add(entry.getValue());
        }
        mAdapter.addAll(items);
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
        return mAdapter != null ? mAdapter : (mAdapter = new SimpleAdapter(new ArrayList<String>()) {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(CIndex.this, o.get(position));
                if (position == 2 || position == 8) {
                    intent.putExtra(AppConstants.KEY_BOOLEAN, true);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean enablePullToLoadMore() {
        return false;
    }


    @Override
    public boolean enablePullToRefresh() {
        return false;
    }

    @Override
    protected void loadData() {

    }


    public void initList() {
        data.put(getString(R.string.label_action_label_view_page_banner), MainActivity.class);
        data.put(getString(R.string.label_action_label_header_view), HeaderRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_header_grid_view), HeaderRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_recycler_view), RefreshRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_grid_view), GridViewActivity.class);
        data.put(getString(R.string.label_action_label_refresh_grid_view), RefreshGridViewActivity.class);
        data.put(getString(R.string.label_action_label_sticky_header_view), StickyHeadersActivity.class);
        data.put(getString(R.string.label_action_label_expandable_view), ExpandableRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_sticky_expandable_view), StickyHeadersActivity.class);
        data.put(getString(R.string.label_action_label_index_view), IndexRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_staggered_grid_view), StaggeredGridRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_data_binding), DataBindingRecyclerActivity.class);
        // data.put("Demo Index Recycler Activity", IndexRecyclerActivity.class);
        // data.put("Viewpager Activity", ViewPagerActivity.class);
        // data.put("Header Grid Footer", ViewPagerActivity.class);
    }
}
