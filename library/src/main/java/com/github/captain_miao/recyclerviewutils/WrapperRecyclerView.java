package com.github.captain_miao.recyclerviewutils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * @author YanLu
 * @since 16/3/23
 */
public class WrapperRecyclerView extends FrameLayout {
    private static final String TAG = "RefreshRecyclerView";

    private RecyclerView mRecyclerView;
    private BaseWrapperRecyclerAdapter mAdapter;
    private PtrFrameLayout mPtrFrameLayout;
    private RefreshRecyclerViewListener mRecyclerViewListener;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    public WrapperRecyclerView(Context context) {
        super(context);
        initRefreshRecyclerView(context);
    }

    public WrapperRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRefreshRecyclerView(context);
    }

    public WrapperRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initRefreshRecyclerView(context);
    }

    private void initRefreshRecyclerView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.refresh_recycler_view, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.service_recycler_view);
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.material_style_ptr_frame);

        // header
        final MaterialHeader header = new MaterialHeader(context);
        //header.setColorSchemeColors(new int[]{R.color.line_color_run_speed_13});
        int[] colors = getResources().getIntArray(R.array.refresh_progress_bar_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrameLayout);


        mPtrFrameLayout.setDurationToCloseHeader(500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.setEnabledNextPtrAtOnce(false);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
        if (layout instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layout;

            mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int pagination, int pageSize) {
                    if(mRecyclerViewListener != null){
                        mRecyclerViewListener.onLoadMore(pagination, pageSize);
                    }
                }
            };
            mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);


            mPtrFrameLayout.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return mEndlessRecyclerOnScrollListener.checkCanBePulledDown();
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    mEndlessRecyclerOnScrollListener.setPagination(1);//恢复第一页
                    if(mRecyclerViewListener != null){
                        mRecyclerViewListener.onRefresh();
                    }
                }
            });
            if(layout instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layout;
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        //加载更多 占领 整个一行
                        if (!mAdapter.isContentView(position)) {
                            return gridLayoutManager.getSpanCount();//number of columns of the grid
                        } else {
                            return 1;
                        }
                    }

                });
            }

        } else {
            Log.e(TAG, "only support LinearLayoutManager");
        }
    }
    //about adapterRecyclerView
    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor, -1);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        mRecyclerView.addItemDecoration(decor, index);
    }


    //about adapter
    public void setAdapter(BaseWrapperRecyclerAdapter adapter){
        this.mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
    }


    //about refresh
    public void refreshComplete() {
        mPtrFrameLayout.refreshComplete();
    }

    public void autoRefresh() {
        autoRefresh(true, 1000);
    }

    public void autoRefresh(boolean atOnce) {
        autoRefresh(atOnce, 1000);
    }

    public void autoRefresh(boolean atOnce, int duration) {
        mPtrFrameLayout.autoRefresh(atOnce, duration);
    }


    //about load more
    public void setPageSize(int pageSize){
        mEndlessRecyclerOnScrollListener.setPageSize(pageSize);
        mEndlessRecyclerOnScrollListener.setPagination(pageSize);
    }
    public void setPagination(int pagination){
        mEndlessRecyclerOnScrollListener.setPagination(pagination);
    }

    public void disableLoadMore(){
        mEndlessRecyclerOnScrollListener.setLoadMoreEnable(false);
    }
    public void enableLoadMore(){
        mEndlessRecyclerOnScrollListener.setLoadMoreEnable(true);
    }
    public void loadMoreComplete(){
        mEndlessRecyclerOnScrollListener.loadComplete();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public PtrFrameLayout getPtrFrameLayout() {
        return mPtrFrameLayout;
    }

    public void setRecyclerViewListener(RefreshRecyclerViewListener recyclerViewListener) {
        this.mRecyclerViewListener = recyclerViewListener;
    }

    public RefreshRecyclerViewListener getRecyclerViewListener() {
        return mRecyclerViewListener;
    }


}
