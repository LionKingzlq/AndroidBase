package com.chetuan.askforit.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.chetuan.askforit.receiver.OnReconnectedListener;
import com.chetuan.askforit.util.ActivityUtil;

import java.util.ArrayList;

/**
 * Created by YT on 2015/11/16.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            if(activeInfo != null)
            {
                ArrayList<Activity> activities = ActivityUtil.getInstance().getActivities();
                int activitySize = activities.size();
                for(int i = 0; i < activitySize; i ++)
                {
                    Activity temp = activities.get(i);
                    if(temp != null && !temp.isFinishing() && temp instanceof OnReconnectedListener)
                    {
                        ((OnReconnectedListener)temp).onReconnected();
                    }
                }
            }
        }
        catch (Exception e) {
            Log.e("xiyuan", e.getMessage(), e);
        }
    }
}
