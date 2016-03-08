package com.ing.usedbooks.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ing.usedbooks.R;
import com.ing.usedbooks.net.UploadSuggestion;

public class SuggestionActivity extends Activity {
	
	private Button btnSend,btnBack ;
	private EditText edSug,edPhone,edQQ;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_suggestion);
	
	edPhone = (EditText) findViewById(R.id.edphone);
	edSug = (EditText) findViewById(R.id.edSug);
	edQQ = (EditText) findViewById(R.id.edQQ);
	
	btnSend = (Button) findViewById(R.id.btnSend);
	btnSend.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String suggestion = edSug.getText().toString();
			String phone = edPhone.getText().toString();
			String qq = edQQ.getText().toString();
			String reg_phone = "^\\d{11}$";
			String reg_qq = "^\\d{5,10}$";
			if (phone.matches(reg_phone)&&qq.matches(reg_qq)&&suggestion!=null) {
			new UploadSuggestion(suggestion, phone, qq, new UploadSuggestion.SuccessCallback() {
				
				@Override
				public void OnSuccess() {
					View toastView = getLayoutInflater().inflate(R.layout.toast, null);
					TextView tv = (TextView) toastView.findViewById(R.id.tv);
					TextView tvTop = (TextView) toastView.findViewById(R.id.tvTop);
					tvTop.setText("发送成功！");
					tv.setText("感谢您使用我们的产品\n您的支持就是我们前进的动力\n我们会做的更好");
					
					Toast toast = new Toast(getApplicationContext());
					toast.setView(toastView);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.show();
				}
			}, new UploadSuggestion.FailCallback() {
				
				@Override
				public void OnFail() {
					
				}
			});
			}else {
				if (!(phone.matches(reg_phone)&&qq.matches(reg_qq))) {
					Toast.makeText(SuggestionActivity.this, "请填入正确的手机号码和QQ号以便我们联系您", Toast.LENGTH_LONG).show();
					return;
				}
				if (!phone.matches(reg_phone)) {
					Toast.makeText(SuggestionActivity.this, "请填入正确的手机号码以便我们联系您", Toast.LENGTH_LONG).show();
					return;
				}
				if (!qq.matches(reg_qq)) {
					Toast.makeText(SuggestionActivity.this, "请填入正确的QQ号以便我们联系您", Toast.LENGTH_LONG).show();
					return;
				}
				if (suggestion==null) {
					Toast.makeText(SuggestionActivity.this, "请填入您的建议", Toast.LENGTH_LONG).show();
					return;
				}
			}
		}
	});
			
	btnBack = (Button) findViewById(R.id.btnBack);
	btnBack.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(SuggestionActivity.this,ViewPagerActivity.class);
			startActivity(i);
		}
	});
}
}
