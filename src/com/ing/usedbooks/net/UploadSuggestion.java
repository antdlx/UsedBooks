package com.ing.usedbooks.net;

public class UploadSuggestion {

	public UploadSuggestion(String suggest,String phone,String qq,final SuccessCallback successCallback,final FailCallback failCallback){
		
		new NetConnection(Config.SERVER_URL_SUGGEST, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				System.out.println("++++++++++++++++"+Config.IMEI);
				successCallback.OnSuccess();
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				failCallback.OnFail();
			}
		}, Config.KEY_SUGGEST,suggest,Config.KEY_PHONE,phone,Config.KEY_QQ,qq);
	}
	
	
	public static interface SuccessCallback{
		void OnSuccess();
	}
	
	public static interface FailCallback{
		void OnFail();
	}
}
