package com.ing.usedbooks.adapters;

import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


/**
 * �·�ViewPager��������
 * @author dlx
 * 2014/10/11
 */
public class MyPagerAdapter extends PagerAdapter {
	public ArrayList<View> mViews;
	public MyPagerAdapter(ArrayList<View> mViews){
		this.mViews = mViews;
	}
	@Override
	public void destroyItem(View v, int position, Object obj) {
		((ViewPager)v).removeView(mViews.get(position));
	}

	@Override
	public void finishUpdate(View arg0) {
		
	}

	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public Object instantiateItem(View v, int position) {
		((ViewPager)v).addView(mViews.get(position));
		return mViews.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		
	}

}
