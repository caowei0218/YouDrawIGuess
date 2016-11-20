package com.example.youdrawiguess.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;

@SuppressLint("NewApi")
public class WebViewActivity extends Activity implements OnClickListener {

	private WebView wv_web;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.webview_layout);

		MyApplication.addActivity(this);

		init();
	}

	private void init() {
		wv_web = (WebView) findViewById(R.id.wv_web);

		wv_web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
		wv_web.getSettings().setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
		wv_web.getSettings().setSupportZoom(true);// 是否可以缩放，默认true
		wv_web.getSettings().setBuiltInZoomControls(true);// 是否显示缩放按钮，默认false
		wv_web.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
		wv_web.getSettings().setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
		wv_web.getSettings().setAppCacheEnabled(true);// 是否使用缓存
		wv_web.getSettings().setDomStorageEnabled(true);// DOM Storage
		// displayWebview.getSettings().setUserAgentString("User-Agent:Android");//设置用户代理，一般不用

		wv_web.loadUrl("http://m2.yilicai.cn/myaccount/login");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			wv_web.setWebContentsDebuggingEnabled(true);
		}
		wv_web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

		});
		wv_web.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
	}

	@Override
	public void onClick(View v) {
	}

	/**
	 * 返回
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			moveTaskToBack(false);
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
