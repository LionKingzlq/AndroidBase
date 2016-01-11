package com.chetuan.askforit.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.rl01.lib.BaseApplication;
import com.rl01.lib.http.HttpConfig;
import com.rl01.lib.utils.StringUtils;
import com.rl01.lib.utils.logger;

import java.lang.reflect.Method;
import java.util.TimeZone;

public class DeviceUtils {


	private DeviceUtils(){

	}

	private static  DeviceUtils instance = null;

	public static DeviceUtils getInstance(){
		if(instance==null){
			synchronized(DeviceUtils.class){
				if(instance==null){
					instance=new DeviceUtils();
				}
			}
		}
		return instance;
	}


	private static String serial;
	/**
	 * 获取当前应用的包名
	 *
	 * @return
	 */
	public String getPackageName() {
		return BaseApplication.getInstance().getPackageName();
	}

	public String getPackEndsName() {
		try {
			return "_"
					+ getPackageName().substring(
							getPackageName().lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}

	public String getAppVersionName() {
		String version = "";
		BaseApplication app = BaseApplication.getInstance();
		try {
			version = app.getPackageManager().getPackageInfo(
					app.getPackageName(), 0).versionName;
			return version;
		} catch (NameNotFoundException e) {
			logger.e(e);
		}
		logger.i("-------current  version-----" + version);
		return "";
	}

	public int getAppVersionCode() {
		int version = 0;
		BaseApplication app = BaseApplication.getInstance();
		try {
			version = app.getPackageManager().getPackageInfo(
					app.getPackageName(), 0).versionCode;
			return version;
		} catch (NameNotFoundException e) {
			logger.e(e);
		}
		logger.i("-------current  version---code--" + version);
		return version;
	}

	public String getUuid(Context activity) {
		String uuid = Settings.Secure.getString(activity.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		return uuid;
	}

	public String getModel() {
		String model = android.os.Build.MODEL;
		return model;
	}

	public final String getProductName() {
		String productname = android.os.Build.PRODUCT;
		return productname;
	}

	public final String getOSVersion() {
		String osversion = android.os.Build.VERSION.RELEASE;
		return osversion;
	}

	public final String getSDKVersion() {
		@SuppressWarnings("deprecation")
		String sdkversion = android.os.Build.VERSION.SDK;
		return sdkversion;
	}

	public final String getTimeZoneID() {
		TimeZone tz = TimeZone.getDefault();
		return (tz.getID());
	}

	public boolean isTablet(Context context) {
		if(context == null){
			return false;
		}

		Resources resources = context.getResources();
		Configuration configuration = resources.getConfiguration();
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public String sn() {
		if(StringUtils.notNull(serial))
		{
			return serial;
		}
		String divider = "";
		try {
			try {
				Class<?> c = Class.forName("android.os.SystemProperties");
				Method get = c.getMethod("get", String.class);
				serial = (String) get.invoke(c, "ro.serialno");
			} catch (Exception e) {
				logger.e(e);
			}

			final TelephonyManager tm = (TelephonyManager) BaseApplication
					.getInstance().getSystemService(
							Context.TELEPHONY_SERVICE);
			divider = StringUtils.isNull(serial)?"": "_";
			serial += divider + tm.getDeviceId();

			divider = StringUtils.isNull(serial)?"": "_";
			serial += divider + Settings.Secure.getString(BaseApplication
							.getInstance().getContentResolver(),
					Settings.Secure.ANDROID_ID);

			if (StringUtils.isNull(serial)) {
				serial = "default_" + HttpConfig.modelsAbility();
			}

			logger.e(serial);

		} catch (Exception e) {
			logger.e(e);
		}
		return serial;
	}

	public void sendSMS(Context cxt, String smsBody) throws Exception {
		if(cxt == null){
			return;
		}
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("sms_body", smsBody);
		cxt.startActivity(intent);
	}

	public String getIMEI() {
		TelephonyManager telephonyManager = (TelephonyManager) BaseApplication
				.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}
}
