package com.github.learn.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.captain_miao.grantap.CheckAnnotatePermission;
import com.example.captain_miao.grantap.annotation.PermissionCheck;
import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;
import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.github.captain_miao.recyclerviewutils.common.DividerItemDecoration;
import com.github.captain_miao.recyclerviewutils.listener.OnRecyclerItemClickListener;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.learn.refreshandload.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YanLu
 * @since 16/3/18
 */
public class AnnotationPermissionActivity extends AppCompatActivity implements OnRecyclerItemClickListener, PermissionListener {
    private static final String TAG = "PermissionActivity";
    private PermissionAdapter mAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_permission);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mRecyclerView =  (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PermissionAdapter(new ArrayList<PermissionEntity>(), this);
        mRecyclerView.setAdapter(mAdapter);
        new AsyncTask<Boolean, Boolean, List<PermissionEntity>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<PermissionEntity> doInBackground(Boolean... params) {
                return getAllPermissions(AnnotationPermissionActivity.this);
            }

            @Override
            protected void onPostExecute(List<PermissionEntity> permissions) {
                setTitle("size: " + permissions.size());
                mAdapter.appendToList(permissions);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onClick(View v, int position) {
        mRequestPermission = mAdapter.getItem(position);
        PermissionStatus status = mRequestPermission.permissionStatus;
        if (status == PermissionStatus.GRANTED) {
            mRequestPermission = null;
            SuperToast.create(this, "GRANT", SuperToast.Duration.LONG).show();
        } else if (status == PermissionStatus.DENIED) {
            //SuperToast.create(this, "DENY", SuperToast.Duration.LONG).show();
            //} else {
            //请求权限
            CheckAnnotatePermission
                    .from(this, this)
                    .addRequestCode(6699)
                    //.setPermissions(mRequestPermission.permissionName)
                    .check();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private List<PermissionEntity> getAllPermissions(Context context) {
        List<PermissionEntity> permissionEntityList = new ArrayList<>();
        List<PermissionInfo> permissions = PermissionUtils.getAllPermissions(context);
        for(PermissionInfo permissionInfo : permissions){
            if (permissionInfo != null) {
                PermissionEntity entity = new PermissionEntity();
                entity.permissionName = permissionInfo.name;
                Log.d(TAG, getString(R.string.uses_permission_name, entity.permissionName));

                entity.permissionLevel = PermissionUtils.protectionToString(permissionInfo.protectionLevel);

                int status = checkSelfPermission(entity.permissionName);
                if(status == PackageManager.PERMISSION_GRANTED) {
                    entity.permissionStatus = PermissionStatus.GRANTED;
                } else if(status == PackageManager.PERMISSION_DENIED){
                    entity.permissionStatus = PermissionStatus.DENIED;
                } else {
                    entity.permissionStatus = PermissionStatus.REQUEST;
                }
                permissionEntityList.add(entity);
            }
        }

        return permissionEntityList;
    }


    PermissionEntity mRequestPermission;

    @PermissionCheck(requestCode = 6699)
    private String[] mPermissions = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.WRITE_CONTACTS
    };

    @PermissionGranted(requestCode = 6699)
    @Override
    public void permissionGranted() {
        if(mRequestPermission != null) {
            mRequestPermission.permissionStatus = PermissionStatus.GRANTED;
            mAdapter.notifyDataSetChanged();
        }
        SuperToast.create(this, "User Grant", SuperToast.Duration.LONG).show();
    }

    @PermissionDenied(requestCode = 6699)
    @Override
    public void permissionDenied() {
        SuperToast.create(this, "User Denied", SuperToast.Duration.LONG).show();
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
