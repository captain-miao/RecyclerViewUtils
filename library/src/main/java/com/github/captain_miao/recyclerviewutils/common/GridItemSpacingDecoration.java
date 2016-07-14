package com.github.captain_miao.recyclerviewutils.common;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author YanLu
 * @since 16/7/14
 */
public class GridItemSpacingDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public GridItemSpacingDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public GridItemSpacingDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
