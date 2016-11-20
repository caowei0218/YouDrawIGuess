package com.example.youdrawiguess.activity;

import io.yunba.android.manager.YunBaManager;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.util.PhoneInfo;
import com.example.youdrawiguess.util.ToastUtil;

public class LoginActivity extends Activity implements OnClickListener {

	private DisplayMetrics displayMetrics;
	private int window_width;
	private int window_height;
	private LayoutParams para;
	private RelativeLayout login_rl_phone;
	private Button login_btn_login;
	private EditText login_et_phone;

	private String myPhoneNumber;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_layout);

		initView();
		initParams();

		String username = getUsername();
		if ("".equals(username)) {
			MyApplication.phoneNumber = getPhoneNumber();
			if (MyApplication.phoneNumber != null
					&& !"".equals(MyApplication.phoneNumber)) {// 如果获取到本机号码了

				// 如果有“+86”，则将电话号码的“+86”去掉
				if (MyApplication.phoneNumber.length() > 11) {
					MyApplication.phoneNumber = MyApplication.phoneNumber
							.substring(3, MyApplication.phoneNumber.length());
				}

				ToastUtil.showToast(this, "已获取本机号码："
						+ MyApplication.phoneNumber + "正在登陆中...",
						Toast.LENGTH_LONG);

				// 判断推送服务是否停止服务
				if (YunBaManager.isStopped(this)) {
					YunBaManager.resume(this);// 如果停止服务 重启
				}
				loginYunba();

			} else {
				ToastUtil.showToast(this, "获取本机号码失败，请手动输入您的手机号码"
						+ MyApplication.phoneNumber, Toast.LENGTH_LONG);
			}
		} else {
			// 判断推送服务是否停止服务
			if (YunBaManager.isStopped(this)) {
				YunBaManager.resume(this);// 如果停止服务 重启
			}
			MyApplication.phoneNumber = username;
			loginYunba();
		}
	}

	private void initView() {
		login_btn_login = (Button) findViewById(R.id.login_btn_login);
		login_rl_phone = (RelativeLayout) findViewById(R.id.login_rl_phone);
		login_et_phone = (EditText) findViewById(R.id.login_et_phone);

		login_btn_login.setOnClickListener(this);
	}

	private void initParams() {
		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		window_height = displayMetrics.heightPixels;
		window_width = displayMetrics.widthPixels;

		para = login_rl_phone.getLayoutParams();
		para.height = (int) (window_height * (80.0 / 1334));
		para.width = (int) (window_width * (582.0 / 750));
		login_rl_phone.setLayoutParams(para);

		para = login_btn_login.getLayoutParams();
		para.height = (int) (window_height * (92.0 / 1334));
		para.width = (int) (window_width * (276.0 / 750));
		login_btn_login.setLayoutParams(para);
	}

	/**
	 * 获得本机电话号码
	 * */
	private String getPhoneNumber() {
		PhoneInfo siminfo = new PhoneInfo(LoginActivity.this);
		return siminfo.getNativePhoneNumber();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login_btn_login:
			myPhoneNumber = login_et_phone.getText().toString().trim();
			if ("".equals(myPhoneNumber)) {
				ToastUtil.showToast(getApplicationContext(), "电话号码不能为空",
						Toast.LENGTH_LONG);
			} else {
				MyApplication.phoneNumber = myPhoneNumber;

				// 判断推送服务是否停止服务
				if (YunBaManager.isStopped(this)) {
					YunBaManager.resume(this);// 如果停止服务 重启
				}
				loginYunba();
			}
			break;
		}
	}

	/**
	 * 登录云巴
	 * */
	private void loginYunba() {
		YunBaManager.setAlias(this, MyApplication.phoneNumber,
				new IMqttActionListener() {
					@Override
					public void onSuccess(IMqttToken asyncActionToken) {

						if (MyApplication.phoneNumber != null
								&& !"".equals(MyApplication.phoneNumber)) {
							saveUsername(MyApplication.phoneNumber);
						} else {
							saveUsername(myPhoneNumber);
						}

						Intent intent = new Intent(LoginActivity.this,
								FragmentActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
					}

					@Override
					public void onFailure(IMqttToken asyncActionToken,
							Throwable exception) {
						if (exception instanceof MqttException) {
							MqttException ex = (MqttException) exception;
							String msg = "setAlias failed with error code : "
									+ ex.getReasonCode();
						}
					}
				});
	}

	/**
	 * 保存登陆信息
	 * */
	private void saveUsername(String username) {
		SharedPreferences sp1 = getSharedPreferences("info",
				Context.MODE_PRIVATE);
		sp1.edit().putString("username", username).commit();
	}

	/**
	 * 获得保存本地的用户名
	 * */
	private String getUsername() {
		SharedPreferences sp1 = getSharedPreferences("info",
				Context.MODE_PRIVATE);
		return sp1.getString("username", "");
	}

}
