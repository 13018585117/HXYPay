package com.hxypay;

import android.app.Activity;
import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.billy.android.swipe.SmartSwipeBack;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.https.HttpsUtils;
import com.daoqidata.okhttputils.log.LoggerInterceptor;
import com.hxypay.tab.OTabActivity;
import com.hxypay.tab.OTabHost;
import com.hxypay.ui.HomeActivity;
import com.hxypay.ui.NewHandActivity;
import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.hxypay.baiduMap.MyLocationListener;
import com.hxypay.response.LoginRes;
import com.wildma.idcardcamera.global.Constant;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;

public class App extends Application {

    public static App app;
    public LocationClient getLocationClient() {
        if (locationClient==null){
            initBaiduMap();
            return locationClient;
        }else {
            return locationClient;
        }
    }

    private static LocationClient locationClient;
    private LocationClientOption locationOption;

    public static void setApp(App app) {
        App.app = app;
    }

    public static App getApp() {
        return app;
    }


    private LoginRes UserInfo;

    public LoginRes getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(LoginRes userInfo) {
        UserInfo = userInfo;
    }


    private boolean memDialogFlag = true;

    public boolean isMemDialogFlag() {
        return memDialogFlag;
    }

    public void setMemDialogFlag(boolean memDialogFlag) {
        this.memDialogFlag = memDialogFlag;
    }

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        CrashReport.initCrashReport(getApplicationContext(), "5421941f77", false);

//        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        MobSDK.init(this);
        intiOkHttpUtils();
        initBaiduMap();
        initX5();
        SmartSwipeBack.activitySlidingBack(App.getApp(), new SmartSwipeBack.ActivitySwipeBackFilter() {
            @Override
            public boolean onFilter(Activity activity) {
//                Toast.makeText(activity, activity.getClass().getName()+"    "+HomeActivity.class.getName(), Toast.LENGTH_SHORT).show();
                //根据传入的activity，返回true代表需要侧滑返回；false表示不需要侧滑返回
                return true;
            }
        });

    }

    /**
     * 初始化X5
     */
    private void initX5() {
        //x5內核初始化回调
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private  void initBaiduMap() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(getApplicationContext());
//声明LocationClient类实例并配置定位参数
        locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
//注册监听函数
        locationClient.registerLocationListener(myLocationListener);
        locationOption.setIsNeedAddress(true);//可选，是否需要地址信息，默认为不需要，即参数为false
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

//如果开发者需要获得当前点的地址信息，此处必须为true
        locationClient.setLocOption(locationOption);
//开始定位
        locationClient.start();
    }


    /**
     * 初始化okutil
     */
    private void intiOkHttpUtils() {
        //  ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

}
