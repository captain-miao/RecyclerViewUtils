package com.github.learn.swiperefreshandload.gridview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.github.captain_miao.recyclerviewutils.BaseLoadMoreRecyclerAdapter;
import com.github.captain_miao.recyclerviewutils.EndlessRecyclerOnScrollListener;
import com.github.learn.swiperefreshandload.R;
import com.github.learn.swiperefreshandload.adapter.SimpleAdapter;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;


public class GridViewActivity extends AppCompatActivity {

    private SimpleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private EndlessRecyclerOnScrollListener mLoadMoreListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView =  (RecyclerView) findViewById(R.id.recycler_view);

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
        mAdapter.setHasMoreData(false);
        mAdapter.setHasFooter(false);
        mRecyclerView.setAdapter(mAdapter);

        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) findViewById(R.id.material_style_ptr_frame);
        // header
        final MaterialHeader header = new MaterialHeader(this);
        //header.setColorSchemeColors(new int[]{R.color.line_color_run_speed_13});
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(ptrFrameLayout);


        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setEnabledNextPtrAtOnce(false);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return layoutManager.findFirstCompletelyVisibleItemPosition() == 0;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mLoadMoreListener.setPagination(1);//恢复第一页
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                        ptrFrameLayout.refreshComplete();
                        mRecyclerView.scrollToPosition(0);
                    }
                }, 500);
            }
        });


        mAdapter.setHasMoreData(true);
        mLoadMoreListener = new EndlessRecyclerOnScrollListener(layoutManager) {

            @Override
            public void onLoadMore(final int pagination, int pageSize) {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setHasFooter(true);
                        mAdapter.notifyDataSetChanged();
                    }
                });


                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int position = mAdapter.getItemCount();
                        if (mAdapter.getItemCount() > 50) {
                            mAdapter.setHasMoreDataAndFooter(false, true);
                        } else {
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                        }
                        mAdapter.notifyDataSetChanged();
                        //java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
                        //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - 5, 5);
                        mRecyclerView.scrollToPosition(position);
                        loadComplete();

                    }
                }, 1500);
            }
        };
        mRecyclerView.addOnScrollListener(mLoadMoreListener);
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
