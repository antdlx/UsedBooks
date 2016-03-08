package com.ing.usedbooks.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ing.usedbooks.R;
import com.ing.usedbooks.entity.Constants;
import com.ing.usedbooks.net.NetCore;
import com.ing.usedbooks.utils.ShowUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
 
public class SaledBookFragment extends Fragment {
	private Context context;
	DisplayImageOptions options;
	String[] imageUrls;
	private ProgressBar pb_mybook_loading;
	private TextView tv_my_book_loading;
	private GridView grid_mybook;
	private ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private String[] picture;
	private String[] price;
	private String[] type;
//	private String[] id=new String[10];
	private String[] campus;
	private String[] date;
	private String[] name;
	private String[] details;
	private String[] x_number;
	private String[] sale;
	private String[] saled_time;
	private String[] scan_number;
//	private String[] user_id=new String[10];
	private int count;
	private View v;
	private Toast toast;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 0:
				if(msg.obj.toString().trim().equalsIgnoreCase("[]")){
					ShowUtils.showTextToast(getActivity(), "你还没有已销售的旧书哦！", toast);
//					Toast.makeText(getActivity(), "你还没有已销售的旧书哦！", Toast.LENGTH_SHORT).show();
					pb_mybook_loading.setVisibility(View.GONE);
					tv_my_book_loading.setText("你还没有已销售的旧书哦！");
					return;
				}  
				if(msg.obj.toString().trim().equalsIgnoreCase("")){
					ShowUtils.showTextToast(getActivity(), "网络未连接或服务器异常！", toast);
//					Toast.makeText(getActivity(), "网络未连接或服务器异常！", Toast.LENGTH_SHORT).show();
					pb_mybook_loading.setVisibility(View.GONE);
					tv_my_book_loading.setText("网络未连接或服务器异常！");
					return;
				}
				pb_mybook_loading.setVisibility(View.GONE);
				tv_my_book_loading.setVisibility(View.GONE);
				
				String result =  msg.obj.toString();
				System.out.println("------"+result);
				JSONArray result1;
				try {
					
					result1 = new JSONArray(result);
					count = result1.length();
					picture =new String[count];
					price =new String[count];
					type =new String[count];
					campus =new String[count];
					name =new String[count];
					details =new String[count];
					sale =new String[count];
					saled_time =new String[count];
					date =new String[count];
					scan_number =new String[count];
					x_number = new String[count];
										
					mData.clear();
		    		for (int i = 0; i < result1.length(); i++) {
		    			Map<String, Object> item = new HashMap<String, Object>();
		    			picture[i]=result1.getJSONObject(i).get("picture").toString();
		    			price[i]=result1.getJSONObject(i).get("price").toString();
		    			type[i]=result1.getJSONObject(i).get("type").toString();
		    			campus[i]=result1.getJSONObject(i).get("campus").toString();
		    			date[i]=result1.getJSONObject(i).get("date").toString();
		    			x_number[i]=result1.getJSONObject(i).get("x_number").toString();
		    			sale[i]=result1.getJSONObject(i).get("sale").toString();
		    			saled_time[i]=result1.getJSONObject(i).get("saled_time").toString();
		    			scan_number[i]=result1.getJSONObject(i).get("scan_number").toString();
		    			name[i]=result1.getJSONObject(i).get("name").toString();
		    			details[i]=result1.getJSONObject(i).get("details").toString();
		    			System.out.println("---"+price[i]+"---"+type[i]+"---"+campus[i]+"---"+date[i]+"---"+sale[i]);
		    			item.put("picture", picture[i]);    			
		    			item.put("price", price[i]);
		    			item.put("type", type[i]);
		    			item.put("campus", campus[i]);
		    			item.put("date", date[i]);
		    			item.put("x_number", x_number[i]);
		    			item.put("sale", sale[i]);
		    			item.put("saled_time", saled_time[i]);
		    			item.put("scan_number", scan_number[i]);
		    			item.put("name", name[i]);
		    			item.put("details", details[i]);
		    			mData.add(item);   
        		}
				} catch (JSONException e) {
					// TODO Auto-generated catch block					
				} 
				grid_mybook.setAdapter(new GridMybookAdapter());
				break;
			case 1:
				break;
			}
		}		
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		inflater = getActivity().getLayoutInflater();
		v = inflater.inflate(R.layout.frag_salingbook,
				(ViewGroup) getActivity().findViewById(R.id.viewpager_myoldbook), false);	
		pb_mybook_loading = (ProgressBar) v.findViewById(R.id.pb_mybook_loading);
		tv_my_book_loading = (TextView) v.findViewById(R.id.tv_my_book_loading);
		grid_mybook = (GridView) v.findViewById(R.id.grid_mybook);
		return v;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		context = getActivity();
		
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
		
		
		
		
		Thread t = new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = "http://1.myoldbooks.sinaapp.com/myoldbooks.php?sale=1&x_number="+getIMEI();
				System.out.println("------"+url);
				try {
					String result = NetCore.getResultFromNet(url);
					Message msg = new Message();
					msg.what=0;
					msg.obj=result;
					System.out.println("$$$$$$$$"+result);
					handler.sendMessage(msg);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	private class GridMybookAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return count;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item__mybook_grid, null);
				holder.iv_item_mybook_grid = (ImageView) convertView.findViewById(R.id.iv_item_mybook_grid);
				holder.tv_item_mybook_price = (TextView) convertView.findViewById(R.id.tv_item_mybook_price);
				holder.tv_item_mybook_campus = (TextView) convertView.findViewById(R.id.tv_item_mybook_campus);
//				holder.tv_item_mybook_fenge = (TextView) convertView.findViewById(R.id.tv_item_mybook_fenge);
				holder.tv_item_mybook_date = (TextView) convertView.findViewById(R.id.tv_item_mybook_date);
				holder.tv_item_mybook_content = (TextView) convertView.findViewById(R.id.tv_item_mybook_content);
//				holder.bt_item_mybook_contact = (Button) convertView.findViewById(R.id.bt_item_mybook_contact);
//				holder.iv_item_mybook_classify = (ImageView) convertView.findViewById(R.id.iv_item_mybook_classify);
				holder.tv_item_mybook_classify = (TextView) convertView.findViewById(R.id.tv_item_mybook_classify);
				holder.tv_item_mybook_scannumber = (TextView) convertView.findViewById(R.id.tv_item_mybook_scannumber);
				holder.bt_item_mybook_contact = (Button) convertView.findViewById(R.id.bt_item_mybook_contact);
				holder.pb_item_mybook = (ProgressBar) convertView.findViewById(R.id.pb_item_mybook);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			/**
			 * return view???????? ???????????? ????????
			 * 
			 * uri:???uri, imageView ??????,  options ?????????,
			 * ImageLoadingListener ???????????, ImageLoadingProgressListener ?????????????
			 * progressListener??????????
			 */
			
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
			imageLoader.displayImage("http://oldbooks.qiniudn.com/"+x_number[position]+date[position], holder.iv_item_mybook_grid, options, new SimpleImageLoadingListener() {
										 @Override
										 public void onLoadingStarted(String imageUri, View view) {//???????
											 holder.pb_item_mybook.setProgress(0);
											 holder.pb_item_mybook.setVisibility(View.VISIBLE);
										 }

										 @Override
										 public void onLoadingFailed(String imageUri, View view,
												 FailReason failReason) {//???????
											 holder.pb_item_mybook.setVisibility(View.GONE);
										 }

										 @Override
										 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {//???????
											 holder.pb_item_mybook.setVisibility(View.GONE);
										 }
									 }, new ImageLoadingProgressListener() {
										 @Override
										 public void onProgressUpdate(String imageUri, View view, int current,
												 int total) {
											 holder.pb_item_mybook.setProgress(Math.round(100.0f * current / total));
										 }
									 }
			);
			holder.tv_item_mybook_price.setText(price[position]+"元");
			holder.tv_item_mybook_campus.setText(campus[position]);
			holder.tv_item_mybook_date.setText(date[position]);
			holder.tv_item_mybook_content.setText(name[position]+":"+details[position]);
			holder.tv_item_mybook_classify.setText(type[position]);
			holder.tv_item_mybook_scannumber.setText("浏览次数："+scan_number[position]);
			holder.bt_item_mybook_contact.setText("已销售");
			holder.bt_item_mybook_contact.setBackgroundResource(R.drawable.item_myoldbook_saled);
			
			return convertView;
		}
		
		
		
	}
	
	public String getIMEI(){
		TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = tm.getDeviceId();		
		return IMEI;		
	}

	private class ViewHolder{
		public TextView tv_item_mybook_price;
		public TextView tv_item_mybook_campus;
		public TextView tv_item_mybook_fenge;
		public TextView tv_item_mybook_date;
		public TextView tv_item_mybook_content;
		public TextView tv_item_mybook_classify;
		public TextView tv_item_mybook_scannumber;
		public ImageView iv_item_mybook_grid;
		public ImageView iv_item_mybook_classify;
		public Button bt_item_mybook_contact;
		public ProgressBar pb_item_mybook;
		
	}

}
