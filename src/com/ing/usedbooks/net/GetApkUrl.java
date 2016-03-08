package com.ing.usedbooks.net;

public class GetApkUrl {

	public GetApkUrl(final SuccssCallback succssCallback, final FailCallback failCallback){
		new NetConnection(Config.SERVER_URL_APK, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
			succssCallback.onSuccess(result);
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				
			}
		} );
		
	}

	public static interface SuccssCallback{
		void onSuccess(String apkUrl);
	}
	public static interface FailCallback{
		void onFail();
	}
}
