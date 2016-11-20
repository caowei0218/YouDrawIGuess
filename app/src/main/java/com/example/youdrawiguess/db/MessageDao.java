package com.example.youdrawiguess.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.bean.Coordinate;
import com.example.youdrawiguess.bean.Coordinate.Type;

public class MessageDao {

	private String username = MyApplication.phoneNumber;

	/**
	 * 获取本地聊天内容
	 * */
	public List<Coordinate> getMessage(String sender, String receiver) {
		update_receive_status(receiver);// 消息改为已读
		List<Coordinate> list = new ArrayList<Coordinate>();
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		Cursor cursor = null;
		String sql = "select * from (select * from chat_content where sender=? and receiver=? and myaccount=? union all select * from chat_content where sender=? and receiver=? and myaccount=?)as temp order by temp.id";
		String[] args = { sender, receiver, username, receiver, sender,
				username };
		cursor = db.rawQuery(sql, args);
		while (cursor.moveToNext()) {
			Coordinate message = new Coordinate();
			message.setSender(cursor.getString(1));
			message.setReceiver(cursor.getString(2));
			message.setMsg(cursor.getString(3));
			message.setType(username.equals(cursor.getString(1)) ? Type.OUTPUT
					: Type.INPUT);
			list.add(message);
		}
		if (db != null) {
			cursor.close();
			db.close();
		}
		return list;
	}

	/**
	 * 将消息存到数据库
	 */
	public void saveMessage(Coordinate message) {
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		ContentValues content_value = new ContentValues();
		content_value.put("sender", message.getSender());
		content_value.put("receiver", message.getReceiver());
		content_value.put("message", message.getMsg());
		if (message.getDate() == null) {
			content_value.put("save_time", new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		content_value.put("isread", "no");
		content_value.put("myaccount", username);
		db.insert("chat_content", null, content_value);
		if (db != null) {
			db.close();
		}
	}

	/**
	 * 获取最近联系人
	 */
	public List<String> get_communication_last() {
		List<String> list = new ArrayList<String>();
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		Cursor cursor = null;
		String sql = "SELECT sender FROM chat_content WHERE sender<>? and myaccount=? UNION SELECT receiver FROM chat_content WHERE receiver<>? and myaccount=?";
		String[] args = { username, username, username, username };
		cursor = db.rawQuery(sql, args);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		if (db != null) {
			cursor.close();
			db.close();
		}
		return list;
	}

	/**
	 * 获取与该用户最后一条聊天内容
	 */
	public String get_last_message(String send, String receive_id) {
		String message = "";
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		Cursor cursor = null;
		String sql = "select * from (select * from chat_content where sender=? and receiver=? union select * from chat_content where sender=? and receiver=?) as temp where myaccount=? order by temp.id";
		String[] args = { send, receive_id, receive_id, send, username };
		cursor = db.rawQuery(sql, args);
		while (cursor.moveToNext()) {
			message = cursor.getString(3);
		}
		if (db != null) {
			cursor.close();
			db.close();
		}
		return message;
	}

	/**
	 * 更改接收消息状态为已读
	 */
	public void update_receive_status(String sender) {
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("isread", "yes");
		String where_clause = "sender=? and receiver=? and myaccount=?";
		String[] where_args = { sender, username, username };
		db.update("chat_content", cv, where_clause, where_args);
		if (db != null) {
			db.close();
		}
	}

	/**
	 * 获取本地所有未读消息个数
	 * */
	public int get_unread_message_count() {
		int i = 0;
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		Cursor cursor = null;
		String sql = "select count(*) from chat_content where receiver=? and myaccount=? and isread='no'";
		String[] args = { username, username };
		cursor = db.rawQuery(sql, args);
		while (cursor.moveToNext()) {
			i = cursor.getInt(0);
		}
		if (db != null) {
			cursor.close();
			db.close();
		}
		return i;
	}

	/**
	 * 获取本地某个好友未读消息个数
	 * */
	public int get_personal_unread_message_count(String friendUsername) {
		int i = 0;
		DBHelper db_helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = db_helper.getWritableDatabase();
		Cursor cursor = null;
		String sql = "select count(*) from chat_content where sender=? and receiver=? and myaccount=? and isread='no'";
		String[] args = { friendUsername, username, username };
		cursor = db.rawQuery(sql, args);
		while (cursor.moveToNext()) {
			i = cursor.getInt(0);
		}
		if (db != null) {
			cursor.close();
			db.close();
		}
		return i;
	}
}
