package com.chetuan.askforit.http;

import android.content.Context;
import android.util.Log;
import android.webkit.URLUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chetuan.askforit.AskForItApplication;
import com.chetuan.askforit.cache.MyPrefer;
import com.chetuan.askforit.model.User;
import com.chetuan.askforit.util.MD5;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.rl01.lib.utils.DeviceUtils;
import com.rl01.lib.utils.PhoneUtil;
import com.rl01.lib.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by YT on 2015/10/8.
 */
public class HttpBean implements IHttpBean {

    private static final int RETRY_DEFAULT_TIME = 5000;
    private int retryTime = -1;
    private Context context;
    private String url;
    private String data;
    private Map<String, String> postParams;
    protected int uniqueType;
    private boolean isCacheUsed = false;
    private HashMap<String, String> headers = new HashMap<>();
    private static HashMap<String, RequestQueue> requestQueues = new HashMap<>();

    public HttpBean(Context context, String url, String data, int uniqueType) {
        this.context = context;
        this.url = (StringUtils.isNull(data) ? url : url + "?data=" + data);
        this.data = (data == null ? "" : data);
        this.uniqueType = uniqueType;
        this.headers = getDefaultHeaders();
    }

    //post  tq 20151109
    public HttpBean(Context context, String url, Map<String, String> postParams, int uniqueType) {
        this.context = context;
        if (!URLUtil.isHttpUrl(url)) {
            new Exception("url格式有误，必须以http开头").printStackTrace();
        }
        this.postParams = postParams;
        this.uniqueType = uniqueType;
        this.headers = getDefaultHeaders();
    }

    public HttpBean setRetryTime(int time) {
        this.retryTime = time;
        return this;
    }

    public int getUniqueType() {
        return this.uniqueType;
    }

    public HttpBean setCacheUsed() {
        this.isCacheUsed = true;
        return this;
    }

    private HashMap<String, String> getDefaultHeaders() {
        HashMap<String, String> headers = new HashMap<>();

        User user = MyPrefer.getInstance().getUser();
        if (user != null) {
            headers.put("X-AID", user.userid);
            headers.put("X-TOKEN", user.token);
        } else {
            headers.put("X-AID", "");
            headers.put("X-TOKEN", "");
        }

        headers.put("X-UUID", DeviceUtils.sn());
        headers.put("X-UA", PhoneUtil.manufacturer());
        headers.put("X-SYSTEM", DeviceUtils.getOSVersion());
        headers.put("X-CHANNEL", "10001");
        if (DeviceUtils.isTablet(AskForItApplication.getInstance())) {
            headers.put("X-CLIENT-TYPE", "4");
        } else {
            headers.put("X-CLIENT-TYPE", "3");
        }
        headers.put("X-VERSION-CODE", DeviceUtils.sn());
        headers.put("X-VERSION-NAME", DeviceUtils.getAppVersionName());
        String curTime = "" + new Date().getTime();
        headers.put("X-SYSTEMTIME", curTime);
        headers.put("X-SIGN", MD5.toMD5(curTime + data + HttpConstants.key));
        return headers;
    }

    @Override
    public void onResponseSuccess(JsonObject jsonObject) {
    }

    @Override
    public void onResponseFail(JsonObject jsonObject) {
    }

    @Override
    public void onErrorResponse(Exception e) {
        Log.e("xiyuan", e.getMessage(), e);
    }


    protected void onResponseSuccess(String responseStr) {
    }

    protected void onResponseFail(String errorMsg) {
    }

    /*以普通的post方式进行提交,以字符串形式请求,服务端返回字符串*/
    public final void postForStr() {
        Log.e("tianqin >>>", HttpBean.this.url + "?" + data);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tianqin >>>", "response:" + response);
                        onResponseSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("tianqin >>>", error + ":error:" + error.getMessage());
                        onResponseFail(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return postParams;
            }
        };

        String className = context.getClass().getName();

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(retryTime == -1 ? RETRY_DEFAULT_TIME : retryTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(className);
        stringRequest.setShouldCache(isCacheUsed);

        RequestQueue requestQueue = requestQueues.get(className);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            requestQueues.put(className, requestQueue);
        }
        requestQueue.add(stringRequest);
    }

    public final void start() {
        GsonRequest<JsonObject> jsonObjRequest = new GsonRequest<JsonObject>(url, JsonObject.class,
                new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject jsonObject) {
                        try {
                            Log.d("xiyuan", HttpBean.this.url + "      " + jsonObject.toString());
                            String code = jsonObject.get("code").getAsString();
                            if (HttpConstants.CODE_SUCCESS.equals(code)) {
                                HttpBean.this.onResponseSuccess(jsonObject);
                            } else {
                                HttpBean.this.onResponseFail(jsonObject);
                            }
                        } catch (Exception e) {
                            HttpBean.this.onErrorResponse(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        HttpBean.this.onErrorResponse(e);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };

        String className = context.getClass().getName();

        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(retryTime == -1 ? RETRY_DEFAULT_TIME : retryTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjRequest.setTag(className);
        jsonObjRequest.setShouldCache(isCacheUsed);

        RequestQueue requestQueue = requestQueues.get(className);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            requestQueues.put(className, requestQueue);
        }
        requestQueue.add(jsonObjRequest);
    }

    /*客户端以普通的post方式进行提交,服务端返回json串*/
    public final void post() {
        if (url == null || context == null || headers == null || postParams == null) {
            new Exception("参数错误，请调用HttpBean()进行初始化").printStackTrace();
            return;
        }
        Log.e("tianqin >>>", HttpBean.this.url + "?" + postParams.toString());
        GsonPostRequest<JsonObject> jsonObjRequest = new GsonPostRequest<JsonObject>(url,
                JsonObject.class, headers, postParams,
                new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject jsonObject) {
                        try {
                            Log.e("tianqin >>>", "response:" + jsonObject.toString());
                            String code = jsonObject.get("code").getAsString();
                            if (HttpConstants.CODE_SUCCESS.equals(code)) {
                                HttpBean.this.onResponseSuccess(jsonObject);
                            } else {
                                HttpBean.this.onResponseFail(jsonObject);
                            }
                        } catch (Exception e) {
                            onErrorResponse(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.e("tianqin >>>", e + ":error:" + e.getMessage());
                        HttpBean.this.onErrorResponse(e);
                    }
                });

        String className = context.getClass().getName();

        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(retryTime == -1 ? RETRY_DEFAULT_TIME : retryTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjRequest.setTag(className);
        jsonObjRequest.setShouldCache(isCacheUsed);

        RequestQueue requestQueue = requestQueues.get(className);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            requestQueues.put(className, requestQueue);
        }
        requestQueue.add(jsonObjRequest);
    }

    public static void cancleAll(Context context) {
        String className = context.getClass().getName();
        RequestQueue requestQueue = requestQueues.get(className);
        if (requestQueue != null) {
            requestQueue.cancelAll(className);
        }
    }

    /**
     * tq 20151109
     */
    public class GsonPostRequest<T> extends Request<T> {
        private final Gson mGson = new Gson();
        private final Class<T> mClazz;
        private final Response.Listener<T> mListener;
        private final Map<String, String> mHeaders;
        private final Map<String, String> mParams;


        public GsonPostRequest(String url, Class<T> clazz,
                               Map<String, String> headers, Map<String, String> mParams,
                               Response.Listener<T> listener,
                               Response.ErrorListener errorListener) {
            this(Method.POST, url, clazz, headers, mParams, listener, errorListener);
        }

        public GsonPostRequest(int method, String url, Class<T> clazz,
                               Map<String, String> headers, Map<String, String> mParams,
                               Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.mClazz = clazz;
            this.mHeaders = headers;
            this.mParams = mParams;
            this.mListener = listener;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return mHeaders != null ? mHeaders : super.getHeaders();
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return mParams;
        }

        @Override
        protected void deliverResponse(T response) {
            mListener.onResponse(response);
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success(mGson.fromJson(json, mClazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }
}
