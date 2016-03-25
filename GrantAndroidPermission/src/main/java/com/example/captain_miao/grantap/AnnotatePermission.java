package com.example.captain_miao.grantap;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.StringRes;
import android.util.Log;

import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;
import com.example.captain_miao.grantap.annotation.PermissionsRequest;
import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.example.captain_miao.grantap.utils.ObjectUtils;
import com.example.captain_miao.grantap.utils.PermissionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author YanLu
 * @since 16/3/19
 */

public class AnnotatePermission implements PermissionListener {
    private static final String TAG = "AnnotatePermission";
    private Object   object;
    private final Context mContext;
    private boolean  hasSettingBtn = false;

    private int      mRequestCode;
    private String[] mPermissions;
    private String   mRationaleConfirmText;
    private String   mRationaleMessage;

    private String   mDenyMessage;
    private String   mDeniedCloseButtonText;

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

        this.hasSettingBtn = hasSettingBtn;
        return this;
    }


    public void check() {
        if (ObjectUtils.isEmpty(mPermissions)) {
            mPermissions = PermissionUtils.findPermissionsWithRequestCode(object, object.getClass(), PermissionsRequest.class, mRequestCode);
        }
        if (ObjectUtils.isEmpty(mPermissions)) {
            throw new NullPointerException("You must setPermissions()");
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.d(TAG, "pre Marshmallow");
                permissionGranted();

            } else {
                Log.d(TAG, "Marshmallow");
                requestPermissions();
            }
        }
    }


    public void requestPermissions( ){
        ShadowPermissionActivity.setPermissionListener(this);
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
