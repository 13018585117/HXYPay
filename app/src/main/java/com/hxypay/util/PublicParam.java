package com.hxypay.util;

import java.util.HashMap;
import java.util.Map;

public class PublicParam {
    public static Map getParam() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", SharedPreStorageMgr.getIntance().getStringValue(Constants.UID));
        params.put("token", SharedPreStorageMgr.getIntance().getStringValue(Constants.TOKEN));
        return params;
    }
}
