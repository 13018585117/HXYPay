package com.hxypay.wxchat;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.hxypay.customview.IToast;

public class WXPay {

    public static void pay(Context context, PayReq request) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, request.appId);
        api.registerApp(request.appId);
        if (!api.isWXAppInstalled()) {
            IToast.getIToast().showIToast("请先安装微信客户端");
            return;
        }
        api.sendReq(request);
    }
}
