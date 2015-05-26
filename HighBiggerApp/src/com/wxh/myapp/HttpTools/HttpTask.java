package com.wxh.myapp.HttpTools;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.wxh.myapp.CommonConfigs.Config;
import com.wxh.myapp.CommonUtils.LogManager;

/**
 * 通用Http通信工具
 * 
 * @author wxh
 *
 */
@SuppressLint("NewApi")
public class HttpTask extends AsyncTask<String, Void, String> {

	private 	final int 			TIMEOUT_IN_MILLIONS = 5000;// 连接超时时间
	private 	final int 			MAX_RETRY_TIME = 3;// 最大重新请求次数
	private 	int 				mRetryCount;// 当前重新请求次数
	private 	HttpCallBack 		mHttpCallBack;// Http回调接口对象
	private 	Context 			mContext;// 上下文
	private 	boolean 			isPost;// 是否是HttpPost形式
	private 	JSONObject 			mRequestData;// 请求参数(Json格式)
	private 	static HttpTask 	instance;// HttpTask对象
	private 	ProgressDialog 		mpDialog;// 进度对话框
	
	//私有构造函数
	private HttpTask(Context context) {
		this.mContext = context;
	}

	/////////////////////////单例模式开始/////////////////////////
	private static synchronized void syncInit(Context context) {
		if (instance == null) {
			instance = new HttpTask(context);
		}
	}

	public static HttpTask getInstance(Context context) {
		if (instance == null) {
			syncInit(context);
		}
		return instance;
	}
	/////////////////////////单例模式结束/////////////////////////
	
	/**
	 * 初始化并发起HttpGet请求
	 * 
	 * @param method
	 *            请求接口
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求回调
	 */
	public void doGet(String method, String params, HttpCallBack callBack) {
		this.isPost = false;
		this.mHttpCallBack = callBack;
		this.mRetryCount = 0;

		String url = Config.ServerUrl + method + params;
		LogManager.d("HttpGet params print:"+params);
		LogManager.d("HttpGet url print:"+url);
		execute(url);
		showProgressDialog("拼命加载中...");
	}

	/**
	 * 初始化并发起HttpPost请求
	 * 
	 * @param method
	 *            请求接口
	 * @param value
	 *            请求参数
	 * @param callBack
	 *            请求回调
	 */
	public void doPost(String method, JSONObject value, HttpCallBack callBack) {
		this.isPost = true;
		this.mHttpCallBack = callBack;
		this.mRequestData = value;
		this.mRetryCount = 0;

		String url = Config.ServerUrl + method;
		LogManager.d("HttpPost params print:"+mRequestData.toString());
		LogManager.d("HttpPost url print:"+url);
		execute(url);
		showProgressDialog("拼命加载中...");
	}
	
	/**
	 * get请求
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            请求url
	 * @return
	 * @throws SSLHandshakeException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private HttpResponse get(Context context, String url) throws SSLHandshakeException, ClientProtocolException,
			IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_IN_MILLIONS);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_IN_MILLIONS);
		HttpClientParams.setRedirecting(httpParams, false);

		url = url.replaceAll(" ", "%20");
		HttpGet get = new HttpGet(url);
		get.setHeader("Content-Type", "application/x-www-form-urlencoded");
		get.setHeader("Charset", "UTF-8");

		return httpClient.execute(get);
	}

	/**
	 * post请求
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private HttpResponse post(Context context, String url, JSONObject params) throws ClientProtocolException,
			IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_IN_MILLIONS);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_IN_MILLIONS);
		HttpClientParams.setRedirecting(httpParams, false);

		url = url.replaceAll(" ", "%20");
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("Charset", "UTF-8");

		if (params != null) {
			StringEntity entity = new StringEntity(params.toString(), HTTP.UTF_8);
			post.setEntity(entity);
		}

		return httpClient.execute(post);
	}
	
	/**
	 * 获取http请求的参数串(仅在get请求方式下使用)
	 * 
	 * @param paramMap
	 *            参数链表
	 * @return
	 */
	public String getParams(LinkedHashMap<String, Object> paramMap) {
		StringBuffer params = new StringBuffer();
		params.append("?");
		for (Entry<String, Object> entry : paramMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			String temps = key + "=" + value;
			params.append(temps);
			params.append("&");
		}

		int location = params.lastIndexOf("&");
		params = params.deleteCharAt(location);

		return params.toString();
	}
	
	private void showProgressDialog(String msg) {
		mpDialog = new ProgressDialog(mContext);
		mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
		mpDialog.setMessage(msg);
		mpDialog.setIndeterminate(false);// 设置进度条是否为不明确
		mpDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
		mpDialog.show();
	}

	private void closeProgressDialog() {
		if (mpDialog != null) {
			mpDialog.dismiss();
			mpDialog = null;
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		String response = null;
		while(response == null && mRetryCount<MAX_RETRY_TIME){
			if(isCancelled()){
				return null;
			}
			String url = params[0];
			try {
				HttpResponse httpResp = isPost?post(mContext, url, mRequestData):get(mContext, url);
				if (httpResp != null && !isCancelled()) {
					if(httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
						LogManager.d("httpConnection success !");
						response = EntityUtils.toString(httpResp.getEntity(), HTTP.UTF_8);
					}else {
						LogManager.d("httpConnection error2 !");
					}
				} else {
					LogManager.d("httpConnection error1 !");
				}
			} catch (SSLHandshakeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mRetryCount++;
		}
		
		LogManager.d("http response print:"+response);
		return response;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (mHttpCallBack != null) {
			mHttpCallBack.onCannel();
			mHttpCallBack = null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		closeProgressDialog();
		if (mHttpCallBack != null && !isCancelled()) {
			if(result != null){
				mHttpCallBack.onResponse(result);
			} else {
				// 弹框提示网络故障
			}
			
			mHttpCallBack = null;
		}
	}

}
