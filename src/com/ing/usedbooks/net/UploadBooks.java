package com.ing.usedbooks.net;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UploadBooks {

	public UploadBooks(String picture, String price, String type, String campus,String phone, String qq,String details,String name,final SuccessCallback successCallback,final FailCallback failCallback){
		//初始化时间格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String c_date = sdf.format(date);
		System.out.println(c_date);
		//获得上传的key
		String key = Config.KEY_IMEI+c_date;
		//上传图片
		PicNet pn = new PicNet();
		pn.UploadPic(picture, key);
		//下载时的url
		String pic_url = Config.SERVER_URL_GETPICS+key;
		
		new NetConnection(Config.SERVER_URL_UPLOAD, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				
			}
		}, Config.KEY_PICTURE,pic_url,
		Config.KEY_PRICE,price,
		Config.KEY_TYPE,type,
		Config.KEY_CAMPUS,campus,
		Config.KEY_PHONE,phone,
		Config.KEY_QQ,qq,
		Config.KEY_DETAILS,details,
		Config.KEY_IMEI,Config.IMEI,
		Config.KEY_NAME,name);
	}
	
	
	
	public static interface SuccessCallback{
		void OnSuccess();
	}
	
	public static interface FailCallback{
		void OnFail();
	}
}
 