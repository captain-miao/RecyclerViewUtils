package com.github.learn.model;

import com.github.captain_miao.uniqueadapter.library.ItemModel;
import com.github.learn.refreshandload.R;

/**
 * @author YanLu
 * @since 16/09/03
 */

public class TextModel extends BaseViewModel implements ItemModel {

    public String text;
    public int    color;

    public TextModel(String text) {
        this.text = text;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.rv_item_view_text;
    }
}
