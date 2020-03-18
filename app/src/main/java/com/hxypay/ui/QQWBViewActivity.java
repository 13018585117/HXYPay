package com.hxypay.ui;

import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.hxypay.BaseActivity;
import com.hxypay.R;

public class QQWBViewActivity extends BaseActivity {

    private String mUrl;
    private String mTitle;
    private WebView webView;
    private long firstTime =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqwb_view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        webView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.web_QQ);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initView();
    }
    public void initView() {
        mUrl = getIntent().getExtras().getString("url");
        mTitle = getIntent().getExtras().getString("title");
        initHeadView(this, R.drawable.icon_back_r, mTitle, 0,null);
//        webView.setBackgroundColor(Color.TRANSPARENT); // WebView 背景透明效果
        init();
    }

    private void init() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);                    //支持Javascript 与js交互
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setAllowFileAccess(true);                      //设置可以访问文件
        settings.setSupportZoom(true);                          //支持缩放
        settings.setBuiltInZoomControls(true);                  //设置内置的缩放控件
        settings.setUseWideViewPort(true);                      //自适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setSupportMultipleWindows(true);               //多窗口
        settings.setDefaultTextEncodingName("utf-8");            //设置编码格式
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);       //缓存模式
        //WebView加载web资源
        if (!TextUtils.isEmpty(mUrl)) {
            webView.loadUrl(mUrl);
        }else{
            String html = getIntent().getStringExtra("h5");
            if (!TextUtils.isEmpty(html)){
                webView.loadDataWithBaseURL(null, html, "text/html", "utf-8",
                        null);
                settings.setUseWideViewPort(false);                      //自适应屏幕
            }
        }
        //覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebView中打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        //页面加载
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //newProgress   1-100之间的整数
                try {
                    if (newProgress == 100) {
                        //页面加载完成，关闭ProgressDialog
                        dismissDialog();
                    } else {
                        //网页正在加载，打开ProgressDialog
                        showDialog("加载中...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLeftListener() {
        super.onLeftListener();
        mLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();

        if (keyCode == KeyEvent.KEYCODE_BACK  && webView.canGoBack()) {
            if (secondTime - firstTime < 1000) {
                finish();
            } else {
                webView.goBack();// 返回webView的上一页面
                firstTime = System.currentTimeMillis();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }
}
