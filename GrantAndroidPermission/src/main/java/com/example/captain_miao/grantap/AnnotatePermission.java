package com.example.captain_miao.grantap;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.util.Log;

import com.example.captain_miao.grantap.annotation.PermissionCheck;
import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;
import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.example.captain_miao.grantap.utils.ObjectUtils;
import com.example.captain_miao.grantap.utils.PermissionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Copyright 2016 Ted Park
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.```
 *
 *
 * modify from https://github.com/ParkSangGwon/TedPermission
 */

public class AnnotatePermission implements PermissionListener {
    private static final String TAG = "AnnotatePermission";
    private Object   object;
    private final Context mContext;

    private int      mRequestCode;
    private String[] mPermissions;
    private String   mRationaleConfirmText;
    private String   mRationaleMessage;

    private String   mDenyMessage;
    private String   mDeniedCloseButtonText;

    private boolean  mHasSettingBtn = false;
    private String   mPackageName;

    public AnnotatePermission(Object object, Context context) {
        this.object = object;
        this.mContext = context;
    }

    public static AnnotatePermission from(Object object, Context context) {
        return new AnnotatePermission(object, context);
    }

    /**
     * request permission of requestCode
     * @param requestCode
     * @return
     */
    public AnnotatePermission addRequestCode(int requestCode){
      this.mRequestCode = requestCode;
      return this;
    }


    /**
     * ask for permissions
     * @param permissions
     * @return
     */
    public AnnotatePermission setPermissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    /**
     * explain to the user why your app wants the permissions
     * @param rationaleMessage
     * @return
     */

    public AnnotatePermission setRationaleMsg(String rationaleMessage) {
        this.mRationaleMessage = rationaleMessage;
        return this;
    }

    /**
     * explain to the user why your app wants the permissions
     * @param stringRes
     * @return
     */
    public AnnotatePermission setRationaleMsg(@StringRes int stringRes) {
        if (stringRes <= 0) {
            throw new IllegalArgumentException("Invalid value for RationaleMessage");
        }
        this.mRationaleMessage = mContext.getString(stringRes);
        return this;
    }

    /**
     * The text to display in the positive button of rationale message dialog
     * @param rationaleConfirmText
     * @return
     */
    public AnnotatePermission setRationaleConfirmText(String rationaleConfirmText) {

        this.mRationaleConfirmText = rationaleConfirmText;
        return this;
    }
    /**
     * The text to display in the positive button of rationale message dialog
     * @param stringRes
     * @return
     */
    public AnnotatePermission setRationaleConfirmText(@StringRes int stringRes) {

        if (stringRes <= 0) {
            throw new IllegalArgumentException("Invalid value for RationaleConfirmText");
        }
        this.mRationaleConfirmText = mContext.getString(stringRes);

        return this;
    }


    /**
     * when user deny permission, show deny message
     * @param denyMessage
     * @return
     */
    public AnnotatePermission setDeniedMsg(String denyMessage) {
        this.mDenyMessage = denyMessage;
        return this;
    }
    /**
     * when user deny permission, show deny message
     * @param stringRes
     * @return
     */
    public AnnotatePermission setDeniedMsg(@StringRes int stringRes) {
        if (stringRes <= 0) {
            throw new IllegalArgumentException("Invalid value for DeniedMessage");
        }
        this.mDenyMessage = mContext.getString(stringRes);
        return this;
    }


    /**
     * The text to display in the close button of deny message dialog
     * @param stringRes
     * @return
     */
    public AnnotatePermission setDeniedCloseButtonText(@StringRes int stringRes) {

        if (stringRes <= 0) {
            throw new IllegalArgumentException("Invalid value for DeniedCloseButtonText");
        }
        this.mDeniedCloseButtonText = mContext.getString(stringRes);

        return this;
    }
    /**
     * The text to display in the close button of deny message dialog
     * @param deniedCloseButtonText
     * @return
     */
    public AnnotatePermission setDeniedCloseButtonText(String deniedCloseButtonText) {

        this.mDeniedCloseButtonText = deniedCloseButtonText;
        return this;
    }


    public AnnotatePermission setGotoSettingButton(boolean hasSettingBtn) {

        this.mHasSettingBtn = hasSettingBtn;
        return this;
    }

    /**
     * show the packageName setting button
     * @param packageName
     * @return
     */
    public AnnotatePermission setPackageName(String packageName) {

        this.mPackageName = packageName;
        return this;
    }

    // requestPermissions
    public void check() {
        if (ObjectUtils.isEmpty(mPermissions)) {
            mPermissions = PermissionUtils.findPermissionsWithRequestCode(object, object.getClass(), PermissionCheck.class, mRequestCode);
        }
        if (ObjectUtils.isEmpty(mPermissions)) {
            throw new NullPointerException("You must setPermissions()");
        } else {
            if (PermissionUtils.isOverMarshmallow()) {
                Log.d(TAG, "Marshmallow");
                requestPermissions();
            } else {
                Log.d(TAG, "pre Marshmallow");
                permissionGranted();
            }
        }
    }


    private void requestPermissions( ){
        ShadowPermissionActivity.setPermissionListener(this);
        Intent intent = new Intent(mContext, ShadowPermissionActivity.class);
        intent.putExtra(ShadowPermissionActivity.EXTRA_PERMISSIONS, mPermissions);
        intent.putExtra(ShadowPermissionActivity.EXTRA_RATIONALE_MESSAGE, mRationaleMessage);
        intent.putExtra(ShadowPermissionActivity.EXTRA_RATIONALE_CONFIRM_TEXT, mRationaleConfirmText);
        intent.putExtra(ShadowPermissionActivity.EXTRA_PACKAGE_NAME, mPackageName);
        intent.putExtra(ShadowPermissionActivity.EXTRA_SETTING_BUTTON, mHasSettingBtn);
        intent.putExtra(ShadowPermissionActivity.EXTRA_DENY_MESSAGE, mDenyMessage);
        intent.putExtra(ShadowPermissionActivity.EXTRA_DENIED_DIALOG_CLOSE_TEXT, mDeniedCloseButtonText);
        mContext.startActivity(intent);
    }


    @Override
    public void permissionGranted() {
        doExecuteGranted(object, mRequestCode);
    }

    @Override
    public void permissionDenied() {
        doExecuteDenied(object, mRequestCode);
    }


    private static void doExecuteGranted(Object activity, int requestCode) {
        Method executeMethod = PermissionUtils.findMethodWithRequestCode(activity.getClass(),
                PermissionGranted.class, requestCode);

        executeMethod(activity, executeMethod);
    }

    private static void doExecuteDenied(Object activity, int requestCode) {
        Method executeMethod = PermissionUtils.findMethodWithRequestCode(activity.getClass(),
                PermissionDenied.class, requestCode);

        executeMethod(activity, executeMethod);
    }

    private static void executeMethod(Object activity, Method executeMethod) {
        if (executeMethod != null) {
            try {
                if (!executeMethod.isAccessible()) executeMethod.setAccessible(true);
                executeMethod.invoke(activity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
