package com.bignerdranch.expandablerecyclerview.ViewHolder;

import android.view.View;

import com.github.captain_miao.recyclerviewutils.common.ClickableViewHolder;


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
 * ViewHolder for a child list
 * item.
 * <p>
 * The user should extend this class and implement as they wish for their
 * child list item.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class ChildViewHolder extends ClickableViewHolder {

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public ChildViewHolder(View itemView) {
        super(itemView);
    }
}