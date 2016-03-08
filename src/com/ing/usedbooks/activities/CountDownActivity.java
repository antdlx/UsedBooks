package com.ing.usedbooks.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ing.usedbooks.R;

public class CountDownActivity extends Activity {
	///
	private long countDownNum;
//	private TextView tvCountDown;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_count_down);
	
	Intent i = getIntent();
	countDownNum = i.getLongExtra("countDownNum", 999);
	
//	tvCountDown = (TextView) findViewById(R.id.tvCountDown);
//	tvCountDown.setText(String.valueOf(countDownNum)+ "天");
}
}
