package com.ing.usedbooks.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ing.usedbooks.R;
import com.ing.usedbooks.adapters.GridViewAdapter;
import com.ing.usedbooks.entity.BookInfos;
import com.ing.usedbooks.entity.Constants;
import com.ing.usedbooks.net.Helper;
import com.miloisbadboy.view.PullToRefreshView;
import com.miloisbadboy.view.PullToRefreshView.OnFooterRefreshListener;
import com.miloisbadboy.view.PullToRefreshView.OnHeaderRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity extends Activity implements OnHeaderRefreshListener, OnFooterRefreshListener {
	private ContentTask task;
	private int current_page=1;
	private int page_count=12;
	private String[] imageUrls;
	private DisplayImageOptions options;
	private PullToRefreshView mPullToRefreshView;
	private GridViewAdapter gridViewAdapter;
	private String searchInfo;
	private String classifyInfo;
	private String url;
	private GridView gridView;
	private TextView tv_title;
	private String titleString;
	private static int  isSuccess1=1;
	private View view;
	private ImageView noItem;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_details);
		

		task = new ContentTask(this);
		Intent intent=this.getIntent();
		searchInfo=intent.getStringExtra("Search");
		classifyInfo=intent.getStringExtra("Classify");
		//Log.v("该死的空指针",classifyInfo);
		if(searchInfo!=null){
			
			url =  "http://1.myoldbooks.sinaapp.com/scan.php?signal=0&pagesize="+page_count+"&page="+current_page+"&details="+searchInfo; 
			//Log.v("该死的空指针",searchInfo);
		}else{
			
			url =  "http://1.myoldbooks.sinaapp.com/scan.php?signal=2&pagesize="+page_count+"&page="+current_page+"&type="+classifyInfo;
			//Log.v("该死的空指针",classifyInfo);
		}
		noItem=(ImageView) findViewById(R.id.search_noitem);
		view=getLayoutInflater().inflate(R.layout.activity_details, null);
		isSuccess1=0;
		iniInfomation();
		view.invalidate();
		iniVariable();
		iniListener();

	}
	private void iniInfomation() {
		Log.v("sub","presetVisible");
		
		AddItemToContainer(current_page++, page_count);
		Log.v("sub",""+isSuccess1);
		
			
	

	}


	private void iniVariable() {
		
		imageUrls = Constants.initImageUrls();

		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub) //..the image will be display during image loading
			.showImageForEmptyUri(R.drawable.ic_empty)//..the image will be display if empty URI (null or empty string) 
			.showImageOnFail(R.drawable.ic_error) //..the image will be displayed if some error occurs during requested 
			.cacheInMemory(true) //..Sets whether loaded image will be cached in memory 
			.cacheOnDisc(true) // .. Sets whether loaded image will be cached on disc 
			.considerExifParams(true) // Sets whether ImageLoader will consider EXIF parameters of JPEG image (rotate, flip)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build(); //Builds configured DisplayImageOptions object 
		tv_title=(TextView) findViewById(R.id.tv_details_title);
		if(searchInfo!=null){
			titleString="搜索结果";
		}else{
			titleString=classifyInfo;
			
		}
		tv_title.setText(titleString);
		Button bt_details_back = (Button) findViewById(R.id.bt_details_back);
		bt_details_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
		gridView = (GridView)findViewById(R.id.gridview);
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
		gridViewAdapter = new GridViewAdapter(this,options,imageUrls);
	

	gridView.setAdapter(gridViewAdapter);
		Log.v("该死的空指针", "5");
		
	}

	

	private void iniListener() {
	
		mPullToRefreshView.setOnHeaderRefreshListener( this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
//		if(swipeLayout==null){
//			Log.v("该死的空指针3333","1");
//		}
//		swipeLayout.setOnRefreshListener(this);
//		//swipeLayout.setOnLoadMoreListener(this);
	}



	

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				System.out.println("上拉加载");
				if(searchInfo!=null){
					
					url =  "http://1.myoldbooks.sinaapp.com/scan.php?signal=0&pagesize="+page_count+"&page="+current_page+"&details="+searchInfo; 
					//Log.v("该死的空指针",searchInfo);
				}else{
					
					url =  "http://1.myoldbooks.sinaapp.com/scan.php?signal=2&pagesize="+page_count+"&page="+current_page+"&type="+classifyInfo;
					//Log.v("该死的空指针",classifyInfo);
				}
				AddItemToContainer(current_page++, page_count);
				gridViewAdapter.notifyDataSetChanged();
				
				
				
				mPullToRefreshView.onFooterRefreshComplete();
				
				
			}
		}, 1000);

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				System.out.println("下拉更新");
				gridViewAdapter.clean();
				current_page = 1;
				if(searchInfo!=null){
					
					url =  "http://1.myoldbooks.sinaapp.com/scan.php?signal=0&pagesize="+page_count+"&page="+current_page+"&details="+searchInfo; 
					//Log.v("该死的空指针",searchInfo);
				}else{
					
					url =  "http://1.myoldbooks.sinaapp.com/scan.php?signal=2&pagesize="+page_count+"&page="+current_page+"&type="+classifyInfo;
					//Log.v("该死的空指针",classifyInfo);
				}
				AddItemToContainer(current_page++, page_count);
				if(isSuccess1==0)
				{
					if(searchInfo!=null){
						
						url =  "http://1.myoldbooks.sinaapp.com/scan.php?signal=0&pagesize="+page_count+"&page="+current_page+"&details="+searchInfo; 
						//Log.v("该死的空指针",searchInfo);
					}else{
						
						url =  "http://1.myoldbooks.sinaapp.com/scan.php?signal=2&pagesize="+page_count+"&page="+current_page+"&type="+classifyInfo;
						//Log.v("该死的空指针",classifyInfo);
					}
					AddItemToContainer(current_page++, page_count);
					Toast.makeText(getApplicationContext(), "没有最新数据",
							Toast.LENGTH_SHORT).show();
				}
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);
		

	}
	public class ContentTask extends AsyncTask<String, Integer, List<BookInfos>> {

		private Context mContext;
		private int totalDataCount;
		
		public ContentTask(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected List<BookInfos> doInBackground(String... params) {
				try {
					
					return parseNewsJSON(params[0]);
				} catch (IOException e) {
					isSuccess1=0;
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<BookInfos> result) {
				
				if(result == null || result.size() <= 0){//有可能因为网络或者数据源本身无数据，如果没有此处逻辑会导致下拉刷新bar不被隐藏滨且无法刷新新数据
					totalDataCount = 0;
			}
				
				totalDataCount = result.size();
				for (BookInfos info : result) {
					
					isSuccess1=1;
					gridViewAdapter.setData(info);
					gridViewAdapter.notifyDataSetChanged();
					Log.v("异步","1"+isSuccess1);
				}
				noItem.setVisibility(View.GONE);
				if(isSuccess1==0&&current_page==2){
					noItem.setVisibility(View.VISIBLE);
				}else if(isSuccess1==0){
					current_page--;
					Toast.makeText(getApplicationContext(), "没有更多数据了",
							Toast.LENGTH_SHORT).show();
					
				}
			}
			 
			@Override
			protected void onPreExecute() {
			}

			public List<BookInfos> parseNewsJSON(String url) throws IOException {
				List<BookInfos> bookList = new ArrayList<BookInfos>();
				String json = "";
				
				if (Helper.checkConnection(mContext)) {
					try {
						json = Helper.getStringFromUrl(url);
						
						

					} catch (IOException e) {
						Log.e("IOException is : ", e.toString());
						e.printStackTrace();
						Log.v("异步", "未获得Json");
						isSuccess1=0;
						return bookList;
					}
				}
				Log.d("MainActiivty", "json:" + json);

				try {
					Log.v("异步", "getBlogJson1");
					
					if (json!= null) {
						
						JSONArray jsonArray = new JSONArray(json);
						Log.v("异步", "getBlogJson");
						
						

						
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject newsInfoLeftObject = jsonArray
									.getJSONObject(i);
							BookInfos bookInfo = new BookInfos();
							bookInfo.setPrice(newsInfoLeftObject.isNull("price") ? ""
											: newsInfoLeftObject.getString("price"));
							bookInfo
									.setType(newsInfoLeftObject.isNull("type") ? ""
											: newsInfoLeftObject.getString("type"));
							bookInfo
									.setCampus(newsInfoLeftObject.isNull("campus") ? ""
									: newsInfoLeftObject.getString("campus"));
							bookInfo
									.setDetails(newsInfoLeftObject.isNull("details") ? ""
									: newsInfoLeftObject.getString("details"));
							bookInfo
									.setQq(newsInfoLeftObject.isNull("qq") ? ""
												: newsInfoLeftObject.getString("qq"));
							bookInfo
									.setPhone(newsInfoLeftObject.isNull("phone") ? ""
									: newsInfoLeftObject.getString("phone"));
							bookInfo.setTime(newsInfoLeftObject.isNull("date") ? ""
									: newsInfoLeftObject.getString("date"));
							bookInfo.setX_number(newsInfoLeftObject.isNull("x_number") ? ""
									: newsInfoLeftObject.getString("x_number"));
							bookList.add(bookInfo);
							Log.v("异步","添加一本书");
						}
					} 
					
				} catch (JSONException e) {
					e.printStackTrace();
					isSuccess1=0;

					
					
				}
				Log.v("异步","添加一本书列表");
				
				return bookList;
			}
		}	
	private void AddItemToContainer(int current_page, int pagecount) {
		if (task.getStatus() != Status.RUNNING) {
			
				
				Log.d("MainActivity", "current url:" + url);
				ContentTask task = new ContentTask(this);
				task.execute(url);
				Log.v("sub","isSuccess:  "+isSuccess1);
				Log.v("sub","execute");
			
		}
	}

	



}
