package com.hxypay.baiduMap;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

public class MyLocationListener extends BDAbstractLocationListener {

    public static String getProvince() {
        if (province==null){
            province ="北京市";
        }
        return province;
    }

    public  void setProvince(String province) {
        this.province = province;
    }

    public static String getCity() {
        if (city==null){
            city ="北京市";
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private static String province;
    private static String city;

    @Override
    public void onReceiveLocation(BDLocation location){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取地址相关的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
        String addr = location.getAddrStr();    //获取详细地址信息
        String country = location.getCountry();    //获取国家
        //获取省份
        province = location.getProvince();
        //获取城市
        city = location.getCity();
        String district = location.getDistrict();    //获取区县
        String street = location.getStreet();    //获取街道信息
        Log.e("位置",country+ province + city +district+street);
    }
}
