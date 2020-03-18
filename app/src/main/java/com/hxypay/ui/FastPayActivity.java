package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxypay.App;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.baiduMap.MyLocationListener;
import com.hxypay.bean.JsonBean;
import com.hxypay.customview.CardPop;
import com.hxypay.customview.IToast;
import com.hxypay.customview.KeyPop1;
import com.hxypay.response.ChannelRes;
import com.hxypay.response.CreditRes;
import com.hxypay.response.DepositRes;
import com.hxypay.response.FastPayRes;
import com.hxypay.response.PoundageRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GetJsonDataUtil;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.util.SharedPreStorageMgr;
import com.hxypay.util.SoftKeyBoardListener;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FastPayActivity extends BaseActivity {
    private RelativeLayout mCardLayout1;
    private RelativeLayout mCardLayout;
    private List<CreditRes.Info1> mCreditData = new ArrayList<CreditRes.Info1>();
    private DepositRes.Info1 depositCard;
    private CreditRes.Info1 creditCard;
    private EditText mAmoutEt;
    private Button mComBtn;

    private String amout, cardId, payPwd,channel;
    private KeyPop1 keyPop1;
    private ImageView mChangeImg;
    private TextView tv_shouxf;
    private LinearLayout ll_shouxf;
    private TextView provice_city_tx,provice_tondao_tx;

    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private Thread thread;
    private boolean isLoaded = false;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
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
    private OptionsPickerView<String> pvOptions;
    private ChannelRes res;
    private LinearLayout ll_provice_city;
    private ScrollView mScrollView;
    private LinearLayout ll_addCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_fastpay);
        //请求下位置；
        App.getApp().getLocationClient().requestLocation();
        initHeadView(this, R.drawable.icon_back_r, "个人收款", 0,"记录");
        //键盘监听；
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
//                Toast.makeText(context, "显示键盘", Toast.LENGTH_SHORT).show();
                ll_shouxf.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {
//                Toast.makeText(context, "隐藏键盘", Toast.LENGTH_SHORT).show();
                request_poundage();
                if (TextUtils.isEmpty(mAmoutEt.getText().toString().trim())) {
                    ll_shouxf.setVisibility(View.GONE);
                }else {
                    ll_shouxf.setVisibility(View.VISIBLE);
                }
            }
        });
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        creditCardData();
    }

    private void initView() {
        handler.sendEmptyMessage(MSG_LOAD_DATA);
        depositCard = (DepositRes.Info1) getIntent().getSerializableExtra("depositCard");
        creditCard = (CreditRes.Info1) getIntent().getSerializableExtra("creditCard");

        mCardLayout1 = (RelativeLayout) findViewById(R.id.card_layout_1);
        ll_addCard = findViewById(R.id.ll_addCard);
        mCardLayout = (RelativeLayout) findViewById(R.id.card_layout);
        mChangeImg = (ImageView) findViewById(R.id.c_img);
        tv_shouxf = (TextView) findViewById(R.id.tv_shouxf);
        ll_shouxf = findViewById(R.id.ll_shouxf);

        mAmoutEt = (EditText) findViewById(R.id.amout_et);
        provice_city_tx = findViewById(R.id.provice_city_tx);
        provice_tondao_tx = findViewById(R.id.provice_tondao_tx);
        ll_provice_city = findViewById(R.id.ll_provice_city);
        setMoneyEditText(mAmoutEt);
        mComBtn = (Button) findViewById(R.id.btn_com);
        mComBtn.setOnClickListener(this);
        mChangeImg.setOnClickListener(this);
        provice_city_tx.setOnClickListener(this);
        mCardLayout1.setOnClickListener(this);
        ll_addCard.setOnClickListener(this);
        provice_tondao_tx.setOnClickListener(this);

        mScrollView = (ScrollView)findViewById(R.id.svid);
        mScrollView.post(new Runnable() {
            public void run() {
                mScrollView.scrollTo(0, 1000);
            }
        });

        setCreditCardView(creditCard);
        setDepositCardView(depositCard);
        ditch();

        tv_right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(FastPayActivity.this,BillActivity.class);
                startActivity(mIntent);
            }
        });
    }

    //获取渠道
    private void ditch() {
        showDialog("加载中...");
        Map<String, String> params = new HashMap<String, String>();
        params.put("passageway","1");
        OkHttpUtils.post()
                .url(Constants.FASTPAY2)
                .headers(PublicParam.getParam())
                .params(params)
                .build().execute(new GenericsCallback<ChannelRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(ChannelRes response, int id) {
                dismissDialog();
//                Toast.makeText(context,response.getData().size()+response.getData().get(0).getType()+response.getData().get(0).getPay_classname() , Toast.LENGTH_SHORT).show();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        provice_tondao_tx.setText(response.getData().get(0).getPay_classname());
                        channel = response.getData().get(0).getType();
                        provice_city_tx.setOnClickListener(FastPayActivity.this);
                        res = response;
                    } else {
                        Toast.makeText(context, "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }

    @Override
    public void onRightListener() {
        super.onRightListener();
        mRightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(context, CardAddOcrActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.c_img:
            case R.id.card_layout_1:
                keyboardHide(findViewById(R.id.layout));
                if (mCreditData != null && mCreditData.size() > 0) {
                    CardPop cardPop = new CardPop(context, screenHeight, mCreditData, new CardPop.ICardListener() {
                        @Override
                        public void card(CardPop dialog, CreditRes.Info1 card) {
                            dialog.dismiss();
                            setCreditCardView(card);
                        }
                    });
                    cardPop.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                } else {
                    IToast.getIToast().showIToast("请添加信用卡");
                }
                break;
            case R.id.ll_addCard:
                mIntent = new Intent(context, CardAddOcrActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_com:
                amout = mAmoutEt.getText().toString();
                if (TextUtils.isEmpty(cardId)) {
                    IToast.getIToast().showIToast("请先添加信用卡片");
                    return;
                }
                if (TextUtils.isEmpty(amout)) {
                    IToast.getIToast().showIToast("请输入转账金额");
                    return;
                }
                if (ll_provice_city.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(provice_city_tx.getText().toString())) {
                        IToast.getIToast().showIToast("请选择地区");
                        return;
                    }
                }
                keyboardHide(findViewById(R.id.layout));
                keyPop1 = new KeyPop1(context, new KeyPop1.KeyPopListener() {
                    @Override
                    public void onClick(String pwd) {
                        keyPop1.dismiss();
                        payPwd = pwd;
                        fastPay();
                    }

                    @Override
                    public void diss() {
                        keyPop1.dismiss();
                    }
                });
                keyPop1.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                break;

            //选中地区；
            case R.id.provice_city_tx:
                showPickerView();
                break;
                //快捷通道；
            case R.id.provice_tondao_tx:
                if (res!=null) {
                    showSelecet(res);
                }else {
                    Toast.makeText(context, "没有更多通道", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private List<String> countList = new ArrayList<String>();
    /**
     * 通道选择
     */
    private void showSelecet(final ChannelRes response) {
        keyboardHide(findViewById(R.id.layout));
        countList.clear();
        for (int i = 0; i <response.getData().size(); i++) {
            countList.add( response.getData().get(i).getPay_classname());
        }
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = countList.get(options1);
                if (options1==0){
                    ll_provice_city.setVisibility(View.GONE);
                }else {
                    ll_provice_city.setVisibility(View.VISIBLE);
                }
                provice_tondao_tx.setText(str);
                channel = response.getData().get(options1).getType();
            }
        }).setTitleText("通道选择").setCancelText("取消").setSubmitText("完成").build();
        pvOptions.setPicker(countList);
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.show();
    }


    private void fastPay() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.FASTPAY)
                .headers(PublicParam.getParam())
                .params(Params.fastPayParams(cardId, amout, payPwd,channel,provice_city_tx.getText().toString(), SharedPreStorageMgr.getIntance().getStringValue(Constants.UID)))
                .build().execute(new GenericsCallback<FastPayRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                Toast.makeText(context, "==="+e.getMessage(), Toast.LENGTH_SHORT).show();
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(FastPayRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
//                       pay_info 支付地址
//                        pay_code 支付参数
//                        注：支付方式是 如果支付参数不为空的话把支付参数拼接到支付链接地址后面进行支付
//                        如果支付参数为空  直接打开支付链接进行支付
//                        1. 处理方式：前端将pay_code里面参数进行URLDecoder解码，拼接成form表单,发送POST 到pay_info支付地址
//                        2. pay_ code 里面的参数要循环处理,参数会随着通道变化
//                        3. pay_info支付地址不是固定的, 会随着通道变
                        if (response.getIsUrl() == 1) {
                            String payParms = response.getData().getPay_code();
                            Gson gson = new Gson();
                            Map<String, String> map = gson.fromJson(payParms, new TypeToken<Map<String, String>>() {
                            }.getType());
                            String payUrl = response.getData().getPay_info();
                            if (map != null && map.size() > 0) {//如果支付参数为空  直接打开支付链接进行支付
                                List<String> k = new ArrayList<String>();
                                k.clear();
                                Set<String> keys = map.keySet();// 得到全部的key
                                Iterator<String> iter = keys.iterator();
                                while (iter.hasNext()) {
                                    String str = iter.next();
                                    k.add(str);
                                }
                                for (int i = 0; i < k.size(); i++) {
                                    if (i == 0) {
                                        payUrl += "?" + k.get(i) + "=" + toURLEncoded(map.get(k.get(i)));
                                    } else {
                                        payUrl += "&" + k.get(i) + "=" + toURLEncoded(map.get(k.get(i)));
                                    }
                                }
                            }
                            mIntent = new Intent(context, FastPayWBViewActivity.class);
                            mIntent.putExtra("title", "个人收款");
                            mIntent.putExtra("url", payUrl);
                            startActivity(mIntent);
                        } else {
                            String payUrl = response.getReturnUrl();
                            if (TextUtils.isEmpty(payUrl)){
                                Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                mIntent = new Intent(context, FastPayWBViewActivity.class);
                                mIntent.putExtra("title", "个人收款");
                                mIntent.putExtra("url", payUrl);
                                startActivity(mIntent);
                            }
                        }
                    }
                    else if (response.getCode().equals("0015")){
                        if (!TextUtils.isEmpty(response.getReturnUrl())){
                            mIntent = new Intent(context, FastPayWBViewActivity.class);
                            mIntent.putExtra("title", "绑定卡片");
                            mIntent.putExtra("url", response.getReturnUrl());
                            startActivity(mIntent);
                        }
                    }
                    else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }

    public String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {

        }
        return "";
    }

    private void creditCardData() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.CREDIT_CARD_LIST)
                .headers(PublicParam.getParam())
                .params(Params.cardMgrParams())
                .build().execute(new GenericsCallback<CreditRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(CreditRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mCreditData.clear();
                        mCreditData = response.getData();
                        if (mCreditData.size() > 0) {
                            initHeadView(FastPayActivity.this, R.drawable.icon_back_r, "个人收款", 0,"记录");
                        } else {
                            initHeadView(FastPayActivity.this, R.drawable.icon_back_r, "个人收款", R.drawable.add,null);
                        }
                    } else {
                        if (response.getCode().equals("0004")) {
                            initHeadView(FastPayActivity.this, R.drawable.icon_back_r, "个人收款", R.drawable.add,null);
                        }
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }


    private void setCreditCardView(CreditRes.Info1 dd) {
        if (dd!=null && !TextUtils.isEmpty(dd.getId())&& !TextUtils.isEmpty(dd.getAccount())) {
            cardId = dd.getId();
            mCardLayout1.setVisibility(View.VISIBLE);
            ll_addCard.setVisibility(View.GONE);
            ImageView img1 = (ImageView) findViewById(R.id.img_1);
            ImageView itemBankLogo1 = (ImageView) findViewById(R.id.bank_logo_img_1);
            ImageView itemBankLogo1_1 = (ImageView) findViewById(R.id.bank_logo_img1_1);
            TextView itemUpdate1 = (TextView) findViewById(R.id.update_tx_1);
            TextView itemBankName1 = (TextView) findViewById(R.id.bank_name_tx_1);
            TextView itemBankType1 = (TextView) findViewById(R.id.bank_type_tx_1);
            TextView getItemBankNo1 = (TextView) findViewById(R.id.bank_card_tx_1);

            Glide.with(context).load(dd.getBankBack()).into(img1);
            Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo1);
            Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo1_1);
            itemBankName1.setText(dd.getBankName());
            itemBankType1.setText("信用卡");
            String cardNo = dd.getAccount();
            getItemBankNo1.setText(" ****    ****    ****    " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
            itemUpdate1.setVisibility(View.GONE);
        }else {
            ll_addCard.setVisibility(View.VISIBLE);
            mCardLayout1.setVisibility(View.GONE);
        }
    }


    private void setDepositCardView(DepositRes.Info1 dd) {
        mCardLayout.setVisibility(View.VISIBLE);
        ImageView itemBankLogo = (ImageView) findViewById(R.id.bank_logo_img);
        ImageView itemBankLogo1 = (ImageView) findViewById(R.id.bank_logo_img1);
        TextView itemUpdate = (TextView) findViewById(R.id.update_tx);
        TextView itemBankName = (TextView) findViewById(R.id.bank_name_tx);
        TextView itemBankType = (TextView) findViewById(R.id.bank_type_tx);
        TextView getItemBankNo = (TextView) findViewById(R.id.bank_card_tx);

        Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo);
        Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo1);
        itemBankName.setText(dd.getBankName());
        itemBankType.setText("储蓄卡");
        String cardNo = dd.getAccount();
        getItemBankNo.setText(" ****    ****    ****    " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
        itemUpdate.setVisibility(View.GONE);
    }


    private void request_poundage() {
        String money = mAmoutEt.getText().toString().trim();
        if (TextUtils.isEmpty(money)){
            return;
        }
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.POUNDAGE)
                .headers(PublicParam.getParam())
                .params(Params.request_poundageParams(money))
                .build().execute(new GenericsCallback<PoundageRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(PoundageRes poundageRes, int id) {
                dismissDialog();
                if ( poundageRes.getData() != null) {
                    tv_shouxf.setText(poundageRes.getData());
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
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
                String province = options1Items.get(options1).getPickerViewText();
                String city = options2Items.get(options1).get(options2);
                provice_city_tx.setText(province +" "+ city);
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
