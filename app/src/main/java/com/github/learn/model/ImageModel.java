package com.github.learn.model;


import com.github.learn.refreshandload.R;

/**
 * @author YanLu
 * @since 16/09/03
 */

public class ImageModel extends BaseViewModel {

    public String url;

    public ImageModel(String url) {
        this.url = url;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.rv_item_view_image;
    }
}
