package com.github.learn.permission;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author YanLu
 * @since 16/3/18
 */
public class PermissionUtils {


    public static List<PermissionInfo> getAllPermissions(Context context) {
//        PackageManager pm = context.getPackageManager();
        List<PermissionInfo> permissionInfoList = new ArrayList<>();


        PackageInfo android;
        try {
            android = context.getPackageManager().getPackageInfo("android", PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return permissionInfoList;
        }


        for (PermissionInfo permission : android.permissions) {
            permissionInfoList.add(permission);
        }




//        List<PermissionGroupInfo> groupList = pm.getAllPermissionGroups(0);
//        groupList.add(null); // ungrouped permissions
//
//        for (PermissionGroupInfo permissionGroup : groupList) {
//            String name = permissionGroup == null ? null : permissionGroup.name;
//            try {
//                permissions.addAll(pm.queryPermissionsByGroup(name, 0));
//            } catch (PackageManager.NameNotFoundException ignored) {
//            }
//        }

        return permissionInfoList;
    }


    public static void printPermissions(Context context) {
        PackageInfo android;
        try {
            android = context.getPackageManager().getPackageInfo("android", PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }


        PermissionInfo[] permissions = android.permissions;
        SparseArrayCompat<List<String>> sparseArray = new SparseArrayCompat<>();
        PermissionInfo permissionInfo;


        int size = permissions.length;
        for (int i = 0; i < size; i++) {
            permissionInfo = permissions[i];
            List<String> list = sparseArray.get(permissionInfo.protectionLevel);
            if (list == null) {
                list = new ArrayList<>(30);
                sparseArray.put(permissionInfo.protectionLevel, list);
            }
            list.add(permissionInfo.name);
        }


        size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            int level = sparseArray.keyAt(i);
            List<String> list = sparseArray.get(level);
            Collections.sort(list);


            Log.i("tag", protectionToString(level));
            for (int j = 0; j < list.size(); j++) {
                Log.i("tag", list.get(j));
            }
        }


    }


    /**
     * Lifted from {@link PermissionInfo}
     */
    public static String protectionToString(int level) {
        String protectionLevel = "????";
        switch (level & PermissionInfo.PROTECTION_MASK_BASE) {
            case PermissionInfo.PROTECTION_NORMAL:
                protectionLevel = "normal";
                break;
            case PermissionInfo.PROTECTION_DANGEROUS:
                protectionLevel = "dangerous";
                break;
            case PermissionInfo.PROTECTION_SIGNATURE:
                protectionLevel = "signature";
                break;
            case PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM:
                protectionLevel = "signatureOrSystem";
                break;
        }
        if ((level & PermissionInfo.PROTECTION_FLAG_PRIVILEGED) != 0) {
            protectionLevel += "|protection privileged";
        }
        if ((level & PermissionInfo.PROTECTION_FLAG_DEVELOPMENT) != 0) {
            protectionLevel += "|protection development";
        }
        if ((level & PermissionInfo.PROTECTION_FLAG_APPOP) != 0) {
            protectionLevel += "|protection appop";
        }
        if ((level & PermissionInfo.PROTECTION_FLAG_PRE23) != 0) {
            protectionLevel += "|protection pre23";
        }
        if ((level & PermissionInfo.PROTECTION_FLAG_INSTALLER) != 0) {
            protectionLevel += "|protection installer";
        }
        if ((level & PermissionInfo.PROTECTION_FLAG_VERIFIER) != 0) {
            protectionLevel += "|protection verifier";
        }
        if ((level & PermissionInfo.PROTECTION_FLAG_PREINSTALLED) != 0) {
            protectionLevel += "|protection preinstalled";
        }
        return protectionLevel;
    }

}
