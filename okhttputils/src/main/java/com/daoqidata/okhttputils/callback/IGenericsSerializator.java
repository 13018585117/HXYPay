package com.daoqidata.okhttputils.callback;

import okhttp3.Response;

/**
 * Created by JimGong on 2016/6/23.
 */
public interface IGenericsSerializator {
    <T> T transform(String response, Class<T> classOfT, Response allResponse);
}
