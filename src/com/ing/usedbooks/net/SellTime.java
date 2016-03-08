package com.ing.usedbooks.net;


public class SellTime {

	public SellTime(String sell_time,String id,final SuccessCallback successCallback,final FailCallback failCallback){
		
		new NetConnection(Config.SERVER_URL_SELLTIME, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				
			}
		}, Config.KEY_SELLTIME,sell_time,
				Config.KEY_BID,id);
	}
	
	
	public static interface SuccessCallback{
		void onSuccess();
	}
	public static interface FailCallback{
		void onFail();
	}
}
