package com.github.learn.refreshandload;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.captain_miao.recyclerviewutils.common.BaseLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.listener.LinearLayoutWithRecyclerOnScrollListener;
import com.github.learn.app.AppConstants;
import com.github.learn.base.BaseActivity;
import com.github.learn.databinding.DataBindingRecyclerActivity;
import com.github.learn.expandable.ExpandableRecyclerActivity;
import com.github.learn.index.IndexRecyclerActivity;
import com.github.learn.refreshandload.adapter.SimpleAdapter;
import com.github.learn.refreshandload.gridview.GridViewActivity;
import com.github.learn.refreshandload.gridview.RefreshGridViewActivity;
import com.github.learn.staggeredgrid.StaggeredGridRecyclerActivity;
import com.github.learn.stickyheaders.StickyHeadersActivity;
import com.github.learn.viewpage.GirlViewModel;
import com.github.learn.viewpage.HeaderViewPageAdapter;
import com.github.learn.viewpage.ViewPageDotView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, PtrFrameLayout.HorizontalMoveArea {

    private SimpleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutWithRecyclerOnScrollListener mLoadMoreListener;
    private PtrFrameLayout mPtrFrameLayout;
    private final int MAX_ITEM_COUNT = 100;
    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.ac_main);

        initRecycleView();

        initViewPage();
        //add header
        mAdapter.addHeaderView(mViewPageContainer);
        mPtrFrameLayout.setHorizontalMoveArea(this);
    }

    private void initRecycleView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SimpleAdapter(new ArrayList<String>());
        mAdapter.setLoadMoreFooterView(new BaseLoadMoreFooterView(this) {
            @Override
            public int getLoadMoreLayoutResource() {
                return R.layout.list_load_more;
            }
        });
        initMockData();
        mRecyclerView.setAdapter(mAdapter);

        mLoadMoreListener = new LinearLayoutWithRecyclerOnScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(final int pagination, int pageSize) {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getItemCount() < MAX_ITEM_COUNT) {
                            mAdapter.showLoadMoreView();
                        } else {
                            mAdapter.showNoMoreDataView();
                        }

                    }
                });


                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //int position = mAdapter.getItemCount();
                        if (mAdapter.getItemCount() >= MAX_ITEM_COUNT) {
                            mAdapter.showNoMoreDataView();
                        } else {
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.append(pagination + " page -> " + mAdapter.getItemCount());
                            mAdapter.notifyDataSetChanged();
                            mAdapter.hideFooterView();
                        }
                        //java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
                        //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - 5, 5);
                        //mRecyclerView.scrollToPosition(position);
                        loadComplete();

                    }
                }, 1500);
            }
        };


        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.material_style_ptr_frame);
        // header
        final MaterialHeader header = new MaterialHeader(this);
        //header.setColorSchemeColors(new int[]{R.color.line_color_run_speed_13});
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrameLayout);


        mPtrFrameLayout.setDurationToCloseHeader(1500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.setEnabledNextPtrAtOnce(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mLoadMoreListener.checkCanDoRefresh();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mLoadMoreListener.setPagination(1);//恢复第一页
                mPtrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrameLayout.refreshComplete();
                        if (mAdapter.getItemCount() < 15) {
                            mAdapter.clear();
                            initMockData();
                        } else {
                            mAdapter.clear();
                            initMockData(5);
                        }
                        mAdapter.hideFooterView();
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(0);
                    }
                }, 500);
            }
        });

        mRecyclerView.addOnScrollListener(mLoadMoreListener);
    }


    //for ViewPage Header
    private ViewPageDotView mWelcomeDotView;
    private RelativeLayout mViewPageContainer;

    public void initViewPage() {
        mViewPageContainer = (RelativeLayout) getLayoutInflater().inflate(R.layout.view_page_header_layout, mRecyclerView, false);
        //mViewPageContainer = (RelativeLayout) findViewById(R.id.vp_container);
        ViewPager mViewPager = (ViewPager) mViewPageContainer.findViewById(R.id.view_page);

        final HeaderViewPageAdapter guideAdapter = new HeaderViewPageAdapter(this);
        guideAdapter.addAll(getGuideViewModels());
        mViewPager.setAdapter(guideAdapter);
        mViewPager.addOnPageChangeListener(this);

        mWelcomeDotView = (ViewPageDotView) mViewPageContainer.findViewById(R.id.dot_view);
        mWelcomeDotView.setNumOfCircles(guideAdapter.getCount(), getResources().getDimensionPixelSize(R.dimen.height_very_small_3));

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mWelcomeDotView.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mWelcomeDotView.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mWelcomeDotView.onPageScrollStateChanged(state);
    }

    @Override
    public boolean isHorizontalMoveArea(float x, float y) {
        Rect rect = new Rect();
        mViewPageContainer.getLocalVisibleRect(rect);
        return rect.contains((int) x, (int) y);
    }




    @Override
    @Deprecated
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_header_recycler_view:
                startActivity(new Intent(this, HeaderRecyclerActivity.class));
                return true;
            case R.id.action_header_grid_recycler_view:
                startActivity(new Intent(this, HeaderRecyclerActivity.class).putExtra(AppConstants.KEY_BOOLEAN, true));
                return true;
            case R.id.action_refresh_recycler_view:
                startActivity(new Intent(this, RefreshRecyclerActivity.class));
                return true;
            case R.id.action_grid_view:
                startActivity(new Intent(this, GridViewActivity.class));
                return true;
            case R.id.action_refresh_grid_view:
                startActivity(new Intent(this, RefreshGridViewActivity.class));
                return true;
            case R.id.action_sticky_header_view:
                startActivity(new Intent(this, StickyHeadersActivity.class));
                return true;
            case R.id.action_expandable_view:
                startActivity(new Intent(this, ExpandableRecyclerActivity.class));
                return true;
            case R.id.action_sticky_expandable_view:
                startActivity(new Intent(this, StickyHeadersActivity.class).putExtra(AppConstants.KEY_BOOLEAN, true));
                return true;
            case R.id.action_index_view:
                startActivity(new Intent(this, IndexRecyclerActivity.class));
                return true;
            case R.id.action_staggered_grid_view:
                startActivity(new Intent(this, StaggeredGridRecyclerActivity.class));
                return true;
            case R.id.action_data_binding_view:
                startActivity(new Intent(this, DataBindingRecyclerActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initMockData(int count){
        for (int i = 0; i < count; i++) {
            mAdapter.appendToTop("1 page -> " + mAdapter.getItemCount() + "");
        }
    }
    private void initMockData() {
        initMockData(25);
    }

    @SuppressWarnings("deprecation")
    private List<GirlViewModel> getGuideViewModels() {
        return new ArrayList<GirlViewModel>() {{
            add(new GirlViewModel(getResources().getColor(R.color.guide_bg_color_1), getString(R.string.guide_title_1),
                    getString(R.string.guide_description_1), "http://ww2.sinaimg.cn/bmiddle/610dc034jw1f3q5semm0wj20qo0hsacv.jpg"));

            add(new GirlViewModel(getResources().getColor(R.color.guide_bg_color_2), getString(R.string.guide_title_2),
                    getString(R.string.guide_description_2), "http://ww3.sinaimg.cn/bmiddle/610dc034jw1f3ozv0wqywj20qo0em0vt.jpg"));

            add(new GirlViewModel(getResources().getColor(R.color.guide_bg_color_3), getString(R.string.guide_title_3),
                    getString(R.string.guide_description_3), "http://ww4.sinaimg.cn/bmiddle/610dc034jw1f3litmfts1j20qo0hsac7.jpg"));
        }};
    }
}
