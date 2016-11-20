package com.example.youdrawiguess.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.activity.ChatActivity;
import com.example.youdrawiguess.activity.SketchpadActivity;
import com.example.youdrawiguess.adapter.FriendsAdapter;
import com.example.youdrawiguess.bean.Coordinate;
import com.example.youdrawiguess.bean.FriendBean;
import com.example.youdrawiguess.db.FriendDao;
import com.example.youdrawiguess.util.ToastUtil;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.yunba.android.manager.YunBaManager;

public class FriendsFragment extends Fragment implements OnClickListener {

	private View messageLayout;

	private RelativeLayout rl_add_friends;
	private TextView tv_title;

	private ListView lv_friends;
	private FriendsAdapter friendsAdapter;
	private List<FriendBean> list;

	private FriendDao friendDao;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		messageLayout = inflater.inflate(R.layout.tab_friends_layout,
				container, false);

		initView();
		return messageLayout;
	}

	@Override
	public void onResume() {
		super.onResume();

		friendDao = new FriendDao();
		list = friendDao.getFriendList(MyApplication.phoneNumber);

		friendsAdapter = new FriendsAdapter(list, getActivity());
		lv_friends.setAdapter(friendsAdapter);
		// ListView item点击事件
		lv_friends.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final String phoneNumber = list.get(position).getPhoneNumber();

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("选择模式：");
				builder.setPositiveButton("涂鸦模式",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								Intent intent = new Intent(getActivity(), SketchpadActivity.class);
//								Intent intent = new Intent(getActivity(), DemoActivity.class);
								intent.putExtra("phoneNumber", phoneNumber);
								startActivity(intent);
								getActivity().overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);// 实现淡入浅出的效果

								Coordinate coordinate = new Coordinate();
								coordinate.setCommand("online");
								coordinate
										.setPhoneNumber(MyApplication.phoneNumber);
								coordinate.setOnline(true);
								handlePublishAlias(coordinate, phoneNumber);
							}
						});
				builder.setNegativeButton("聊天模式",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Intent intent = new Intent(getActivity(),
										ChatActivity.class);
								intent.putExtra("friend", phoneNumber);
								startActivity(intent);
								// startActivityForResult(intent, 1);
								getActivity().overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);// 实现淡入浅出的效果
							}
						});
				builder.show();
			}
		});
	}

	private void initView() {
		rl_add_friends = (RelativeLayout) messageLayout
				.findViewById(R.id.rl_add_friends);
		lv_friends = (ListView) messageLayout.findViewById(R.id.lv_friends);
		tv_title = (TextView) messageLayout.findViewById(R.id.tv_title);

		rl_add_friends.setOnClickListener(this);

		rl_add_friends.setVisibility(View.VISIBLE);

		tv_title.setText("我的好友");
	}

	/**
	 * 发送信息
	 * */
	private void handlePublishAlias(Coordinate coordinate, String alias) {
		if (TextUtils.isEmpty(alias)) {
			return;
		}

		YunBaManager.publishToAlias(getActivity(), alias,
				new Gson().toJson(coordinate), new IMqttActionListener() {
					public void onSuccess(IMqttToken asyncActionToken) {
					}

					@Override
					public void onFailure(IMqttToken asyncActionToken,
							Throwable exception) {
					}
				});
	}

	@Override
	public void onClick(View arg0) {
		LayoutInflater inflater;
		final View textEntryView;
		final EditText et_name;
		final EditText et_number;
		final AlertDialog.Builder builder;

		switch (arg0.getId()) {
		case R.id.rl_add_friends:
			inflater = LayoutInflater.from(getActivity());
			textEntryView = inflater.inflate(R.layout.dialog_string_layout, null);
			et_name = (EditText) textEntryView.findViewById(R.id.et_name);
			et_number = (EditText) textEntryView.findViewById(R.id.et_number);
			builder = new AlertDialog.Builder(getActivity());
			builder.setCancelable(false);
			builder.setTitle("请输入对方号码：");
			builder.setView(textEntryView);
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String username = et_name.getText().toString()
									.trim();
							String phone = et_number.getText().toString()
									.trim();

							Pattern pattern = Pattern.compile("1\\d{10}");
							Matcher matcher = pattern.matcher(phone);

							if (matcher.matches()) {
								FriendBean friendBean = new FriendBean();
								friendBean.setUsername(username);
								friendBean.setPhoneNumber(phone);
								friendDao = new FriendDao();
								friendDao.saveFriend(friendBean,
										MyApplication.phoneNumber);

								list = friendDao
										.getFriendList(MyApplication.phoneNumber);
								friendsAdapter = new FriendsAdapter(list,
										getActivity());
								lv_friends.setAdapter(friendsAdapter);
								// friendsAdapter.notifyDataSetChanged();
							} else {
								ToastUtil.showToast(getActivity(), "电话号码不合法",
										Toast.LENGTH_LONG);
							}
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			builder.show();
			break;
		}
	}

}
