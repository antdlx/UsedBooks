package com.ing.usedbooks.entity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.provider.Telephony.Sms.Conversations;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.ing.usedbooks.R;

public class ChooseClassificationPopupwindow extends PopupWindow {
	public Button getBtnKaoyan() {
		return btnKaoyan= (Button) contentView.findViewById(R.id.btnKaoyan);
	}
	public Button getBtnComputer() {
		return btnComputer= (Button) contentView.findViewById(R.id.btnComputer);
	}

	public Button getBtnCET() {
		return btnCET = (Button) contentView.findViewById(R.id.btnCET);
	}

	public Button getBtnNovel() {
		return btnNovel= (Button) contentView.findViewById(R.id.btnNovel);
	}

	public Button getBtnCartoon() {
		return btnCartoon= (Button) contentView.findViewById(R.id.btnCartoon);
	}

	public Button getBtnMagazine() {
		return btnMagazine= (Button) contentView.findViewById(R.id.btnMagazine);
	}

	public Button getBtnElse() {
		return btnElse= (Button) contentView.findViewById(R.id.btnElse);
	}

	public Button getBtnLiterature() {
		return btnLiterature= (Button) contentView.findViewById(R.id.btnLiterature);
	}

	private View contentView;
	private Button btnKaoyan,btnComputer,btnCET,btnNovel,btnCartoon,btnMagazine,btnElse,btnLiterature;
	private Context context;
	
	public ChooseClassificationPopupwindow(final Activity context){
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(R.layout.classification_popupwindow, null);
		
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
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
			int px = (int)(-2*density);
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, px);
		} else {
			this.dismiss();
		}
	}
	

}