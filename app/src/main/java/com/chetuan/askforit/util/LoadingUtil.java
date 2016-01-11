package com.chetuan.askforit.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chetuan.askforit.R;

import java.util.Date;

/**
 * Created by YT on 2015/11/13.
 */
public class LoadingUtil {

    private static LoadingUtil instance;

    private Context context;

    private static final long DEFALUT_TIME = 1500;

    private long canclableAfter = DEFALUT_TIME;

    private LoadingDialog loadingDialog;

    public static  LoadingUtil getInstance()
    {
        if(instance == null)
        {
            instance = new LoadingUtil();
        }
        return instance;
    }

    public static void show(@NonNull Context context)
    {
        show(context, DEFALUT_TIME);
    }

    public static void show(@NonNull Context context, long canclableAfter)
    {
        getInstance();
        try
        {
            if(context != instance.context)
            {
                if(instance.loadingDialog != null)
                {
                    instance.loadingDialog.dismiss();
                }
                instance.context = context;
                instance.loadingDialog = new LoadingDialog(context);
            }
            if(!instance.loadingDialog.isShowing())
            {
                instance.canclableAfter = canclableAfter;
                instance.loadingDialog.show();
            }
        }
        catch (Exception e)
        {
            Log.e("xiyuan", e.getMessage(), e);
        }
    }

    public static void hide()
    {
        getInstance();
        try
        {
            if(instance.context != null && instance.loadingDialog != null && instance.loadingDialog.isShowing())
            {
                instance.loadingDialog.dismiss();
            }
        }
        catch (Exception e)
        {
            Log.e("xiyuan", e.getMessage(), e);
        }
    }

    static class LoadingDialog extends Dialog implements View.OnClickListener
    {
        private View loadingView;

        public LoadingDialog(Context context) {
            super(context, R.style.MyDialog);
            setContentView(R.layout.util_loading_dialog);

            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
            setCanceledOnTouchOutside(false);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND | WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            (loadingView = findViewById(R.id.loading_dialog)).setOnClickListener(this);

        }

        @Override
        public void show() {
            super.show();
            loadingView.setTag(R.id.tag_time, new Date().getTime());
        }

        @Override
        public void onClick(View v) {
            onBackPressed();
        }

        @Override
        public void onBackPressed() {
            long startShow = (long) loadingView.getTag(R.id.tag_time);
            if(new Date().getTime() >= startShow + instance.canclableAfter)
            {
                dismiss();
            }
        }

    }

}
