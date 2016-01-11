package com.rl01.lib.utils;

import java.lang.reflect.Method;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.rl01.lib.BaseApplication;
import com.rl01.lib.http.HttpConfig;

public class DeviceUtils {

	private static String serial;
	/**
	 * 获取当前应用的包名
	 * 
	 * @return
	 */
	public static String getPackageName() {
		return BaseApplication.getInstance().getPackageName();
	}

	public static String getPackEndsName() {
		try {
			return "_"
					+ getPackageName().substring(
							getPackageName().lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}

	public static String getAppVersionName() {
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
	
	public static int getAppVersionCode() {
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

	public final static String getUuid(Context activity) {
		String uuid = Settings.Secure.getString(activity.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		return uuid;
	}

	public final static String getModel() {
		String model = android.os.Build.MODEL;
		return model;
	}

	public final static String getProductName() {
		String productname = android.os.Build.PRODUCT;
		return productname;
	}

	public final static String getOSVersion() {
		String osversion = android.os.Build.VERSION.RELEASE;
		return osversion;
	}

	public final static String getSDKVersion() {
		@SuppressWarnings("deprecation")
		String sdkversion = android.os.Build.VERSION.SDK;
		return sdkversion;
	}

	public final static String getTimeZoneID() {
		TimeZone tz = TimeZone.getDefault();
		return (tz.getID());
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static String sn() {
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

	public static void sendSMS(Context cxt, String smsBody) throws Exception {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("sms_body", smsBody);
		cxt.startActivity(intent);
	}

	public static String getIMEI() {
		TelephonyManager telephonyManager = (TelephonyManager) BaseApplication
				.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}
}
