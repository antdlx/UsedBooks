package com.ing.usedbooks.utils;

import android.content.Context;
import android.widget.Toast;

public class ShowUtils {

	/**
	 * 弹出toast,避免toast堆积
	 * @param context 上下文
	 * @param msg 弹出信息
	 * @param toast 初始化toast
	 */
	public static void showTextToast(Context context, String msg, Toast toast) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

}
