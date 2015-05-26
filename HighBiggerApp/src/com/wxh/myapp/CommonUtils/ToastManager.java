package com.wxh.myapp.CommonUtils;

import android.content.Context;
import android.widget.Toast;

import com.wxh.myapp.CommonConfigs.Config;

/**
 * Toast统一管理类
 * 
 * @author wxh
 */
public class ToastManager {

	private ToastManager() {
		// 私有构造函数，防止实例化
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isShow = true;
	private static Toast mToast = null;

	public static void showToast(Context context, String msg, int duration) {
		if (isShow) {
			if (mToast == null) {
				mToast = Toast.makeText(context, msg, duration);
				mToast.setGravity(Config.TOAST_GRAVITY_DEFULT, 0, 100);
			} else {
				mToast.setGravity(Config.TOAST_GRAVITY_DEFULT, 0, 100);
				mToast.setText(msg);
			}
			mToast.show();
		}
	}
}