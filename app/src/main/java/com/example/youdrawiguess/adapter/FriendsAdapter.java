package com.example.youdrawiguess.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.youdrawiguess.R;
import com.example.youdrawiguess.bean.FriendBean;

@SuppressLint({ "ViewHolder", "InflateParams" })
public class FriendsAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private Context context;
	private List<FriendBean> friendBeans;

	public FriendsAdapter(List<FriendBean> friendBeans, Context context) {
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.friendBeans = friendBeans;
	}

	public int getCount() {
		return friendBeans.size();
	}

	public FriendBean getItem(int position) {
		return friendBeans.get(position);
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.show_name.setText(friendBeans.get(position).getUsername());
		holder.show_number.setText(friendBeans.get(position).getPhoneNumber());

		return convertView;
	}

	class ViewHolder {
		TextView show_name;
		TextView show_number;
	}

}
