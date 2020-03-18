package com.hxypay.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxypay.BaseActivity;
import com.hxypay.R;


public class FastPayWBViewActivity extends BaseActivity {
    private WebView webView;
    private String mTitle, mUrl;

    private String lastUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wb_view);
        initView();
    }

    public void initView() {
        mUrl = getIntent().getExtras().getString("url");
        mTitle = getIntent().getExtras().getString("title");
        initHeadView(this, R.drawable.icon_back_r, mTitle, 0,null);
        init();
    }


    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        //WebView加载web资源
        webView.loadUrl(mUrl);
        //覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebView中打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候是控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器打开
//                lastUrl = url;
//                if(lastUrl.equals(Constants.GOURL)){
//                    mIntent = new Intent(context,OKActivity.class);
//                    mIntent.putExtra("amout",getIntent().getExtras().getString("amout"));
//                    mIntent.putExtra("orderId",getIntent().getExtras().getString("orderId"));
//                    startActivity(mIntent);
//                    finish();
//                }else{
//                    view.loadUrl(url);
//                }
                view.loadUrl(url);
                return true;
            }
            //WebViewClient帮助WebView去处理一些页面控制和请求通知
        });
        //启用支持Javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        //自动加载图片
        settings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        //页面加载
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //newProgress   1-100之间的整数
                if (newProgress == 100) {
                    //页面加载完成，关闭ProgressDialog
                    dismissDialog();
                } else {
                    //网页正在加载，打开ProgressDialog
                    showDialog("加载中...");
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
//                if (webView.canGoBack()) {
//                    webView.goBack();
//                } else {
//                    finish();
//                }
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            webView.goBack();// 返回webView的上一页面
            finish();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
