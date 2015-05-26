package com.wxh.myapp.HttpTools;

/**
 * http请求回调接口
 * 
 * @author wxh
 * 
 */
public interface HttpCallBack {

	public void onResponse(String result);

	public void onCannel();
}
