package com.hxypay.customview;

import android.widget.Toast;

import com.hxypay.App;

public class IToast {

    private static IToast iToast;

    public static IToast getIToast() {
        if (iToast == null) {
            iToast = new IToast();
        }
        return iToast;
    }

    public void showIToast(String msg) {
        Toast.makeText(App.getApp().getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
    }

    public void showIToast() {
        Toast.makeText(App.getApp().getApplicationContext(), "系统繁忙！！！", Toast.LENGTH_SHORT).show();
    }
}
