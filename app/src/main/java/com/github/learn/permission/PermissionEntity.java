package com.github.learn.permission;

import com.github.learn.refreshandload.R;

import java.io.Serializable;

/**
 * @author YanLu
 * @since 16/3/18
 */
public class PermissionEntity implements Serializable {

    public String permissionName;
    public PermissionStatus permissionStatus;
    public String permissionLevel;


    public int getStatusIcon(){
        if(permissionStatus == PermissionStatus.GRANTED) {
            return R.drawable.ic_granted_24dp;
        } else if(permissionStatus == PermissionStatus.DENIED){
            return R.drawable.ic_denied_24dp;
        } else {
            return R.drawable.ic_request_24dp;
        }
    }

}
