package com.hxypay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	static int payType=-1;//标识

	public static Handler Typehandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			payType=msg.what; //获取支付类型
			Log.e("微信支付 ","当前获取类型 "+payType);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);

	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		Log.e("微信支付","");
	}

	@Override
	public void onResp(BaseResp resp) {
//		Log.e("微信支付 ","进入支付回调页onResp "+payType +" 支付完成code "+resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode==-1){
				setPayResult("支付失败");
			}
			if(resp.errCode==0){
				setPayResult("支付完成");
				PayResp resps = (PayResp) resp;
				String payType = resps.extData;
				switch (payType){
					case "kcp":
//						Log.e("卡测评支付的","卡测评");
						break;
					case "sj":
						break;
				}
			}
			if(resp.errCode==-2){
				setPayResult("取消支付");
			}
		}
	}private void setPayResult(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		finish();
	}
}