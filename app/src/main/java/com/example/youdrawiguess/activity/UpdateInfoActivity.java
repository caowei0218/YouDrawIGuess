package com.example.youdrawiguess.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;

public class UpdateInfoActivity extends Activity implements OnClickListener {

	private String nikename;
	private String email;
	private String description;
	private String address;
	private String city;
	private String gender;
	private String phoneNumber;

	private Intent intent;

	private TextView tv_title;
	private RelativeLayout rl_menu;
	private ImageView iv_menu;
	private EditText et_content;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_personal_info_layout);
		MyApplication.addActivity(this);

		init();
	}

	private void init() {
		findViewById();
		getData();
		setOnClickListener();
	}

	private void getData() {
		intent = getIntent();
		nikename = intent.getStringExtra("nikename");
		email = intent.getStringExtra("email");
		description = intent.getStringExtra("description");
		address = intent.getStringExtra("address");
		city = intent.getStringExtra("city");
		gender = intent.getStringExtra("gender");
		phoneNumber = intent.getStringExtra("phoneNumber");

		if (nikename != null) {
			tv_title.setText("昵称");
			et_content.setText(nikename);
		}
		if (email != null) {
			tv_title.setText("邮箱");
			et_content.setText(email);
		}
		if (description != null) {
			tv_title.setText("个性签名");
			et_content.setText(description);
		}
		if (address != null) {
			tv_title.setText("地址");
			et_content.setText(address);
		}
		if (city != null) {
			tv_title.setText("地区");
			et_content.setText(city);
		}
		if (gender != null) {
			tv_title.setText("性别");
			et_content.setText(gender);
		}
		if (phoneNumber != null) {
			tv_title.setText("电话");
			et_content.setText(phoneNumber);
		}
	}

	private void setOnClickListener() {
		rl_menu.setOnClickListener(this);
	}

	private void findViewById() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_menu = (RelativeLayout) findViewById(R.id.rl_menu);
		iv_menu = (ImageView) findViewById(R.id.iv_menu);
		et_content = (EditText) findViewById(R.id.et_content);

		rl_menu.setVisibility(View.VISIBLE);
		iv_menu.setImageDrawable(getResources()
				.getDrawable(R.drawable.btn_back));
	}

	@Override
	public void onClick(View v) {

		// UpdateInfoWebservice updateInfoWebservice;

		switch (v.getId()) {
		// case R.id.tv_update:
		// if (nikename != null) {
		// updateInfoWebservice = new UpdateInfoWebservice(
		// UpdateInfoActivity.this, this, "nickname", et_content
		// .getText().toString().trim());
		// updateInfoWebservice.execute();
		// }
		// if (description != null) {
		// updateInfoWebservice = new UpdateInfoWebservice(
		// UpdateInfoActivity.this, this, "description",
		// et_content.getText().toString().trim());
		// updateInfoWebservice.execute();
		// }
		// if (email != null) {
		// updateInfoWebservice = new UpdateInfoWebservice(
		// UpdateInfoActivity.this, this, "email", et_content
		// .getText().toString().trim());
		// updateInfoWebservice.execute();
		// }
		// if (address != null) {
		// updateInfoWebservice = new UpdateInfoWebservice(
		// UpdateInfoActivity.this, this, "address", et_content
		// .getText().toString().trim());
		// updateInfoWebservice.execute();
		// }
		// if (city != null) {
		// updateInfoWebservice = new UpdateInfoWebservice(
		// UpdateInfoActivity.this, this, "city", et_content
		// .getText().toString().trim());
		// updateInfoWebservice.execute();
		// }
		// if (gender != null) {
		// updateInfoWebservice = new UpdateInfoWebservice(
		// UpdateInfoActivity.this, this, "gender", et_content
		// .getText().toString().trim());
		// updateInfoWebservice.execute();
		// }
		// if (phoneNumber != null) {
		// updateInfoWebservice = new UpdateInfoWebservice(
		// UpdateInfoActivity.this, this, "phoneNumber",
		// et_content.getText().toString().trim());
		// updateInfoWebservice.execute();
		// }
		// break;
		case R.id.rl_menu:
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
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
