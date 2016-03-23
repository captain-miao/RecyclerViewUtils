package com.example.captain_miao.grantap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;
import com.example.captain_miao.grantap.utils.PermissionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.example.captain_miao.grantap.utils.PermissionUtils.getActivity;

/**
 * Copyright 2015 Seunghwan Kim
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * modify from https://github.com/lovedise/PermissionGen
 */

public class PermissionGen {
  private String[] mPermissions;
  private int mRequestCode;
  private Object object;

  private PermissionGen(Object object) {
    this.object = object;
  }

  public static PermissionGen with(Activity activity){
    return new PermissionGen(activity);
  }

  public static PermissionGen with(Fragment fragment){
    return new PermissionGen(fragment);
  }
  public PermissionGen permissions(String... permissions){
    this.mPermissions = permissions;
    return this;
  }

  public PermissionGen addRequestCode(int requestCode){
    this.mRequestCode = requestCode;
    return this;
  }

  @TargetApi(value = Build.VERSION_CODES.M)
  public void request(){
    requestPermissions(object, mRequestCode, mPermissions);
  }

  public static void needPermission(Activity activity, int requestCode, String[] permissions){
    requestPermissions(activity, requestCode, permissions);
  }

  public static void needPermission(Fragment fragment, int requestCode, String[] permissions){
    requestPermissions(fragment, requestCode, permissions);
  }

  public static void needPermission(Activity activity, int requestCode, String permission){
    needPermission(activity, requestCode, new String[] { permission });
  }

  public static void needPermission(Fragment fragment, int requestCode, String permission){
    needPermission(fragment, requestCode, new String[] { permission });
  }

  @TargetApi(value = Build.VERSION_CODES.M)
  private static void requestPermissions(Object object, int requestCode, String[] permissions){
    if(!PermissionUtils.isOverMarshmallow()) {
      doExecuteSuccess(object, requestCode);
      return;
    }
    List<String> deniedPermissions = PermissionUtils.findDeniedPermissions(getActivity(object), permissions);

    if(deniedPermissions.size() > 0){
      if(object instanceof Activity){
        ((Activity)object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
      } else if(object instanceof Fragment){
        ((Fragment)object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
      } else {
        throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
      }

    } else {
      doExecuteSuccess(object, requestCode);
    }
  }


  private static void doExecuteSuccess(Object activity, int requestCode) {
    Method executeMethod = PermissionUtils.findMethodWithRequestCode(activity.getClass(),
        PermissionGranted.class, requestCode);

    executeMethod(activity, executeMethod);
  }

  private static void doExecuteFail(Object activity, int requestCode) {
    Method executeMethod = PermissionUtils.findMethodWithRequestCode(activity.getClass(),
        PermissionDenied.class, requestCode);

    executeMethod(activity, executeMethod);
  }

  private static void executeMethod(Object activity, Method executeMethod) {
    if(executeMethod != null){
      try {
        if(!executeMethod.isAccessible()) executeMethod.setAccessible(true);
        executeMethod.invoke(activity, new Object());
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }

  public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
      int[] grantResults) {
    requestResult(activity, requestCode, permissions, grantResults);
  }

  public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
      int[] grantResults) {
    requestResult(fragment, requestCode, permissions, grantResults);
  }

  private static void requestResult(Object obj, int requestCode, String[] permissions,
      int[] grantResults){
    List<String> deniedPermissions = new ArrayList<>();
    for(int i=0; i<grantResults.length; i++){
      if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
        deniedPermissions.add(permissions[i]);
      }
    }

    if(deniedPermissions.size() > 0){
      doExecuteFail(obj, requestCode);
    } else {
      doExecuteSuccess(obj, requestCode);
    }
  }
}
