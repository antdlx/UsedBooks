package com.ing.usedbooks.net;

public class GetVersionName {

	public GetVersionName(final SuccessCallbcak successCallbcak,final FailCallback failCallback){
		
		new NetConnection(Config.SERVER_URL_GETVERSIONNAME, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				successCallbcak.onSuccess(result);
			}
		}, new NetConnection.FailCallback() {
			@Override
			public void onFail() {
				
			}
		});
	}
	
	public static interface SuccessCallbcak{
		void onSuccess(String versionName);
	}
	public static interface FailCallback{
		void onFail();
	}
}
