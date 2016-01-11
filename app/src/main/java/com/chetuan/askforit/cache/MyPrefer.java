package com.chetuan.askforit.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.chetuan.askforit.model.User;
import com.chetuan.askforit.util.ModelUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rl01.lib.BaseApplication;

import java.util.ArrayList;
import java.util.List;

public class MyPrefer {

	private static MyPrefer prefer = null;
	private SharedPreferences settings;

	private MyPrefer() {
		settings = BaseApplication.getInstance().getSharedPreferences(
				BaseApplication.getPackName(), Context.MODE_APPEND);
	}

	public static MyPrefer getInstance(Context ctx) {
		if (prefer == null) {
			prefer = new MyPrefer();
		}
		return prefer;
	}

	public static MyPrefer getInstance() {
		return getInstance(BaseApplication.getInstance());
	}
	
	public void clean(){
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}
	
	public void savePhone(String phone){
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("phone", phone);
		editor.commit();
	}
	
	public String getPhone(){
		return settings.getString("phone", "");
	}
	
	public void saveName(String name){
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("name", name);
		editor.commit();
	}
	
	public String getName(){
		return settings.getString("name", "");
	}
	
	public void saveCurrentLocationStatus(int status){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("LocationStatus", status);
		editor.commit();
	}
	
	public boolean isMsgPushEnable()
	{
		return settings.getBoolean("MsgPush", true);
	}

	public void setMsgPushStatus(boolean status)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("MsgPush", status);
		editor.commit();
	}

	public void setLoginInfo(JsonObject loginInfo){
		String loginInfoStr = "";
		if(loginInfo == null)
		{
			return;
		}
		else
		{
			loginInfoStr = loginInfo.toString();
		}
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("LOGIN_INFO", loginInfoStr);
		editor.commit();
	}

	public JsonObject getLoginInfo()
	{
		String loginInfoStr = settings.getString("LOGIN_INFO", null);
		if(loginInfoStr == null || loginInfoStr.equals(""))
		{
			return null;
		}
		else
		{
			JsonObject jsonObject = null;
			try{
				jsonObject = new JsonParser().parse(loginInfoStr).getAsJsonObject();
				return jsonObject;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}

	public void setUserName(String userName)
	{
		JsonObject lognInfo = getLoginInfo();
		if(lognInfo == null)
		{
			lognInfo = new JsonObject();
		}
		lognInfo.addProperty("userName", userName);
		setLoginInfo(lognInfo);
	}

	public void setUserPhone(String userPhone)
	{
		JsonObject lognInfo = getLoginInfo();
		if(lognInfo == null)
		{
			lognInfo = new JsonObject();
		}
		lognInfo.addProperty("userPhone", userPhone);
		setLoginInfo(lognInfo);
	}

	public void setUserInfo(String userName, String userPhone, String sex)
	{
		JsonObject lognInfo = getLoginInfo();
		if(lognInfo == null)
		{
			lognInfo = new JsonObject();
		}
		lognInfo.addProperty("userName", userName);
		lognInfo.addProperty("userPhone", userPhone);
		lognInfo.addProperty("userSex", sex);
		setLoginInfo(lognInfo);
	}

	public String[] getUserInfo()
	{
		String[] nameAndPhone = new String[3];
		JsonObject lognInfo = getLoginInfo();
		if(lognInfo == null)
		{
			String[] strs = {"", "", "男"};
			return strs;
		}
		if(lognInfo.get("userName") != null)
		{
			nameAndPhone[0] = lognInfo.get("userName").getAsString();
		}
		else
		{
			nameAndPhone[0] = "";
		}
		if(lognInfo.get("userPhone") != null)
		{
			nameAndPhone[1] = lognInfo.get("userPhone").getAsString();
		}
		else
		{
			nameAndPhone[1] = "";
		}
		if(lognInfo.get("userSex") != null)
		{
			nameAndPhone[2] = lognInfo.get("userSex").getAsString();
		}
		else
		{
			nameAndPhone[2] = "男";
		}
		return nameAndPhone;
	}

	public void saveHttpUrls(JsonObject urlJson)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("HTTP_URLS", urlJson.toString());
		editor.commit();
        Log.d("HTTP_URLS",urlJson.toString());
    }

	public JsonObject getHttpUrls()
	{
		String urlJsonStr = settings.getString("HTTP_URLS", null);
		if(urlJsonStr == null || urlJsonStr.equals(""))
		{
			return null;
		}
		else
		{
			JsonObject jsonObject = null;
			try{
				jsonObject = new JsonParser().parse(urlJsonStr).getAsJsonObject();
				return jsonObject;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}

	public void saveNewVersionUsed(int code)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("NEW_VERSION_" + code + "_USED", true);
		editor.commit();
	}

	public boolean isNewVersionUsed(int code)
	{
		return settings.getBoolean("NEW_VERSION_" + code + "_USED", false);
	}

	public void saveCarCompare(List<JsonObject> cars)
	{
		JsonArray jsonArray = new JsonArray();
		for(int i = 0; i < cars.size(); i ++)
		{
			JsonObject obj = cars.get(i);
			if(obj.get("carId") == null)
			{
				continue;
			}
			jsonArray.add(obj);
		}
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("CAR_COMPARE_HISTORY", jsonArray.toString());
		editor.commit();
	}

	public List<JsonObject> getCarCompare()
	{
		String jsonStr = settings.getString("CAR_COMPARE_HISTORY", "[]");
		JsonArray jsonArray = new JsonParser().parse(jsonStr).getAsJsonArray();
		List<JsonObject> cars = new ArrayList<>();
		for(int i = 0; i < jsonArray.size(); i ++)
		{
			cars.add(jsonArray.get(i).getAsJsonObject());
		}
		return cars;
	}

	public void saveUser(User user)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("USER", ModelUtil.getInstance().modelToJsonStr(user));
		editor.commit();
	}

	public User getUser()
	{
		String jsonStr = settings.getString("USER", "");
		JsonElement jsonele = new JsonParser().parse(jsonStr);
		User user = ModelUtil.getInstance().jsonToModel(jsonele,User.class);
		return user;
	}

//	public void saveSearchHistory(ArrayList<SearchHistorySortable> histories)
//	{
//		if(histories == null)
//		{
//			return;
//		}
//		JsonArray jsonArray = new JsonArray();
//		int size = histories.size();
//		for(int i = 0; i < size; i ++)
//		{
//			JsonElement item = ModelUtil.getInstance().modelToJson(histories.get(i).getValue());
//			if(item != null)
//			{
//				jsonArray.add(item);
//			}
//		}
//		SharedPreferences.Editor editor = settings.edit();
//		editor.putString(SearchHistorySortable.class.getSimpleName(), jsonArray.toString());
//		editor.commit();
//	}
//
//	public ArrayList<SearchHistorySortable> getSearchHistory()
//	{
//		ArrayList<SearchHistorySortable> histories = new ArrayList<>();
//		String str = settings.getString(SearchHistorySortable.class.getSimpleName(), "[]");
//		JsonArray jsonArray = new JsonParser().parse(str).getAsJsonArray();
//		int size = jsonArray.size();
//		for(int i = 0; i < size; i ++)
//		{
//			SearchHistory historyItem = ModelUtil.getInstance().jsonToModel(jsonArray.get(i), SearchHistory.class);
//			if(historyItem != null)
//			{
//				histories.add(new SearchHistorySortable(historyItem));
//			}
//		}
//		MinHeap.sort(histories);
//		return histories;
//	}

}
