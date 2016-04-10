package com.github.captain_miao.recyclerviewutils.common;

import android.content.Context;

import com.github.captain_miao.recyclerviewutils.R;

/**
 * @author YanLu
 * @since 16/4/10
 */
public class DefaultLoadMoreFooterView extends BaseLoadMoreFooterView {


    public DefaultLoadMoreFooterView(Context context) {
        super(context);
    }

    @Override
    public int getLoadMoreLayoutResource() {
        return R.layout.default_load_more_view;
    }
}
