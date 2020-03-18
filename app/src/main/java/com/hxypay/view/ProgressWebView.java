package com.hxypay.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;


import java.util.Map;

public class ProgressWebView extends WebView {
    private SwipeRefreshLayout swipeRefreshLayout;

    public ProgressWebView(Context context, SwipeRefreshLayout swipeRefreshLayout){
        super(context);
        this.swipeRefreshLayout = swipeRefreshLayout;
    }



    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (this.getScrollY() == 0){
            swipeRefreshLayout.setEnabled(true);
        }else {
            swipeRefreshLayout.setEnabled(false);
        }

        Log.e("newish",this.getScaleY()+"");
    }

}
