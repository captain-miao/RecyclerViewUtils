package com.github.captain_miao.recyclerviewutils.stickyandexpandable;

import java.util.List;

/**
 * @author YanLu
 * @since 16/7/30
 */
public interface StickyHeaderItem<T extends StickyHeaderItem> {

    long getHeaderId();

    boolean isParentItem();

    boolean isExpanded();
    void setExpanded(boolean expanded);


    List<T> getChildItemList();
}
