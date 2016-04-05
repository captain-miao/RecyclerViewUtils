package com.bignerdranch.expandablerecyclerview.Model;

import java.util.List;

/*
 *  Copyright 2016 https://github.com/bignerdranch/expandable-recycler-view
 *
 * The MIT License
 *
 * Copyright (c) 2015 Big Nerd Ranch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *    from https://github.com/bignerdranch/expandable-recycler-view
 */

/**
 * Wrapper used to link expanded state with a {@link ParentListItem}.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 6/11/15
 */
public class ParentWrapper {

    private boolean mExpanded;
    private ParentListItem mParentListItem;

    /**
     * Default constructor.
     *
     * @param parentListItem The {@link ParentListItem} to wrap
     */
    public ParentWrapper(ParentListItem parentListItem) {
        mParentListItem = parentListItem;
        mExpanded = false;
    }

    /**
     * Gets the {@link ParentListItem} being wrapped.
     *
     * @return The {@link ParentListItem} being wrapped
     */
    public ParentListItem getParentListItem() {
        return mParentListItem;
    }

    /**
     * Sets the {@link ParentListItem} to wrap.
     *
     * @param parentListItem The {@link ParentListItem} to wrap
     */
    public void setParentListItem(ParentListItem parentListItem) {
        mParentListItem = parentListItem;
    }

    /**
     * Gets the expanded state associated with the {@link ParentListItem}.
     *
     * @return true if expanded, false if not
     */
    public boolean isExpanded() {
        return mExpanded;
    }

    /**
     * Sets the expanded state associated with the {@link ParentListItem}.
     *
     * @param expanded true if expanded, false if not
     */
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public boolean isInitiallyExpanded() {
        return mParentListItem.isInitiallyExpanded();
    }

    public List<?> getChildItemList() {
        return mParentListItem.getChildItemList();
    }
}
