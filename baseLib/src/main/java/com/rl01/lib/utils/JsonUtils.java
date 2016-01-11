package com.rl01.lib.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

	public static void put(JSONObject jsonObject, String key, Object value)
	{
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void add(JSONArray jsonArray, Object value)
	{
		jsonArray.put(value);
	}

	public static JSONObject string2JSONObject(String json)
	{
		JSONObject temp = null;
		try {
			temp = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public static JSONObject getJSONObject(JSONArray arr, int index)
	{
		JSONObject temp = null;
		try {
			temp = (JSONObject) arr.get(index);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public static String string2json2(String s) {
		if (StringUtils.isNull(s))
			return "";
		if((s.startsWith("{")&&s.endsWith("}")) 
				|| (s.startsWith("[")&&s.endsWith("]"))){
			return s;
		}else{
			logger.e("---error---http----response---json---string--");
			return "";
		}
	}
	
	public static String string2json(String s) {
		if (StringUtils.isNull(s))
			return "";
		s.trim();
		if(s.contains("{")){
			String json = s.substring(s.indexOf("{"));
			Pattern p = Pattern.compile("(\\{|,)\"(\\w*)\":null");
			Matcher m = p.matcher(json);
			while (m.find()) {
				json = json.replace(m.group(),
						m.group().substring(0, m.group().length() - 4) + "\"\"");
			}
			return json;
		}
		return s;
	}

	/**
	 * 处理json没有参数字段时的异常
	 * 
	 * @param jsonObj
	 *            json对象
	 * @param param
	 *            参数名
	 * @return
	 */
	public static String getString(JSONObject jsonObj, String param) {
		String jsonString = "";
		try {
			jsonString = StringUtils.nullToString(jsonObj.optString(param));
		} catch (Exception e) {
			logger.w(e.getMessage());
		}
		return jsonString;
	}

	/**
	 * 处理json没有参数字段时的异常
	 * 
	 * @param jsonObj
	 *            json对象
	 * @param param
	 *            参数名
	 * @return
	 */
	public static int getInt(JSONObject jsonObj, String param) {
		int jsonInt = 0;
		try {
			jsonInt = jsonObj.optInt(param, 0);
		} catch (Exception e) {
			logger.w(e.getMessage());
		}
		return jsonInt;
	}

	public static float getFloat(JSONObject jsonObj, String param) {
		double jsonInt = 0;
		try {
			jsonInt = jsonObj.optDouble(param);
		} catch (Exception e) {
			logger.w(e.getMessage());
		}
		return (float)jsonInt;
	}

	public static long getLong(JSONObject jsonObj, String param) {
		Long jsonInt = 0l;
		try {
			jsonInt = jsonObj.optLong(param);
		} catch (Exception e) {
			logger.w(e.getMessage());
		}
		return jsonInt;
	}
	
	public static boolean getBoolean(JSONObject jsonObj, String param) {
		boolean jsonInt = false;
		try {
			jsonInt = jsonObj.optBoolean(param);
		} catch (Exception e) {
			logger.w(e.getMessage());
		}
		return jsonInt;
	}
	
	public static double getDouble(JSONObject jsonObj, String param) {
		double jsonInt = 0;
		try {
			jsonInt = jsonObj.optLong(param);
		} catch (Exception e) {
			logger.w(e.getMessage());
		}
		return jsonInt;
	}

	public static String getArrayString(JSONObject jsonObj, String param) {
		String temp = "";
		try {
			JSONArray jsonInt = jsonObj.optJSONArray(param);
			if (jsonInt != null) {
				temp = jsonInt.toString();
			}
		} catch (Exception e) {
			logger.w(e.getMessage());
		}
		return temp;
	}

	public static JSONObject getJSONObject(JSONObject jsonObj, String param) {
		JSONObject temp = null;
		try {
			temp = jsonObj.optJSONObject(param);
		} catch (Exception e) {
			logger.w(e.getMessage());
		}
		return temp;
	}

	public static JSONArray getJSONArray(JSONObject jsonObj, String param) {
		JSONArray temp = null;
		try {
			temp = jsonObj.optJSONArray(param);
		} catch (Exception e) {
			logger.w(e.getMessage());
		}
		return temp;
	}

	public static List<Map<String, Object>> parseJSON2List(String jsonStr)
			throws JSONException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (!StringUtils.isNull(jsonStr)) {
			return list;
		}
		JSONArray jsonArr = new JSONArray(jsonStr);
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject json2 = jsonArr.getJSONObject(i);
			list.add(parseJSON2Map(json2.toString()));
		}
		return list;
	}

	public static Map<String, Object> parseJSON2Map(String jsonStr)
			throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNull(jsonStr)) {
			return map;
		}
		// 最外层解析
		JSONObject json = new JSONObject(jsonStr);
		Iterator it = json.keys();
		while (it.hasNext()) {
			Object k = it.next();
			Object v = json.get(k.toString());
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				JSONArray ar = (JSONArray) v;
				for (int i = 0; i < ar.length(); i++) {
					JSONObject json2 = ar.getJSONObject(i);
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	
}
