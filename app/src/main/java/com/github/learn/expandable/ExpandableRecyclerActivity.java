package com.github.learn.expandable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.github.captain_miao.recyclerviewutils.common.DividerItemDecoration;
import com.github.learn.refreshandload.R;

import java.util.ArrayList;
import java.util.List;

public class ExpandableRecyclerActivity extends AppCompatActivity {

    private SimpleExpandableAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_recycler);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new SimpleExpandableAdapter(this, generateMockData());
        mAdapter.onRestoreInstanceState(savedInstanceState);

        recyclerView.setAdapter(mAdapter);
        //add DividerItemDecoration for child item view
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, new DividerItemDecoration.DividerJudge() {
            @Override
            public boolean isDecorate(View view, RecyclerView parent) {
                RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
                return viewHolder instanceof ChildViewHolder;
            }
        });
        itemDecoration.setShowFirstDivider(false);
        itemDecoration.setShowLastDivider(false);
        recyclerView.addItemDecoration(itemDecoration);
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

    private List<ParentListItem> generateMockData() {
        List<ParentListItem> parentListItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SimpleParentItem simpleParentItem = new SimpleParentItem();
            simpleParentItem.setTitle("ParentItem #" + i);
            simpleParentItem.setSolved(i % 2 == 0);
            List<SimpleChild> childItemList = new ArrayList<>();
            childItemList.add(new SimpleChild(simpleParentItem.getTitle() + "# content 0", simpleParentItem.isSolved()));
            childItemList.add(new SimpleChild(simpleParentItem.getTitle() + "# content 1", simpleParentItem.isSolved()));
            childItemList.add(new SimpleChild(simpleParentItem.getTitle() + "# content 2", simpleParentItem.isSolved()));
            simpleParentItem.setChildItemList(childItemList);
            parentListItems.add(simpleParentItem);
        }
        return parentListItems;
    }
}
