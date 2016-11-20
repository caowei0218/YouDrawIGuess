package com.example.youdrawiguess.receiver;

import io.yunba.android.manager.YunBaManager;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.activity.ChatActivity;
import com.example.youdrawiguess.activity.SketchpadActivity;
import com.example.youdrawiguess.bean.Coordinate;
import com.example.youdrawiguess.db.MessageDao;
import com.google.gson.Gson;
//import com.example.youdrawiguess.util.JsonBinder;

public class PaintReceiver extends BroadcastReceiver {

	private static int count = 0;

	private ActivityManager activityManager;
	private String runningActivity;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {

		if (YunBaManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {

			String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);

            Coordinate coordinate = new Gson().fromJson(msg, Coordinate.class);
			switch (coordinate.getCommand()) {
			case "wake_friend":

				// 获得当前顶层Activity的名字 需要添加权限"android.permission.GET_TASKS"
				activityManager = (ActivityManager) context
						.getSystemService(Context.ACTIVITY_SERVICE);
				runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
						.getClassName();
				// 如果顶层已经是画板界面 将不再唤醒
				if (!"com.example.youdrawiguess.activity.SketchpadActivity"
						.equals(runningActivity)) {

					NotificationManager notificationManager = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(
							R.drawable.logo, "你收到一条新信息",
							System.currentTimeMillis());
					// 实例化Intent
					Intent intent2 = new Intent(context,
							SketchpadActivity.class);
					intent2.putExtra("phoneNumber", coordinate.getPhoneNumber());
					intent2.putExtra("online", true);

					// 获得PendingIntent
					PendingIntent pendingIntent = PendingIntent.getActivity(
							context, 0, intent2, 0);
					// 设置事件信息
//					notification.setLatestEventInfo(context, "涂鸦",
//							coordinate.getPhoneNumber() + "想与您一同画画",
//							pendingIntent);

					notification.flags |= Notification.FLAG_AUTO_CANCEL; // 通知被点击后，自动消失
					notification.defaults |= Notification.DEFAULT_SOUND;// 默认通知音乐
					// notification.sound = Uri
					// .parse("file:///sdcard/notification/ringer.mp3");//
					// 如果使用自定义声音

					// 发出通知
					notificationManager.notify(count++, notification);
				}
				break;
			case "online":
				MyApplication.onlineMap.put(coordinate.getPhoneNumber(),
						coordinate.isOnline());
				processCustomMessage(context, coordinate);
				break;
			case "message":
				// 将聊天消息保存本地数据库
				MessageDao messageDao = new MessageDao();
				messageDao.saveMessage(coordinate);

				processCustomMessage(context, coordinate);

				// 获得当前顶层Activity的名字 需要添加权限"android.permission.GET_TASKS"
				activityManager = (ActivityManager) context
						.getSystemService(Context.ACTIVITY_SERVICE);
				runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
						.getClassName();
				// 如果顶层已经是聊天界面 将不在弹出Notification
				if (!"com.example.youdrawiguess.activity.ChatActivity"
						.equals(runningActivity)) {
					NotificationManager notificationManager = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(
							R.drawable.logo, "你收到一条新信息",
							System.currentTimeMillis());
					// 实例化Intent
					Intent intent2 = new Intent(context, ChatActivity.class);
					intent2.putExtra("friend", coordinate.getPhoneNumber());

					// 获得PendingIntent
					PendingIntent pendingIntent = PendingIntent.getActivity(
							context, 0, intent2, 0);
					// 设置事件信息
//					notification.setLatestEventInfo(context, "涂鸦",
//							coordinate.getPhoneNumber() + "发来一条新消息",
//							pendingIntent);

					notification.flags |= Notification.FLAG_AUTO_CANCEL; // 通知被点击后，自动消失
					notification.defaults |= Notification.DEFAULT_SOUND;// 默认通知音乐
					// notification.sound = Uri
					// .parse("file:///sdcard/notification/ringer.mp3");//
					// 如果使用自定义声音

					// 发出通知
					notificationManager.notify(count++, notification);
				}
				break;
			default:
				processCustomMessage(context, coordinate);
				break;
			}
		}
	}

	/**
	 * 唤醒画板
	 * */
	private void wakeSketchpad(Context context, Coordinate coordinate) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ComponentName componentName = new ComponentName(
				"com.example.youdrawiguess",
				"com.example.youdrawiguess.activity.SketchpadActivity");
		intent.setComponent(componentName);
		intent.putExtra("phoneNumber", coordinate.getPhoneNumber());
		intent.putExtra("online", true);
		context.startActivity(intent);
	}

	/**
	 * 发送广播
	 * */
	private void processCustomMessage(Context context, Coordinate coordinate) {
		Intent intent = new Intent();
		intent.setAction("com.example.paint");
		intent.putExtra("coordinate", coordinate);
		context.sendBroadcast(intent);
	}
}
