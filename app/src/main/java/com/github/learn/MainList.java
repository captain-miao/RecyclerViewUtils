package com.github.learn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.learn.databinding.DataBindingRecyclerActivity;
import com.github.learn.expandable.ExpandableRecyclerActivity;
import com.github.learn.index.IndexRecyclerActivity;
import com.github.learn.refreshandload.HeaderRecyclerActivity;
import com.github.learn.refreshandload.R;
import com.github.learn.refreshandload.RefreshRecyclerActivity;
import com.github.learn.refreshandload.ViewPagerActivity;
import com.github.learn.refreshandload.gridview.GridViewActivity;
import com.github.learn.refreshandload.gridview.RefreshGridViewActivity;
import com.github.learn.staggeredgrid.StaggeredGridRecyclerActivity;
import com.github.learn.stickyheaders.StickyHeadersActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hesk on 23/5/16.
 */
public class MainList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView mList;
    LinkedHashMap<String, Class> data = new LinkedHashMap<>();
    ArrayList<Class> o = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        mList = (ListView) findViewById(android.R.id.list);
        ArrayList<String> items = new ArrayList<>();
        initList();
        for (Map.Entry<String, Class> entry : data.entrySet()) {
            String key = entry.getKey();
            items.add(key);
            o.add(entry.getValue());
        }
        mList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        mList.setOnItemClickListener(this);
    }

    public void initList() {
        data.put(getString(R.string.label_action_label_header_view), HeaderRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_recycler_view), RefreshRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_grid_view), GridViewActivity.class);
        data.put(getString(R.string.label_action_label_refresh_grid_view), RefreshGridViewActivity.class);
        data.put(getString(R.string.label_action_label_sticky_header_view), StickyHeadersActivity.class);
        data.put(getString(R.string.label_action_label_expandable_view), ExpandableRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_sticky_expandable_view), StickyHeadersActivity.class);
        data.put(getString(R.string.label_action_label_index_view), IndexRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_staggered_grid_view), StaggeredGridRecyclerActivity.class);
        data.put(getString(R.string.label_action_label_data_binding), DataBindingRecyclerActivity.class);
        data.put("Demo Index Recycler Activity", IndexRecyclerActivity.class);
        data.put("Viewpager Activity", ViewPagerActivity.class);
        data.put("Header Grid Footer", ViewPagerActivity.class);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, o.get(position));
        if (position == 6) {
            intent.putExtra("StickyAndExpandable", true);
        }
        if (position == 2) {
            intent.putExtra(GridViewActivity.span_count, 2);
        }
        if (position == 3) {
            intent.putExtra(GridViewActivity.span_count, 3);
        }
        startActivity(intent);
    }


}
