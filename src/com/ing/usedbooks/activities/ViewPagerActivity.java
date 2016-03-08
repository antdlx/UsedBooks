package com.ing.usedbooks.activities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ing.usedbooks.R;
import com.ing.usedbooks.adapters.GridViewAdapter;
import com.ing.usedbooks.adapters.MyPagerAdapter;
import com.ing.usedbooks.entity.BookInfos;
import com.ing.usedbooks.entity.Constants;
import com.ing.usedbooks.entity.MorePopWindow;
import com.ing.usedbooks.net.GetApkUrl;
import com.ing.usedbooks.net.GetVersionName;
import com.ing.usedbooks.net.Helper;
import com.ing.usedbooks.utils.NetUtils;
import com.miloisbadboy.view.PullToRefreshView;
import com.miloisbadboy.view.PullToRefreshView.OnFooterRefreshListener;
import com.miloisbadboy.view.PullToRefreshView.OnHeaderRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ViewPagerActivity extends Activity implements
		OnCheckedChangeListener, OnHeaderRefreshListener,
		OnFooterRefreshListener, OnItemClickListener {
	private RadioGroup mRadioGroup;
	private RadioButton mRadioButtonLeft;
	private RadioButton mRadioButton1;
	private RadioButton mRadioButton2;
	private RadioButton mRadioButtonRight;
	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离.
	// private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
	private ViewPager mViewPager; // 下方的可横向拖动的控件
	private ArrayList<View> mViews;// 用来存放下方滚动的layout(layout_1,layout_2)
	public static int SCREEN_WIDTH = 0; // 获得屏幕的宽度
	// private SwipeRefreshLayout swipeLayout;
	private boolean isRefresh = false;// 标记是否在刷新中
	private boolean isSuccess = true;// 标记是否更新成功
	private GridViewAdapter gridViewAdapter;
	private View v;
	private View v1;
	PullToRefreshView mPullToRefreshView;
	DisplayImageOptions options;
	String[] imageUrls;
	private ContentTask task;
	private int current_page = 1;
	private int page_count = 12;// 每次请求获取数据数量
	private boolean isPullToRefresh = false;
	ListView classifyListView;

	private Dialog d;
	private Dialog uploadDialog;
	private TextView tvBarProgress;
	private String url;
	private String last_time;//
	private ProgressBar pb_main_loading;
	public View tv_main_loading;
	private String VersionName ;
	private String CVersionName ;
	private String apkURL=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager);

		if (NetUtils.isNetworkAvailable(this) == false) {
			Toast.makeText(getApplicationContext(), "请检查网络连接",
					Toast.LENGTH_SHORT).show();

		}
		task = new ContentTask(this);

		new GetApkUrl(new GetApkUrl.SuccssCallback() {
			
			@Override
			public void onSuccess(String apkUrl) {
				apkURL = apkUrl;
			}
		}, new GetApkUrl.FailCallback() {
			
			@Override
			public void onFail() {
			}
		});
		
		// 获取本地版本信息
		try {
			VersionName = ViewPagerActivity.this.getPackageManager()
					.getPackageInfo("com.ing.usedbooks", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		GetVersionName gvn = new GetVersionName(
				new GetVersionName.SuccessCallbcak() {

					@Override
					public void onSuccess(String versionName) {
						CVersionName =versionName;
					}
				}, new GetVersionName.FailCallback() {

					@Override
					public void onFail() {
					}
				});

		ImageButton btn_viewpager_search = (ImageButton) findViewById(R.id.btn_viewpager_search);
		btn_viewpager_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ViewPagerActivity.this,
						SearchActivity.class);
				startActivity(i);
			}
		});
		final ImageButton btn_viewpager_more = (ImageButton) findViewById(R.id.btn_viewpager_more);
		btn_viewpager_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final MorePopWindow morePop = new MorePopWindow(
						ViewPagerActivity.this);
				morePop.getUsedBookTextView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(ViewPagerActivity.this,
										MyOldBookActivity.class);
								startActivity(i);
								morePop.dismiss();
							}
						});

				morePop.getUploadTextVIew().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								morePop.dismiss();
								
								Log.e("===", VersionName);
								Log.e("+++", CVersionName);

								if (!VersionName.equals(CVersionName)) {
									// 初始询问的dialog
									View dialogVIew = LayoutInflater.from(
											ViewPagerActivity.this).inflate(
											R.layout.my_dialog, null);

									Button PositiveBtn = (Button) dialogVIew
											.findViewById(R.id.PositiveBtn);
									// 确定下载的监听器
									PositiveBtn
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													// 创造一个新的下载dialog
													View uploadDialogView = LayoutInflater
															.from(ViewPagerActivity.this)
															.inflate(
																	R.layout.upoload_dialog,
																	null);
													// 下载dialog的取消按钮
													Button CancelBtn = (Button) uploadDialogView
															.findViewById(R.id.cancelBtn);
													CancelBtn
															.setOnClickListener(new OnClickListener() {

																@Override
																public void onClick(
																		View v) {
																	// try {
																	// 关闭线程，原理同interrupt
																	stop = true;
																	uploadDialog
																			.dismiss();
																	// 删除未下载完整的废文件
																	File file = new File(
																			Environment
																					.getExternalStorageDirectory(),
																			"usedbook.apk");
																	file.delete();
																	// bis.close();
																	// inputStream.close();
																	// outputStream.close();
																	// } catch
																	// (IOException
																	// e) {
																	// e.printStackTrace();
																	// }
																}
															});

													// 下载dialog的进度条
													uploadProgressBar = (ProgressBar) uploadDialogView
															.findViewById(R.id.progressBar);
													tvBarProgress = (TextView) uploadDialogView
															.findViewById(R.id.tvBarProgress);

													// 创建下载dialog
													uploadDialog = new Dialog(
															ViewPagerActivity.this,
															R.style.MyDialog);
													uploadDialog
															.setContentView(uploadDialogView);

													Window dialogWindowx = uploadDialog
															.getWindow();
													WindowManager.LayoutParams managerx = dialogWindowx
															.getAttributes();

													int WIDTH = getResources()
															.getDisplayMetrics().widthPixels;
													int HEIGHT = getResources()
															.getDisplayMetrics().heightPixels;

													managerx.width = (WIDTH / 7) * 5;
													managerx.height = HEIGHT / 4;

													dialogWindowx
															.setAttributes(managerx);

													// 隐去初始询问的dialog并显示下载dialog
													d.dismiss();
													uploadDialog.show();
													DownloadAPK dapk = new DownloadAPK();
													Thread t = new Thread(dapk);
													t.start();
												}
											});

									Button Negative = (Button) dialogVIew
											.findViewById(R.id.NegativeBtn);
									Negative.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											d.dismiss();
										}
									});

									d = new Dialog(ViewPagerActivity.this,
											R.style.MyDialog);
									d.setContentView(dialogVIew);

									Window dialogWindow = d.getWindow();
									WindowManager.LayoutParams manager = dialogWindow
											.getAttributes();

									int WIDTH = getResources()
											.getDisplayMetrics().widthPixels;
									int HEIGHT = getResources()
											.getDisplayMetrics().heightPixels;

									manager.width = (WIDTH / 7) * 5;
									manager.height = HEIGHT / 4;

									dialogWindow.setAttributes(manager);

									d.show();
								} else {
									final Dialog latest = new Dialog(
											ViewPagerActivity.this,
											R.style.MyDialog);
									View vx = LayoutInflater.from(
											ViewPagerActivity.this).inflate(
											R.layout.latest_dialog, null);
									vx.findViewById(R.id.NegativeBtn)
											.setOnClickListener(
													new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															latest.dismiss();
														}
													});
									latest.setContentView(vx);

									Window dialogWindow = latest.getWindow();
									WindowManager.LayoutParams manager = dialogWindow
											.getAttributes();

									int WIDTH = getResources()
											.getDisplayMetrics().widthPixels;
									int HEIGHT = getResources()
											.getDisplayMetrics().heightPixels;

									manager.width = (WIDTH / 7) * 5;
									manager.height = HEIGHT / 4;

									dialogWindow.setAttributes(manager);
									latest.show();
								}
							}
						});
				morePop.getSuggestionTextVIew().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(ViewPagerActivity.this,
										SuggestionActivity.class);
								startActivity(i);
								morePop.dismiss();
							}
						});

				morePop.getAboutTextVIew().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(ViewPagerActivity.this,
										AboutActivity.class);
								startActivity(i);
								morePop.dismiss();
							}
						});

				morePop.showPopupWindow(btn_viewpager_more);
			}
		});
		ImageButton btn_viewpager_send = (ImageButton) findViewById(R.id.btn_viewpager_send);
		btn_viewpager_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViewPagerActivity.this,
						PushActivity.class);
				startActivity(intent);
			}
		});

		iniController();
		iniInfomation();
		iniVariable();
		iniListener();// 更改了一下两个方法的顺序，如果先初始化监听器会造成空指针 ——Oathkeeper

		mRadioButton1.setChecked(true);
		mViewPager.setCurrentItem(1);
		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();

	}

	private void iniInfomation() {
		last_time = "2014-10-17 03:03:33";
		AddItemToContainer(current_page++, page_count);

	}

	private void iniVariable() {

		imageUrls = Constants.initImageUrls();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub) // ..the image will be
														// display during image
														// loading
				.showImageForEmptyUri(R.drawable.ic_empty)// ..the image will be
															// display if empty
															// URI (null or
															// empty string)
				.showImageOnFail(R.drawable.ic_error) // ..the image will be
														// displayed if some
														// error occurs during
														// requested
				.cacheInMemory(true) // ..Sets whether loaded image will be
										// cached in memory
				.cacheOnDisc(true) // .. Sets whether loaded image will be
									// cached on disc
				.considerExifParams(true) // Sets whether ImageLoader will
											// consider EXIF parameters of JPEG
											// image (rotate, flip)
				.bitmapConfig(Bitmap.Config.RGB_565).build(); // Builds
																// configured
																// DisplayImageOptions
																// object

		v = getLayoutInflater().inflate(R.layout.viewpager_layout1, null);
		v1 = getLayoutInflater().inflate(R.layout.viewpager_layout2, null);

		mViews = new ArrayList<View>();
		GridView gridView = (GridView) v.findViewById(R.id.gridview);
		pb_main_loading = (ProgressBar) v.findViewById(R.id.pb_main_loading);
		pb_main_loading.setVisibility(View.VISIBLE);
		tv_main_loading = (TextView) v.findViewById(R.id.tv_main_loading);
		mPullToRefreshView = (PullToRefreshView) v
				.findViewById(R.id.main_pull_refresh_view);
		gridViewAdapter = new GridViewAdapter(this, options, imageUrls);
		gridView.setAdapter(gridViewAdapter);

		classifyListView = (ListView) v1.findViewById(R.id.myList);
		initListView();

		mViews.add(getLayoutInflater()
				.inflate(R.layout.viewpager_layout0, null));
		mViews.add(v);
		mViews.add(v1);
		mViews.add(getLayoutInflater()
				.inflate(R.layout.viewpager_layout2, null));
		mViews.add(getLayoutInflater()
				.inflate(R.layout.viewpager_layout0, null));
		// GridView gv = v.findViewById(R.id.gridView);
		mViewPager.setAdapter(new MyPagerAdapter(mViews));// 设置ViewPager的适配器
	}

	private void initListView() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < bookname.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("imageid", ImageId[i]);
			listItem.put("bookname", bookname[i]);
			listItem.put("describe", describe[i]);
			listItems.add(listItem);

		}
		SimpleAdapter simpleadapter = new SimpleAdapter(this, listItems,
				R.layout.simple_item, new String[] { "imageid", "bookname",
						"describe" }, new int[] { R.id.image, R.id.bookname,
						R.id.describe });
		classifyListView.setAdapter(simpleadapter);
		classifyListView.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Intent intent;
		switch (arg2) {
		case 0:
			intent = new Intent(getApplicationContext(), SubActivity.class);
			intent.putExtra("Classify", "考研书籍");
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(getApplicationContext(), SubActivity.class);
			intent.putExtra("Classify", "计算机/软件");
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(getApplicationContext(), SubActivity.class);
			intent.putExtra("Classify", "四六级/雅思托福");
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(getApplicationContext(), SubActivity.class);
			intent.putExtra("Classify", "文学");
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(getApplicationContext(), SubActivity.class);
			intent.putExtra("Classify", "小说");
			startActivity(intent);
			break;
		case 6:
			intent = new Intent(getApplicationContext(), SubActivity.class);
			intent.putExtra("Classify", "漫画");
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(getApplicationContext(), SubActivity.class);
			intent.putExtra("Classify", "杂志期刊");
			startActivity(intent);
			break;
		case 7:
			intent = new Intent(getApplicationContext(), SubActivity.class);
			intent.putExtra("Classify", "其它");
			startActivity(intent);
			break;

		}
	}

	/**
	 * RadioGroup点击CheckedChanged监听
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// 动画集合类
		AnimationSet _AnimationSet = new AnimationSet(true);
		TranslateAnimation _TranslateAnimation;

		if (checkedId == R.id.btnHorizontalScrollView1
				|| checkedId == R.id.btnleft) {
			// _TranslateAnimation = new TranslateAnimation(
			// mCurrentCheckedRadioLeft, 0, 0f, 0f);
			// _AnimationSet.addAnimation(_TranslateAnimation);
			// If fillBefore is true, this animation will apply its
			// transformation before the start time of the animation.
			// Defaults to true if setFillEnabled(boolean) is not set to true.
			// 如果setFillBefore设为false，
			// 动画播放时会有一个跳动，可以看到View从目标位置跳到原始位置。
			// _AnimationSet.setFillBefore(true);
			// 设置吧动画执行完之后view停留在当前位置
			// _AnimationSet.setFillAfter(true);
			// 以上两个方法需要这个方法启动
			// _AnimationSet.setFillEnabled(true);
			// 动画持续时间
			// _AnimationSet.setDuration(100);

			// underbar.startAnimation(_AnimationSet);// 开始上面蓝色横条图片的动画切换

			// 跳转到viewpager的指定页面
			mViewPager.setCurrentItem(1);// 让下方ViewPager跟随上面的HorizontalScrollView切换
		} else if (checkedId == R.id.btnHorizontalScrollView2
				|| checkedId == R.id.btnright) {
			// _TranslateAnimation = new TranslateAnimation(
			// mCurrentCheckedRadioLeft, SCREEN_WIDTH / 2, 0f, 0f);
			//
			// _AnimationSet.addAnimation(_TranslateAnimation);
			// _AnimationSet.setFillBefore(true);
			// _AnimationSet.setFillAfter(true);
			// _AnimationSet.setFillEnabled(true);
			// _AnimationSet.setDuration(100);
			//
			// // mImageView.bringToFront();
			// underbar.startAnimation(_AnimationSet);

			mViewPager.setCurrentItem(2);
		}

		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();// 更新当前蓝色横条距离左边的距离

	}

	/**
	 * 获得当前被选中的RadioButton距离左侧的距离
	 */
	private float getCurrentCheckedRadioLeft() {
		if (mRadioButton1.isChecked()) {
			return 0;
		} else if (mRadioButton2.isChecked()) {
			return SCREEN_WIDTH / 2;
		}
		return 0f;
	}

	private void iniListener() {
		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager
				.setOnPageChangeListener((OnPageChangeListener) new MyPagerOnPageChangeListener());
		Log.v("该死的空指针", "1");
		if (mPullToRefreshView == null) {
			Log.v("该死的空指针", "mPullToRefreshView==null");
		}
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		// if(swipeLayout==null){
		// Log.v("该死的空指针3333","1");
		// }
		// swipeLayout.setOnRefreshListener(this);
		// //swipeLayout.setOnLoadMoreListener(this);
	}

	private void iniController() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		SCREEN_WIDTH = (int) dm.widthPixels;

		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mRadioButtonLeft = (RadioButton) findViewById(R.id.btnleft);
		mRadioButton1 = (RadioButton) findViewById(R.id.btnHorizontalScrollView1);
		mRadioButton2 = (RadioButton) findViewById(R.id.btnHorizontalScrollView2);
		mRadioButtonRight = (RadioButton) findViewById(R.id.btnright);
		mViewPager = (ViewPager) findViewById(R.id.pager);
	}

	//
	/**
	 * ViewPager的PageChangeListener(页面改变的监听器)
	 * 
	 * @author dlx 2014/10/11
	 */
	private class MyPagerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		/**
		 * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
		 */
		@Override
		public void onPageSelected(int position) {
			if (position == 0) {
				mViewPager.setCurrentItem(1);
			} else if (position == 1) {
				mRadioButton1.performClick();
				mRadioButton1.setBackgroundColor(Color.parseColor("#fd6c01"));
				mRadioButton2.setBackgroundColor(Color.parseColor("#ffffff"));
				mRadioButton1.setTextColor(Color.parseColor("#ffffff"));
				mRadioButton2.setTextColor(Color.parseColor("#fd6c01"));
				mRadioButtonLeft
						.setBackgroundResource(R.drawable.main_left_orange);
				mRadioButtonRight
						.setBackgroundResource(R.drawable.main_right_write);

			} else if (position == 2) {
				mRadioButton2.performClick();
				mRadioButton2.setBackgroundColor(Color.parseColor("#fd6c01"));
				mRadioButton1.setBackgroundColor(Color.parseColor("#ffffff"));
				mRadioButton1.setTextColor(Color.parseColor("#fd6c01"));
				mRadioButton2.setTextColor(Color.parseColor("#ffffff"));
				mRadioButtonLeft
						.setBackgroundResource(R.drawable.main_left_write);
				mRadioButtonRight
						.setBackgroundResource(R.drawable.main_right_orange);

			} else if (position == 3) {
				mViewPager.setCurrentItem(2);
			}
		}

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				System.out.println("上拉加载");
				AddItemToContainer(current_page++, page_count);
				gridViewAdapter.notifyDataSetChanged();
				if (isSuccess == false) {
					current_page--;
					Toast.makeText(getApplicationContext(), "没有更多数据了",
							Toast.LENGTH_SHORT).show();
				}
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
				isPullToRefresh = true;
				current_page = 1;
				last_time = "2014-10-17 03:03:33";
				gridViewAdapter.clean();
				pb_main_loading.setVisibility(View.VISIBLE);
				tv_main_loading.setVisibility(View.VISIBLE);
				//
				// //判断部分
				//
				// url =
				// "http://1.myoldbooks.sinaapp.com/scan.php?signal=1&pagesize="
				// + page_count + "&page=" +current_page+"&last_time="+last_time
				// ;
				// String check=null;
				// try {
				// check = Helper.getStringFromUrl(url);
				//
				//
				// } catch (ClientProtocolException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// if(check.substring(0, 1).equals("没")){
				// current_page++;
				// Toast.makeText(getApplicationContext(), "没有最新数据",
				// Toast.LENGTH_SHORT).show();
				// AddItemToContainer(current_page++, page_count);
				// }else
				// {
				// pb_main_loading.setVisibility(View.VISIBLE);
				// tv_main_loading.setVisibility(View.VISIBLE);
				// AddItemToContainer(current_page++, page_count);
				//
				// }

				AddItemToContainer(current_page++, page_count);
				if (isSuccess == false) {
					AddItemToContainer(current_page - 1, page_count);
					Toast.makeText(getApplicationContext(), "没有最新数据",
							Toast.LENGTH_SHORT).show();
				}

				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);

	}

	public class ContentTask extends
			AsyncTask<String, Integer, List<BookInfos>> {

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
				e.printStackTrace();
				isSuccess = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<BookInfos> result) {

			if (result == null || result.size() <= 0) {// 有可能因为网络或者数据源本身无数据，如果没有此处逻辑会导致下拉刷新bar不被隐藏滨且无法刷新新数据
				totalDataCount = 0;
				isSuccess = false;
			}

			totalDataCount = result.size();
			for (BookInfos info : result) {
				gridViewAdapter.setData(info);
				gridViewAdapter.notifyDataSetChanged();
				isSuccess = true;
				pb_main_loading.setVisibility(View.GONE);
				tv_main_loading.setVisibility(View.GONE);
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
					isSuccess = false;

					return bookList;
				}
			}
			Log.d("MainActiivty", "json:" + json);

			try {
				Log.v("异步", "getBlogJson1");
				if (json != null) {
					JSONArray jsonArray = new JSONArray(json);
					Log.v("异步", "getBlogJson");

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject newsInfoLeftObject = jsonArray
								.getJSONObject(i);
						BookInfos bookInfo = new BookInfos();
						bookInfo.setPrice(newsInfoLeftObject.isNull("price") ? ""
								: newsInfoLeftObject.getString("price"));
						bookInfo.setType(newsInfoLeftObject.isNull("type") ? ""
								: newsInfoLeftObject.getString("type"));
						bookInfo.setCampus(newsInfoLeftObject.isNull("campus") ? ""
								: newsInfoLeftObject.getString("campus"));
						bookInfo.setDetails(newsInfoLeftObject
								.isNull("details") ? "" : newsInfoLeftObject
								.getString("details"));
						bookInfo.setQq(newsInfoLeftObject.isNull("qq") ? ""
								: newsInfoLeftObject.getString("qq"));
						bookInfo.setPhone(newsInfoLeftObject.isNull("phone") ? ""
								: newsInfoLeftObject.getString("phone"));
						bookInfo.setTime(newsInfoLeftObject.isNull("date") ? ""
								: newsInfoLeftObject.getString("date"));
						bookInfo.setX_number(newsInfoLeftObject
								.isNull("x_number") ? "" : newsInfoLeftObject
								.getString("x_number"));
						bookList.add(bookInfo);
						Log.v("异步", "添加一本书");
					}
				} else {
					Toast.makeText(getApplicationContext(), "加载失败",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				isSuccess = false;

			}
			if (bookList != null) {
				isSuccess = true;
				if (isPullToRefresh == true) {
					gridViewAdapter.clean();
					isPullToRefresh = false;

				}
			}
			return bookList;
		}
	}

	private void AddItemToContainer(int pageindex, int pagecount) {

		// 获取时间
		if (current_page != 2) {
			SimpleDateFormat sdf1 = new SimpleDateFormat(
					"yyyy-MM-dd%20HH:mm:ss"); // 实例化模板对象

			Date now = new Date(System.currentTimeMillis());

			last_time = sdf1.format(now);
		}
		if (task.getStatus() != Status.RUNNING) {

			url = "http://1.myoldbooks.sinaapp.com/scan.php?signal=1&pagesize="
					+ pagecount + "&page=" + pageindex + "&last_time="
					+ last_time;

			Log.d("MainActivity", "current url:" + url);
			ContentTask task = new ContentTask(this);
			task.execute(url);

		}
	}

	private String[] bookname = { "考研书籍", "计算机/软件", "四六级/雅思托福", "文学", "小说",
			"杂志期刊", "漫画", "其他" };
	private String[] describe = { "考研英语，政治，等各种相关书籍",
			"编程语言与程序设计，数据库，操作系统等相关专业书籍及考试资料",
			"四六级/托福雅思模拟试题，听力口语练习，词汇专项训练等相关英语资料", "中外名著，散文，随笔，诗词歌赋等相关文学书籍",
			"玄幻，武侠，言情，推理，都市，社会，科幻等", "计算机，篮球，娱乐，商业，电影，科学，言情等",
			"二次元,动漫,游戏,耽美百合", "没有明确分类的都在此" };
	private int[] ImageId = { R.drawable.classify_kaoyan,
			R.drawable.classify_computer, R.drawable.classify_english,
			R.drawable.classify_liter, R.drawable.classify_novel,
			R.drawable.classify_comic, R.drawable.classify_mag,
			R.drawable.classify_others};
	private int progress = 0;
	private int total = 0;
	private int current = 0;
	private ProgressBar uploadProgressBar;
	private int cprogress = 0;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 9999) {
				cprogress = msg.arg1;
				// System.out.println(cprogress+"ss");
				tvBarProgress.setText(cprogress + "%");
				uploadProgressBar.setProgress(cprogress);
			}
		}
	};

	private InputStream inputStream;
	private OutputStream outputStream;
	private BufferedInputStream bis;
	private boolean stop = false;

	// 下载更新的版本
	public void downloadUpdateFile(String down_url) throws Exception {
		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		// 设置连接超时的时间
		httpURLConnection.setConnectTimeout(5000);
		// 设置时间内服务器无数据返回
		httpURLConnection.setReadTimeout(5000);
		// 获取总大小
		total = httpURLConnection.getContentLength();
		Log.e("大小", total + "");

		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		File file = new File(Environment.getExternalStorageDirectory(),
				"usedbook.apk");
		bis = new BufferedInputStream(inputStream);
		outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = bis.read(buffer)) != -1 && (!stop)) {
			current += readsize;
			Message msg = new Message();
			msg.what = 9999;
			progress = 100 * current / total;
			msg.arg1 = progress;
			handler.sendMessage(msg);
			outputStream.write(buffer, 0, readsize);
		}

		bis.close();
		inputStream.close();
		outputStream.close();
	}

	// 安装更新
	public void InstallApk(File file) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		uploadDialog.dismiss();
		startActivity(i);

	}

	class DownloadAPK implements Runnable {

		@Override
		public void run() {
		
					try {
						downloadUpdateFile(apkURL);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!stop) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"usedbook.apk");
						InstallApk(file);
					}
				}
			}
	
	public void onResume() {
		super.onResume();
	}
}