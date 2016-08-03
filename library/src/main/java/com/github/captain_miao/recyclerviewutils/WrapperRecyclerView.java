package com.github.captain_miao.recyclerviewutils;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.github.captain_miao.recyclerviewutils.listener.LinearLayoutWithRecyclerOnScrollListener;
import com.github.captain_miao.recyclerviewutils.listener.RecyclerOnScrollListener;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.github.captain_miao.recyclerviewutils.listener.StaggeredGridWithRecyclerOnScrollListener;

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
    private RecyclerOnScrollListener mOnScrollListener;

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
        mEmptyViewContainer = (FrameLayout) findViewById(R.id.empty_view_container);

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

    // default enable load more listener
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        setLayoutManager(layout, true);
    }
    public void setLayoutManager(RecyclerView.LayoutManager layout, boolean enableLoadMore) {
        mRecyclerView.setLayoutManager(layout);

        if (layout instanceof LinearLayoutManager) {
            if (enableLoadMore) {
                setLinearLayoutOnScrollListener((LinearLayoutManager) layout);
            }
            setPtrHandler();
            setGridLayoutManager(layout);

        } else if(layout instanceof StaggeredGridLayoutManager) {
            if (enableLoadMore) {
                setStaggeredGridOnScrollListener((StaggeredGridLayoutManager) layout);
            }
            setPtrHandler();
        } else {
            Log.e(TAG, "only support LinearLayoutManager and StaggeredGridLayoutManager");
        }
    }


    private void setStaggeredGridOnScrollListener(StaggeredGridLayoutManager layoutManager){
        mOnScrollListener = new StaggeredGridWithRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int pagination, int pageSize) {
                if(mRecyclerViewListener != null){
                    mRecyclerViewListener.onLoadMore(pagination, pageSize);
                }
            }

            public boolean checkCanDoRefresh() {
                //todo < api 14
                //return !mRecyclerView.canScrollVertically(-1);

                if (android.os.Build.VERSION.SDK_INT < 14) {
                    return !(ViewCompat.canScrollVertically(mRecyclerView, -1) || mRecyclerView.getScrollY() > 0);
                } else {
                    return !ViewCompat.canScrollVertically(mRecyclerView, -1);
                }

            }
        };
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void setLinearLayoutOnScrollListener(LinearLayoutManager layoutManager){
        mOnScrollListener = new LinearLayoutWithRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int pagination, int pageSize) {
                if(mRecyclerViewListener != null){
                    mRecyclerViewListener.onLoadMore(pagination, pageSize);
                }
            }
        };
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void setPtrHandler(){
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mOnScrollListener.checkCanDoRefresh();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mOnScrollListener.setPagination(1);// reset 1th
                if(mRecyclerViewListener != null){
                    mRecyclerViewListener.onRefresh();
                }
            }
        });
    }

    private void setGridLayoutManager(RecyclerView.LayoutManager layout){
        if(layout instanceof GridLayoutManager) {
            GridLayoutManager.SpanSizeLookup lookup = ((GridLayoutManager) layout).getSpanSizeLookup();
            //if user not define, it,s DefaultSpanSizeLookup, then custom it.
            if (lookup instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layout;
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (mAdapter.isContentView(position)) {
                            return 1;
                        } else {
                            //full line
                            return gridLayoutManager.getSpanCount();//number of columns of the grid
                        }
                    }

                });
            }

        }
    }

    //about adapterRecyclerView
    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor, -1);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        mRecyclerView.addItemDecoration(decor, index);
    }

    public void setRecyclerViewPadding(int left, int top, int right, int bottom){
        mRecyclerView.setPadding(left, top, right, bottom);
    }

    public void setRecyclerViewClipToPadding(boolean clipToPadding){
        mRecyclerView.setClipToPadding(clipToPadding);
    }

    //about adapter
    public void setAdapter(BaseWrapperRecyclerAdapter adapter){
        if(mRegisterCheckEmptyView){
            unregisterAdapterDataObserver();
        }
        this.mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);

        // check RegisterCheckEmptyView
        if(mEmptyView != null){
            registerAdapterDataObserver();
        }
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
        mOnScrollListener.setPageSize(pageSize);
        mOnScrollListener.setPagination(pageSize);
    }
    public void setPagination(int pagination){
        mOnScrollListener.setPagination(pagination);
    }


    public void disableRefresh(){
        mPtrFrameLayout.setEnabled(false);
    }

    public void enableRefresh(){
        mPtrFrameLayout.setEnabled(true);
    }

    public void disableLoadMore(){
        if(mOnScrollListener == null){
            throw new IllegalArgumentException("mOnScrollListener is null, this method could only be called after setLayoutManager(layout, true)");
        } else {
            mOnScrollListener.setLoadMoreEnable(false);
        }
    }

    public void enableLoadMore(){
        if(mOnScrollListener == null){
            throw new IllegalArgumentException("mOnScrollListener is null, this method could only be called after setLayoutManager(layout, true)");
        } else {
            mOnScrollListener.setLoadMoreEnable(true);
        }
    }

    public void loadMoreComplete(){
        if(mOnScrollListener != null){
            mOnScrollListener.loadComplete();
        }
    }

    public void showLoadMoreView(){
        mAdapter.showLoadMoreView();
    }
    public void showNoMoreDataView(){
        mAdapter.showNoMoreDataView();
    }
    public void hideFooterView(){
        mAdapter.hideFooterView();
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


    // add empty view
    private View mEmptyView;
    private boolean mRegisterCheckEmptyView = false;
    private FrameLayout mEmptyViewContainer;
    final private RecyclerView.AdapterDataObserver mAdapterObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
          checkIfEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
          checkIfEmptyView();
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            checkIfEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
          checkIfEmptyView();
        }
      };

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;

        if(mEmptyViewContainer.getChildCount() > 0){
            Log.e(TAG, "had empty view...maybe setEmptyView twice");
            mEmptyViewContainer.removeAllViews();
        }

        mEmptyViewContainer.addView(emptyView);

        registerAdapterDataObserver();
    }

    private void registerAdapterDataObserver(){
        if(!mRegisterCheckEmptyView && mEmptyView != null && mAdapter != null){
            mRegisterCheckEmptyView = true;
            mAdapter.registerAdapterDataObserver(mAdapterObserver);
        }
    }

    private void unregisterAdapterDataObserver(){
        if(mRegisterCheckEmptyView){
            mAdapter.registerAdapterDataObserver(mAdapterObserver);
            mRegisterCheckEmptyView = false;
        }
    }

    private void checkIfEmptyView() {
      if (mEmptyView != null && mAdapter != null) {
          if(mAdapter.getItemCount() == 0 || (mAdapter.getItemCount() == 1 && mAdapter.isShowingLoadMoreView())){
              mEmptyViewContainer.setVisibility(VISIBLE);
              mRecyclerView.setVisibility(GONE);
          } else {
              mRecyclerView.setVisibility(VISIBLE);
              mEmptyViewContainer.setVisibility(GONE);
          }
      }
    }
}
