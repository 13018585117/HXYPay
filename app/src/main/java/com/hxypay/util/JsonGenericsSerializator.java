package com.hxypay.util;

import android.util.Log;

import com.daoqidata.okhttputils.callback.IGenericsSerializator;
import com.google.gson.Gson;


import okhttp3.Response;

public class JsonGenericsSerializator implements IGenericsSerializator {
    Gson mGson = new Gson();
    @Override
    public <T> T transform(String response, Class<T> classOfT, Response allResponse) {
        Log.i("zbpay返回json数据",response);
        return mGson.fromJson(response, classOfT);
    }

}
