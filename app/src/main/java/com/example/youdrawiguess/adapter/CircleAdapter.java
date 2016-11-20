package com.example.youdrawiguess.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youdrawiguess.R;

@SuppressLint({ "ViewHolder", "InflateParams" })
public class CircleAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;

	public CircleAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_excoo_item, null);// 这个过程相当耗时间
			holder = new ViewHolder();
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	class ViewHolder {
		RelativeLayout ll_user;
		TextView tv_save;
	}

}
