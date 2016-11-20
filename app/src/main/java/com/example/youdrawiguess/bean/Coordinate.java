package com.example.youdrawiguess.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 推送内容实体
 * */
@SuppressWarnings("serial")
public class Coordinate implements Serializable {
	private String phoneNumber;

	private String command;// 命令：paint、wake_friend、online、paint_color、clear_sketchpad、isclear、message

	private boolean online;// 是否在线
	private boolean clear;// 清屏同意指令 true同意 false不同意
	private int paint_color;
	private int canvas_color;

	private long order;// 顺序
	private int window_width;// 屏幕宽度
	private int window_height;// 屏幕高度
	private float x;
	private float y;
	private float stopx;
	private float stopY;
	
	private String msg;// 消息内容
	private Type type;// 消息类型 枚举
	private String sender;// 发送人
	private String receiver;// 收信人
	private Date date;// 发送日期

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public enum Type {
		INPUT, OUTPUT
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCommand() {
		return command;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isClear() {
		return clear;
	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}

	public int getPaint_color() {
		return paint_color;
	}

	public void setPaint_color(int paint_color) {
		this.paint_color = paint_color;
	}

	public int getCanvas_color() {
		return canvas_color;
	}

	public void setCanvas_color(int canvas_color) {
		this.canvas_color = canvas_color;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public int getWindow_width() {
		return window_width;
	}

	public void setWindow_width(int window_width) {
		this.window_width = window_width;
	}

	public int getWindow_height() {
		return window_height;
	}

	public void setWindow_height(int window_height) {
		this.window_height = window_height;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getStopx() {
		return stopx;
	}

	public void setStopx(float stopx) {
		this.stopx = stopx;
	}

	public float getStopY() {
		return stopY;
	}

	public void setStopY(float stopY) {
		this.stopY = stopY;
	}

}
