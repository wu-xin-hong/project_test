package com.wxh.myapp.HttpTools.qihoo.gamecenter.sdk.demosp.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * QihooUserInfo，是应用服务器请求360服务器得到的360用户信息数据。
 */
public class QihooUserInfo {

	private String id;
	private int code;
	private String msg;
	private String accountId;
	private String creator;
	private String nickName;

	public static QihooUserInfo parseJson(String jsonString) {
		QihooUserInfo userInfo = null;
		if (!TextUtils.isEmpty(jsonString)) {
			try {
				// JSONObject jsonObj =
				// String status = jsonObj.getString("status");
				JSONObject dataJsonObj = new JSONObject(jsonString);
				// if (status != null && status.equals("ok")) {
				// 必返回项
				String id = dataJsonObj.getString("id");
				String state = dataJsonObj.getString("state");
				String data = dataJsonObj.getString("data");
				JSONObject jo = new JSONObject(state);
				JSONObject jo2 = new JSONObject(data);
				int code = jo.getInt("code");
				String msg = jo.getString("msg");
				String accountId = jo2.getString("accountId");
				String creator = jo2.getString("creator");
				String nickName = jo2.getString("nickName");

				userInfo = new QihooUserInfo();
				userInfo.setId(id);
				userInfo.setCode(code);
				userInfo.setMsg(msg);
				userInfo.setAccountId(accountId);
				userInfo.setCreator(creator);
				userInfo.setNickName(nickName);
				// }
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return userInfo;
	}

	public static QihooUserInfo parseUserInfo(JSONObject joInfo) {
		QihooUserInfo userInfo = null;
		if (joInfo != null) {
			try {
				// 必须返回
				String name = joInfo.getString("name");
				String avatar = joInfo.getString("avatar");

				userInfo = new QihooUserInfo();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return userInfo;
	}

	public boolean isValid() {
		return !TextUtils.isEmpty(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
