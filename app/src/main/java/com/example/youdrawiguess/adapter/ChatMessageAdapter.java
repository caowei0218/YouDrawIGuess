package com.example.youdrawiguess.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.bean.Coordinate;
import com.example.youdrawiguess.bean.Coordinate.Type;
import com.example.youdrawiguess.util.BitmapCache;
import com.example.youdrawiguess.util.Common;
import com.example.youdrawiguess.view.CustomNetworkImageView;

@SuppressLint("InflateParams")
public class ChatMessageAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<Coordinate> messages;

	private ImageLoader imageLoader;
	private RequestQueue mQueue;

	public ChatMessageAdapter(Context context, List<Coordinate> messages) {
		mInflater = LayoutInflater.from(context);
		this.messages = messages;
		this.context = context;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 接受到消息为1，发送消息为0
	 */
	@Override
	public int getItemViewType(int position) {
		Coordinate msg = messages.get(position);
		return msg.getType() == Type.INPUT ? 1 : 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Coordinate chatMessage = messages.get(position);

		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();

			mQueue = Volley.newRequestQueue(context);
			imageLoader = new ImageLoader(mQueue, new BitmapCache(
					MyApplication.phoneNumber));

			if (chatMessage.getType() == Type.INPUT) {
				convertView = mInflater.inflate(R.layout.main_chat_from_msg,
						parent, false);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.chat_from_content);
				viewHolder.iv_avatar = (CustomNetworkImageView) convertView
						.findViewById(R.id.chat_from_icon);
				convertView.setTag(viewHolder);
			} else {
				convertView = mInflater.inflate(R.layout.main_chat_send_msg,
						null);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.chat_send_content);
				viewHolder.iv_avatar = (CustomNetworkImageView) convertView
						.findViewById(R.id.chat_send_icon);
				convertView.setTag(viewHolder);
			}

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.content.setText(chatMessage.getMsg());
		viewHolder.iv_avatar.setImageUrl(Common.IMAGES_URL
				+ MyApplication.phoneNumber, imageLoader);

		return convertView;
	}

	private class ViewHolder {
		CustomNetworkImageView iv_avatar;
		TextView content;
	}

}
