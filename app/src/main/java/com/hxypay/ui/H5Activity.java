package com.hxypay.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxypay.BaseActivity;
import com.hxypay.R;


public class H5Activity extends BaseActivity {
    private WebView webView;
    private String html;
    public static DisplayMetrics displayMetrics;
    private String lastUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        initView();
    }

    public void initView() {
        initHeadView(this, R.drawable.icon_back_r, getIntent().getExtras().getString("title"), 0,null);
        displayMetrics = getResources().getDisplayMetrics();
        webView = (WebView) findViewById(R.id.webview);
        html = getIntent().getExtras().getString("h5");
        if (!TextUtils.isEmpty(html)) {
            showWebView();
        }
    }


    private void showWebView() {
        // 设置WevView要显示的网页
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8",
                null);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setJavaScriptEnabled(true); // 设置支持Javascript
        webView.requestFocus(); // 触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
        // luntanListview.getSettings().setBuiltInZoomControls(true); //页面添加缩放按钮
        // luntanListview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        // //取消滚动条

        // 点击链接由自己处理，而不是新开Android的系统browser响应该链接。
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 设置点击网页里面的链接还是在当前的webview里跳转
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
        });
        // luntanListview.setOnFocusChangeListener(new
        // View.OnFocusChangeListener() {
        // @Override
        // public void onFocusChange(View v, boolean hasFocus) {
        // if (hasFocus) {
        // try {
        // // 禁止网页上的缩放
        // Field defaultScale = WebView.class
        // .getDeclaredField("mDefaultScale");
        // defaultScale.setAccessible(true);
        // defaultScale.setFloat(luntanListview, 1.0f);
        // } catch (SecurityException e) {
        // e.printStackTrace();
        // } catch (IllegalArgumentException e) {
        // e.printStackTrace();
        // } catch (IllegalAccessException e) {
        // e.printStackTrace();
        // } catch (NoSuchFieldException e) {
        // e.printStackTrace();
        // }
        // }
        // }
        // });
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();// 返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
