package com.cn.red.point.mobile;

import com.cn.red.point.common.Md5;
import com.cn.red.point.net.Net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SMSTool {

	public static String sendMsg(String mobile) {
		String account = "RedP";
		String password = "redp0425";
		String content = "您的的订单被消费，请及时查看.";
		String url = null;
		try {
			url = "http://www.js10088.com:18001/?Action=SendSms&UserName=" + account + "&Password=" + Md5.Secret(password) + "&Mobile=" + mobile + "&Message=" + URLEncoder.encode(content,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Net.doGet(url);
	}
}
