package com.hxypay.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hxypay.R;

/**
 * 弹出Loading对话框加载数据
 * 
 * @author liujian
 * 
 */
public class CustomAlertDialog extends Dialog {
	private String message;
	private TextView textView;
	private Context context;
	public CustomAlertDialog(Context context, String message) {
		super(context, R.style.custom_dialog);
		if(context == null){
			return ;
		}
		this.context = context;
		this.message=message;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_dialog_layout);
		// 点击对话框区域外禁止取消对话框
		setCanceledOnTouchOutside(false);
		initDialog();
		
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0x01){
				if(textView==null){
					return ;
				}
				textView.setText((String)msg.obj);
			}
		}
	};
	
	public void setDialogShowMessage(String msg){
		if(context!=null){
			if(!TextUtils.isEmpty(msg)){
				mHandler.sendMessage(mHandler.obtainMessage(0x01,msg));
			}
		}
	}

	private void initDialog() {
		// int screenWidth =
		// getContext().getResources().getDisplayMetrics().widthPixels;
		// // 根据屏幕计算
		// int panelWidth = (int) (screenWidth * 0.85);
		// View view = findViewById(R.id.custom_dialog_ll);
		// ViewGroup.LayoutParams params = view.getLayoutParams();
		// params.width = panelWidth;
		// // 设置给包含按钮的布局
		// view.setLayoutParams(params);
		textView = (TextView) findViewById(R.id.text);
		if (!TextUtils.isEmpty(message)) {
			textView.setText(message);
			textView.setVisibility(View.VISIBLE);
		} 
	}
}
