package com.ing.usedbooks.activities;

//1.图片上传失败

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ing.usedbooks.R;
import com.ing.usedbooks.entity.ChooseCampusPopwindow;
import com.ing.usedbooks.entity.ChooseClassificationPopupwindow;
import com.ing.usedbooks.net.NetCore;
import com.ing.usedbooks.utils.NetUtils;
import com.ing.usedbooks.utils.ShowUtils;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
//import com.qiniu.auth.JSONObjectRet;
//import com.qiniu.io.IO;
//import com.qiniu.io.PutExtra;
import com.qiniu.android.http.ResponseInfo;

@SuppressLint("NewApi")
public class PushActivity extends Activity {

	@SuppressLint("NewApi")
	Button bt_school, bt_category, bt_push, bt_push_back;
	ImageButton bt_camera;
	int schoolnumber, categorynumber;
	private int CAMERA = 0;
	private final String IMAGE_TYPE = "image/*";
	private int IMAGE_CODE = 1;
	String usage[] = new String[] { "拍一张", "选择一张照片" };
	EditText edt_phonenumber, edt_QQnumber, edt_price, edt_name;
	String str_phone, str_QQ;
	private String filename;
	private String time;
	private String IMEI;
	private String str_price;
	private String str_name;
	private ImageView iv_activity_push_photoline, iv_push_back;
	// private String str_describe;
	private Toast toast = null;
	private boolean hasPicture = false;
	private boolean hasContent = false;
	private String campus = "";
	private String type = "";
	private String details = "";
	private EditText describe;
	private String date = "";
	private String bad_times = "";
	private ProgressDialog progress;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				String result0 = msg.obj.toString();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result0);
					bad_times = jsonObject.getString("bad_times");
					date = jsonObject.getString("time");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (result0.equalsIgnoreCase("-1")
						|| bad_times.equalsIgnoreCase("0")
						|| isToDate(date, bad_times)) {
					pushInit();
				} else {
					progress.dismiss();
					textUserInit(date, bad_times);
				}
				break;
			case 1:
				String token = msg.obj.toString();
				System.out.println("=========" + token);
				UploadPic(filename, IMEI + time, token);
				break;
			case 2:
				String result = msg.obj.toString();
				if (result.equals("1")) {
					ShowUtils
							.showTextToast(PushActivity.this, "信息发送成功！", toast);
					progress.dismiss();
					finish();
				} else if (result.equals("0")) {
					ShowUtils
							.showTextToast(PushActivity.this, "信息发送失败！", toast);
					progress.dismiss();
				}
				bt_push.setEnabled(true);
				break;

			}
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push);

		iv_push_back = (ImageView) findViewById(R.id.iv_push_back);
		bt_push_back = (Button) findViewById(R.id.bt_push_back);
		bt_push_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				str_phone = edt_phonenumber.getText().toString().trim();
				str_QQ = edt_QQnumber.getText().toString().trim();
				str_price = edt_price.getText().toString().trim();
				str_name = edt_name.getText().toString().trim();
				campus = bt_school.getText().toString().trim();
				type = bt_category.getText().toString().trim();
				details = describe.getText().toString().trim();

				if ((!hasPicture) && str_phone.isEmpty() && details.isEmpty()
						&& str_QQ.isEmpty() && str_price.isEmpty()
						&& str_name.isEmpty()
						&& campus.equalsIgnoreCase("选择校区")
						&& type.equalsIgnoreCase("选择分类")) {
					finish();
				} else {
					View sureDialogView = LayoutInflater.from(PushActivity.this)
							.inflate(R.layout.sure_dialog, null);
					TextView tv_content = (TextView) sureDialogView.findViewById(R.id.tv_sure_dialog_content);
					tv_content.setText("您有已填写内容，要舍弃它们么。");
					Button bt_sure = (Button) sureDialogView
							.findViewById(R.id.bt_sure_dialog_ok);
					bt_sure.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							finish();
						}
					});
					final Button bt_cancle = (Button) sureDialogView
							.findViewById(R.id.bt_sure_dialog_cancle);
					final Dialog alertDialog = new Dialog(PushActivity.this,
							R.style.MyDialog);
					alertDialog.setContentView(sureDialogView);
					alertDialog.show();
					bt_cancle.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							alertDialog.dismiss();
						}
					});
				}
			}
		});
		bt_push_back.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					iv_push_back
							.setImageResource(R.drawable.activity_push_back_pressed);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					iv_push_back
							.setImageResource(R.drawable.activity_myoldbook_back);
				}
				return false;
			}
		});
		// 照相的功能在这里实现
		bt_camera = (ImageButton) findViewById(R.id.camera1);
		// 选择拍照还是照片
		bt_camera.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PushActivity.this).setTitle("选择").setItems(usage,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									// 拍照
									Intent takePhoto = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									startActivityForResult(takePhoto, CAMERA);
								} else if (which == 1) {
									// 选择一张照片

									Intent getAlbum = new Intent(
											Intent.ACTION_GET_CONTENT);
									getAlbum.setType(IMAGE_TYPE);
									startActivityForResult(getAlbum, IMAGE_CODE);

								}
								dialog.dismiss();
							}
						});
				builder.show();
			}
		});

		// 关于书的描述
		describe = (EditText) findViewById(R.id.Describe1);
		describe.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		// describe.setSingleLine(false);
		describe.setHorizontallyScrolling(false);
		// 手机号码获取

		// QQ号码获取

		// 校区信息按钮弹出式菜单
		bt_school = (Button) findViewById(R.id.school);
		bt_school.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
				final ChooseCampusPopwindow campusPopwindow = new ChooseCampusPopwindow(
						PushActivity.this);
				campusPopwindow.getTvElse().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// more to do here
								bt_school.setText("其它校区");
								campusPopwindow.dismiss();
							}
						});
				campusPopwindow.getTvSoftWare().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// more to do here

								bt_school.setText("软件园校区");
								campusPopwindow.dismiss();
							}
						});
				campusPopwindow.showPopupWindow(bt_school);

			}
		});
		// 书目信息弹出式菜单
		bt_category = (Button) findViewById(R.id.category1);
		bt_category.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final ChooseClassificationPopupwindow chooseClassificationPopupwindow = new ChooseClassificationPopupwindow(
						PushActivity.this);
				chooseClassificationPopupwindow.getBtnKaoyan()
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						bt_category.setText("考研书籍");
						chooseClassificationPopupwindow.dismiss();
					}
				});
				chooseClassificationPopupwindow.getBtnComputer()
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								bt_category.setText("计算机/软件");
								chooseClassificationPopupwindow.dismiss();
							}
						});
				chooseClassificationPopupwindow.getBtnCET().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								bt_category.setText("四六级/雅思托福");
								chooseClassificationPopupwindow.dismiss();
							}
						});
				chooseClassificationPopupwindow.getBtnNovel()
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								bt_category.setText("小说");
								chooseClassificationPopupwindow.dismiss();
							}
						});
				chooseClassificationPopupwindow.getBtnLiterature()
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								bt_category.setText("文学");
								chooseClassificationPopupwindow.dismiss();
							}
						});
				chooseClassificationPopupwindow.getBtnCartoon()
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								bt_category.setText("漫画");
								chooseClassificationPopupwindow.dismiss();
							}
						});
				chooseClassificationPopupwindow.getBtnMagazine()
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								bt_category.setText("杂志期刊");
								chooseClassificationPopupwindow.dismiss();
							}
						});
				chooseClassificationPopupwindow.getBtnElse()
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								bt_category.setText("其它");
								chooseClassificationPopupwindow.dismiss();
							}
						});

				chooseClassificationPopupwindow.showPopupWindow(bt_category);
			}

		});
		// 发布时判断电话号码和QQ号码是否填写

		edt_QQnumber = (EditText) findViewById(R.id.qqnumber1);
		bt_push = (Button) findViewById(R.id.push);
		edt_phonenumber = (EditText) findViewById(R.id.tel1);
		edt_price = (EditText) findViewById(R.id.price1);
		edt_name = (EditText) findViewById(R.id.bookname);
		iv_activity_push_photoline = (ImageView) findViewById(R.id.iv_activity_push_photoline);

		bt_push.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				bt_push = (Button) arg0;

				str_phone = edt_phonenumber.getText().toString().trim();
				str_QQ = edt_QQnumber.getText().toString().trim();
				str_price = edt_price.getText().toString().trim();
				str_name = edt_name.getText().toString().trim();
				campus = bt_school.getText().toString().trim();
				type = bt_category.getText().toString().trim();
				details = describe.getText().toString().trim();

				if (!hasPicture) {
					ShowUtils.showTextToast(PushActivity.this,
							"请拍摄或选择一张图片以供他人参考购买。", toast);
					return;
				}
				if (str_name.equalsIgnoreCase("")) {
					ShowUtils.showTextToast(PushActivity.this,
							"请填写书名，否则他人将无法搜索到该旧书。", toast);
					return;
				}
				if (str_price.equalsIgnoreCase("")) {
					ShowUtils.showTextToast(PushActivity.this,
							"请填写价格以供他人比较购买。", toast);
					return;
				}
				if (campus.equalsIgnoreCase("选择校区")) {
					ShowUtils.showTextToast(PushActivity.this,
							"请选择校区以供他人比较购买。", toast);
					return;
				}
				if (type.equalsIgnoreCase("选择分类")) {
					ShowUtils.showTextToast(PushActivity.this,
							"请选择分类，否则他人将无法从分类中找到该旧书。", toast);
					return;
				}
				
				String reg_price = "^[0-9]*$";
				if (!str_price.matches(reg_price)) {
					ShowUtils.showTextToast(PushActivity.this,
							"请填写正确的价格，以供他人参考购买。", toast);
					return;
				}
				if (str_phone.equalsIgnoreCase("")) {
					ShowUtils.showTextToast(PushActivity.this,
							"请填写手机号码，否则他人将无法联系到您。", toast);
					return;
				}
				if (str_QQ.equalsIgnoreCase("")) {
					ShowUtils.showTextToast(PushActivity.this,
							"请填写QQ号，以便他人联系到您。", toast);
					return;
				}
				if (!str_phone.isEmpty()) {
					String reg_phone = "^\\d{11}$";
					if (!str_phone.matches(reg_phone)) {
						ShowUtils.showTextToast(PushActivity.this,
								"请填写正确的手机号码，否则他人将无法联系到您。", toast);
						return;
					}
				}
				if (!str_QQ.isEmpty()) {
					String reg_qq = "^\\d{5,10}$";
					if (!str_QQ.matches(reg_qq)) {
						ShowUtils.showTextToast(PushActivity.this,
								"请填写正确的QQ号，否则他人将无法联系到您。", toast);
						return;
					}
				}

				if (!NetUtils.isNetworkAvailable(PushActivity.this)) {
					ShowUtils.showTextToast(PushActivity.this,
							"发送失败，请连接网络后重试。", toast);
					return;
				}

				bt_push.setEnabled(false);
				progress = new ProgressDialog(PushActivity.this);
				progress.setMessage("正在发布...");
				progress.setCanceledOnTouchOutside(false);
				progress.setCancelable(false);
				progress.show();
				time = getTime();
				IMEI = getIMEI();

				Thread t0 = new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message msg = new Message();
						String url = "http://1.myoldbooks.sinaapp.com/bad_record.php?x_number="
								+IMEI;
						try {
							String result = NetCore.getResultFromNet(url);
							msg.what = 0;
							msg.obj = result;
							handler.sendMessage(msg);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				t0.start();

			}
		});

		// Button textbtn_horizontalScrolview = (Button)
		// findViewById(R.id.textbtn_horizontalscrolview);
	}

	public void textUserInit(String dateTime, String bad_times) {
		// TODO Auto-generated method stub

		int days = 0;
		String userType = "";
		if (bad_times.equalsIgnoreCase("1")) {
			days = 1;
			userType = "轻度恶意";
		} else if (bad_times.equalsIgnoreCase("2")) {
			days = 3;
			userType = "中度恶意";
		}
		if (bad_times.equalsIgnoreCase("3")) {
			days = 7;
			userType = "重度恶意";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		Date myDate = null;
		try {
			myDate = sdf.parse(dateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar date = Calendar.getInstance();
		date.setTime(myDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) + days);
		Date endDate = date.getTime();
		String end = sdf1.format(endDate);

		boolean flag = myDate.before(endDate);// 在当前日期之后

		View sureDialogView = LayoutInflater.from(PushActivity.this)
				.inflate(R.layout.warning_dialog, null);
		TextView tv_content = (TextView) sureDialogView.findViewById(R.id.tv_warning_dialog_content);
		tv_content.setText("由于您发布了恶意或无关的信息，您已被我们标记为" + userType + "用户，并在" + days
				+ "日内不得发布信息。您将在" + end + "后恢复正常使用。");
		Button bt_sure = (Button) sureDialogView
				.findViewById(R.id.bt_warning_dialog_ok);
		
		final Dialog alertDialog = new Dialog(PushActivity.this,
				R.style.MyDialog);
		alertDialog.setContentView(sureDialogView);
		alertDialog.show();
		bt_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				alertDialog.dismiss();
				finish();
			}
		});
	}

	public void pushInit() {
		// TODO Auto-generated method stub

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				String url = "http://1.myoldbooks.sinaapp.com/upload_token.php";
				try {
					String token = NetCore.getResultFromNet(url);
					msg.what = 1;
					msg.obj = token;
					handler.sendMessage(msg);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t1.start();
		time = getTime();
		IMEI = getIMEI();
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				String picUrl = "http://oldbooks.qiniudn.com/" + IMEI + time;
				String url = "http://1.myoldbooks.sinaapp.com/upload_book.php?picture="
						+ picUrl
						+ "&price="
						+ str_price
						+ "&type="
						+ type
						+ "&campus="
						+ campus
						+ "&phone="
						+ str_phone
						+ "&qq="
						+ str_QQ
						+ "&details="
						+ details
						+ "&x_number="
						+ IMEI
						+ "&name="
						+ str_name
						+ "&time="
						+ time
						+ "&sale="
						+ "0";
				url = url.replace(" ", "%20");
				try {
					String result = NetCore.getResultFromNet(url);
					msg.what = 2;
					msg.obj = result;
					handler.sendMessage(msg);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t2.start();
	}

	public void UploadPic(String PicPath, String key, String token) {
		UploadManager uploadManager = new UploadManager();
		uploadManager.put(PicPath, key, token, new UpCompletionHandler() {
			@Override
			public void complete(String key, ResponseInfo info,
					JSONObject response) {
				Log.i("qiniu", info + "");
				ShowUtils.showTextToast(PushActivity.this, "图片上传成功！", toast);
				finish();
			}
		}, null);
	}

	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}

	public String getIMEI() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = tm.getDeviceId();
		return IMEI;
	}

	// 照相并保存的回调方法
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAMERA) {
			switch (resultCode) {
			case RESULT_OK: // 用户照相

				Bundle bundle = data.getExtras();
				// 获取相机返回的数据，并转换为图片格式
				Bitmap camera_bitmap = (Bitmap) bundle.get("data");
				// camera_bitmap =
				// ThumbnailUtils.extractThumbnail(camera_bitmap, 300, 400);
				iv_activity_push_photoline
						.setImageBitmap(outputBitmap(camera_bitmap));
				bt_camera.setBackgroundDrawable(null);
				hasPicture = true;

				break;
			case RESULT_CANCELED: // 用户取消照相
				return;
			}

			// 显示图片
		}
		if (requestCode == IMAGE_CODE) {
			switch (resultCode) {
			case RESULT_OK:
				try {
					Uri uri = data.getData();
					if (uri == null) {
						Log.i("张雯", "huhu");
						return;
					}
					Log.i("张糊糊", uri.toString());
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(uri,
							filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					// filename = cursor.getString(columnIndex);

					ContentResolver cr = this.getContentResolver();

					Bitmap album_bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(uri));
					if (album_bitmap == null) {
						Log.i("张雯", "糊糊");
					}
					album_bitmap = ThumbnailUtils.extractThumbnail(
							album_bitmap, 150, 180);
					iv_activity_push_photoline
							.setImageBitmap(outputBitmap(album_bitmap));
					hasPicture = true;

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					ShowUtils.showTextToast(getApplicationContext(),
							"图片太大,请换张图片！", toast);
					return;
				}
				break;
			case RESULT_CANCELED: // 用户取消照相
				return;
			}
		}
	}

	/**
	 * 把图片压缩并切圆角，保存到本地
	 * 
	 * @param bitmap
	 * @return
	 */
	public Bitmap outputBitmap(Bitmap bitmap) {
		new DateFormat();
		String name = DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		FileOutputStream fout = null;
		final String SDpath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File file = new File(SDpath + "/sdcard/BookPicture/");
		file.mkdirs();
		filename = file.getPath() + name;
		try {
			fout = new FileOutputStream(filename);

			bitmap = toRoundCorner(bitmap, 10);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fout.flush();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * 将图片设置为圆角
	 */
	public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 比较是否已经过了冻结时间
	 * 
	 * @param date
	 * @return
	 */
	public boolean isToDate(String dateTime, String bad_times) {
		// TODO Auto-generated method stub
		int days = 0;
		if (bad_times.equalsIgnoreCase("1")) {
			days = 1;
		} else if (bad_times.equalsIgnoreCase("2")) {
			days = 3;
		}
		if (bad_times.equalsIgnoreCase("3")) {
			days = 7;
		}
		Date nowdate = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date myDate = null;
		try {
			myDate = sdf.parse(dateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar date = Calendar.getInstance();
		date.setTime(nowdate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - days);
		Date endDate = date.getTime();

		boolean flag = myDate.before(endDate);// 在当前日期之后
		System.out.println(myDate.toString()
				+ "################$$$$$$____________" + endDate.toString());
		return flag;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		str_phone = edt_phonenumber.getText().toString().trim();
		str_QQ = edt_QQnumber.getText().toString().trim();
		str_price = edt_price.getText().toString().trim();
		str_name = edt_name.getText().toString().trim();
		campus = bt_school.getText().toString().trim();
		type = bt_category.getText().toString().trim();
		details = describe.getText().toString().trim();

		if ((!hasPicture) && str_phone.isEmpty() && details.isEmpty()
				&& str_QQ.isEmpty() && str_price.isEmpty()
				&& str_name.isEmpty() && campus.equalsIgnoreCase("选择校区")
				&& type.equalsIgnoreCase("选择分类")) {
			finish();
		} else {

			View sureDialogView = LayoutInflater.from(PushActivity.this)
					.inflate(R.layout.sure_dialog, null);
			TextView tv_content = (TextView) sureDialogView.findViewById(R.id.tv_sure_dialog_content);
			tv_content.setText("您有已填写内容，要舍弃它们么。");
			Button bt_sure = (Button) sureDialogView
					.findViewById(R.id.bt_sure_dialog_ok);
			bt_sure.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			final Button bt_cancle = (Button) sureDialogView
					.findViewById(R.id.bt_sure_dialog_cancle);
			final Dialog alertDialog = new Dialog(PushActivity.this,
					R.style.MyDialog);
			alertDialog.setContentView(sureDialogView);
			alertDialog.show();
			bt_cancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.dismiss();
				}
			});
		}
	}
}
