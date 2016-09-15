package com.github.learn.utils;

import android.graphics.Color;

import java.security.SecureRandom;

/**
 * @author YanLu
 * @since 16/9/14
 */
public class RandomDataUtil {

    public static int getRandomColor() {
        SecureRandom secureRandom = new SecureRandom();
        return Color.HSVToColor(150, new float[]{
                secureRandom.nextInt(359), 1, 1
        });
    }
}
