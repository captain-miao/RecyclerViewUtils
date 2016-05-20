package com.github.learn.viewpage;

/**
 * @author YanLu
 * @since 16/5/13
 */
public class GirlViewModel {
    public int bgColor;
    public String title;
    public String description;
    public String imageUrl;


    public GirlViewModel(int bgColor, String title, String description, String imageUrl) {
        this.bgColor = bgColor;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
