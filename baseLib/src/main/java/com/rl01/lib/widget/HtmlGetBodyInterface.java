package com.rl01.lib.widget;

import android.webkit.JavascriptInterface;

import com.rl01.lib.widget.RLWebView.WebviewResponseListener;

public class HtmlGetBodyInterface {
	
	private WebviewResponseListener listener;
	
	public HtmlGetBodyInterface(WebviewResponseListener listener){
		this.listener = listener;
	}
	
	@JavascriptInterface
	public void getBody(String body) {
		//logger.e(body);
		if(listener != null){
			//logger.e("00000000");
			listener.onWebviewResponseData(body);
		}
	}
	
}
