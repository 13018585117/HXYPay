package com.hxypay.util;

import android.content.Context;
import android.content.Intent;

import com.hxypay.App;
import com.hxypay.ui.LoginActivity;

public class GoLogin {
    public static void goLogin(String code) {
        if (code.equals("0006")) {
            Context context = App.getApp().getApplicationContext();
            Intent mIntent = new Intent(context, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }
    }
}
