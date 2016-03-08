package com.ing.usedbooks.entity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ing.usedbooks.R;
import com.ing.usedbooks.net.GetResearch;

public class MorePopWindow extends PopupWindow {
	private View contentView;
	private TextView tvUsedbook,tvUpload,tvSuggestion,tvAbout;
	private Context context;
	
	public MorePopWindow(final Activity context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(R.layout.more_popup, null);
		
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(contentView);
		this.setWidth(w / 3 );
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		this.setAnimationStyle(R.style.AnimationPreview1);
	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			float density = context.getResources().getDisplayMetrics().density;
			//实现单位转化
			int px = (int)(11*density);
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, px+1);
		} else {
			this.dismiss();
		}
	}
	
	public TextView getUsedBookTextView(){
		return 	tvUsedbook = (TextView) contentView.findViewById(R.id.tvMyUsedbook);
	}
	
	public TextView getUploadTextVIew(){
		return	tvUpload = (TextView) contentView.findViewById(R.id.tvUpload);
	}
	public TextView getSuggestionTextVIew(){
		return	tvSuggestion = (TextView) contentView.findViewById(R.id.tvSuggestion);
	}
	public TextView getAboutTextVIew(){
		return 	tvAbout = (TextView) contentView.findViewById(R.id.tvAbout);
	}
}
