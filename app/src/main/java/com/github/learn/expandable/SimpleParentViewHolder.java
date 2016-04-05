package com.github.learn.expandable;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.github.learn.refreshandload.R;

public class SimpleParentViewHolder extends ParentViewHolder {
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    public TextView mTvTitle;
    public ImageButton mParentDropDownArrow;

    public SimpleParentViewHolder(View itemView) {
        super(itemView);

        mTvTitle = (TextView) itemView.findViewById(R.id.parent_list_item_title_text_view);
        mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_list_item_expand_arrow);
        mParentDropDownArrow.setClickable(false);
    }

    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }

        if (expanded) {
            mParentDropDownArrow.setRotation(ROTATED_POSITION);
        } else {
            mParentDropDownArrow.setRotation(INITIAL_POSITION);
        }
    }
}