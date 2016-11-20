package com.example.youdrawiguess.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.youdrawiguess.view.ColorPickerView;

public class ColorPickerDialog extends Dialog {

	private OnColorChangedListener mListener;
	private int mInitialColor;

	/**
	 * @param listener
	 *            选中的颜色监听
	 * @param initialColor
	 *            默认颜色
	 * */
	public ColorPickerDialog(Context context, OnColorChangedListener listener,
			int initialColor) {
		super(context);

		mListener = listener;
		mInitialColor = initialColor;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		OnColorChangedListener changedListener = new OnColorChangedListener() {
			public void colorChanged(int color) {
				mListener.colorChanged(color);
				dismiss();
			}
		};

		setContentView(new ColorPickerView(getContext(), changedListener,
				mInitialColor));

	}

	public interface OnColorChangedListener {
		void colorChanged(int color);
	}

}