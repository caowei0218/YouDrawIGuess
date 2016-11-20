package com.example.youdrawiguess.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.util.AppInfo;

/**
 * 关于开发者
 * */
public class AboutActivity extends Activity implements OnClickListener {

	private TextView tv_title;
	private TextView version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about_layout);

		MyApplication.addActivity(this);

		init();

		tv_title.setText("关于");
	}

	@Override
	protected void onResume() {
		super.onResume();
		version.setText(AppInfo.getAppVersionName(this));
	}

	private void init() {
		version = (TextView) findViewById(R.id.version);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	public void onClick(View arg0) {

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
