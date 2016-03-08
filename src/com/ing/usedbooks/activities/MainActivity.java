package com.ing.usedbooks.activities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ing.usedbooks.R;
import com.ing.usedbooks.net.GetApkUrl;
import com.ing.usedbooks.net.GetVersionName;

public class MainActivity extends Activity {

	private Double VersionName;
	private TextView tvBarProgress;
	private Dialog d;
	private boolean weatherFinished = true;
	private String apkURL = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if (weatherFinished) {
					CountDown();
					if (countDownDays > 35) {
						Intent i = new Intent(MainActivity.this,
								ViewPagerActivity.class);
						startActivity(i);
					} else {
						Intent i = new Intent(MainActivity.this,
								CountDownActivity.class);
						i.putExtra("countDownNum", countDownDays);
						startActivity(i);
					}
					MainActivity.this.finish();
				}
			}
		};
		timer.schedule(task, 1000 * 3);
		
		new GetVersionName(new GetVersionName.SuccessCallbcak() {

			@Override
			public void onSuccess(String versionName) {
				try {
					VersionName = Double.valueOf(MainActivity.this
							.getPackageManager().getPackageInfo(
									"com.ing.usedbooks", 0).versionName);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				if ((!VersionName.equals(versionName))&&countDownDays > 35) {
					weatherFinished = false;
					// 初始询问的dialog
					View dialogVIew = LayoutInflater.from(MainActivity.this)
							.inflate(R.layout.my_dialog, null);

					Button PositiveBtn = (Button) dialogVIew.findViewById(R.id.PositiveBtn);
					// 确定下载的监听器
					PositiveBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							// 创造一个新的下载dialog
							View uploadDialogView = LayoutInflater.from(
									MainActivity.this).inflate(
									R.layout.upoload_dialog, null);
							// 下载dialog的取消按钮
							Button CancelBtn = (Button) uploadDialogView
									.findViewById(R.id.cancelBtn);
							CancelBtn.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									weatherFinished = true;
									// try {
									// 关闭线程，原理同interrupt
									stop = true;
									uploadDialog.dismiss();
									// 删除未下载完整的废文件
									File file = new File(Environment
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

										Intent i = new Intent(
												MainActivity.this,
												ViewPagerActivity.class);

										startActivity(i);

									MainActivity.this.finish();
								}
							});

							// 下载dialog的进度条
							uploadProgressBar = (ProgressBar) uploadDialogView
									.findViewById(R.id.progressBar);
							// uploadProgressBar.setVisibility(View.VISIBLE);
							// uploadProgressBar.setProgress(20);
							tvBarProgress = (TextView) uploadDialogView
									.findViewById(R.id.tvBarProgress);

							// 创建下载dialog
							uploadDialog = new Dialog(MainActivity.this,
									R.style.MyDialog);
							uploadDialog.setContentView(uploadDialogView);

							Window dialogWindowx = uploadDialog.getWindow();
							WindowManager.LayoutParams managerx = dialogWindowx
									.getAttributes();

							int WIDTH = getResources().getDisplayMetrics().widthPixels;
							int HEIGHT = getResources().getDisplayMetrics().heightPixels;

							managerx.width = (WIDTH / 7) * 5;
							managerx.height = HEIGHT / 4;

							dialogWindowx.setAttributes(managerx);

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
//							if (countDownDays > 35) {
								Intent i = new Intent(MainActivity.this,
										ViewPagerActivity.class);
								startActivity(i);
//							} else {
//								Intent i = new Intent(MainActivity.this,
//										CountDownActivity.class);
//								i.putExtra("countDownNum", countDownDays);
//								startActivity(i);
//								weatherFinished=false;
//							}
							MainActivity.this.finish();
							d.dismiss();
						}
					});

					d = new Dialog(MainActivity.this, R.style.MyDialog);
					d.setContentView(dialogVIew);

					Window dialogWindow = d.getWindow();
					WindowManager.LayoutParams manager = dialogWindow
							.getAttributes();

					int WIDTH = getResources().getDisplayMetrics().widthPixels;
					int HEIGHT = getResources().getDisplayMetrics().heightPixels;

					manager.width = (WIDTH / 7) * 5;
					manager.height = HEIGHT / 4;

					dialogWindow.setAttributes(manager);

					d.show();
				}
			}
		}, new GetVersionName.FailCallback() {

			@Override
			public void onFail() {
				Toast.makeText(MainActivity.this, "呵呵，失败了呢", Toast.LENGTH_LONG);
			}
		});
	}

	private int currentDate, currentMonth;
	private long countDownDays;
	private InputStream inputStream;
	private OutputStream outputStream;
	private BufferedInputStream bis;
	private boolean stop = false;
	private int progress = 0;
	private int total = 0;
	private int current = 0;
	private ProgressBar uploadProgressBar;
	private int cprogress = 0;
	private Dialog uploadDialog;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 9999) {
				cprogress = msg.arg1;
				tvBarProgress.setText(cprogress + "%");
				uploadProgressBar.setProgress(cprogress);
			}
		}
	};

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

	/**
	 * 
	 * @param 计算假期剩余天数
	 */
	public void CountDown() {
		Calendar current = Calendar.getInstance();
		currentDate = current.get(Calendar.DATE);
		currentMonth = current.get(Calendar.MONTH);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date currentDate1;
		Date currentDate2;

		try {
			 currentDate1 =
			 sdf.parse("2014-"+String.valueOf(currentMonth+1)+"-"+String.valueOf(currentDate));
//			测试数据
//			currentDate1 = sdf.parse("2015-02-16");
			// 寒假假期结束时间为2015-03-01
			currentDate2 = sdf.parse("2015-03-01");

			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();

			cal1.setTime(currentDate1);
			cal2.setTime(currentDate2);
			countDownDays = (cal2.getTimeInMillis() - cal1.getTimeInMillis())
					/ (3600 * 24 * 1000);
			System.out
					.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyThe date dis is :"
							+ countDownDays);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		//
		//
		// if (cur) {
		//
		// }
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
				File file = new File(Environment.getExternalStorageDirectory(),
						"usedbook.apk");
				InstallApk(file);
				weatherFinished = true;
				MainActivity.this.finish();
			}
		}
	}
}
