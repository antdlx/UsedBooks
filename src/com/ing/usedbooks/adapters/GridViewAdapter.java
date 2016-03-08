package com.ing.usedbooks.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ing.usedbooks.R;
import com.ing.usedbooks.activities.PushActivity;
import com.ing.usedbooks.activities.ViewPagerActivity;
import com.ing.usedbooks.entity.BookInfos;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class GridViewAdapter extends BaseAdapter {
	DisplayImageOptions options;
	String[] imageUrls;
	
	//设计上使用动态数组储存数据
	public ArrayList<BookInfos> data = new ArrayList<BookInfos>();
	
	private Context context=null;

	private LayoutInflater mInflater;

	//面向对象的优化
	public GridViewAdapter(Context context,DisplayImageOptions options,String[] imageUrls){
		this.context = context;
		this.options = options;
		this.imageUrls = imageUrls;;
	}
	//面向对象的优化，注意：构造方法不能有return，故自定义此方法
	public Context getContext(){
		return context;
	}
	
	public void clean(){
		data.clear();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public BookInfos getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	//自定义方法，用于动态设置数据使用
	public void setData(int picture, String price, String phone, String qq,
			String details, String type, String campus, String time){
		BookInfos cell =new BookInfos();
		cell.setPicture(picture+"");
		cell.setPrice(price);
		cell.setPhone(phone);
		cell.setQq(qq);
		cell.setDetails(details);
		cell.setType(type);
		cell.setCampus(campus);
		cell.setTime(time);
		data.add(cell);
	}
	
	public void setData(BookInfos cell){
		data.add(cell);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BookInfos currentInfos = getItem(position);
		
		
		final ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.infos_list, null);
			holder.tvDescription = (TextView) convertView.findViewById(R.id.book_description);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.book_price);
			holder.tvTime = (TextView) convertView.findViewById(R.id.book_time);
			holder.tvArea = (TextView) convertView.findViewById(R.id.book_area);
			holder.tvType = (TextView) convertView.findViewById(R.id.book_type);
			holder.ivImage = (ImageView) convertView.findViewById(R.id.book_image);
			holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress);
			holder.ib_contact = (ImageButton) convertView.findViewById(R.id.button_contect);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag(); 
		}
		holder.tvDescription.setText(currentInfos.details);
		holder.tvPrice.setText(currentInfos.price+"元");
		holder.tvTime.setText(currentInfos.time);
		holder.tvArea.setText(currentInfos.campus);
		holder.tvType.setText(currentInfos.type);
		holder.ib_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				View sureDialogView = LayoutInflater.from(context)
						.inflate(R.layout.contact_dialog, null);
				Button bt_contact_dialog_tel = (Button) sureDialogView
						.findViewById(R.id.bt_contact_dialog_tel);
				
				final Button bt_contact_dialog_mes = (Button) sureDialogView
						.findViewById(R.id.bt_contact_dialog_mes);
				final Button bt_contact_dialog_qq = (Button) sureDialogView
						.findViewById(R.id.bt_contact_dialog_qq);
				LinearLayout l_contact_dialog_tel = (LinearLayout) sureDialogView.findViewById(R.id.l_contact_dialog_tel);
				LinearLayout l_contact_dialog_qq = (LinearLayout) sureDialogView.findViewById(R.id.l_contact_dialog_qq);
//				if(currentInfos.qq.trim().equalsIgnoreCase("")){
//					bt_contact_dialog_qq.setVisibility(View.INVISIBLE);
//					l_contact_dialog_qq.setVisibility(View.INVISIBLE);
//				}
//				if(currentInfos.phone.trim().equalsIgnoreCase("")){
//					bt_contact_dialog_tel.setVisibility(View.INVISIBLE);
//					l_contact_dialog_tel.setVisibility(View.INVISIBLE);
//					l_contact_dialog_qq.setVisibility(View.INVISIBLE);
//					bt_contact_dialog_mes.setVisibility(View.INVISIBLE);
//				}
				final Dialog alertDialog = new Dialog(context,
						R.style.MyDialog);
				alertDialog.setContentView(sureDialogView);
				alertDialog.show();
				bt_contact_dialog_tel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri
								.parse("tel:"+currentInfos.phone));
						context.startActivity(intent);
						alertDialog.dismiss();
					}
				});
				bt_contact_dialog_mes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 发短信
						Uri smsToUri = Uri.parse("smsto:"+currentInfos.phone);
						Intent intent = new Intent(Intent.ACTION_SENDTO,
								smsToUri);
						intent.putExtra("sms_body", false);

						context.startActivity(intent);
						alertDialog.dismiss();
					}
				});
				bt_contact_dialog_qq.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						copy(currentInfos.qq, context);
						alertDialog.dismiss();
					}
				});
			}
		});
		
	    LayoutParams params = holder.ivImage.getLayoutParams();  
	    
	     //
	    params.width =(int) (ViewPagerActivity.SCREEN_WIDTH/2.4);
	    holder.ivImage.setLayoutParams(params);
		
		/**
		 * return view后，会增加 “展示的图片任务” 到线程池里
		 * 
		 * uri:目标uri, imageView 目标组件,  options 显示图片条件,
		 * ImageLoadingListener 图片加载监听器, ImageLoadingProgressListener 图片加载进度监听器
		 * progressListener：进度监听器
		 */
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		imageLoader.displayImage("http://oldbooks.qiniudn.com/"+currentInfos.x_number+currentInfos.time, holder.ivImage, options, new SimpleImageLoadingListener() {
									 @Override
									 public void onLoadingStarted(String imageUri, View view) {//开始加载
										 holder.progressBar.setProgress(0);
										 holder.progressBar.setVisibility(View.VISIBLE);
									 }

									 @Override
									 public void onLoadingFailed(String imageUri, View view,
											 FailReason failReason) {//加载失败
										 holder.progressBar.setVisibility(View.GONE);
									 }

									 @Override
									 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {//加载完成
										 holder.progressBar.setVisibility(View.GONE);
									 }
								 }, new ImageLoadingProgressListener() {
									 @Override
									 public void onProgressUpdate(String imageUri, View view, int current,
											 int total) {
										 holder.progressBar.setProgress(Math.round(100.0f * current / total));
									 }
								 }
		);
		
		return convertView;
	}
	String items[] = new String[] { "打电话", "发短信", "复制QQ号" };
	class ViewHolder {     
        public ProgressBar progressBar;
        public TextView tvDescription;
        public TextView tvPrice;
        public TextView tvTime;
        public TextView tvArea;
        public TextView tvType;
        public ImageView ivImage;
        public ImageButton ib_contact;
    } 	
	public void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

}
