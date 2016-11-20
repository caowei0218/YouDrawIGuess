package com.example.youdrawiguess.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DataBase_NAME = "YOUDRAWIGUESS.DB";
	private static final int DataBase_Verision = 1;
	private static final String FRIENDS = "create table friends(id integer primary key autoincrement,username,nickname,email,description,address,city,gender,phoneNumber,alias,myaccount,photoName)";// 用来存放好友列表
	private static final String MESSAGE_TABLE = "create table chat_content(id integer primary key autoincrement,sender,receiver,message,save_time,isread,myaccount)";// 用来存放聊天内容

	public DBHelper(Context context) {
		super(context, DataBase_NAME, null, DataBase_Verision);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 第一次安装该APP 创建表
		db.execSQL(MESSAGE_TABLE);
		db.execSQL(FRIENDS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 升级数据库
	}

}