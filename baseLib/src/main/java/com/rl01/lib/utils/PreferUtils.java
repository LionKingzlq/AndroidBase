package com.rl01.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.rl01.lib.BaseApplication;

public class PreferUtils {

	private static PreferUtils prefer = null;
	private SharedPreferences settings;

	private PreferUtils() {
		settings = BaseApplication.getInstance().getSharedPreferences(
				BaseApplication.getPackName(), Context.MODE_APPEND);
	}

	public static PreferUtils getInstance(Context ctx) {
		if (prefer == null) {
			prefer = new PreferUtils();
		}
		return prefer;
	}

	public static PreferUtils getInstance() {
		return getInstance(BaseApplication.getInstance());
	}
	
	public void clean(){
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}
	
	public void storeWH(String token) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("X-system-WH", token);
		editor.commit();
	}

	public String getWH() {
		String  wh = settings.getString("X-system-WH", "");
		if(wh == null || wh.equals(""))
		{
			WindowManager manager = (WindowManager) BaseApplication.getInstance()
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = manager.getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			wh = metrics.widthPixels + "*" + metrics.heightPixels;
			storeWH(wh);
		}
		return wh;
	}
	
	public void storePushInitFlag(boolean token) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("X-PushInitFlag", token);
		editor.commit();
		storeLoginStatus();
	}

	public boolean getPushInitFlag() {
		return settings.getBoolean("X-PushInitFlag", false);
	}

	public void storeToken(String token) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("X-TOKEN", token);
		editor.commit();
		storeLoginStatus();
	}

	public String getToken() {
		return settings.getString("X-TOKEN", "");
	}
	
	public void storeLoginStatus() {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("X-l-s", StringUtils.createStatus());
		editor.commit();
	}

	public boolean getLoginStatus() {
		String status = settings.getString("X-l-s", "");
		if(StringUtils.isNull(status)){
			return false;
		}else{
			return status.equals(StringUtils.createStatus());
		}
	}
	
	public String getAlias() {
		return getUserInfo("alias");
	}

	public String getUserId() {
		return getUserInfo("userId");
	}
	
	public int getUserType() {
		try {
			return Integer.parseInt(getUserInfo("type"));
		} catch (Exception e) {
			return -1;
		}
	}
	
	public void storeUserInfo(String key,String value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("user_"+key+"_info", value);
		editor.commit();
	}

	public String getUserInfo(String key) {
		return settings.getString("user_"+key+"_info", "");
	}

	/**
	 * 存储登录自动登录状态
	 */
	public void storeTimeDuration(long account) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("rl_TimeDuration", account);
		editor.commit();
	}

	public long getTimeDuration() {
		return settings.getLong("rl_TimeDuration", -1l);
	}

	public void storeInitEquipment(String role) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("init_equipment", role);
		editor.commit();
	}

	public String isInitEquipment() {
		return settings.getString("init_equipment", "");
	}
	
	public void storeHasHistory(boolean hasHistory) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("hasHistory", hasHistory);
		editor.commit();
	}

	public boolean getHasHistory() {
		return settings.getBoolean("hasHistory", false);
	}
	
	public void storeUnReadNumber(boolean clean,int number) {
		SharedPreferences.Editor editor = settings.edit();
		if(clean){
			editor.putInt("rl_unread_number",number);
		}else{
			int count =  settings.getInt("rl_unread_number", 0) ;
			editor.putInt("rl_unread_number",count+number);
		}
		editor.commit();
	}
	
	public void storeReadNumber(boolean clean,int number) {
		SharedPreferences.Editor editor = settings.edit();
		if(clean){
			editor.putInt("rl_read_number",number);
		}else{
			int count =  settings.getInt("rl_read_number", 0) ;
			editor.putInt("rl_read_number",count + number);
		}
		editor.commit();
	}

	public int getUnReadNumber() {
		int count =  settings.getInt("rl_unread_number", 0) 
				- settings.getInt("rl_read_number", 0);
		if(count>0){
			return count;
		}else{
			return 0;
		}
	}
	
	public void storeThemeStyle(boolean which){
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("themeWhite", which);
		editor.commit();
	}
	
	public boolean getThemeStyle() {
		return settings.getBoolean("themeWhite", true);
	}

}
