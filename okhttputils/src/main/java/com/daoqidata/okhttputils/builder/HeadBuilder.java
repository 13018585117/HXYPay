package com.daoqidata.okhttputils.builder;


import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.request.OtherRequest;
import com.daoqidata.okhttputils.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
