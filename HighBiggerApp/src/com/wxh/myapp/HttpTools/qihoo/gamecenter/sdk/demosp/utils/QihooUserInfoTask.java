package com.wxh.myapp.HttpTools.qihoo.gamecenter.sdk.demosp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/***
 * 此类使用Access Token，请求您的应用服务器，获取QihooUserInfo。
 * （注：应用服务器由360SDK使用方自行搭建，用于和360服务器进行安全交互，具体协议请查看文档中，服务器端接口）。
 */
public class QihooUserInfoTask {

	private static final String TAG = "QihooUserInfoTask";

	/**
	 * 应用服务器为应用客户端提供的接口Url，用于通过AccessToken获取QihooUserInfo
	 * (这是DEMO专用的URL，请使用方自己搭建自己的应用服务器，此服务器只认DEMO的AppKey。)
	 */
	private static final String DEMO_APP_SERVER_URL_GET_USER = "http://sdk.g.uc.cn/cp/account.verifySession";
	// private static final String DEMO_APP_SERVER_URL_GET_USER =
	// "http://sdk.test4.g.uc.cn/cp/account.verifySession";

	private SdkHttpTask sSdkHttpTask;

	public static QihooUserInfoTask newInstance() {
		return new QihooUserInfoTask();
	}

	public void doRequest(Context context, JSONObject keyValueObject, final QihooUserInfoListener listener) {

		// DEMO使用的应用服务器url仅限DEMO示范使用，禁止正式上线游戏把DEMO应用服务器当做正式应用服务器使用，请使用方自己搭建自己的应用服务器。
		String url = DEMO_APP_SERVER_URL_GET_USER;

		// 如果存在，取消上一次请求
		if (sSdkHttpTask != null) {
			sSdkHttpTask.cancel(true);
		}

		// 新请求
		sSdkHttpTask = new SdkHttpTask(context);
		sSdkHttpTask.doPost(new SdkHttpListener() {

			@Override
			public void onResponse(String response) {
				Log.d("wxh", "onResponse=" + response);
				QihooUserInfo userInfo = QihooUserInfo.parseJson(response);
				listener.onGotUserInfo(userInfo);
				sSdkHttpTask = null;
			}

			@Override
			public void onCancelled() {
				listener.onGotUserInfo(null);
				sSdkHttpTask = null;
			}
		}, keyValueObject, url);
		// sSdkHttpTask.doGet(new SdkHttpListener() {
		//
		// @Override
		// public void onResponse(String response) {
		// Log.d("Test", "onResponse=" + response);
		// QihooUserInfo userInfo = QihooUserInfo.parseJson(response);
		// listener.onGotUserInfo(userInfo);
		// sSdkHttpTask = null;
		// }
		//
		// @Override
		// public void onCancelled() {
		// listener.onGotUserInfo(null);
		// sSdkHttpTask = null;
		// }
		//
		// }, url);

		// Log.d(TAG, "url=" + url);
	}

	public boolean doCancel() {
		return (sSdkHttpTask != null) ? sSdkHttpTask.cancel(true) : false;
	}

	public static String encryption(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

	public static String getExOrderID(String roleid, int giftgem, int buygem, int wid) {
		return String.format("%s-%d-%d-%d-%d", roleid, giftgem, buygem, System.currentTimeMillis(), wid);
	}

}
