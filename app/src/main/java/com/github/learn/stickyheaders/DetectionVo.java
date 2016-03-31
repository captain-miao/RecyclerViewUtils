package com.github.learn.stickyheaders;

import java.io.Serializable;

/**
 * @author YanLu
 * @since 16/3/28
 */
public class DetectionVo implements Serializable{
    public int categoryId;
    public String category;
    public String title;
    public String value;
    public boolean isQualified;

    public DetectionVo(int categoryId, String category, String title, String value) {
        this.categoryId = categoryId;
        this.category = category;
        this.title = title;
        this.value = value;
        this.isQualified = true;
    }

    public DetectionVo(int categoryId, String category, String title, String value, boolean isQualified) {
        this.categoryId = categoryId;
        this.category = category;
        this.title = title;
        this.value = value;
        this.isQualified = isQualified;
    }
}
