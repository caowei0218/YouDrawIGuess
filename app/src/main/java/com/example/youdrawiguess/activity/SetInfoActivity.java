package com.example.youdrawiguess.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;

import io.yunba.android.manager.YunBaManager;

public class SetInfoActivity extends Activity implements OnClickListener {

	private RelativeLayout rl_account_security, rl_versionName, rl_about,
			rl_exit, rl_switch_notification, rl_switch_sound,
			rl_switch_vibrate, rl_switch_speaker;
	private TextView tv_title;
	private ImageView iv_switch_open_notification;
	private ImageView iv_switch_close_notification;
	private ImageView iv_switch_open_sound;
	private ImageView iv_switch_close_sound;
	private ImageView iv_switch_open_vibrate;
	private ImageView iv_switch_close_vibrate;
	private ImageView iv_switch_open_speaker;
	private ImageView iv_switch_close_speaker;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.set_info_layout);

		MyApplication.addActivity(this);

		init();
	}

	private void init() {
		rl_account_security = (RelativeLayout) findViewById(R.id.rl_account_security);
		rl_versionName = (RelativeLayout) findViewById(R.id.rl_versionName);
		rl_about = (RelativeLayout) findViewById(R.id.rl_about);
		rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_sound = (RelativeLayout) findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
		rl_switch_speaker = (RelativeLayout) findViewById(R.id.rl_switch_speaker);

		tv_title = (TextView) findViewById(R.id.tv_title);

		iv_switch_open_notification = (ImageView) findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView) findViewById(R.id.iv_switch_close_notification);
		iv_switch_open_sound = (ImageView) findViewById(R.id.iv_switch_open_sound);
		iv_switch_close_sound = (ImageView) findViewById(R.id.iv_switch_close_sound);
		iv_switch_open_vibrate = (ImageView) findViewById(R.id.iv_switch_open_vibrate);
		iv_switch_close_vibrate = (ImageView) findViewById(R.id.iv_switch_close_vibrate);
		iv_switch_open_speaker = (ImageView) findViewById(R.id.iv_switch_open_speaker);
		iv_switch_close_speaker = (ImageView) findViewById(R.id.iv_switch_close_speaker);

		rl_account_security.setOnClickListener(this);
		rl_versionName.setOnClickListener(this);
		rl_about.setOnClickListener(this);
		rl_exit.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		rl_switch_speaker.setOnClickListener(this);

		tv_title.setText("设置");
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_account_security:
			intent = new Intent(SetInfoActivity.this, SafetyActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_versionName:
			// // 检查版本更新
			// GetNewVersionWebservice getVersion = new GetNewVersionWebservice(
			// SetInfoActivity.this, this);
			// getVersion.execute();
			break;
		case R.id.rl_about:
			intent = new Intent(SetInfoActivity.this, AboutActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_exit:
			SharedPreferences prefereces = SetInfoActivity.this
					.getSharedPreferences("info", Context.MODE_PRIVATE);
			prefereces.edit().clear().commit();
			YunBaManager.stop(this);
			startActivity(new Intent(SetInfoActivity.this, LoginActivity.class));
			MyApplication.exit();
			break;
		case R.id.rl_switch_notification:
			if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);

			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
				rl_switch_sound.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);

			}
			break;
		case R.id.rl_switch_sound:
			if (iv_switch_open_sound.getVisibility() == View.VISIBLE) {
				iv_switch_open_sound.setVisibility(View.INVISIBLE);
				iv_switch_close_sound.setVisibility(View.VISIBLE);
			} else {
				iv_switch_open_sound.setVisibility(View.VISIBLE);
				iv_switch_close_sound.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.rl_switch_vibrate:
			if (iv_switch_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
				iv_switch_close_vibrate.setVisibility(View.VISIBLE);
			} else {
				iv_switch_open_vibrate.setVisibility(View.VISIBLE);
				iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.rl_switch_speaker:
			if (iv_switch_open_speaker.getVisibility() == View.VISIBLE) {
				iv_switch_open_speaker.setVisibility(View.INVISIBLE);
				iv_switch_close_speaker.setVisibility(View.VISIBLE);
			} else {
				iv_switch_open_speaker.setVisibility(View.VISIBLE);
				iv_switch_close_speaker.setVisibility(View.INVISIBLE);
			}
			break;
		default:
			break;
		}
	}

	// /**
	// * 检查更新版本
	// * */
	// public void refresh(String newVersion, String link, String fileName) {
	// if (!AppInfo.getAppVersionName(SetInfoActivity.this).equals(newVersion))
	// {
	// UpdateManager updateManager = new UpdateManager(
	// SetInfoActivity.this, link, fileName);
	// updateManager.checkUpdateInfo();
	//
	// } else {
	// showToast("已经是最新版本！");
	// }
	// }

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
