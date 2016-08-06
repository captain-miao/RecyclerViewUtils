package com.github.captain_miao.recyclerviewutils.common;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/*
 * Copyright 2016 right https://github.com/zokipirlo
 * from https://gist.github.com/zokipirlo/82336d89249e05bba5aa
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;
    private boolean mShowFirstDivider = false;
    private boolean mShowLastDivider = false;

    int mOrientation = -1;

    public SpaceItemDecoration(Context context, AttributeSet attrs) {
        mSpace = 0;
    }

    public SpaceItemDecoration(Context context, AttributeSet attrs, boolean showFirstDivider,
                               boolean showLastDivider) {
        this(context, attrs);
        mShowFirstDivider = showFirstDivider;
        mShowLastDivider = showLastDivider;
    }

    public SpaceItemDecoration(int spaceInPx) {
        mSpace = spaceInPx;
    }

    public SpaceItemDecoration(int spaceInPx, boolean showFirstDivider,
                               boolean showLastDivider) {
        this(spaceInPx);
        mShowFirstDivider = showFirstDivider;
        mShowLastDivider = showLastDivider;
    }

    public SpaceItemDecoration(Context ctx, int resId) {
        mSpace = ctx.getResources().getDimensionPixelSize(resId);
    }

    public SpaceItemDecoration(Context ctx, int resId, boolean showFirstDivider,
                               boolean showLastDivider) {
        this(ctx, resId);
        mShowFirstDivider = showFirstDivider;
        mShowLastDivider = showLastDivider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mSpace == 0) {
            return;
        }

        if (mOrientation == -1)
            getOrientation(parent);

        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION || (position == 0 && !mShowFirstDivider)) {
            return;
        }

        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.top = mSpace;
            if (mShowLastDivider && position == (state.getItemCount() - 1)) {
                outRect.bottom = outRect.top;
            }
        } else {
            outRect.left = mSpace;
            if (mShowLastDivider && position == (state.getItemCount() - 1)) {
                outRect.right = outRect.left;
            }
        }
    }

    private int getOrientation(RecyclerView parent) {
        if (mOrientation == -1) {
            if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
                mOrientation = layoutManager.getOrientation();
            } else {
                throw new IllegalStateException(
                        "DividerItemDecoration can only be used with a LinearLayoutManager.");
            }
        }
        return mOrientation;
    }
}
