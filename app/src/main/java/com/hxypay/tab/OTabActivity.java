package com.hxypay.tab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.bean.BuildConfig;
import com.hxypay.customview.TipDialog;
import com.hxypay.response.AppVerRes;
import com.hxypay.ui.HomeActivity;
import com.hxypay.ui.MineActivity2;
import com.hxypay.ui.SJActivity;
import com.hxypay.ui.ShareActivity2;
import com.hxypay.update.UpdateDialog;
import com.hxypay.util.Constants;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;

/**
 * TabHost切换标签页面
 *
 * @author liujian
 */
public class OTabActivity extends OTabHost {

    public static OTabActivity mainAct;

    List<TabItem> mItems;
    TipDialog tipDialog;
    private UpdateDialog updateDialog;

    /**
     * 在初始化TabWidget前调用 和TabWidget有关的必须在这里初始化
     */
    @Override
    protected void prepare() {

        mainAct = this;

        TabItem dk = new TabItem("首页", R.drawable.tab_home_selector, new Intent(this, HomeActivity.class));

//        TabItem home = new TabItem("卡包", R.drawable.tab_card_selector, new Intent(this, CardBagActivity.class));

        TabItem share = new TabItem("推广", R.drawable.tab_share_selector, new Intent(this, ShareActivity2.class));

//        TabItem wallet = new TabItem("升级", R.drawable.tab_news_selector, new Intent(this, NewsActivity.class));
        TabItem wallet = new TabItem("升级", R.drawable.tab_news_selector, new Intent(this, SJActivity.class));

        TabItem account = new TabItem("我的", R.drawable.tab_mine_selector, new Intent(this, MineActivity2.class));


        mItems = new ArrayList<TabItem>();

        mItems.add(dk);
//        mItems.add(home);
        mItems.add(share);
        mItems.add(wallet);
        mItems.add(account);

        // // 设置分割线
        // TabWidget tabWidget = getTabWidget();
        // tabWidget.setDividerDrawable(R.drawable.tab_divider);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentTab(0);
//		// 这里执行的话会重新执行tab每个Activity的生命周期
        for (int i = 0; i < mItems.size(); i++) {
//			if (i == 1) {
//            mItems.get(i).getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			}
        }
    }


    private void goToUpdate(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent mIntent = new Intent();
        mIntent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);//此处填链接
        mIntent.setData(content_url);
        OTabActivity.this.startActivity(mIntent);
        finish();
    }


    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }


    /**
     * tab的title，icon，边距设定等等
     */
    @Override
    protected void setTabItemTextView(TextView textView, int position) {
        textView.setPadding(3, 3, 3, 3);
        textView.setText(mItems.get(position).getTitle());
        textView.setCompoundDrawablesWithIntrinsicBounds(0, mItems.get(position).getIcon(), 0, 0);

    }

    /**
     * tab唯一的id
     */
    @Override
    protected String getTabItemId(int position) {
        return mItems.get(position).getTitle(); // 我们使用title来作为id，你也可以自定
    }

    /**
     * 点击tab时触发的事件
     */
    @Override
    protected Intent getTabItemIntent(int position) {
        return mItems.get(position).getIntent();
    }

    @Override
    protected int getTabItemCount() {
        return mItems.size();
    }

    long exitTime = 0;

    /**
     * 退出
     */
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                //退出
//                SharedPreStorageMgr.getIntance().remove(Constants.ACCOUNT_PWD);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();


        updateVer();

    }

    private void updateVer() {
        OkHttpUtils.post()
                .url(Constants.APP_VER)
                .headers(PublicParam.getParam())
                .params(Params.AppVerParams("1"))
                .build().execute(new GenericsCallback<AppVerRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(final AppVerRes response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        final String appVerUpdateFlag = response.getData().getType();
                        final String appVerUpdateUrl = response.getData().getUrl();
                        String content = null;
                        String sVer = null;
                        String lVer = null;
                        try {
                            content = response.getData().getContent();
                            if (TextUtils.isEmpty(content)){
                                content = "有新版本啦！";
                            }
                            else {
                                content = content.replace("\\n", "\n");
                            }
                            BuildConfig.VERSION_URL = appVerUpdateUrl;
                            sVer = response.getData().getVersion().replace(".", "");
                            lVer = getLocalVersionName(OTabActivity.this).replace(".", "");

                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        if (Integer.parseInt(sVer) > Integer.parseInt(lVer)) {
                            if (updateDialog==null){
                                updateDialog = new UpdateDialog(OTabActivity.this, "更新的内容", content,response.getData().getVersion(), false);
                            }
                            updateDialog.setClickListener(new UpdateDialog.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {
                                }

                                @Override
                                public void doCancel() {
                                    if (appVerUpdateFlag.equals("1")){////强制更新，点击取消直接退出
                                        Toast.makeText(OTabActivity.this, "必须下载更新，否则无法正常使用", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    updateDialog.cancel();
                                }
                            });
                            updateDialog.show();


//                            tipDialog  = new TipDialog(OTabActivity.this, "有新版本啦，"+ response.getData().getVersion(),response.getData().getContent(), new String[]{"取消", "确定"}, new TipDialog.OnCustomDialogListener() {
//                                @Override
//                                public void ok(TipDialog dialog) {
//
//                                    if (appVerUpdateFlag.equals("0")) {
//                                        dialog.dismiss();
//                                        goToUpdate(appVerUpdateUrl);//跳转的URL，客户自行下载
//                                    }
//                                    if (appVerUpdateFlag.equals("1")) {//强制更新，点击取消直接退出
//                                        dialog.dismiss();
//                                        goToUpdate(appVerUpdateUrl);//跳转的URL，客户自行下载
//                                        finish();
//                                    }
//                                }
//
//                                @Override
//                                public void cancle(TipDialog dialog) {
//                                    if (appVerUpdateFlag.equals("0")) {
//                                        dialog.dismiss();
//                                    }
//                                    if (appVerUpdateFlag.equals("1")) {//强制更新，点击取消直接退出
//                                        dialog.dismiss();
//                                        finish();
//                                    }
//                                }
//                            });
//                            if (appVerUpdateFlag.equals("0")) {
//                                tipDialog.setCancelable(true);
//                            }
//                            if (appVerUpdateFlag.equals("1")) {
//                                tipDialog.setCancelable(false);
//                            }
//                            tipDialog.show();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(tipDialog!=null&&tipDialog.isShowing()){
            tipDialog.dismiss();
        }
        if (updateDialog!=null&&updateDialog.isShowing()){
            updateDialog.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
