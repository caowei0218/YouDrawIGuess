package com.example.youdrawiguess;

import android.app.Activity;
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.yunba.android.manager.YunBaManager;

public class MyApplication extends Application {

	private final static String TAG = "MyApplication";

	private static List<Activity> activities = new LinkedList<Activity>();

	private static MyApplication instance;

	public static String phoneNumber;

	public static Map<String, Boolean> onlineMap = new HashMap<String, Boolean>();

	private static RequestQueue queue;// Volley请求队列

	public static MyApplication getInstance() {
		return instance;
	}

	public void onCreate() {
		super.onCreate();

		queue = Volley.newRequestQueue(getApplicationContext());

		instance = this;

		startBlackService();
	}

	public static RequestQueue getHttpQueues() {
		return queue;
	}

	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	/**
	 * 结束所有Activity并且退出程序
	 * */
	public static void exit() {
		try {
			for (Activity activity : activities) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	/**
	 * 启动云巴通讯服务
	 * */
	private void startBlackService() {
		// 初始化 YunBa SDK.
		YunBaManager.start(getApplicationContext());

		// 可以 订阅 一个或者多个 Topics, 以便可以接收来自 Topic 的 Message.
		YunBaManager.subscribe(getApplicationContext(), new String[] { "t1" },
				new IMqttActionListener() {
					@Override
					public void onSuccess(IMqttToken arg0) {
					}

					@Override
					public void onFailure(IMqttToken arg0, Throwable arg1) {
					}
				});
	}

}
