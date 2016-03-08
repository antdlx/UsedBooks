package com.ing.usedbooks.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class PicNet {

	private HttpClient httpClient;
	public PicNet(){
		
	}
	
	public void UploadPic(final String PicPath,final String key){
		
		new NetConnection(Config.SERVER_URL_TOKEN, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				String token = result;
				System.out.println(token);
				 UploadManager uploadManager = new UploadManager();
				    uploadManager.put(PicPath, key, token,
				    new UpCompletionHandler() {
				        @Override
				        public void complete(String key, ResponseInfo info, JSONObject response) {
				            Log.i("qiniu", info+"");
				        }
				    }, null);	
				
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public Bitmap DownLoadPic(String url){
		httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		
		try {
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			InputStream is  = null;
//			 InputStream in = entity.getContent();  
//             bitmap = BitmapFactory.decodeStream(in);  
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			if (response.getStatusLine().getStatusCode()==200&&entity!=null) {
				is = entity.getContent();
				byte [] buff = new byte[1024];
				int readbytes=-1;
				while((readbytes = is.read(buff))!=-1){
					baos.write(buff, 0, readbytes);
				}
			}
			if (is!=null) {
				is.close();
			}
			if (baos!=null) {
				baos.close();
			}
			byte [] imageArray = baos.toByteArray();
			return BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
