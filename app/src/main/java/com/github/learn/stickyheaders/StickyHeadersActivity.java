package com.github.learn.stickyheaders;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.learn.app.AppConstants;
import com.github.learn.refreshandload.R;


public class StickyHeadersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sticky_header);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            if (getIntent() != null && getIntent().getBooleanExtra(AppConstants.KEY_BOOLEAN, false)) {
                fragmentTransaction.add(R.id.container, new StickyAndExpandableHeadersFragment());
            } else {
                fragmentTransaction.add(R.id.container, new StickyHeadersFragment());
            }
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
            fragmentTransaction.commit();
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

}
