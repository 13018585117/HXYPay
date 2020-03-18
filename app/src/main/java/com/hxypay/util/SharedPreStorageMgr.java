package com.hxypay.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hxypay.App;

/**
 * @author liujian
 * @date 2012-10-9
 * @function
 * @function 封装存数基本类型数据的SharedPreference
 */
public class SharedPreStorageMgr {
    private static final SharedPreStorageMgr instance = new SharedPreStorageMgr();
    private Context context = App.getApp().getApplicationContext();
    private SharedPreferences sp = context.getSharedPreferences(Constants.PREFS_NAMES,
            Context.MODE_PRIVATE);

    public static SharedPreStorageMgr getIntance() {
        return instance;
    }

    /**
     * 保存字符串
     *
     * @param key
     * @param value
     * @return
     */
    public boolean saveStringValue(String key, String value) {
        Editor editor = sp.edit();
        editor.putString(key, value);
        boolean rs = editor.commit();
        return rs;
    }

    /**
     * 获取字符串
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        String value = sp.getString(key, "");
        return value;
    }

    /**
     * ֵ 保存布尔值ֵ
     *
     * @param key
     * @param value
     * @return
     */
    public boolean saveBooleanValue(String key, boolean value) {
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        boolean rs = editor.commit();
        return rs;
    }

    /**
     * ֵ 获取布尔值
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        boolean value = sp.getBoolean(key, false);
        return value;
    }

    /**
     * ֵ 获取布尔值 登录处默认勾选
     *
     * @param key
     * @return
     */
    public boolean getBooleanLoginValue(String key) {
        boolean value = sp.getBoolean(key, true);
        return value;
    }

    /**
     * 保存整形值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean saveIntegerValue(String key, int value) {
        Editor editor = sp.edit();
        editor.putInt(key, value);
        boolean rs = editor.commit();
        return rs;
    }

    /**
     * 获取整形值
     *
     * @param key
     * @return
     */
    public int getIntegerValue(String key) {
        int value = sp.getInt(key, 0);
        return value;
    }

    public int getIntegerValue11(String key) {
        int value = sp.getInt(key, 0);
        return value;
    }


    public void remove(String key) {
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
}
