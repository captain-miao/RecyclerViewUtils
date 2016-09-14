package com.github.learn.refreshandload.gridview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.captain_miao.recyclerviewutils.common.BaseLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.common.GridItemSpacingDecoration;
import com.github.captain_miao.recyclerviewutils.common.LayoutManagers;
import com.github.captain_miao.recyclerviewutils.listener.LinearLayoutWithRecyclerOnScrollListener;
import com.github.learn.refreshandload.R;
import com.github.learn.refreshandload.adapter.SimpleAdapter;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

import static com.github.learn.refreshandload.adapter.SimpleAdapter.getRandomColor;


public class GridViewActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String span_count = "SPAN_COUNT";
    private SimpleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutWithRecyclerOnScrollListener mLoadMoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, getIntent().getExtras().getInt(span_count));
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.isContentView(position)) {
                    return 1;
                } else if (mAdapter.isHeaderView(position)) {
                    return layoutManager.getSpanCount();
                } else {
                    return layoutManager.getSpanCount();
                }
            }
        });


        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridItemSpacingDecoration(this, R.dimen.grid_item_spacing));
        mAdapter = new SimpleAdapter(values);
        addHeaderView();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreFooterView(new BaseLoadMoreFooterView(this) {
            @Override
            public int getLoadMoreLayoutResource() {
                return R.layout.list_load_more;
            }
        });
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
                return layoutManager.findFirstCompletelyVisibleItemPosition() <= 0;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                mLoadMoreListener.setPagination(1);//恢复第一页
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
//                        mAdapter.notifyDataSetChanged();
                        ptrFrameLayout.refreshComplete();
                        mRecyclerView.scrollToPosition(0);
                    }
                }, 500);
            }
        });

        mLoadMoreListener = new LinearLayoutWithRecyclerOnScrollListener(layoutManager) {

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
                        }
                        mAdapter.notifyDataSetChanged();
                        //java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
                        //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - 5, 5);
                        mRecyclerView.scrollToPosition(position);
                        loadComplete();
                        mAdapter.hideFooterView();

                    }
                }, 1500);
            }
        };
        mRecyclerView.addOnScrollListener(mLoadMoreListener);
    }

    private View mRecyclerViewHeader;
    private TextView mTvHeader;

    private void addHeaderView() {
        if (mAdapter.getHeaderSize() > 0) {
            Toast.makeText(this, "already has a header view", Toast.LENGTH_LONG).show();
        } else {
            mRecyclerViewHeader = LayoutInflater.from(this).inflate(R.layout.recycler_view_header, null);
            mRecyclerViewHeader.findViewById(R.id.btn_header_change_color).setOnClickListener(this);
            mTvHeader = (TextView) mRecyclerViewHeader.findViewById(R.id.tv_header);
            mAdapter.addHeaderView(mRecyclerViewHeader, true);
        }
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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_header_change_color:
                mTvHeader.setTextColor(getRandomColor());
                break;
        }
    }


}
