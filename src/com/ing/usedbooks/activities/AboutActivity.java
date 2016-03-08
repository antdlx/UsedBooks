package com.ing.usedbooks.activities;

import com.ing.usedbooks.R;

import android.app.Activity;
import android.os.Bundle;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		Button bt_about_back = (Button) findViewById(R.id.bt_about_back);
		bt_about_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
