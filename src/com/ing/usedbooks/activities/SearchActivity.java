package com.ing.usedbooks.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ing.usedbooks.R;
import com.ing.usedbooks.adapters.SearchListAdapter;
import com.ing.usedbooks.entity.ValidityCheck;

public class SearchActivity extends Activity {
	private Button btnSearch;
	private EditText et;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private String search;
	private int index = 1;
	private ListView list;
	private SearchListAdapter searchListAdapter;
	private List<String> data = new ArrayList<String>();
	private String s="";
	private ValidityCheck vc;
	
	//动态更新UI
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what==8888&&msg.arg1==1) {
				data.clear();
				searchListAdapter.addAll(data);
				searchListAdapter.notifyDataSetChanged();
				list.setAdapter(searchListAdapter);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		searchListAdapter = new SearchListAdapter(SearchActivity.this);
		list = (ListView) findViewById(R.id.listView);
		list.setAdapter(searchListAdapter);

		sp = getSharedPreferences("HISTORY", Context.MODE_PRIVATE);
		editor = sp.edit();

		et = (EditText) findViewById(R.id.etSearch);
		vc = new ValidityCheck();
		
		Button bt_search_back = (Button) findViewById(R.id.bt_search_back);
		bt_search_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i  = new Intent(SearchActivity.this,ViewPagerActivity.class);
				startActivity(i);
				finish();
			}
		});

		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (vc.isUseable(et.getText().toString())) {
					index = sp.getInt("index", 1);
					search = et.getText().toString();
					System.out.println(search+"search");
					editor.putString("history" + index, search);
					editor.putInt("index", ++index);
					editor.commit();
					
					//请在此输入跳转语句
					Intent i = new Intent(SearchActivity.this,SubActivity.class);
					i.putExtra("Search", search);
					startActivity(i);
				}else {
					Toast.makeText(SearchActivity.this, "请重新输入，搜索内容应为1~15位的数字、汉字或字母", Toast.LENGTH_LONG).show();
				}
			}
		});
		startList();
		
		Button cleanHistory =(Button)findViewById(R.id.cleanHistory);
		cleanHistory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					editor.clear().commit();
					index=1;
					Message msg = new Message();
					msg.what=8888;
					msg.arg1=1;
					handler.sendMessage(msg);
			}
		});
	}

	private void startList() {
		index = sp.getInt("index", 1)-1;
		System.out.println(index+"index");
		//至多显示六条
		if (index<=6) {
			for (int j = 1; j <=index; j++) {
				s = sp.getString("history"+j, "null");
				System.out.println(s+"222222222222");
				data.add(s);
			}
			searchListAdapter.addAll(data);
		}else {
			for (int j = index-5; j <= index; j++) {
				s = sp.getString("history"+j, "");
				System.out.println(s+"222222222222");
				data.add(s);
			}
			searchListAdapter.addAll(data);
		}
	}
}
