package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.HKCardListAdapter;
import com.hxypay.customview.DialTableView;
import com.hxypay.customview.DialView;
import com.hxypay.customview.IToast;
import com.hxypay.customview.KeyPop;
import com.hxypay.response.CreditRes;
import com.hxypay.response.RouletteRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.view.ListViewForScrollView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HKCardListActivity extends BaseActivity {

    private ListViewForScrollView mListView;
    private HKCardListAdapter mAdapter;
    private List<CreditRes.Info1> mData = new ArrayList<CreditRes.Info1>();
    private int maxMoney = 200000;
    private int defMoney = 10000;
    private int defCount = 2;
    private int defDay = 1;
    private int currentPro = 0;
    private LinearLayout mAddCardLayout;
    private TextView mMoneyTx;
    private DialTableView mZpView;
    private TextView mZzjText, mFeeText;
    private Button mCountBtn, mDayBtn;
    private KeyPop keyPop;
    private List<String> countList = new ArrayList<String>();
    private List<String> dayList = new ArrayList<String>();
    private OptionsPickerView pvOptions;
    private String levelId;
    @BindView(R.id.ll_vipMoney)
    LinearLayout ll_vipMoney;
    @BindView(R.id.ll_hhrMoney)
    LinearLayout ll_hhrMoney;
    @BindView(R.id.ll_svipMoney)
    LinearLayout ll_svipMoney;
    @BindView(R.id.tv_vipMoney)
    TextView tv_vipMoney;
    @BindView(R.id.tv_hhrMoney)
    TextView tv_hhrMoney;
    @BindView(R.id.tv_svipMoney)
    TextView tv_svipMoney;
    private DecimalFormat fnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hk_cardlist);
        ButterKnife.bind(this);
        initView();
        fnum = new DecimalFormat("##0.00"); //保留2位
        initData();
    }


    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "智能还款", 0,"还款记录");
        levelId = getIntent().getStringExtra("level");
        if (levelId.equals("3")){
            ll_vipMoney.setVisibility(View.GONE);
            ll_hhrMoney.setVisibility(View.GONE);
        }
        mMoneyTx = (TextView) findViewById(R.id.money_tx);
        mMoneyTx.setText(defMoney + "");

        mCountBtn = (Button) findViewById(R.id.count_btn);
        mCountBtn.setText(defCount + "");
        mDayBtn = (Button) findViewById(R.id.day_btn);
        mDayBtn.setText(defDay + "");
        mZzjText = (TextView) findViewById(R.id.zzj_tx);
        mFeeText = (TextView) findViewById(R.id.fee_tx);
        mZpView = (DialTableView) findViewById(R.id.zp_view);
        mZpView.setEnabled(false);
        moneySetting(defMoney);


        mListView =  findViewById(R.id.list_data);
        mAddCardLayout =(LinearLayout) findViewById(R.id.addcard_layout);
        mAdapter = new HKCardListAdapter(context, null);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null && mData.size() > 0) {
                    Intent mIntent = new Intent(context, HKChannelActivity.class);
                    mIntent.putExtra("card", mData.get(position));
                    startActivity(mIntent);
                }
            }
        });

        mAddCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(context,CardAddOcrActivity.class);
                startActivity(mIntent);
            }
        });
        tv_right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData!=null && mData.size()>0) {
                    mIntent = new Intent(context, TradeBillActivity.class);
                    mIntent.putExtra("cards", (Serializable) mData);
                    startActivity(mIntent);
                }else {
                    IToast.getIToast().showIToast("暂无记录");
                }
            }
        });

        mZpView.setOnDegreesChangedListener(new DialView.DegreesChangedListener() {
            @Override
            public void onDegreesChanged(float newDegrees) {
                int newD = (int) newDegrees;
//                IToast.getIToast().showIToast((int) newDegrees + "");
                if (newD == 0) {
                    mMoneyTx.setText(defMoney + "");
                    setZzjAndFee(defMoney, Integer.parseInt(mCountBtn.getText().toString()), Integer.parseInt(mDayBtn.getText().toString()));
                } else if (newD == 180) {
                    mMoneyTx.setText(maxMoney + "");
                    setZzjAndFee(maxMoney, Integer.parseInt(mCountBtn.getText().toString()), Integer.parseInt(mDayBtn.getText().toString()));
                } else {
                    int m = ((maxMoney - defMoney) / 180) * newD + defMoney;
                    mMoneyTx.setText(m + "");
                    setZzjAndFee(m, Integer.parseInt(mCountBtn.getText().toString()), Integer.parseInt(mDayBtn.getText().toString()));
                }
            }
        });
    }
    private void moneySetting(int m) {
        int okMoney;
        if (m == defMoney) {
            okMoney = defMoney;
            currentPro = 0;
        } else if (m == maxMoney) {
            okMoney = maxMoney;
            currentPro = 180;
        } else {
            okMoney = m;
            float a1 = m - defMoney;
            float a2 = maxMoney - defMoney;
            float a3 = a2 / 180;
            float tempCurrPro = a1 / a3;
            Float f = new Float(tempCurrPro);
            currentPro = f.intValue();
        }
        mZpView.setDegreesInt(currentPro);
        mZpView.invalidate();
        setZzjAndFee(okMoney, Integer.parseInt(mCountBtn.getText().toString()), Integer.parseInt(mDayBtn.getText().toString()));
    }
    private void setZzjAndFee(int m, int c, int d) {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.ROULETTE)
                .headers(PublicParam.getParam())
                .params(Params.RouletteParams(levelId,mMoneyTx.getText().toString().trim(),mDayBtn.getText().toString().trim(),mCountBtn.getText().toString().trim()))
                .build().execute(new GenericsCallback<RouletteRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(RouletteRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mFeeText.setText(response.getData().getHandlingFee());
                        mZzjText.setText(""+response.getData().getWorkingFund());
                        tv_vipMoney.setText(response.getData().getSubordinate());
                        tv_hhrMoney.setText(response.getData().getSuperior());
                        tv_svipMoney.setText(response.getData().getSubordinate() +" X 12月 = 省"+ fnum.format( Float.parseFloat(response.getData().getSubordinate()) * 12));
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
        //手续费按金额*0.0065+笔数*天数算，周转金按金额/笔数/天数算。最低金额10000，最高200000。
      /*  mFeeText.setText(String.format("%.2f", (m * 0.0065) + (c * d)));
        mZzjText.setText((m / c / d) + "");*/

    }


    private void initData() {
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
                        mData.clear();
                        mData = response.getData();
                        mAdapter.setData(mData);
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

    @OnClick(R.id.money_tx)
    public void Money_TX(){
        keyPop = new KeyPop(context, mMoneyTx.getText().toString(), new KeyPop.KeyPopListener() {
            @Override
            public void onFinish(String num) {
                keyPop.dismiss();
                mMoneyTx.setText(num);
                moneySetting(Integer.parseInt(num));
            }
        });
        keyPop.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
    }
    @OnClick(R.id.count_btn)
    public void count_btn(){
        countSelect();
    }
    private void countSelect() {
        keyboardHide(findViewById(R.id.layout));
        countList.clear();
        for (int i = 1; i <= 5; i++) {
            countList.add(i + "");
        }
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = countList.get(options1);
                mCountBtn.setText(str);
                moneySetting(Integer.parseInt(mMoneyTx.getText().toString()));
            }
        }).setTitleText("还款次数").setCancelText("取消").setSubmitText("完成").build();
        pvOptions.setPicker(countList);
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.show();
    }
    @OnClick(R.id.day_btn)
    public void day_btn(){
        daySelect();
    }
    private void daySelect() {
        keyboardHide(findViewById(R.id.layout));
        dayList.clear();
        for (int i = 1; i <= 25; i++) {
            dayList.add(i + "");
        }
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = dayList.get(options1);
                mDayBtn.setText(str);
                moneySetting(Integer.parseInt(mMoneyTx.getText().toString()));
            }
        }).setTitleText("还款天数").setCancelText("取消").setSubmitText("完成").build();
        pvOptions.setPicker(dayList);
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.show();
    }
}
