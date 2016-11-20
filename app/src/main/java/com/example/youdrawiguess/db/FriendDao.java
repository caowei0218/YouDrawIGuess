package com.example.youdrawiguess.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.bean.FriendBean;

public class FriendDao {

	/**
	 * 保存好友列表到本地
	 */
	public void saveFriend(FriendBean friendBean, String myAccount) {
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		ContentValues content_value = new ContentValues();
		content_value.put("username", friendBean.getUsername());
		content_value.put("nickname", friendBean.getNickname());
		content_value.put("email", friendBean.getEmail());
		content_value.put("description", friendBean.getDescription());
		content_value.put("address", friendBean.getAddress());
		content_value.put("city", friendBean.getCity());
		content_value.put("gender", friendBean.getGender());
		content_value.put("phoneNumber", friendBean.getPhoneNumber());
		content_value.put("alias", friendBean.getAlias());
		content_value.put("myaccount", myAccount);

		db.insert("friends", null, content_value);
		if (db != null) {
			db.close();
		}
	}

	/**
	 * 获取本地好友列表
	 */
	public List<FriendBean> getFriendList(String myaccount) {
		List<FriendBean> list = new ArrayList<FriendBean>();
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		Cursor cursor = null;
		String sql = "select * from friends where myaccount=?";
		String[] args = { myaccount };
		cursor = db.rawQuery(sql, args);
		while (cursor.moveToNext()) {
			FriendBean friendBean = new FriendBean();
			friendBean.setUsername(cursor.getString(1));
			friendBean.setNickname(cursor.getString(2));
			friendBean.setEmail(cursor.getString(3));
			friendBean.setDescription(cursor.getString(4));
			friendBean.setAddress(cursor.getString(5));
			friendBean.setCity(cursor.getString(6));
			friendBean.setGender(cursor.getString(7));
			friendBean.setPhoneNumber(cursor.getString(8));
			friendBean.setAlias(cursor.getString(9));
			friendBean.setPhotoName(cursor.getString(11));
			list.add(friendBean);
		}
		if (db != null) {
			cursor.close();
			db.close();
		}
		return list;
	}

	/**
	 * 保存好友列表到本地
	 */
	public void save_friend_list(List<FriendBean> list, String myAccount) {
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		db.execSQL("delete from friends");
		for (int i = 0; i < list.size(); i++) {
			ContentValues content_value = new ContentValues();
			content_value.put("username", list.get(i).getUsername());
			content_value.put("nickname", list.get(i).getNickname());
			content_value.put("email", list.get(i).getEmail());
			content_value.put("description", list.get(i).getDescription());
			content_value.put("address", list.get(i).getAddress());
			content_value.put("city", list.get(i).getCity());
			content_value.put("gender", list.get(i).getGender());
			content_value.put("phoneNumber", list.get(i).getPhoneNumber());
			content_value.put("alias", list.get(i).getAlias());
			content_value.put("myaccount", myAccount);

			// content_value.put("photoName", Common.photoName[i]);

			db.insert("friends", null, content_value);
		}
		if (db != null) {
			db.close();
		}
	}
}
