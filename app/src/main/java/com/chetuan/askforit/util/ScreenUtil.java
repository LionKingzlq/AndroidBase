package com.chetuan.askforit.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by YT on 2015/8/20.
 */
public class ScreenUtil {

    public static Size getScreenSize(Context context)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Size size = new Size(metrics.widthPixels, metrics.heightPixels);
        return size;
    }

    public static class Size
    {
        public int width;

        public int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

}
