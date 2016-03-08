package com.ing.usedbooks.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ing.usedbooks.R;

public class SearchListAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> list = new ArrayList<String>();
	private List<String> x = new ArrayList<String>();
	public SearchListAdapter(Context context){
		this.context = context;
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	public Context getContext(){
		
		return context;
	}
	
	public void addAll(List<String> data){
		int size = data.size();
		x.clear();
		list.clear();
		//反序排列
		for (int i = 0; i < size; i++) {
			x.add(i, data.get(size-i-1));
		}
		list.addAll(x);
		notifyDataSetChanged();
	}
	
	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView  =  LayoutInflater.from(getContext()).inflate(R.layout.item_searchlist, null);
			convertView.setTag(new ListCell((TextView)convertView.findViewById(R.id.textView)));
		}
		ListCell  lc = (ListCell) convertView.getTag();
		
		String s = getItem(position);
		
		lc.getTV().setText(s);
		
		return convertView;
	}

	
	private static class ListCell{
		private TextView tv;
		
		public ListCell(TextView tv){
			this.tv = tv;
		}
		
		public TextView getTV(){
			return tv;
		}
	}
}
