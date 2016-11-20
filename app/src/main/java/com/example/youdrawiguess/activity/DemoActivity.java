package com.example.youdrawiguess.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class DemoActivity extends Activity {

	private Paint mPaint;

	private DisplayMetrics displayMetrics;
	private int window_width;
	private int window_height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		window_height = displayMetrics.heightPixels;
		window_width = displayMetrics.widthPixels;

		setContentView(new TouchView(this));

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFF00FF00);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(10);
	}

	public class TouchView extends View {

		private Bitmap mBitmap;
		private Canvas mCanvas;
		private Path path;
		private Paint mBitmapPaint;

		public TouchView(Context c) {
			super(c);
			/*
			 * android.graphics.Bitmap.Config是一个枚举类型,里面定义了位图的四种格式
			 * ALPHA_8:数字为8，图形参数应该由一个字节来表示,应该是一种8位的位图
			 * ARGB_4444:4+4+4+4=16，图形的参数应该由两个字节来表示,应该是一种16位的位图.
			 * ARGB_8888:8+8+8+8=32，图形的参数应该由四个字节来表示,应该是一种32位的位图.
			 * RGB_565:5+6+5=16，图形的参数应该由两个字节来表示,应该是一种16位的位图.
			 * 
			 * 网上讲,ALPHA_8，ARGB_4444,ARGB_8888都是透明的位图,也就是所字母A代表透明.
			 * ARGB_4444:意味着有四个参数,即A,R,G,B,每一个参数由4bit表示. 同理:
			 * ARGB_8888:意味着有四个参数,即A,R,G,B,每一个参数由8bit来表示. 同理:
			 * RGB_565:意味着有三个参数,R,G,B,三个参数分别占5bit,6bit,5bit.
			 */
			mBitmap = Bitmap.createBitmap(window_width, window_height,
					Bitmap.Config.RGB_565);
			mCanvas = new Canvas(mBitmap);
			path = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
			canvas.drawPath(path, mPaint);
		}

		private float mX, mY;
		// 只有当移动的距离大于4px时,才在屏幕上绘图
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			path.reset();
			path.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);

			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {

				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);// 二次方贝塞尔曲线
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {
			path.lineTo(mX, mY);
			mCanvas.drawPath(path, mPaint);
			path.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
			return true;
		}
	}
}