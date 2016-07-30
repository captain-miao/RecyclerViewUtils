package com.github.captain_miao.recyclerviewutils.stickyandexpandable;

/**
 * @author YanLu
 * @since 16/7/30
 */
public interface StickyHeaderItem {

    long getHeaderId();

    boolean isParentItem();
    boolean isExpanded();
    void setExpanded(boolean expanded);

}
