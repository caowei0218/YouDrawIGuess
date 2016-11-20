package com.example.youdrawiguess.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.bean.Coordinate;
import com.example.youdrawiguess.util.ColorPickerDialog;
import com.example.youdrawiguess.util.ColorPickerDialog.OnColorChangedListener;
import com.example.youdrawiguess.util.ToastUtil;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import io.yunba.android.manager.YunBaManager;

/**
 * 画板
 * */
public class SketchpadActivity extends Activity implements OnClickListener,
		OnTouchListener {

	private float startX;// 画板X坐标
	private float startY;// 画板Y坐标

	private final String online = "当前画板";
	private final String unonline = "不在当前画板";

	private DisplayMetrics displayMetrics;
	private int window_width;
	private int window_height;

	private ImageView iv_sketchpad;// 画板
	private ImageView iv_wake_friend;
	private ImageView iv_paint_color;
	private ImageView iv_clear_sketchpad;
	private TextView iv_isOnline;

	private Bitmap baseBitmap;
	private Canvas canvas;// 画布
	private Paint paint;// 画笔
	private Paint friendPaint;// 好友画笔

	private Coordinate coordinate;
	private static long order = 0;

	private String phoneNumber;// 好友电话号码

	private MyBroadcastReceiver broadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sketchpad_layout);
		MyApplication.addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		registerBoradcastReceiver();

		// 好友电话号码
		phoneNumber = (String) getIntent().getExtras().get("phoneNumber");

		init();
		findById();

		// 是否在当前画板标志
		boolean isOnline = getIntent().getBooleanExtra("online", false);
		if (isOnline) {
			MyApplication.onlineMap.put(phoneNumber, isOnline);
			coordinate = new Coordinate();
			coordinate.setCommand("online");
			coordinate.setPhoneNumber(MyApplication.phoneNumber);
			coordinate.setOnline(true);
			handlePublishAlias(coordinate, phoneNumber);// 告诉好友 我在线了

			iv_isOnline.setText("\"" + phoneNumber + "\"" + online);
		}

		createCanvas(window_width, window_height, Color.WHITE);

		paint = createPaint(Color.BLACK, 10);

		friendPaint = createPaint(Color.BLACK, 10);

		// 先将背景画上
		canvas.drawBitmap(baseBitmap, new Matrix(), paint);

		iv_sketchpad.setImageBitmap(baseBitmap);
		iv_sketchpad.setOnTouchListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		unregisterReceiver(broadcastReceiver);

		// 推送下线指令
		coordinate = new Coordinate();
		coordinate.setCommand("online");
		coordinate.setOnline(false);
		coordinate.setPhoneNumber(MyApplication.phoneNumber);
		handlePublishAlias(coordinate, phoneNumber);
	}

	private void init() {
		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		window_height = displayMetrics.heightPixels;
		window_width = displayMetrics.widthPixels;
	}

	private void findById() {
		iv_sketchpad = (ImageView) findViewById(R.id.iv_sketchpad);
		iv_wake_friend = (ImageView) findViewById(R.id.iv_wake_friend);
		iv_paint_color = (ImageView) findViewById(R.id.iv_paint_color);
		iv_clear_sketchpad = (ImageView) findViewById(R.id.iv_clear_sketchpad);
		iv_isOnline = (TextView) findViewById(R.id.iv_isOnline);

		if (MyApplication.onlineMap.get(phoneNumber) == null) {
			iv_isOnline.setText("\"" + phoneNumber + "\"" + unonline);
		} else {
			if (MyApplication.onlineMap.get(phoneNumber)) {
				iv_isOnline.setText("\"" + phoneNumber + "\"" + online);
			} else {
				iv_isOnline.setText("\"" + phoneNumber + "\"" + unonline);
			}
		}

		iv_wake_friend.setOnClickListener(this);
		iv_paint_color.setOnClickListener(this);
		iv_clear_sketchpad.setOnClickListener(this);
	}

	/**
	 * 创建画布
	 * */
	private void createCanvas(int width, int height, int color) {
		// 创建一张空白图片
		baseBitmap = Bitmap
				.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 创建一张画布
		canvas = new Canvas(baseBitmap);
		// 画布背景为灰色
		canvas.drawColor(color);
	}

	/**
	 * 创建画笔
	 * */
	private Paint createPaint(int color, int paintWidth) {
		// 创建画笔
		Paint paint = new Paint();
		// 画笔颜色为黑色
		paint.setColor(color);
		// 宽度10个像素
		paint.setStrokeWidth(paintWidth);

		paint.setAntiAlias(true);// 抗锯齿
		paint.setStyle(Style.STROKE);// 设置画笔为空心
		paint.setStrokeCap(Cap.ROUND);// 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式Cap.ROUND,或方形样式Cap.SQUARE

		return paint;
	}

	/**
	 * 发送信息
	 * */
	private void handlePublishAlias(Coordinate coordinate, String alias) {
		if (TextUtils.isEmpty(alias)) {
			return;
		}

		YunBaManager.publishToAlias(getApplicationContext(), alias,
				new Gson().toJson(coordinate), new IMqttActionListener() {
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
		LayoutInflater inflater;
		AlertDialog.Builder builder;
		Coordinate coo;

		@Override
		public void onReceive(Context context, Intent intent) {
			Coordinate coordinate = (Coordinate) intent.getExtras().get(
					"coordinate");

			switch (coordinate.getCommand()) {
			case "paint":
				if (phoneNumber.equals(coordinate.getPhoneNumber())) {
					updateSketchpad(coordinate, false);
				}
				break;
			case "online":
				if (coordinate.isOnline()) {
					iv_isOnline.setText("\"" + phoneNumber + "\"" + online);
				} else {
					iv_isOnline.setText("\"" + phoneNumber + "\"" + unonline);
				}
				break;
			case "paint_color":
				friendPaint = createPaint(coordinate.getPaint_color(), 10);
				break;
			case "clear_sketchpad":
				inflater = LayoutInflater.from(SketchpadActivity.this);
				builder = new AlertDialog.Builder(SketchpadActivity.this);
				builder.setCancelable(false);
				builder.setTitle("收到清屏指令，确定清屏吗？：");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 推送清屏同意指令
								coo = new Coordinate();
								coo.setCommand("isclear");
								coo.setClear(true);
								handlePublishAlias(coo, phoneNumber);

								createCanvas(window_width, window_height,
										Color.WHITE);
								iv_sketchpad.setImageBitmap(baseBitmap);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 推送清屏不同意指令
								coo = new Coordinate();
								coo.setCommand("isclear");
								coo.setClear(false);
								handlePublishAlias(coo, phoneNumber);
							}
						});
				builder.show();

				break;
			case "isclear":
				if (coordinate.isClear()) {
					createCanvas(window_width, window_height, Color.WHITE);
					iv_sketchpad.setImageBitmap(baseBitmap);
				} else {
				}
				break;
			}
		}
	}

	/**
	 * 更新画板
	 * */
	private void updateSketchpad(Coordinate coordinate, boolean isMy) {

		if (isMy) {
			canvas.drawLine(
					window_width * coordinate.getX()
							/ coordinate.getWindow_width(),
					window_height * coordinate.getY()
							/ coordinate.getWindow_height(),
					window_width * coordinate.getStopx()
							/ coordinate.getWindow_width(),
					window_height * coordinate.getStopY()
							/ coordinate.getWindow_height(), paint);
			iv_sketchpad.setImageBitmap(baseBitmap);
		} else {

			canvas.drawLine(
					window_width * coordinate.getX()
							/ coordinate.getWindow_width(),
					window_height * coordinate.getY()
							/ coordinate.getWindow_height(),
					window_width * coordinate.getStopx()
							/ coordinate.getWindow_width(),
					window_height * coordinate.getStopY()
							/ coordinate.getWindow_height(), friendPaint);
			iv_sketchpad.setImageBitmap(baseBitmap);
		}
	}

	@Override
	public void onClick(View arg0) {
		ColorPickerDialog pickerDialog;

		switch (arg0.getId()) {
		case R.id.iv_wake_friend:
			coordinate = new Coordinate();
			coordinate.setPhoneNumber(MyApplication.phoneNumber);
			coordinate.setCommand("wake_friend");
			handlePublishAlias(coordinate, phoneNumber);
			ToastUtil.showToast(this, "唤醒朋友", Toast.LENGTH_LONG);
			break;
		case R.id.iv_paint_color:
			pickerDialog = new ColorPickerDialog(this,
					new OnColorChangedListener() {
						@Override
						public void colorChanged(int color) {
							paint = createPaint(color, 10);// 先改变自己画笔颜色

							coordinate = new Coordinate();
							coordinate.setCommand("paint_color");
							coordinate.setPaint_color(color);
							handlePublishAlias(coordinate, phoneNumber);
						}
					}, Color.BLACK);
			pickerDialog.setTitle("画笔颜色");
			pickerDialog.show();

			break;
		case R.id.iv_clear_sketchpad:
			coordinate = new Coordinate();
			coordinate.setCommand("clear_sketchpad");
			handlePublishAlias(coordinate, phoneNumber);

			ToastUtil.showToast(this, "发送清屏指令，等待好友同意", Toast.LENGTH_LONG);
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 获取手按下时的坐标
			startX = event.getX();
			startY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			// 获取手移动后的坐标
			float stopX = event.getX();
			float stopY = event.getY();
			// 在开始和结束坐标间画一条线
			canvas.drawLine(startX, startY, stopX, stopY, paint);

			coordinate = new Coordinate();
			coordinate.setCommand("paint");
			coordinate.setPhoneNumber(MyApplication.phoneNumber);
			coordinate.setWindow_width(window_width);
			coordinate.setWindow_height(window_height);
			coordinate.setX(startX);
			coordinate.setY(startY);
			coordinate.setStopx(stopX);
			coordinate.setStopY(stopY);
			coordinate.setOrder(order++);

			handlePublishAlias(coordinate, phoneNumber);
			// 实时更新开始坐标
			startX = event.getX();
			startY = event.getY();
			iv_sketchpad.setImageBitmap(baseBitmap);
			break;
		}

		return true;
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
