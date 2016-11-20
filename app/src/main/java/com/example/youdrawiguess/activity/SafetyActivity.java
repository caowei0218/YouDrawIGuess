package com.example.youdrawiguess.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;

public class SafetyActivity extends Activity implements OnClickListener {

	private RelativeLayout rl_phone, rl_email;
	private TextView tv_title;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.safety_layout);

		MyApplication.addActivity(this);

		init();
	}

	private void init() {
		rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
		rl_email = (RelativeLayout) findViewById(R.id.rl_email);

		tv_title = (TextView) findViewById(R.id.tv_title);

		rl_phone.setOnClickListener(this);
		rl_email.setOnClickListener(this);

		tv_title.setText("安全");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_phone:
			Toast.makeText(this, "待开发", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rl_email:
			Toast.makeText(this, "待开发", Toast.LENGTH_SHORT).show();
			break;
		}
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
