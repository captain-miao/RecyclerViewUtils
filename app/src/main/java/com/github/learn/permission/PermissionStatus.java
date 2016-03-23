package com.github.learn.permission;

/**
 * @author YanLu
 * @since 16/3/18
 */
public enum PermissionStatus {
    REQUEST(1, "需要授权"),
    GRANTED(0, "已经授权"),
    DENIED(-1, "已被禁止");


    int code;
    String status;
    PermissionStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
