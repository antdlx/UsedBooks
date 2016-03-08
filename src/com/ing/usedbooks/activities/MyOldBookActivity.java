package com.ing.usedbooks.activities;

import java.util.ArrayList;

import com.ing.usedbooks.R;
import com.ing.usedbooks.fragment.SaledBookFragment;
import com.ing.usedbooks.fragment.SalingBookFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
//import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MyOldBookActivity extends FragmentActivity {
	private ViewPager viewpager_myoldbook;
	private SalingBookFragment frag_salingBook;
	private SaledBookFragment frag_saledBook;
	private ArrayList<Fragment> fragmentList;
	private ArrayList<String>   titleList    = new ArrayList<String>();
	private PagerTabStrip pagertab_myoldbook;
	private Button bt_myoldbook_back;
	private ImageView iv_myoldbook_back;
	  
//	private PagerTitleStrip pagerTitleStrip_myoldbook;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_myoldbook);		
		
		viewpager_myoldbook = (ViewPager)findViewById(R.id.viewpager_myoldbook);		
		pagertab_myoldbook=(PagerTabStrip) findViewById(R.id.pagertab_myoldbook);
		pagertab_myoldbook.setTabIndicatorColor(getResources().getColor(android.R.color.holo_orange_dark)); 
		pagertab_myoldbook.setBackgroundColor(getResources().getColor(R.color.myoldbooks_gray));
				
		frag_salingBook = new SalingBookFragment();
		frag_saledBook = new SaledBookFragment();

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(frag_salingBook);
		fragmentList.add(frag_saledBook);
		
	    titleList.add("正在销售");
	    titleList.add("已销售");
		
	    viewpager_myoldbook.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
	    iv_myoldbook_back = (ImageView) findViewById(R.id.iv_myoldbook_back);
	    bt_myoldbook_back = (Button) findViewById(R.id.bt_myoldbook_back);
	    bt_myoldbook_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	    bt_myoldbook_back.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub 
		        if(event.getAction() == MotionEvent.ACTION_DOWN){  
		        	iv_myoldbook_back.setImageResource(R.drawable.activity_push_back_pressed);  
		        }else if(event.getAction() == MotionEvent.ACTION_UP){  
		        	iv_myoldbook_back.setImageResource(R.drawable.activity_myoldbook_back);   
		        } 
				return false;
			}
		});
	}
	
	public class MyViewPagerAdapter extends FragmentPagerAdapter{
		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}
	}

}
