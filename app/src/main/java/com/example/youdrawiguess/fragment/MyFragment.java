package com.example.youdrawiguess.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.activity.MyInfoActivity;
import com.example.youdrawiguess.activity.SetInfoActivity;
import com.example.youdrawiguess.activity.WebViewActivity;
import com.example.youdrawiguess.util.BitmapCache;
import com.example.youdrawiguess.util.Common;
import com.example.youdrawiguess.view.CustomNetworkImageView;

@SuppressLint("NewApi")
public class MyFragment extends Fragment implements OnClickListener {

	View messageLayout;

	private TextView tv_title;
	private TextView tv_nickname, tv_username;
	private RelativeLayout rl_info, rl_collect, rl_schedule, rl_setting,
			rl_web;
	private CustomNetworkImageView iv_avatar;

	private ImageLoader imageLoader;
	private RequestQueue mQueue;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		messageLayout = inflater.inflate(R.layout.tab_my_layout, container,
				false);

		init();

		return messageLayout;
	}

	@Override
	public void onResume() {
		super.onResume();

		mQueue = Volley.newRequestQueue(getActivity());
		imageLoader = new ImageLoader(mQueue, new BitmapCache(
				MyApplication.phoneNumber));
		iv_avatar.setImageUrl(Common.IMAGES_URL + MyApplication.phoneNumber,
				imageLoader);
		tv_username.setText(MyApplication.phoneNumber);
		tv_nickname.setText(MyApplication.phoneNumber);
	}

	private void init() {
		tv_title = (TextView) messageLayout.findViewById(R.id.tv_title);
		rl_info = (RelativeLayout) messageLayout.findViewById(R.id.rl_info);
		rl_collect = (RelativeLayout) messageLayout
				.findViewById(R.id.rl_collect);
		rl_schedule = (RelativeLayout) messageLayout
				.findViewById(R.id.rl_schedule);
		rl_setting = (RelativeLayout) messageLayout
				.findViewById(R.id.rl_setting);
		rl_web = (RelativeLayout) messageLayout.findViewById(R.id.rl_web);
		tv_nickname = (TextView) messageLayout.findViewById(R.id.tv_nickname);
		tv_username = (TextView) messageLayout.findViewById(R.id.tv_username);
		iv_avatar = (CustomNetworkImageView) messageLayout
				.findViewById(R.id.iv_avatar);

		rl_info.setOnClickListener(this);
		rl_collect.setOnClickListener(this);
		rl_schedule.setOnClickListener(this);
		rl_setting.setOnClickListener(this);
		rl_web.setOnClickListener(this);

		tv_title.setText("我的");

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_info:
			intent = new Intent(getActivity(), MyInfoActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_collect:
			Toast.makeText(getActivity(), "待开发", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rl_schedule:
			Toast.makeText(getActivity(), "待开发", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rl_setting:
			intent = new Intent(getActivity(), SetInfoActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		case R.id.rl_web:
			intent = new Intent(getActivity(), WebViewActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);// 实现淡入浅出的效果
			break;
		}
	}
}
