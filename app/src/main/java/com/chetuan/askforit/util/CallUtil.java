package com.chetuan.askforit.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.rl01.lib.utils.StringUtils;

/**
 * Created by YT on 2015/8/26.
 */
public class CallUtil {

    private CallUtil(){

    }
    private static  CallUtil instance = null;

    public static CallUtil getInstance(){
        if(instance==null){
            synchronized(CallUtil.class){
                if(instance==null){
                    instance=new CallUtil();
                }
            }
        }
        return instance;
    }

    public void makeCall(Context context, String phoneNum)

    {
        if(StringUtils.isNull(phoneNum) || context == null)
        {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum.replaceAll(" ", "")));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
