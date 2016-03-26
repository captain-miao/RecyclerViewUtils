package com.example.captain_miao.grantap;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.util.Log;

import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.example.captain_miao.grantap.utils.ObjectUtils;
import com.example.captain_miao.grantap.utils.PermissionUtils;

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

public class ListenerPermission {
    private static final String TAG = "ListenerPermission";
    private final Context mContext;
    private PermissionListener mPermissionListener;

    private String[] mPermissions;
    private String   mRationaleConfirmText;
    private String   mRationaleMessage;

    private String   mDenyMessage;
    private String   mDeniedCloseButtonText;

    private boolean  mHasSettingBtn = false;
    private String   mPackageName;

    public ListenerPermission(Context context) {
        this.mContext = context;
    }

    public static ListenerPermission from(Context context) {
        return new ListenerPermission(context);
    }

    /**
     * granted or denied callback
     * @param listener
     * @return
     */
    public ListenerPermission setPermissionListener(PermissionListener listener) {
        this.mPermissionListener = listener;
        return this;
    }

    /**
     * ask for permissions
     * @param permissions
     * @return
     */
    public ListenerPermission setPermissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    /**
     * explain to the user why your app wants the permissions
     * @param rationaleMessage
     * @return
     */

    public ListenerPermission setRationaleMsg(String rationaleMessage) {
        this.mRationaleMessage = rationaleMessage;
        return this;
    }

    /**
     * explain to the user why your app wants the permissions
     * @param stringRes
     * @return
     */
    public ListenerPermission setRationaleMsg(@StringRes int stringRes) {
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
    public ListenerPermission setRationaleConfirmText(String rationaleConfirmText) {

        this.mRationaleConfirmText = rationaleConfirmText;
        return this;
    }
    /**
     * The text to display in the positive button of rationale message dialog
     * @param stringRes
     * @return
     */
    public ListenerPermission setRationaleConfirmText(@StringRes int stringRes) {

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
    public ListenerPermission setDeniedMsg(String denyMessage) {
        this.mDenyMessage = denyMessage;
        return this;
    }
    /**
     * when user deny permission, show deny message
     * @param stringRes
     * @return
     */
    public ListenerPermission setDeniedMsg(@StringRes int stringRes) {
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
    public ListenerPermission setDeniedCloseButtonText(@StringRes int stringRes) {

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
    public ListenerPermission setDeniedCloseButtonText(String deniedCloseButtonText) {

        this.mDeniedCloseButtonText = deniedCloseButtonText;
        return this;
    }

    /**
     * when user deny permission,show the setting button
     * @param hasSettingBtn
     * @return
     */
    public ListenerPermission setGotoSettingButton(boolean hasSettingBtn) {

        this.mHasSettingBtn = hasSettingBtn;
        return this;
    }

    /**
     * show the packageName setting button
     * @param packageName
     * @return
     */
    public ListenerPermission setPackageName(String packageName) {

        this.mPackageName = packageName;
        return this;
    }

    // requestPermissions
    public void check() {

        if (mPermissionListener == null) {
            throw new NullPointerException("You must setPermissionListener() on ListenerPermission");
        } else if (ObjectUtils.isEmpty(mPermissions)) {
            throw new NullPointerException("You must setPermissions() on ListenerPermission");
        }


        if (PermissionUtils.isOverMarshmallow()) {
            Log.d(TAG, "Marshmallow");
            requestPermissions();
        } else {
            Log.d(TAG, "pre Marshmallow");
            mPermissionListener.permissionGranted();
        }
    }


    private void requestPermissions( ){
        ShadowPermissionActivity.setPermissionListener(mPermissionListener);
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

}
