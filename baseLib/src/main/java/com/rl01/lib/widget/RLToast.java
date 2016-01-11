package com.rl01.lib.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rl01.lib.base.R;
import com.rl01.lib.utils.logger;

public class RLToast extends Toast {
	
	public static RLToast toast = null;

	public RLToast(Context context) {
		super(context);
	}

	@Override
	public void setView(View view) {
		super.setView(view);
	}

	public static RLToast makeRLText(Context context, String text,int imageRes,int bgRes,int duration,int gravity) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.rich_layout,null);
		((TextView) layout.findViewById(R.id.textView)).setText(text);
		if(imageRes>0){
			((ImageView) layout.findViewById(R.id.imageView)).setImageResource(imageRes);
			((ImageView) layout.findViewById(R.id.imageView)).setVisibility(View.VISIBLE);
		}else{
			((ImageView) layout.findViewById(R.id.imageView)).setVisibility(View.GONE);
		}

		try {
			if(toast!=null){
				toast.cancel();
				toast = null;
			}
		} catch (Exception e) {
			logger.e(e);
		}
		
		toast = new RLToast(context);
		toast.setDuration(duration);
		toast.setView(layout);
		toast.setGravity(gravity, 0, 0);
		return toast;
	}
	
	public static void showRLToast(Context context, String text, int imageRes,int bgRes,int duration,int gravity){
		makeRLText(context, text,imageRes, bgRes,duration,gravity).show();
	}
	
	public static void showRLToast(Context context, String text, int imageRes,int bgRes,int gravity){
		showRLToast(context, text,imageRes,bgRes,2000,gravity);//default 1 mint
	}
	
	public static void showRLToast(Context context, String text, int imageRes,int gravity){
		showRLToast(context, text,imageRes,R.drawable.rounded_corners,gravity);
	}
	
	public static void showRLToast(Context context, String text, int imageRes){
		showRLToast(context, text,imageRes,R.drawable.rounded_corners,Gravity.CENTER);
	}
	
	public static void showRLToast(Context context, String text){
		showRLToast(context, text,-1);
	}
	
}