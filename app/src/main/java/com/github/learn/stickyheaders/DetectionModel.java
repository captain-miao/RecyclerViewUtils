package com.github.learn.stickyheaders;

import com.github.captain_miao.recyclerviewutils.stickyandexpandable.StickyHeaderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YanLu
 * @since 16/7/30
 */
public class DetectionModel implements StickyHeaderItem {

    public long headerId;
    public boolean expanded;
    public boolean parentItem;
    public List<DetectionModel> childItems = new ArrayList<>();

    public int categoryId;
    public String category;
    public String title;
    public String value;
    public boolean isQualified;

    public DetectionModel(long headerId, boolean expanded) {
        this.headerId = headerId;
        this.expanded = expanded;
    }

    @Override
    public long getHeaderId() {
        return headerId;
    }

    @Override
    public boolean isParentItem() {
        return parentItem;
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public List getChildItemList() {
        return childItems;
    }
}
