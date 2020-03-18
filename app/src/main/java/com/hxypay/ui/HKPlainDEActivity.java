package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.google.gson.Gson;
import com.hxypay.App;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.baiduMap.MyLocationListener;
import com.hxypay.bean.JsonBean;
import com.hxypay.bean.RQBean;
import com.hxypay.customview.IToast;
import com.hxypay.customview.RQPop;
import com.hxypay.response.ChannelRes;
import com.hxypay.response.CreditRes;
import com.hxypay.response.MccRes;
import com.hxypay.response.PriviewPlainRes;
import com.hxypay.response.RQRes;
import com.hxypay.util.Constants;
import com.hxypay.util.DateTools;
import com.hxypay.util.GetJsonDataUtil;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class HKPlainDEActivity extends BaseActivity {

    private CreditRes.Info1 card;
    private ChannelRes.Info1 channel;

    private EditText mMoneyEt;
//    private TextView mStartTimeText;
    private TextView mEndTimeText;
    private TextView mCountText;
    private TextView mProvinceCityText;
    private TextView mMccText;

    private RQPop rqPop;

    private Button mComBtn;

    private String money, /*sTime,*/ eTime, count, pc, province, city, mcc, mccCode;
    private List<MccRes.Info1> mccList = new ArrayList<MccRes.Info1>();

    private String cTime = DateTools.getDateTodayYMD();
    private OptionsPickerView pvOptions;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;

    private int selectProvince =0; //选择的省
    private int selectCity =0;  //选择的市  默认为第一个；
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    showDialog("加载中...");
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    dismissDialog();
                    break;
                case MSG_LOAD_FAILED:
                    isLoaded = false;
                    dismissDialog();
                    break;
            }
            super.handleMessage(msg);
        }

    };
    private String cardNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hk_plain_de);
        //请求下位置；
        App.getApp().getLocationClient().requestLocation();
        initView();
        getMcc();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "智能还款", 0,null);
        channel = (ChannelRes.Info1) getIntent().getSerializableExtra("channel");
        card = (CreditRes.Info1) getIntent().getSerializableExtra("card");
        setCardView(card);
        handler.sendEmptyMessage(MSG_LOAD_DATA);
        mMoneyEt = (EditText) findViewById(R.id.money_et);
        setMoneyEditText(mMoneyEt);
//        mStartTimeText = (TextView) findViewById(R.id.text_start_time);
        mEndTimeText = (TextView) findViewById(R.id.text_end_time);
        mCountText = (TextView) findViewById(R.id.count_tx);
        mProvinceCityText = (TextView) findViewById(R.id.provice_city_tx);
        mMccText = (TextView) findViewById(R.id.mcc_tx);
        mComBtn = (Button) findViewById(R.id.btn_com);

        mProvinceCityText.setText(MyLocationListener.getProvince()+" "+MyLocationListener.getCity());
//        mStartTimeText.setOnClickListener(this);
        mEndTimeText.setOnClickListener(this);
        mCountText.setOnClickListener(this);
        mProvinceCityText.setOnClickListener(this);
        mMccText.setOnClickListener(this);
        mComBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.text_start_time:
//                timeSelect(1);
//                break;
            case R.id.text_end_time:
//                timeSelect(2);
                getRQ();
                break;
            case R.id.count_tx:
                hkCountSelect();
                break;
            case R.id.provice_city_tx:
                showPickerView();
                break;
            case R.id.mcc_tx:
                hkMccSelect(mccList);
                break;
            case R.id.btn_com:
                money = mMoneyEt.getText().toString();
                eTime = mEndTimeText.getText().toString();
                count = mCountText.getText().toString();
                pc = mProvinceCityText.getText().toString();
                mcc = mMccText.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    IToast.getIToast().showIToast("请输入还款金额");
                    return;
                }
                if (TextUtils.isEmpty(eTime)) {
                    IToast.getIToast().showIToast("请选择还款时间");
                    return;
                }
                if (TextUtils.isEmpty(count)) {
                    IToast.getIToast().showIToast("请选择消费次数");
                    return;
                }
                if (TextUtils.isEmpty(pc)) {
                    IToast.getIToast().showIToast("请输选择商户地区");
                    return;
                }
                if (TextUtils.isEmpty(mcc)) {
                    IToast.getIToast().showIToast("请输选择消费商户");
                    return;
                }
                priviewPlain();
                break;
        }
    }

    private void setCardView(CreditRes.Info1 dd) {
        ImageView img = (ImageView) findViewById(R.id.img);
        ImageView itemBankLogo = (ImageView) findViewById(R.id.bank_logo_img);
        TextView tv_billDay = (TextView) findViewById(R.id.tv_billDay);
        TextView itemUpdate = (TextView) findViewById(R.id.update_tx);
        TextView itemBankName = (TextView) findViewById(R.id.bank_name_tx);
        TextView tv_repaymentDay = (TextView) findViewById(R.id.tv_repaymentDay);
        TextView getItemBankNo = (TextView) findViewById(R.id.bank_card_tx);

        Glide.with(context).load(dd.getBankBack()).into(img);
        Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo);
        itemBankName.setText(dd.getBankName());
        tv_billDay.setText(dd.getBillDay());
        tv_repaymentDay.setText(dd.getRepaymentDay());
        cardNo = dd.getAccount();
        getItemBankNo.setText(" ****    ****    ****    " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
        itemUpdate.setVisibility(View.GONE);
    }

//    private void timeSelect(final int type) {
//        keyboardHide(findViewById(R.id.layout));
//        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {
//                if (type == 1) {
//                    sTime = getDateStr2(date);
//                    Long LcTime = Long.parseLong(cTime);
//                    Long LsTime = Long.parseLong(sTime);
//                    if (LsTime < LcTime) {
//                        IToast.getIToast().showIToast("请选择正确时间");
//                        return;
//                    }
//                    mStartTimeText.setText(getDateStr(date));
//                }
//                if (type == 2) {
//                    eTime = getDateStr2(date);
//                    Long LcTime = Long.parseLong(cTime);
//                    Long LeTime = Long.parseLong(eTime);
//                    if (LeTime < LcTime) {
//                        IToast.getIToast().showIToast("请选择正确时间");
//                        return;
//                    }
//                    mEndTimeText.setText(getDateStr(date));
//                }
//            }
//        }).setTitleText("选择时间").setCancelText("取消").setSubmitText("完成").build();
//        pvTime.show();
//    }
//
//    public static String getDateStr(Date date) {
////        String format = "yyyy-MM-dd HH:mm:ss";
//        String format = "yyyy-MM-dd";
//        SimpleDateFormat formatter = new SimpleDateFormat(format);
//        return formatter.format(date);
//    }
//
//    public static String getDateStr2(Date date) {
////        String format = "yyyy-MM-dd HH:mm:ss";
//        String format = "yyyyMMdd";
//        SimpleDateFormat formatter = new SimpleDateFormat(format);
//        return formatter.format(date);
//    }


    private void priviewPlain() {
        showDialog("加载中...");
        if (count.contains("一笔消费")){
            count = "1";
        }else if (count.contains("两笔消费")){
            count = "2";
        }else {
            count = "3";
        }
        OkHttpUtils.post()
                .url(Constants.PREVIEW_REPAY_PLAN1)
                .headers(PublicParam.getParam())
                .params(Params.previewPlanDataParams1(channel.getType(), card.getId(), money, eTime, count, pc, mccCode))
                .build().execute(new GenericsCallback<PriviewPlainRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(PriviewPlainRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mIntent = new Intent(context, HKPlainPreviewActivity.class);
                        mIntent.putExtra("channelId", channel.getType());
                        mIntent.putExtra("parentOrder", response.getParentOrder());
                        mIntent.putExtra("fee", response.getAllpayfee());
                        mIntent.putExtra("payfee", response.getPayfee());
                        mIntent.putExtra("downDraw", response.getDownDraw());
                        mIntent.putExtra("zzj", response.getDeposit_xin());
                        mIntent.putExtra("money", money);
                        mIntent.putExtra("pc", pc);
                        mIntent.putExtra("card", card);
                        mIntent.putExtra("mccName", mMccText.getText().toString());
                        mIntent.putExtra("mccCode", mccCode);
                        mIntent.putExtra("mccList", (ArrayList<MccRes.Info1>)mccList);
                        mIntent.putExtra("plain", (ArrayList<PriviewPlainRes.Info1>) response.getIndex_data());
                        startActivity(mIntent);
                    } else {
                        if (response.getCode().equals("0015")) {
                            mIntent = new Intent(context, QQWBViewActivity.class);
                            mIntent.putExtra("title", "绑定卡片");
                            mIntent.putExtra("url", response.getReturnUrl());
                            startActivity(mIntent);
                        } else {
                            IToast.getIToast().showIToast(response.getMsg());
                            GoLogin.goLogin(response.getCode());
                        }
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }


    private void getRQ() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.REPAY_DAY)
                .headers(PublicParam.getParam())
                .params(Params.rq(card.getId()))
                .build().execute(new GenericsCallback<RQRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(RQRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        String rq = response.getData();
                        if (!TextUtils.isEmpty(rq)) {
                            String[] rqArr = rq.split("\\,");
                            List<RQBean> rqList = new ArrayList<RQBean>();
                            rqList.clear();
                            RQBean rqBean;
                            for (int i = 0; i < rqArr.length; i++) {
                                rqBean = new RQBean();
                                rqBean.setTextNo(rqArr[i]);
                                rqBean.setTextSelectFlag(false);
                                rqList.add(rqBean);
                            }
                            if (rqPop==null) {
                                rqPop = new RQPop(context, rqList, new RQPop.IRQListener() {
                                    @Override
                                    public void rq(RQPop dialog, String rq) {
                                        dialog.dismiss();
                                        mEndTimeText.setText(rq);
                                    }
                                });
                            }
                            rqPop.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                        }
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }


    private void getMcc() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.MCC)
                .headers(PublicParam.getParam())
                .params(Params.mcc(channel.getType()))
                .build().execute(new GenericsCallback<MccRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(MccRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mccList.clear();
                        if (response.getData() != null && response.getData().size() > 0) {
                            mccList = response.getData();
                            mMccText.setText(mccList.get(0).getMccName());
                            mccCode = mccList.get(0).getMcc();
                        } else {
                            mccCode = "-1";
                            mMccText.setText("默认");
                        }
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }

    private List<String> mccNameData = new ArrayList<String>();

    private void hkMccSelect(final List<MccRes.Info1> data) {
        keyboardHide(findViewById(R.id.layout));
        mccNameData.clear();
        for (int i = 0; i < data.size(); i++) {
            mccNameData.add(data.get(i).getMccName());
        }
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = mccNameData.get(options1);
                mccCode = data.get(options1).getMcc();
                mMccText.setText(str);
            }
        }).setTitleText("选择商户").setCancelText("取消").setSubmitText("完成").build();
        pvOptions.setPicker(mccNameData);
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.show();
    }


    private List<String> countList = new ArrayList<String>();

    private void hkCountSelect() {
        keyboardHide(findViewById(R.id.layout));
        countList.clear();
        for (int i = 2; i <= 6; i+=2) {
            switch (i){
                case 2:
                    countList.add("一笔消费，一笔还款");
                    break;
                    case 4:
                        countList.add("两笔消费，一笔还款");
                    break;
                    case 6:
                        countList.add("三笔消费，一笔还款");
                    break;
            }
        }
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = countList.get(options1);
                mCountText.setText(str);
            }
        }).setTitleText("消费次数").setCancelText("取消").setSubmitText("完成").build();
        pvOptions.setPicker(countList);
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.show();
    }


    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        handler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    private void showPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                province = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
                mProvinceCityText.setText(province +" "+ city);
            }
        }).setTitleText("商户地区").setCancelText("取消").setSubmitText("完成").build();
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/

        //设置显示当前位置地址；
        for (int i=0;i<options1Items.size();i++){
            String province = options1Items.get(i).getName();
            if (MyLocationListener.getProvince().contains(province)){
                selectProvince = i;
//               Toast.makeText(this, province+i, Toast.LENGTH_SHORT).show();
                for (int j=0;j<options1Items.get(i).getCityList().size();j++){
                    String city = options1Items.get(i).getCityList().get(j).getName();
                    if (MyLocationListener.getCity().contains(city)){
//                       Toast.makeText(this, options1Items.get(i).getCityList().get(j).getName()+j, Toast.LENGTH_SHORT).show();
                        selectCity = j;
                        break;
                    }
                }
                break;
            }
        }
        pvOptions.setSelectOptions(selectProvince, selectCity, 0);
        pvOptions.show();
    }

}
