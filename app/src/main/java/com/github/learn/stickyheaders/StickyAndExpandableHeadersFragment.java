package com.github.learn.stickyheaders;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.captain_miao.recyclerviewutils.common.DividerItemDecoration;
import com.github.learn.refreshandload.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YanLu
 * @since 16/7/30
 */
public class StickyAndExpandableHeadersFragment extends Fragment {


    private List<DetectionModel> mDataList = new ArrayList<>();
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
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        // Set adapter populated with example dummy data
        initMockData();
        final StickyAndExpandableAdapter adapter = new StickyAndExpandableAdapter(recyclerView);
        adapter.addAll(mDataList);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

        // Set layout manager
        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), orientation, false);
        recyclerView.setLayoutManager(layoutManager);

        // Add decoration for dividers between list items
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));


        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private int getLayoutManagerOrientation(int activityOrientation) {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return LinearLayoutManager.VERTICAL;
        } else {
            return LinearLayoutManager.HORIZONTAL;
        }
    }


    private void initMockData() {
        for(int i = 0; i < 20; i++){
            List<DetectionModel> detectionVoList = new ArrayList<>();
            for(int j= 0; j < 20; j++) {
                DetectionModel model = new DetectionModel(i, false);
                model.categoryId = i;
                model.category = i +" Header ";
                model.title =  i +" Title " + j;
                model.value = i +" Value " + j;
                model.isQualified = j % 2 == 0;

                detectionVoList.add(model);
            }

            DetectionModel model = new DetectionModel(i, false);
            model.categoryId = i;
            model.category = i +" Header ";
            model.parentItem = true;
            model.childItems.addAll(detectionVoList);

            mDataList.add(model);
        }
    }


}
