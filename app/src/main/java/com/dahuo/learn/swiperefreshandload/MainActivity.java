package com.dahuo.learn.swiperefreshandload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dahuo.learn.swiperefreshandload.view.SwipeRefreshAndLoadLayout;

import java.util.ArrayList;


public class MainActivity extends Activity implements SwipeRefreshAndLoadLayout.OnRefreshListener {

    protected ListView mListView;
    private ArrayAdapter<String> mListAdapter;
    SwipeRefreshAndLoadLayout mSwipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        mListView = (ListView) findViewById(R.id.list);
        mListAdapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, values);
		mListView.setAdapter(mListAdapter);


        mSwipeLayout = (SwipeRefreshAndLoadLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                                    android.R.color.holo_green_light,
                                    android.R.color.holo_orange_light,
                                    android.R.color.holo_red_light);
        mSwipeLayout.setmMode(SwipeRefreshAndLoadLayout.Mode.BOTH);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, TextActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    ArrayList<String> values = new ArrayList<String>() {{
        add("Android");
        add("iPhone");
        add("WindowsMobile");
        add("Blackberry");
//        add("WebOS");
//        add("Ubuntu");
//        add("Windows7");
//        add("Max OS X");
//        add("Linux");
//        add("OS/2");
//        add("Ubuntu");
//        add("Windows7");
//        add("Max OS X");
//        add("Linux");
//        add("OS/2");
//        add("Ubuntu");
//        add("Windows7");
//        add("Max OS X");
//        add("Linux");
//        add("OS/2");
//        add("Android");
//        add("iPhone");
//        add("WindowsMobile");
    }};

    @Override
    public void onRefresh() {
        values.add(0, "Add " + values.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
                mListAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        values.add("Add " + values.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
                mListAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }
}
