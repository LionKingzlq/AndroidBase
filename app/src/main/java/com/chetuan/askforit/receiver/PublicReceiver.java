package com.chetuan.askforit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by YT on 2015/11/2.
 */
public class PublicReceiver extends BroadcastReceiver {

    public OnReceiveListener receiveListener;

    public void setReceiveListener(OnReceiveListener receiveListener) {
        this.receiveListener = receiveListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(receiveListener != null)
        {
            receiveListener.onReceive(context, intent);
        }
    }

    public interface OnReceiveListener
    {
        void onReceive(Context context, Intent intent);
    }
}
