package com.example.youdrawiguess.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.adapter.ChatMessageAdapter;
import com.example.youdrawiguess.bean.Coordinate;
import com.example.youdrawiguess.bean.Coordinate.Type;
import com.example.youdrawiguess.db.MessageDao;
import com.example.youdrawiguess.util.ToastUtil;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.List;

import io.yunba.android.manager.YunBaManager;

public class ChatActivity extends Activity implements OnClickListener {

	private RelativeLayout rl_menu;
	private ImageView iv_menu;
	private TextView tv_title;

	private EditText et_message;
	private Button send;
	private ListView lv_chat;

	private String friendUserId;
	private Coordinate message;
	private List<Coordinate> messages;

	private ChatMessageAdapter chatMessageAdapter;

	private MessageDao messageDao;

	private MyBroadcastReceiver broadcastReceiver;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.chat_layout);
		MyApplication.addActivity(this);// 将该activity添加到管理类中去

		friendUserId = (String) getIntent().getExtras().get("friend");
		messageDao = new MessageDao();
		messages = messageDao.getMessage(MyApplication.phoneNumber,
				friendUserId);

		init();
		// // 判断该人是否是好友
		// if (friendBean.getUsername().equals(
		// messageDao.getFriendInfo(friendBean.getUsername())
		// .getUsername())) {
		// // 是好友 隐藏添加按钮
		// btn_add_friends.setVisibility(View.INVISIBLE);
		// } else {
		// // 不是好友 显示添加按钮
		// btn_add_friends.setVisibility(View.VISIBLE);
		// }

	}

	@Override
	protected void onResume() {
		super.onResume();

		setOnClickListener();

		chatMessageAdapter = new ChatMessageAdapter(this, messages);
		lv_chat.setAdapter(chatMessageAdapter);
		lv_chat.setSelection(messages.size() - 1);// 默认最后一行

		registerBoradcastReceiver();
	}

	@Override
	protected void onStop() {
		super.onStop();

		unregisterReceiver(broadcastReceiver);
	}

	/**
	 * 发送信息
	 * */
	private void handlePublishAlias(final String msg, final String alias) {
		if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(alias)) {
			ToastUtil.showToast(this, "消息不能为空", Toast.LENGTH_LONG);
			return;
		}

		// 将发送者id和msg进行josn封装
//		JsonBinder jsonBinder = JsonBinder.buildNonDefaultBinder();
		message = new Coordinate();
		message.setCommand("message");
		message.setPhoneNumber(MyApplication.phoneNumber);
		message.setSender(MyApplication.phoneNumber);// 如果取不到值就取值后面的参数
		message.setMsg(msg);
		message.setReceiver(alias);
		message.setType(Type.OUTPUT);
		// 将聊天记录添加到本地数据库中
		messageDao = new MessageDao();
		messageDao.saveMessage(message);

		updateChatView(message);
		YunBaManager.publishToAlias(getApplicationContext(), alias, new Gson().toJson(message), new IMqttActionListener() {
					public void onSuccess(IMqttToken asyncActionToken) {

					}

					@Override
					public void onFailure(IMqttToken asyncActionToken,
							Throwable exception) {
					}
				});
	}

	/**
	 * 注册广播
	 * */
	private void registerBoradcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.example.paint");
		broadcastReceiver = new MyBroadcastReceiver();
		registerReceiver(broadcastReceiver, intentFilter);
	}

	/**
	 * 广播接收器
	 * */
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Coordinate message = (Coordinate) intent.getExtras().get(
					"coordinate");
			message.setType(Type.INPUT);

			switch (message.getCommand()) {
			case "message":
				updateChatView(message);
				break;
			}
		}
	}

	/**
	 * 更新聊天窗口
	 * */
	public void updateChatView(Coordinate message) {
		messageDao = new MessageDao();
		messages = messageDao.getMessage(MyApplication.phoneNumber,
				friendUserId);

		chatMessageAdapter = new ChatMessageAdapter(this, messages);
		lv_chat.setAdapter(chatMessageAdapter);
		lv_chat.setSelection(messages.size() - 1);// 默认最后一行
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.send:
			if (!"".equals(et_message.getText().toString().trim())) {
				handlePublishAlias(this.et_message.getText().toString().trim(),
						friendUserId);
				et_message.setText("");
			}
			// YunBaManager.publish(getApplicationContext(),
			// MyApplication.TOPIC, "r", null);
			break;
		case R.id.rl_menu:
			moveTaskToBack(false);
			setResult(1);
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		default:
			break;
		}
	}

	private void init() {
		rl_menu = (RelativeLayout) findViewById(R.id.rl_menu);
		iv_menu = (ImageView) findViewById(R.id.iv_menu);
		tv_title = (TextView) findViewById(R.id.tv_title);

		et_message = (EditText) findViewById(R.id.et_message);
		send = (Button) findViewById(R.id.send);
		lv_chat = (ListView) findViewById(R.id.lv_chat);

		rl_menu.setVisibility(View.VISIBLE);
		iv_menu.setImageDrawable(getResources()
				.getDrawable(R.drawable.btn_back));
		tv_title.setText(friendUserId);

	}

	private void setOnClickListener() {
		send.setOnClickListener(this);
		rl_menu.setOnClickListener(this);
	}

	/**
	 * 返回
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			moveTaskToBack(false);
			setResult(1);
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
