package com.ing.usedbooks.entity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ing.usedbooks.R;

public class ChooseCampusPopwindow extends PopupWindow {
	private View contentView;
	private Button btnSoftWare,btnElse;
	private Context context;
	
	public ChooseCampusPopwindow(final Activity context){
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(R.layout.change_campus_popupwindow, null);
		
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
//		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(contentView);
		float density = context.getResources().getDisplayMetrics().density;
		//实现单位转化
		int px = (int)(247*density);
		this.setWidth(px);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		this.setAnimationStyle(R.style.AnimationPreview2);
	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			float density = context.getResources().getDisplayMetrics().density;
			int px = (int)(2*density);
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, px);
		} else {
			this.dismiss();
		}
	}
	
	public Button getTvSoftWare(){
		return 	btnSoftWare = (Button) contentView.findViewById(R.id.tvSoftWare);
	}
	
	public Button getTvElse(){
		return	btnElse = (Button) contentView.findViewById(R.id.tvElse);
	}

}