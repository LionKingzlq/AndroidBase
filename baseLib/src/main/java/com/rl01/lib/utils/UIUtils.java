package com.rl01.lib.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Selection;
import android.text.Spannable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.rl01.lib.BaseApplication;
import com.rl01.lib.widget.RLToast;

public class UIUtils {
	
	public static void logout(Activity acti){
		PreferUtils.getInstance().clean();
//		BaseApplication.getInstance().removeParseTag();
		BaseApplication.getInstance().setNeedFinishActivity(true);
		acti.finish();
	}
	
	public static int getStateBarHeight(Context paramContext) {
		try {
			Class<?> localClass = Class.forName("com.android.internal.R$dimen");
			Object localObject = localClass.newInstance();
			int i = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
			int j = paramContext.getResources().getDimensionPixelSize(i);
			return j;
		} catch (Exception e) {
			logger.e(e);
		}
		return 0;
	}

	public static String formatNumber(double num, int wei) {
		BigDecimal bd = new BigDecimal(num);
		bd = bd.setScale(wei, BigDecimal.ROUND_HALF_UP);
		return bd.toString();
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		if (bm == null) {
			return null;
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();
		}
	}

	public static Bitmap bytes2Bimap(byte[] b) {
		if (b != null && b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public static void openLocalPdfWordPPTExcel(Context cxt, String url) {
		try {
			Intent intent = new Intent();
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setAction(Intent.ACTION_VIEW);
			Uri content_url = Uri.fromFile(new File(url));
			if (url.endsWith(".pdf")) {
				intent.setDataAndType(content_url, "application/pdf");
			} else if (url.endsWith(".ppt")) {
				intent.setDataAndType(content_url,
						"application/vnd.ms-powerpoint");
			} else if (url.endsWith(".doc")) {
				intent.setDataAndType(content_url, "application/vnd.ms-word");
			} else if (url.endsWith(".xls")) {
				intent.setDataAndType(content_url, "application/vnd.ms-excel");
			} else if (url.endsWith(".pptx")) {
				intent.setDataAndType(content_url,
						"application/vnd.openxmlformats-officedocument.presentationml.presentation");
			} else if (url.endsWith(".docx")) {
				intent.setDataAndType(content_url,
						"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			} else if (url.endsWith(".xlsx")) {
				intent.setDataAndType(content_url,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			}
			cxt.startActivity(intent);
		} catch (Exception e) {
			logger.e(e);
			RLToast.showRLToast(cxt, "您的手机无法打开此文件,请先安装office软件!");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri
					.parse("http://ev.vstudying.com/message/apk/Thinkfree.apk"));
			cxt.startActivity(intent);
		}
	}

	// 获取server是否运行
	public static boolean getServiceIsStart(Context context, String serviceName) {
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Activity.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager
				.getRunningServices(30);
		for (int i = 0; i < mServiceList.size(); i++) {
			if (serviceName.equals(mServiceList.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static void doBackFalse(Context cxt) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		cxt.startActivity(intent);
	};

	public static String getString(int stringId) {
		return BaseApplication.getInstance().getString(stringId);
	}

	public static Drawable getDrawable(int resId) {
		return BaseApplication.getInstance().getResources().getDrawable(resId);
	}

	public static long getTimeDuration() {
		return PreferUtils.getInstance(BaseApplication.getInstance())
				.getTimeDuration();
	}
	
	public static long stringToTime(String timeStr, String model) {
		try {
			DateFormat gmt = new SimpleDateFormat(model, Locale.ENGLISH);
			Date d = gmt.parse(timeStr);
			return d.getTime();
		} catch (Exception e) {
			logger.e(e);
		}
		return -1;
	}

	// 读取服务器当前时间
	public static long getCurrentTime() {
		long time = getTimeDuration();
		if (time == -1) {
			return new Date().getTime();
		} else {
			return new Date().getTime() + time;
		}
	}

	/**
	 * 判断application是否在前端显示
	 * 
	 * @param packageName
	 * @param context
	 * @return
	 */
	public static boolean isApplicationShowing(String packageName,
			Context context) {
		boolean result = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
		if (appProcesses != null) {
			for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
				if (runningAppProcessInfo.processName.equals(packageName)) {
					int status = runningAppProcessInfo.importance;
					if (status == RunningAppProcessInfo.IMPORTANCE_VISIBLE
							|| status == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 判断activity是否显示
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean isTopActivity(Context context, Class<?> activity) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			if (activity.getName().equals(
					tasksInfo.get(0).topActivity.getClassName())) {
				return true;
			}
		}
		return false;

	}
	
	public static void playLocal(Context cxt, String url) {
		Intent it = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse(url);
		it.setDataAndType(uri, "video/*");
		cxt.startActivity(it);
	}
	
	public static String getIntentClass(Intent intent){
		String className = "";
		try{
			Class<?> cls = intent.getClass();
			Field fd1 = cls.getDeclaredField("mComponent");
			fd1.setAccessible(true);
			ComponentName cn = (ComponentName)fd1.get(intent);
			if(cn!=null){
				className = cn.getClassName();
			}
		}catch(Exception e){
			logger.e(e);
		}
		return className;
	}
	
	public static Map<String,Object> getHeadersMap(Context context){
		Map<String,Object> map = new HashMap<String, Object>();
		/*try {
			map.put("X-APP-ID", BaseApplication.getAppId());
			map.put("X-APP-VERSION", BaseApplication.getVersionName());
			map.put("X-UP-BEAR-TYPE", PhoneUtil.getNetType(context));
			map.put("X-PLATFORM-TYPE", BaseConfig.X_PLATFORM_TYPE);
			String uuid = "";
			try {
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				uuid = tm.getDeviceId();
			} catch (Exception e) {
				logger.e(e);
			}
			if(StringUtils.isNull(uuid)){
				uuid = PreferUtils.getInstance().getUUID();
			}
			map.put("X-UUID", uuid);
			map.put("X-TEST-TYPE", logger.logFlag?1:0);
			map.put("X-UA-DETAIL", PhoneUtil.getPhoneModel());
		} catch (Exception e) {
			logger.e(e);
		}*/
		return map;
	}
	
	public static void deleteAllCache(Context context){
		try{
			com.rl01.lib.utils.FileUtils.deleteFile(DirUtils.getInstance().getDefaultDirPath());
		}catch(Exception e){
			logger.e(e);
		}
	}
	
    public static void showInput(EditText editText){
    	editText.requestFocus();
    	Selection.setSelection((Spannable)editText.getText(), editText.getText().toString().trim().length());
    	InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
    	imm.showSoftInput(editText,InputMethodManager.SHOW_FORCED);  
    }
    
    public static void dismissInput(EditText editText){
    	editText.requestFocus();
    	Selection.setSelection((Spannable)editText.getText(), editText.getText().toString().trim().length());
    	InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
    	imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘  
    }
    
    public static void dismissInput(Activity activity){
    	if(activity.getCurrentFocus()!=null && activity.getCurrentFocus().getWindowToken()!=null){
    		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);  
        	imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘  
    	}
    }
    
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
    
    public static void showToast(Context context, String text, boolean isLong) {
		if (isLong) {
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showToast(Context context, int text, boolean isLong) {
		if (isLong) {
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showToast(Context context, String text) {
		showToast(context, text, false);
	}

	public static void showToast(Context context, int text) {
		showToast(context, text, false);
	}

	public static void showToast(String text) {
		showToast(BaseApplication.getInstance(), text, false);
	}

	public static void showToast(int text) {
		showToast(BaseApplication.getInstance(), text, false);
	}
	
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
