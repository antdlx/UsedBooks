package com.ing.usedbooks.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ing.usedbooks.entity.BookInfos;

public class MyBooks {

	public MyBooks(String x_number,String sale,final SuccessCallback successCallback,final FailCallback failCallback){
		
		new NetConnection(Config.SERVER_URL_MYBOOKS, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				try {
					JSONArray Jarray = new JSONArray(result);
					List<BookInfos> bookList = new ArrayList<BookInfos>();
					JSONObject jb = new JSONObject();
					for (int i = 0; i < Jarray.length(); i++) {
						jb = Jarray.getJSONObject(i);
						BookInfos bookInfo = new BookInfos();
						bookInfo.setPrice(jb.isNull("price") ? ""
										: jb.getString("price"));
						bookInfo
								.setType(jb.isNull("type") ? ""
										: jb.getString("type"));
						bookInfo
								.setCampus(jb.isNull("campus") ? ""
								: jb.getString("campus"));
						bookInfo
								.setDetails(jb.isNull("details") ? ""
								: jb.getString("details"));
						bookInfo
								.setQq(jb.isNull("qq") ? ""
											: jb.getString("qq"));
						bookInfo
								.setPhone(jb.isNull("phone") ? ""
								: jb.getString("phone"));
						bookInfo.setTime(jb.isNull("date") ? ""
								: jb.getString("date"));
						bookList.add(bookInfo);
					}
					successCallback.onSuccess(bookList);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				
			}
		}, Config.KEY_IMEI,x_number,Config.KEY_SALE,sale);
	}
	
	
	public static interface SuccessCallback{
		void onSuccess(List<BookInfos> bookList);
	}
	public static interface FailCallback{
		void onFail();
	}
}
