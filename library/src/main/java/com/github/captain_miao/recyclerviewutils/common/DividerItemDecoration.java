package com.github.captain_miao.recyclerviewutils.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/*
 * Copyright 2016 right https://github.com/zokipirlo
 * from https://gist.github.com/zokipirlo/82336d89249e05bba5aa
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;
    private boolean mShowFirstDivider = false;
    private boolean mShowLastDivider = false;

    int mOrientation = -1;

    private DividerJudge mDividerJudge;

    public interface DividerJudge{
        //是否需要显示分割线
        boolean isDecorate(View view, RecyclerView parent);
    }
    public DividerItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public DividerItemDecoration(Context context, DividerJudge dividerJudge) {
        this(context);
        mDividerJudge = dividerJudge;
    }

    public DividerItemDecoration(Context context, AttributeSet attrs) {
        final TypedArray a = context
                .obtainStyledAttributes(attrs, new int[]{android.R.attr.listDivider});
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public DividerItemDecoration(Context context, AttributeSet attrs, boolean showFirstDivider,
                                 boolean showLastDivider) {
        this(context, attrs);
        mShowFirstDivider = showFirstDivider;
        mShowLastDivider = showLastDivider;
    }

    public DividerItemDecoration(Context context, int resId) {
        mDivider = ContextCompat.getDrawable(context, resId);
    }

    public DividerItemDecoration(Context context, int resId, boolean showFirstDivider,
                                 boolean showLastDivider) {
        this(context, resId);
        mShowFirstDivider = showFirstDivider;
        mShowLastDivider = showLastDivider;
    }

    public DividerItemDecoration(Drawable divider) {
        mDivider = divider;
    }

    public DividerItemDecoration(Drawable divider, boolean showFirstDivider,
                                 boolean showLastDivider) {
        this(divider);
        mShowFirstDivider = showFirstDivider;
        mShowLastDivider = showLastDivider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mDivider == null) {
            return;
        }

        if (mDividerJudge != null && !mDividerJudge.isDecorate(view, parent)) {
            return;
        }

        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION || (position == 0 && !mShowFirstDivider)) {
            return;
        }

        if (mOrientation == -1)
            getOrientation(parent);

        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.top = mDivider.getIntrinsicHeight();
            if (mShowLastDivider && position == (state.getItemCount() - 1)) {
                outRect.bottom = outRect.top;
            }
        } else {
            outRect.left = mDivider.getIntrinsicWidth();
            if (mShowLastDivider && position == (state.getItemCount() - 1)) {
                outRect.right = outRect.left;
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            super.onDrawOver(c, parent, state);
            return;
        }

        // Initialization needed to avoid compiler warning
        int left = 0, right = 0, top = 0, bottom = 0, size;
        int orientation = mOrientation != -1 ? mOrientation : getOrientation(parent);
        int childCount = parent.getChildCount();

        if (orientation == LinearLayoutManager.VERTICAL) {
            size = mDivider.getIntrinsicHeight();
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else { //horizontal
            size = mDivider.getIntrinsicWidth();
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
        }

        for (int i = mShowFirstDivider ? 0 : 1; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (mDividerJudge == null || mDividerJudge.isDecorate(child, parent)) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                if (orientation == LinearLayoutManager.VERTICAL) {
                    top = child.getTop() - params.topMargin - size;
                    bottom = top + size;
                } else { //horizontal
                    left = child.getLeft() - params.leftMargin;
                    right = left + size;
                }
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        // show last divider
        if (mShowLastDivider && childCount > 0) {
            View child = parent.getChildAt(childCount - 1);
            if (mDividerJudge == null || mDividerJudge.isDecorate(child, parent)) {
                if (parent.getChildAdapterPosition(child) == (state.getItemCount() - 1)) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        top = child.getBottom() + params.bottomMargin;
                        bottom = top + size;
                    } else { // horizontal
                        left = child.getRight() + params.rightMargin;
                        right = left + size;
                    }
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
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

    public DividerJudge getDividerJudge() {
        return mDividerJudge;
    }

    public void setDividerJudge(DividerJudge mDividerJudge) {
        this.mDividerJudge = mDividerJudge;
    }

    public boolean isShowFirstDivider() {
        return mShowFirstDivider;
    }

    public void setShowFirstDivider(boolean mShowFirstDivider) {
        this.mShowFirstDivider = mShowFirstDivider;
    }

    public boolean isShowLastDivider() {
        return mShowLastDivider;
    }

    public void setShowLastDivider(boolean mShowLastDivider) {
        this.mShowLastDivider = mShowLastDivider;
    }
}