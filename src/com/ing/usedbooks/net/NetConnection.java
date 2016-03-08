package com.ing.usedbooks.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class NetConnection {

	HttpClient httpclient;
	
	public NetConnection(final String url,final SuccessCallback successCallback,final FailCallback failCallbcak,final String ...kvs){
		
		new AsyncTask<Void, Integer, String>() {

			//doInBackground是AsyncTask的一个方法，必须实现
			@Override
			protected String doInBackground(Void... arg0) {
				
				httpclient = new DefaultHttpClient();
				
				StringBuffer params = new StringBuffer();		
				for (int i = 0; i < kvs.length; i+=2) {
					params.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
				}
					Log.i("UURRLL",url+params.toString() );
					HttpGet get = new HttpGet(url+params.toString());
				try {
					HttpResponse response = httpclient.execute(get);
					HttpEntity entity = response.getEntity();
					if (entity!=null) {
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),Config.CHARSET));
						String line = null;
						StringBuffer result = new StringBuffer();
						while((line=br.readLine())!=null){
							result.append(line);
						}
						System.out.println("Result:"+result);
						return result.toString();
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}
		
			
			//onPostExecute接收的是doInBackground的返回值
		@Override
		protected void onPostExecute(String result) {
			if (result!=null) {
				if (successCallback!=null) {
					successCallback.onSuccess(result);
				}else {
					failCallbcak.onFail();
				}
			}
			super.onPostExecute(result);
		}
		}.execute();
	}
	
	
	//两个回调方法
	public static interface SuccessCallback{
		 void onSuccess(String result);
	}
	public static interface FailCallback{
		void onFail();
	}
}
