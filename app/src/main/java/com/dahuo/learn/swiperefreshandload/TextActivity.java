package com.dahuo.learn.swiperefreshandload;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.dahuo.learn.swiperefreshandload.view.SwipeRefreshAndLoadLayout;

import java.util.ArrayList;


public class TextActivity extends Activity implements SwipeRefreshAndLoadLayout.OnRefreshListener {

    protected TextView mTextView;
    SwipeRefreshAndLoadLayout mSwipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_textview);

        mTextView = (TextView) findViewById(R.id.tv);

        updateText();
        mSwipeLayout = (SwipeRefreshAndLoadLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                                    android.R.color.holo_green_light,
                                    android.R.color.holo_orange_light,
                                    android.R.color.holo_red_light);
    }


    ArrayList<String> values = new ArrayList<String>() {{
        add("Android");
        add("iPhone");
        add("WindowsMobile");
        add("Blackberry");
        add("WebOS");
        add("Ubuntu");
        add("Windows7");
        add("Max OS X");
        add("Linux");
        add("OS/2");
        add("Ubuntu");
        add("Windows7");
        add("Max OS X");
        add("Linux");
        add("OS/2");
        add("Ubuntu");
        add("Windows7");
        add("Max OS X");
        add("Linux");
        add("OS/2");
        add("Android");
        add("iPhone");
    }};

    @Override
    public void onRefresh() {
        values.add(0, "Add " + values.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateText();
                mSwipeLayout.setRefreshing(false);
            }
        }, 5000);
    }

    @Override
    public void onLoadMore() {
        values.add("Add " + values.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateText();
                mSwipeLayout.setRefreshing(false);
            }
        }, 5000);
    }


    public void updateText(){
        StringBuilder sb = new StringBuilder();
        for (String v : values) {
            sb.append(v).append("\n");
        }
        mTextView.setText(sb);
    }
}
