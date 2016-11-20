package com.example.youdrawiguess.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.util.BitmapCache;
import com.example.youdrawiguess.util.Common;
import com.example.youdrawiguess.view.CustomNetworkImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class MyInfoActivity extends Activity implements OnClickListener {

	private TextView tv_title;
	private RelativeLayout rl_menu;
	private ImageView iv_menu;

	private RelativeLayout rl_avatar, rl_nikename, rl_erweima, rl_email,
			rl_description, rl_address, rl_city, rl_gender, rl_phoneNumber;
	private TextView tv_username, tv_nikename, tv_email, tv_description,
			tv_address, tv_city, tv_gender, tv_phoneNumber;

	private CustomNetworkImageView iv_avatar;

	private ImageLoader imageLoader;
	private RequestQueue mQueue;

	private File photoFile;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_info_layout);
		MyApplication.addActivity(this);

		init();
		updateData();
	}

	private void init() {
		rl_avatar = (RelativeLayout) findViewById(R.id.rl_avatar);
		iv_avatar = (CustomNetworkImageView) findViewById(R.id.iv_avatar);
		rl_nikename = (RelativeLayout) findViewById(R.id.rl_nikename);
		rl_erweima = (RelativeLayout) findViewById(R.id.rl_erweima);
		rl_email = (RelativeLayout) findViewById(R.id.rl_email);
		rl_description = (RelativeLayout) findViewById(R.id.rl_description);
		rl_address = (RelativeLayout) findViewById(R.id.rl_address);
		rl_city = (RelativeLayout) findViewById(R.id.rl_city);
		rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
		rl_phoneNumber = (RelativeLayout) findViewById(R.id.rl_phoneNumber);

		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_nikename = (TextView) findViewById(R.id.tv_nikename);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_description = (TextView) findViewById(R.id.tv_description);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		tv_phoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);

		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_menu = (RelativeLayout) findViewById(R.id.rl_menu);
		iv_menu = (ImageView) findViewById(R.id.iv_menu);

		rl_menu.setVisibility(View.VISIBLE);
		iv_menu.setImageDrawable(getResources()
				.getDrawable(R.drawable.btn_back));

		rl_menu.setOnClickListener(this);
		rl_avatar.setOnClickListener(this);
		rl_nikename.setOnClickListener(this);
		rl_erweima.setOnClickListener(this);
		rl_email.setOnClickListener(this);
		rl_description.setOnClickListener(this);
		rl_address.setOnClickListener(this);
		rl_city.setOnClickListener(this);
		rl_gender.setOnClickListener(this);
		rl_phoneNumber.setOnClickListener(this);
	}

	/**
	 * 修改页面数据
	 * */
	private void updateData() {
		tv_username.setText(MyApplication.phoneNumber);
		tv_phoneNumber.setText(MyApplication.phoneNumber);

		tv_title.setText("个人信息");

		// // 如果头像在本地有 就直接去本地加载 没有再去网络加载
		// if (BitmapUtil.isExists(Common.userBean.getPhotoName())) {
		// iv_avatar.setLocalImageBitmap(BitmapUtil
		// .getLocalBitmap(Common.userBean.getPhotoName()));
		// } else {
		// // Volley框架 头像加载
		// mQueue = Volley.newRequestQueue(this);
		// imageLoader = new ImageLoader(mQueue, new BitmapCache(
		// Common.userBean.getPhotoName()));
		// iv_avatar.setImageUrl(
		// Common.IMAGES_URL + Common.userBean.getPhotoName(),
		// imageLoader);
		// }

		mQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(mQueue, new BitmapCache(
				MyApplication.phoneNumber));
		iv_avatar.setImageUrl(Common.IMAGES_URL + MyApplication.phoneNumber,
				imageLoader);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_avatar:
			addImage();
			break;
		case R.id.rl_nikename:
			intent = new Intent(MyInfoActivity.this, UpdateInfoActivity.class);
			intent.putExtra("nikename", tv_nikename.getText().toString().trim());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_erweima:
			intent = new Intent(MyInfoActivity.this, QRCodeActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_email:
			intent = new Intent(MyInfoActivity.this, UpdateInfoActivity.class);
			intent.putExtra("email", tv_email.getText().toString().trim());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_description:
			intent = new Intent(MyInfoActivity.this, UpdateInfoActivity.class);
			intent.putExtra("description", tv_description.getText().toString()
					.trim());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_address:
			intent = new Intent(MyInfoActivity.this, UpdateInfoActivity.class);
			intent.putExtra("address", tv_address.getText().toString().trim());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_city:
			intent = new Intent(MyInfoActivity.this, UpdateInfoActivity.class);
			intent.putExtra("city", tv_city.getText().toString().trim());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_gender:
			intent = new Intent(MyInfoActivity.this, UpdateInfoActivity.class);
			intent.putExtra("gender", tv_gender.getText().toString().trim());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_phoneNumber:
			intent = new Intent(MyInfoActivity.this, UpdateInfoActivity.class);
			intent.putExtra("phoneNumber", tv_phoneNumber.getText().toString()
					.trim());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_menu:
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2) { // 获取原图
			startPhotoZoom(Uri.fromFile(photoFile));
		}

		if (requestCode == 1) {
			if (data != null) {
				startPhotoZoom(data.getData());
			}
		}

		if (requestCode == 3) {
		}
	}

	/**
	 * 添加图片
	 * */
	private void addImage() {
		try {
			final String[] options = new String[] { "拍照", "从相册获取" };
			new AlertDialog.Builder(MyInfoActivity.this).setTitle("请选择图片获取方式")
					.setItems(options, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (options[which].equals("拍照")) {
								takePhoto();
							} else if (options[which].equals("从相册获取")) {
								Intent intent = new Intent(
										Intent.ACTION_GET_CONTENT, null);
								intent.setDataAndType(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
										"image/*");
								startActivityForResult(intent, 1);
							}
						}
					}).show();
		} catch (Exception e) {
		}
	}

	/**
	 * 获取图片
	 * */
	private void takePhoto() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		simpleDateFormat.format(new Date());
		// 判断是否有内存卡
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			photoFile = new File(Environment.getExternalStorageDirectory()// 获取SD卡根目录
					+ "/DCIM/Camera/"
					+ simpleDateFormat.format(new Date())
					+ ".jpg");// 图片储存路径
		} else {
			photoFile = new File(Environment.getRootDirectory()// 获取手机根目录
					+ "/DCIM/Camera/"
					+ simpleDateFormat.format(new Date())
					+ ".jpg");// 图片储存路径
		}
		if (!photoFile.getParentFile().exists()) {
			photoFile.getParentFile().mkdirs();
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
		startActivityForResult(intent, 2);
	}

	/** 图片裁剪 */
	public void startPhotoZoom(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);

		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
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
