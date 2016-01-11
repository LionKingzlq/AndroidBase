package com.chetuan.askforit.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chetuan.askforit.R;

/**
 * Created by YT on 2015/11/20.
 */
public class HttpErrorDialog extends Dialog implements View.OnClickListener
{
    private OnTryAgainListener listener;

    public void setOnTryAgainListener(OnTryAgainListener listener) {
        this.listener = listener;
    }

    public HttpErrorDialog(Context context) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.http_error_dialog);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        setCanceledOnTouchOutside(true);

//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND | WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        findViewById(R.id.dialog_outside).setOnClickListener(this);
        findViewById(R.id.cancle).setOnClickListener(this);
        findViewById(R.id.try_again).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dialog_outside:
            case R.id.cancle: {
                dismiss();
                break;
            }
            case R.id.try_again: {
                if(listener != null)
                {
                    listener.onTryAgain();
                }
            }
        }
    }

    public interface OnTryAgainListener {
        void onTryAgain();
    }

}