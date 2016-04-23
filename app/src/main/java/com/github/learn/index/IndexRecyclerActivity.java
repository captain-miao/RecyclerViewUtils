package com.github.learn.index;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.captain_miao.recyclerviewutils.common.DividerItemDecoration;
import com.github.captain_miao.recyclerviewutils.common.EasySidebar;
import com.github.learn.refreshandload.R;
import com.github.learn.stickyheaders.DetectionVo;
import com.github.learn.stickyheaders.VehicleDetectionAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IndexRecyclerActivity extends AppCompatActivity implements EasySidebar.OnClickSectionListener {

    private RecyclerView mRecyclerView;
    VehicleDetectionAdapter mAdapter;
    private EasySidebar mSidebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_index_recycler_view);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSidebar = (EasySidebar) this.findViewById(R.id.sidebar);
        TextView floatingTv = (TextView) this.findViewById(R.id.floating_tv);
        mSidebar.setFloatView(floatingTv);
        // Set adapter populated with example dummy data
        mAdapter = new VehicleDetectionAdapter();
        //no header
        //mAdapter.addData(new DetectionVo(-1, " Title ", " Title ", " Value "));
        mAdapter.addAllData(getMockDataSet());
        mRecyclerView.setAdapter(mAdapter);

        // Set layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Add decoration for dividers between list items
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });



        mSidebar.setOnClickSectionListener(this);
        this.mSidebar.setSections(sections);
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


    @Override
    public void onClickSection(int sectionIndex) {
        //scroll to position
        String section = sections.get(sectionIndex);
        int position = sectionsMap.get(section);
        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);
    }

    private List<String> sections = new ArrayList<>();
    private Map<String, Integer> sectionsMap = new HashMap<String, Integer>();
    private List<DetectionVo> getMockDataSet() {
        List<DetectionVo> detectionVoList = new ArrayList<>();
        char A = 'A';
        for(int i = 0; i < 20; i++){
            String section = (char) (A + i) + "";
            sectionsMap.put(section, detectionVoList.size());
            sections.add(section);
            for(int j= 0; j < 20; j++) {
                detectionVoList.add(new DetectionVo( i, section, i +" Title " + j, i +" Value " + j, j % 2 == 0));
            }
        }
        return detectionVoList;
    }

}
