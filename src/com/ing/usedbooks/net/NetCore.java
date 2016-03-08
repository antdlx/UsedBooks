package com.ing.usedbooks.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

public class NetCore {
	
	public static String  GetResultFromNEet(String url) throws Exception{
		
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		String result = "";
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		if(response.getStatusLine().getStatusCode()==200){
			BufferedReader reader = new  BufferedReader(new InputStreamReader(response.getEntity().getContent()));	
			StringBuilder sBuilder = new StringBuilder();	
			while(reader.readLine()!=null){
				sBuilder.append(reader.readLine());
				System.out.println("#######"+sBuilder.toString());
			}
			reader.close();// �ر�������
			result = sBuilder.toString();
		}
		
		return result; 		
	}
	
	public static String getResultFromNet(String url) throws Exception{
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		System.out.println(url+"------$");
		String result = "";
		HttpGet get = new HttpGet(url);
		HttpResponse response = null;		
		try {
			response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == 200) {
				BufferedReader bin = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				System.out.println(url+"------");
				
				String s;
				StringBuilder sBuilder = new StringBuilder();
        		while (((s = bin.readLine()) != null)) {
        			sBuilder.append(s);
        			System.out.println(url+"------#");
        		}
        		bin.close();// �ر�������
		
        		result = sBuilder.toString();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(url+"------");
		return result;		
	}

}
