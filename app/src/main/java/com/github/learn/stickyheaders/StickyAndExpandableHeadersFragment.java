package com.github.learn.stickyheaders;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.captain_miao.recyclerviewutils.common.BaseLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.listener.LinearLayoutWithRecyclerOnScrollListener;
import com.github.learn.refreshandload.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YanLu
 * @since 16/7/30
 */
public class StickyAndExpandableHeadersFragment extends Fragment {

    private StickyAndExpandableAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutWithRecyclerOnScrollListener mLoadMoreListener;

    public StickyAndExpandableHeadersFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_sticky_header, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        // Set adapter populated with example dummy data
        //addMockData(0);
        mAdapter = new StickyAndExpandableAdapter(mRecyclerView);
        mAdapter.addAll(addMockData(0));

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(false);

        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // Add decoration for dividers between list items
        // mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        initLoadMore(layoutManager);

        return view;
    }

    private void initLoadMore(LinearLayoutManager layoutManager) {

        mAdapter.setLoadMoreFooterView(new BaseLoadMoreFooterView(getActivity()) {
            @Override
            public int getLoadMoreLayoutResource() {
                return R.layout.list_load_more;
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
                            mAdapter.addAll(addMockData(position), false);
                            mAdapter.notifyDataSetChanged();
                            mAdapter.hideFooterView();
                        }
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


    private List<DetectionModel> addMockData(int id) {
        List<DetectionModel> dataList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            List<DetectionModel> detectionVoList = new ArrayList<>();
            for(int j= 0; j < 20; j++) {
                DetectionModel model = new DetectionModel(i + "", false);
                model.categoryId = i + id;
                model.category = (i + id) +" Header ";
                model.title =  (i + id) +" Title " + j;
                model.value = (i + id) +" Value " + j;
                model.isQualified = j % 2 == 0;

                detectionVoList.add(model);
            }

            DetectionModel model = new DetectionModel((i + id) + "", false);
            model.categoryId = (i + id);
            model.category = (i + id) +" Header ";
            model.parentItem = true;
            model.childItems.addAll(detectionVoList);

            dataList.add(model);
        }

        return dataList;
    }

}
