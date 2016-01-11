package com.rl01.lib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rl01.lib.utils.logger;

public class RLWebView extends WebView {

	private boolean needResoposeData = false;
	private WebviewResponseListener listener;
	private WebviewLoadProgressListener progressListener;

	public RLWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RLWebView(Context context) {
		super(context);
	}

	public void init(int mode) {
		WebSettings set = this.getSettings();
		set.setCacheMode(mode);
		set.setJavaScriptEnabled(true);
		set.setBuiltInZoomControls(true);
		set.setSupportZoom(true);
		set.setAllowFileAccess(true);
		//set.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		addJavascriptInterface(new HtmlGetBodyInterface(listener), "demo");
		setWebChromeClient(new MyWebChromeClient());
		setWebViewClient(new MyWebViewClient());
		//this.setBackgroundColor(0);
	}

	class MyWebChromeClient extends WebChromeClient {

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			//logger.e(url);
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public boolean onJsBeforeUnload(WebView view, String url,
				String message, JsResult result) {
			//logger.e(url);
			return super.onJsBeforeUnload(view, url, message, result);
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			//logger.e(url);
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, JsPromptResult result) {
			//logger.e(url);
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if(progressListener!=null){
				progressListener.onWebviewProgressChange(newProgress);
			}
		}

		@Override
		public void onReceivedTouchIconUrl(WebView view, String url,
				boolean precomposed) {
			//logger.e(url);
			super.onReceivedTouchIconUrl(view, url, precomposed);
		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			logger.e("onShowCustomView");
			super.onShowCustomView(view, callback);
		}

	}

	class MyWebViewClient extends WebViewClient {

		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// return true;
		// }
		
		@Override
		public void onPageFinished(WebView view, String url) {
			logger.e(url);
			if (needResoposeData && url !=null && url.contains("/stripe/redirectURL.msp")) {
				logger.e("----0000----"+url);
				try {
					view.loadUrl("javascript:demo.getBody(document.getElementsByTagName('html')[0].innerHTML);");
				} catch (Exception e) {
					logger.e(e);
				}
			}
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			super.onLoadResource(view, url);
			//logger.e(url);
		}

	}

	public void setNeedShowResponseData(boolean flag) {
		this.needResoposeData = flag;
	}

	public void setWebviewResponseListener(WebviewResponseListener listener){
		this.listener = listener;
	}
	
	public void setWebviewLoadProgressListener(WebviewLoadProgressListener listener){
		this.progressListener = listener;
	}
	
	public interface WebviewResponseListener{
		public void onWebviewResponseData(String data);
	}
	
	public interface WebviewLoadProgressListener{
		public void onWebviewProgressChange(int progress);
	}
	
}
