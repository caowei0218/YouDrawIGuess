package com.example.youdrawiguess.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.db.MessageDao;

@SuppressLint({ "ViewHolder", "InflateParams" })
public class RecentlyAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private Context context;
	private List<String> friends;

	private MessageDao messageDao = new MessageDao();;

	public RecentlyAdapter(List<String> friends, Context context) {
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.friends = friends;
	}

	public int getCount() {
		return friends.size();
	}

	public String getItem(int position) {
		return friends.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.friends_layout_item,
					null);// 这个过程相当耗时间
			holder = new ViewHolder();
			holder.show_name = (TextView) convertView
					.findViewById(R.id.show_name);
			holder.show_number = (TextView) convertView
					.findViewById(R.id.show_number);
			holder.tv_unread_messages = (TextView) convertView
					.findViewById(R.id.tv_unread_messages);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.show_name.setText(friends.get(position));
		holder.show_number.setText(getLastMessage(friends.get(position)));

		int unread = getUnreadMessages(friends.get(position));
		if (unread == 0) {
			holder.tv_unread_messages.setVisibility(View.INVISIBLE);
		} else {
			holder.tv_unread_messages.setVisibility(View.VISIBLE);
			holder.tv_unread_messages.setText("" + unread);
		}

		return convertView;
	}

	class ViewHolder {
		TextView show_name;
		TextView show_number;
		TextView tv_unread_messages;
	}

	/**
	 * 查询最后一条消息
	 * */
	private String getLastMessage(String userName) {
		String last_message = messageDao.get_last_message(
				MyApplication.phoneNumber, userName);
		return last_message;
	}

	/**
	 * 查询未读消息个数
	 * */
	private int getUnreadMessages(String userName) {
		int unread = messageDao.get_personal_unread_message_count(userName);
		return unread;
	}

}
