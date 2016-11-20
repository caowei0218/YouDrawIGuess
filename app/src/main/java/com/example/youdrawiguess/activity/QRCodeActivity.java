package com.example.youdrawiguess.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.util.QRCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class QRCodeActivity extends Activity {

	private ImageView iv_qr_code;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr_code_layout);
		MyApplication.addActivity(this);

		init();

		QRCode qrCode = new QRCode();
		Bitmap bm;
		try {
			bm = qrCode.getQRCode(MyApplication.phoneNumber,
					BarcodeFormat.QR_CODE);
			iv_qr_code.setImageBitmap(bm);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
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
