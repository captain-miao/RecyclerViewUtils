package com.github.learn.expandable;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.github.learn.refreshandload.R;


public class SimpleChildViewHolder extends ChildViewHolder {

    public TextView mTvContent;
    public CheckBox mCheckBox;

    public SimpleChildViewHolder(View itemView) {
        super(itemView);

        mTvContent = (TextView) itemView.findViewById(R.id.child_list_item_view);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.child_list_item_check_box);
    }
}
