package com.wxh.myapp;

import org.json.JSONException;
import org.json.JSONObject;

import com.wxh.myapp.HttpTools.HttpCallBack;
import com.wxh.myapp.HttpTools.HttpTask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView tv = null;
	Button btn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.tv_test);
		btn = (Button) findViewById(R.id.btn_test);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject data = new JSONObject();
				try {
					data.put("name", "wxh");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				HttpTask.getInstance(MainActivity.this).doPost("", data, new HttpCallBack() {

					@Override
					public void onResponse(String result) {
						try {
							if (result != null) {
//								JSONObject jo = new JSONObject(result);
								tv.setText(result);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onCannel() {

					}
				});
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
