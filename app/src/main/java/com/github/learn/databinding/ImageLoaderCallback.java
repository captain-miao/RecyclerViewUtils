package com.github.learn.databinding;

/**
 * @author YanLu
 * @since 16/4/22
 */
public interface ImageLoaderCallback {
    // called initially
    void onImageLoading();

   // called when image has successfully loaded
   void onImageReady();

   // called when image loading fails
   void onImageLoadError();
}
