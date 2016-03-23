package com.example.captain_miao.grantap;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.StringRes;
import android.util.Log;

import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.example.captain_miao.grantap.utils.ObjectUtils;

/**
 * @author YanLu
 * @since 16/3/19
 */

public class ListenerPermission {
    private static final String TAG = "ListenerPermission";
    private final Context mContext;
    private PermissionListener mPermissionListener;
    private boolean  hasSettingBtn = false;

    private String[] mPermissions;
    private String   mRationaleConfirmText;
    private String   mRationaleMessage;

    private String   mDenyMessage;
    private String   mDeniedCloseButtonText;

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


    public ListenerPermission setGotoSettingButton(boolean hasSettingBtn) {

        this.hasSettingBtn = hasSettingBtn;
        return this;
    }


    public void check() {

        if (mPermissionListener == null) {
            throw new NullPointerException("You must setPermissionListener() on ListenerPermission");
        } else if (ObjectUtils.isEmpty(mPermissions)) {
            throw new NullPointerException("You must setPermissions() on ListenerPermission");
        }


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d(TAG, "pre Marshmallow");
            mPermissionListener.permissionGranted();

        } else {
            Log.d(TAG, "Marshmallow");
            requestPermissions();
        }
    }


    public void requestPermissions( ){
        ShadowPermissionActivity.setPermissionListener(mPermissionListener);
        Intent intent = new Intent(mContext, ShadowPermissionActivity.class);
        intent.putExtra(ShadowPermissionActivity.EXTRA_PERMISSIONS, mPermissions);
        intent.putExtra(ShadowPermissionActivity.EXTRA_RATIONALE_MESSAGE, mRationaleMessage);
        intent.putExtra(ShadowPermissionActivity.EXTRA_RATIONALE_CONFIRM_TEXT, mRationaleConfirmText);
        intent.putExtra(ShadowPermissionActivity.EXTRA_PACKAGE_NAME, "com.github.learn.refreshandload");
        intent.putExtra(ShadowPermissionActivity.EXTRA_SETTING_BUTTON, hasSettingBtn);
        intent.putExtra(ShadowPermissionActivity.EXTRA_DENY_MESSAGE, mDenyMessage);
        intent.putExtra(ShadowPermissionActivity.EXTRA_DENIED_DIALOG_CLOSE_TEXT, mDeniedCloseButtonText);
        mContext.startActivity(intent);
    }



    public static void requestPermissions(Context context, String[] permissions, PermissionListener permissionListener){
        ShadowPermissionActivity.setPermissionListener(permissionListener);
        Intent intent = new Intent(context, ShadowPermissionActivity.class);
        intent.putExtra(ShadowPermissionActivity.EXTRA_PERMISSIONS, permissions);
        intent.putExtra(ShadowPermissionActivity.EXTRA_RATIONALE_MESSAGE, "申请权限，爷，给个呗。");
        intent.putExtra(ShadowPermissionActivity.EXTRA_DENY_MESSAGE, "爷没给权限啊啊啊啊啊啊。");
        intent.putExtra(ShadowPermissionActivity.EXTRA_PACKAGE_NAME, "com.github.learn.refreshandload");
        intent.putExtra(ShadowPermissionActivity.EXTRA_SETTING_BUTTON, true);
        intent.putExtra(ShadowPermissionActivity.EXTRA_RATIONALE_CONFIRM_TEXT, "知道了");
        intent.putExtra(ShadowPermissionActivity.EXTRA_DENIED_DIALOG_CLOSE_TEXT, "关闭");
        context.startActivity(intent);
    }

}
