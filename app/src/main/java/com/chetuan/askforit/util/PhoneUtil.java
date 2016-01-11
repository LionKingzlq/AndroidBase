package com.chetuan.askforit.util;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.rl01.lib.utils.logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class PhoneUtil {

	private PhoneUtil(){

	}
	private static  PhoneUtil instance = null;

	public static PhoneUtil getInstance(){
		if(instance==null){
			synchronized(PhoneUtil.class){
				if(instance==null){
					instance=new PhoneUtil();
				}
			}
		}
		return instance;
	}


	private static TelephonyManager getTelephonyManager(Context context) {
		return (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * 获取本机号码
	 *
	 * @param context
	 * @return
	 */
	public String getPhoneNumber(Context context) {
		String number = "";
		try {
			TelephonyManager tm = getTelephonyManager(context);
			number = tm.getLine1Number();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return number;
	}

	/**
	 * 获取本机imsi
	 *
	 * @param context
	 * @return
	 */
	public String getPhoneIMSI(Context context) {
		String imsi = "";
		try {
			TelephonyManager tm = getTelephonyManager(context);
			imsi = tm.getSubscriberId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imsi;
	}

	/**
	 * 获取本机imei
	 *
	 * @param context
	 * @return
	 */
	public String getPhoneIMEI(Context context) {
		String imei = "";
		try {
			TelephonyManager tm = getTelephonyManager(context);
			imei = tm.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imei;
	}

	/**
	 * 获取运营商
	 *
	 * @return
	 */
	public String getProvidersName(Context context) {
		String imsi = getPhoneIMSI(context);
		String providersName = "";
		try {
			// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				providersName = "中国移动";
			} else if (imsi.startsWith("46001")) {
				providersName = "中国联通";
			} else if (imsi.startsWith("46003")) {
				providersName = "中国电信";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return providersName;
	}

	/**
	 * 手机型号
	 *
	 * @return
	 */
	public String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取屏幕像素
	 *
	 * @param context
	 * @return 返回宽度x长度形式 比如480x800
	 */
	public String getPhoneDisplay(Context context) {
		String result = "";
		try {
			DisplayMetrics dm = new DisplayMetrics();
			dm = context.getResources().getDisplayMetrics();
			int width = dm.widthPixels;
			int height = dm.heightPixels;
			result = width + "*" + height;
//			result = height + "x" +width;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取屏幕dpi
	 *
	 * @param context
	 * @return
	 */
	public String getPhoneDPI(Context context) {
		String result = "";
		try {
			DisplayMetrics dm = new DisplayMetrics();
			dm = context.getResources().getDisplayMetrics();
			result = dm.densityDpi+"";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取系统版本
	 *
	 * @return
	 */
	public String getPhoneOS() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机制造厂商
	 * @return
	 */
	public String manufacturer(){
		return android.os.Build.MANUFACTURER;
	}

	/**
	 * 获取mac地址
	 *
	 * @param context
	 * @return
	 */
	public String getLocalMacAddress(Context context) {
		String mac = "";
		try {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			mac = info.getMacAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mac;
	}

	/**
	 * 获取ip地址
	 *
	 * @return
	 */
	public String getLocalIpAddress() {
		String ip = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ip = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	//网络是否可用
	public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
        	//如果仅仅是用来判断网络连接 则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

	/**
	 * 获取网络接入状态
	 * @param context
	 */
	public String getNetType(Context context) {
		try{
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);//获取系统的连接服务
			if(isNetworkAvailable(context)){
				NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();//获取网络的连接情况
				if(activeNetInfo.getType()==ConnectivityManager.TYPE_WIFI){
					//判断WIFI网
					return "1";
				}else {
					int networkType = getTelephonyManager(context).getNetworkType();
					if (networkType == TelephonyManager.NETWORK_TYPE_UMTS //  12
							|| networkType == TelephonyManager.NETWORK_TYPE_HSDPA
							|| networkType == TelephonyManager.NETWORK_TYPE_EVDO_0
							|| networkType == TelephonyManager.NETWORK_TYPE_EVDO_A
							|| networkType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
						//3g
						return "3";
					} else if (networkType == TelephonyManager.NETWORK_TYPE_GPRS
							|| networkType == TelephonyManager.NETWORK_TYPE_EDGE
							|| networkType == TelephonyManager.NETWORK_TYPE_CDMA) {
						// 2g
						return "2";
					} else if(networkType == TelephonyManager.NETWORK_TYPE_LTE) {
						return "4";
					} else {
						return "2";
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "0";
	}

	/**
	 * 获取位置
	 * @param context
	 * @return
	 */
	public Location getLocation(Context context){
		try{
			LocationManager locationManager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
			Criteria criteria =new Criteria();
			 criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			 criteria.setAltitudeRequired(false);
	         criteria.setBearingRequired(false);
	         criteria.setCostAllowed(true);
	         criteria.setPowerRequirement(Criteria.POWER_HIGH);
	         criteria.setSpeedRequired(false);
	         String currentProvider = locationManager.getBestProvider(criteria, true);
	         Location currentLocation = locationManager.getLastKnownLocation(currentProvider);
	         return currentLocation;
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 判断手机是否root
	 * @return
	 */
	public boolean isRoot(){
		boolean bool = false;
        try{
            if ((!new File("/system/bin/su").exists())
            	 && (!new File("/system/xbin/su").exists())){
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {

        }
        return bool;
	}

	/**
	 * 判断是否是双卡手机
	 * @param context
	 * @return
	 */
	public boolean initIsDoubleTelephone(Context context){
		Method method = null; 
		try{
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
			method = TelephonyManager.class.getMethod("getSimStateGemini",new Class[] { int.class });
			Object result_0 = method.invoke(tm, new Object[] {new Integer(0)});  
			Object result_1 = method.invoke(tm, new Object[] {new Integer(1)});
			return true;
		}catch(Exception e){
			logger.e("single sim card");
		}
		return false;
	}
}
