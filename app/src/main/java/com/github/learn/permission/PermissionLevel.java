package com.github.learn.permission;

import android.content.pm.PermissionInfo;

/**
 * @author YanLu
 * @since 16/3/18
 */
public enum PermissionLevel {
    NORMAL(PermissionInfo.PROTECTION_NORMAL, "NORMAL"),
    DANGEROUS(PermissionInfo.PROTECTION_DANGEROUS, "DANGEROUS"),
    SYSTEM(PermissionInfo.PROTECTION_SIGNATURE, "SYSTEM"),
    UNKNOWN(-1, "UNKNOWN");


    int code;
    String level;
    PermissionLevel(int code, String level) {
        this.code = code;
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
