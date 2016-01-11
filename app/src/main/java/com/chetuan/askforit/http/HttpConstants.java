package com.chetuan.askforit.http;

import com.chetuan.askforit.cache.MyPrefer;
import com.google.gson.JsonObject;

public class HttpConstants {

	public static final String CODE_SUCCESS = "0000";

	public static final String SUCCESS = "0000";
	public static final String FAIL = "1111";

	public static final String CODE = "code";
	public static final String RESULTMESSAGE = "msg";

	public static final String SYSTEM_TIME = "systemTime";

	public static final String USER_STATE = "usertate";

	public static final String key = "f8dd1926807ea0fd01c21f8e30073609d5a1b87b";

//	public static final String ROOT_URL = "http://192.168.1.249:8180/carAgent/agent/appConfig_getAppconfig";
	public static final String ROOT_URL = "http://115.159.33.55:8980/carAgent/agent/appConfig_getAppconfig";

	private static JsonObject URLS = new JsonObject();

	public static void set(JsonObject urls)
	{
		URLS = urls;
	}

	public static String get(String urlName)
	{
		String url = "";
		if(URLS == null || !URLS.has(urlName))
		{
			URLS = MyPrefer.getInstance().getHttpUrls();
			if(URLS != null && URLS.has(urlName))
			{
				url = URLS.get(urlName).getAsString();
			}
		}
		else
		{
			url = URLS.get(urlName).getAsString();
		}
		return  url;
	}

	public static final String getLoadPage = "getLoadPage";
	public static final String getapplyDeposit = "getapplyDeposit";
	public static final String order_applyAgain = "order_applyAgain";
	public static final String updateRealCount = "updateRealCount";
	public static final String getFeedbackList = "getFeedbackList";
	public static final String getCode = "getCode";
	public static final String order_save = "order_save";
	public static final String applyRealCard = "applyRealCard";
	public static final String version = "version";
	public static final String getOnlyGrade = "getOnlyGrade";
	public static final String getCarinfoDetails = "getCarinfoDetails";
	public static final String getBank = "getBank";
	public static final String order_detail = "order_detail";
	public static final String upordeleCustomer = "upordeleCustomer";
	public static final String queryDeposit = "queryDeposit";
	public static final String order_search = "order_search";
	public static final String submitDeposit = "submitDeposit";
	public static final String getInDeposit = "getInDeposit";
	public static final String login = "login";
	public static final String addCustomer = "addCustomer";
	public static final String getGrade = "getGrade";
	public static final String order_delete = "order_delete";
	public static final String order_list = "order_list";
	public static final String order_applySign = "order_applySign";
	public static final String getRealCount = "getRealCount";
	public static final String msg_typeList = "msg_typeList";
	public static final String applyDeposit = "applyDeposit";
	public static final String clupdatePassword = "clupdatePassword";
	public static final String register = "register";
	public static final String getRealCard = "getRealCard";
	public static final String logout = "logout";
	public static final String msg_list = "msg_list";
	public static final String msg_delete = "msg_delete";
	public static final String getCarInfoUrlList = "getCarInfoUrlList";
	public static final String clickBaobei = "clickBaobei";
	public static final String getCustomer = "getCustomer";
	public static final String updatePassword = "updatePassword";

}
