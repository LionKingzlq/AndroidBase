package com.chetuan.askforit.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by YT on 2015/11/18.
 */
public class DoubleBackPressUtil {

    public static final int DO_FINISH = -1;

    public static final int DO_NOTHING = 0;

    public static final int SHOW_TOAST_NEXTCLICK = 1;

    private static final int MSG_BACKPRESS_OVER = 0;

    private static final int MSG_BACKPRESS_NEXT = 1;

    private int backPressTime = 0;

    private boolean canBackPress = true;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case MSG_BACKPRESS_OVER:
                    backPressTime = 0;
                    break;
                case MSG_BACKPRESS_NEXT:
                    canBackPress = true;
                    break;
            }
        }
    };

    public int onBackPress()
    {
        if(canBackPress && backPressTime >= 1)
        {
            handler.removeMessages(MSG_BACKPRESS_OVER);
            handler.removeMessages(MSG_BACKPRESS_NEXT);
            return DO_FINISH;
        }
        else if(canBackPress)
        {
            canBackPress = false;
            backPressTime ++;
            handler.removeMessages(MSG_BACKPRESS_OVER);
            handler.sendEmptyMessageDelayed(MSG_BACKPRESS_OVER, 2000);
            return SHOW_TOAST_NEXTCLICK;
        }
        else
        {
            handler.removeMessages(MSG_BACKPRESS_NEXT);
            handler.sendEmptyMessageDelayed(MSG_BACKPRESS_NEXT, 200);
            return DO_NOTHING;
        }
    }

}
