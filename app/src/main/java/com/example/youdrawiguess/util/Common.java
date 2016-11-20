package com.example.youdrawiguess.util;

import java.util.List;

import com.example.youdrawiguess.bean.FriendBean;
import com.example.youdrawiguess.bean.UserBean;

public class Common {
	public static UserBean userBean;
	public static List<FriendBean> friendBeans;

	public static boolean share = false;
	public static String SESSIONID;// 定义一个静态的字段，保存sessionID

	public static String URL = "http://192.168.1.86:8080/ExcooServerSoa/";

	// //家里外网环境
	// public static final String[] IMAGES = new String[] {
	// "http://60.20.141.202/caowei/images/big_img1.jpg",
	// "http://60.20.141.202/caowei/images/big_img2.jpg",
	// "http://60.20.141.202/caowei/images/big_img3.jpg",
	// "http://60.20.141.202/caowei/images/big_img4.jpg",
	// "http://60.20.141.202/caowei/images/big_img5.jpg",
	// "http://60.20.141.202/caowei/images/big_img6.jpg",
	// "http://60.20.141.202/caowei/images/big_img7.jpg",
	// "http://60.20.141.202/caowei/images/big_img8.jpg",
	// "http://60.20.141.202/caowei/images/big_img9.jpg",
	// "http://60.20.141.202/caowei/images/img1.png",
	// "http://60.20.141.202/caowei/images/img2.png",
	// "http://60.20.141.202/caowei/images/img3.png" };

	// 公司内网环境
	public static final String IMAGES_URL = "http://10.86.255.26:8080/caowei/images/";

	public static final String[] photoName = new String[] { "big_img1.jpg",
			"big_img2.jpg", "big_img3.jpg", "big_img4.jpg", "big_img5.jpg",
			"big_img6.jpg", "big_img7.jpg", "big_img8.jpg", "big_img9.jpg",
			"img1.png", "img2.png", "img3.png" };
}
