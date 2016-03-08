package com.ing.usedbooks.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.ing.usedbooks.entity.BookInfos;



public class GetBrowse {

	public GetBrowse(String pagesize,String page, final SuccessCallback successCallback,final FailCallback failCallback){
		new NetConnection(Config.SERVER_URL_BROWSE, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				try {
						List<BookInfos> SearchResult = new ArrayList<BookInfos>();
						
						JSONArray JArray = new JSONArray(result);
						JSONObject jb;
						for (int i = 0; i < JArray.length(); i++) {
							jb = JArray.getJSONObject(i);
							//获取图片并储存在bitmap中
//							String pic_url = jb.getString(Config.KEY_PICTURE);
//							PicNet pn = new PicNet();
//							Bitmap bm = pn.DownLoadPic(pic_url);
							
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
							
							bookInfo.setPicture(jb.isNull(Config.KEY_PICTURE) ? ""
									: jb.getString(Config.KEY_PICTURE));
							
							bookInfo.setScan_number(jb.isNull(Config.KEY_SCANNUM) ? ""
									: jb.getString(Config.KEY_SCANNUM));
							
							bookInfo.setName(jb.isNull(Config.KEY_NAME) ? ""
									: jb.getString(Config.KEY_NAME));
							
							bookInfo.setTime(jb.isNull(Config.KEY_ID) ? ""
									: jb.getString(Config.KEY_ID));
							
							SearchResult.add(bookInfo);
							
						}

						successCallback.onSuccess(SearchResult);
				} catch (JSONException e) {
					e.printStackTrace();
					if (failCallback!=null) {
						failCallback.onFail();
					}
				}
				
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				
			}
		}, Config.KEY_SIGNAL,"1",
				Config.KEY_PAGESIZE,pagesize,
				Config.KEY_PAGE,page
				);
		
	}
	
	
	public static interface SuccessCallback{
		void onSuccess(List<BookInfos> list);
	}
	public static interface FailCallback{
		void onFail();
	}
}
