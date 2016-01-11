package com.chetuan.askforit.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by xujie on 2015/9/17.
 */
public class ResoureUtil {


    private ResoureUtil(){

    }

    private static  ResoureUtil instance = null;

    public static ResoureUtil getInstance(){
        if(instance==null){
            synchronized(ResoureUtil.class){
                if(instance==null){
                    instance=new ResoureUtil();
                }
            }
        }
        return instance;
    }

    public String getResText(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public Drawable getDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    public Spanned html2Text(Context context, int resId, Object... param) {
        String orgin = ResoureUtil.getInstance().getResText(context, resId);
        orgin = String.format(orgin, param);
        return Html.fromHtml(orgin);
    }

    public String[] getStringArr(Context context, int resId) {
        return context.getResources().getStringArray(resId);
    }
}
