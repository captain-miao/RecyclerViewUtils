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
import android.widget.Toast;

import com.github.learn.refreshandload.R;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YanLu
 * @since 16/3/31
 */
public class StickyHeadersFragment extends Fragment {
    public StickyHeadersFragment() {
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
        final VehicleDetectionAdapter adapter = new VehicleDetectionAdapter();
        //no header
        adapter.addData(new DetectionVo(-1, " Title ", " Title ", " Value "));
        adapter.addAllData(getMockDataSet());
        recyclerView.setAdapter(adapter);

        // Set layout manager
        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), orientation, false);
        recyclerView.setLayoutManager(layoutManager);

        // Add decoration for dividers between list items
        // recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
        recyclerView.addItemDecoration(headersDecor);



        // Add touch listeners
            StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
            touchListener.setOnHeaderClickListener(
                    new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                        @Override
                        public void onHeaderClick(View header, int position, long headerId) {
                            Toast.makeText(getActivity(), "Header position: " + position + ", id: " + headerId,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            recyclerView.addOnItemTouchListener(touchListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private List<DetectionVo> getMockDataSet() {
        List<DetectionVo> detectionVoList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            for(int j= 0; j < 20; j++) {
                detectionVoList.add(new DetectionVo(i, i +" Header ", i +" Title " + j, i +" Value " + j, j % 2 == 0));
            }
        }
        return detectionVoList;
    }

    private int getLayoutManagerOrientation(int activityOrientation) {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return LinearLayoutManager.VERTICAL;
        } else {
            return LinearLayoutManager.HORIZONTAL;
        }
    }
}
