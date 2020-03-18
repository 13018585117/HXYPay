package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.HKPreviewPlainListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.customview.KeyPop1;
import com.hxypay.response.CreditRes;
import com.hxypay.response.MccRes;
import com.hxypay.response.PriviewPlainRes;
import com.hxypay.response.PublicRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HKPlainPreviewActivity extends BaseActivity implements HKPreviewPlainListAdapter.MccSelectListener {

    private List<PriviewPlainRes.Info1> plain;
    private List<MccRes.Info1> mccList;
    private CreditRes.Info1 card;
    private TextView mOpenMemberText, mFeeText,mFee_jiesuan, mZZJText;
    private OptionsPickerView pvOptions;
    private Map<Integer,String> selectMap = new HashMap<>();


    private ListView mListView;
    private HKPreviewPlainListAdapter mAdapter;

    private Button mComBtn;

    private String channelId, parentOrder, allFee, zzj, money, pc, payPwd;
    private KeyPop1 keyPop1;

    private String mccName, mccCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hk_plain_preview);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "智能还款", 0,null);
        channelId = getIntent().getExtras().getString("channelId");
        parentOrder = getIntent().getExtras().getString("parentOrder");
        allFee = getIntent().getExtras().getString("fee");
        String payfee = getIntent().getExtras().getString("payfee");
        String downDraw = getIntent().getExtras().getString("downDraw");
        zzj = getIntent().getExtras().getString("zzj");
        money = getIntent().getExtras().getString("money");
        pc = getIntent().getExtras().getString("pc");
        card = (CreditRes.Info1) getIntent().getSerializableExtra("card");
        mccName = getIntent().getExtras().getString("mccName");
        mccCode = getIntent().getExtras().getString("mccCode");
        mccList = (List<MccRes.Info1>) getIntent().getSerializableExtra("mccList");
        plain = (List<PriviewPlainRes.Info1>) getIntent().getSerializableExtra("plain");

        mOpenMemberText = (TextView) findViewById(R.id.open_member_tx);
        mFeeText = (TextView) findViewById(R.id.fee_tx);
        mFee_jiesuan = (TextView) findViewById(R.id.fee_jiesuan);
        mZZJText = (TextView) findViewById(R.id.zzj_tx);
        mComBtn = (Button) findViewById(R.id.btn_com);

        mFeeText.setText(payfee + "元");
        mZZJText.setText(zzj + "元");
        mFee_jiesuan.setText(downDraw + "元");

        mListView = (ListView) findViewById(R.id.list_data);
        mAdapter = new HKPreviewPlainListAdapter(context, plain,selectMap);
        mListView.setAdapter(mAdapter);
        mAdapter.setMccSelectListener(this);

        mOpenMemberText.setOnClickListener(this);
        mComBtn.setOnClickListener(this);
    }

    @Override
    public void mccSelect(TextView mccText, String orderId,int position) {
        hkMccSelect(mccText, orderId,position);
    }

    private List<String> mccNameData = new ArrayList<String>();

    private void hkMccSelect(final TextView mccText, final String orderId, final int position) {
        keyboardHide(findViewById(R.id.layout));
        mccNameData.clear();
        for (int i = 0; i < mccList.size(); i++) {
            mccNameData.add(mccList.get(i).getMccName());
        }
        pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = mccNameData.get(options1);
                mccCode = mccList.get(options1).getMcc();
                mccText.setText(str);
                selectMap.put(position,str);
                updateMcc(orderId, mccCode);
            }
        }).setTitleText("选择商户").setCancelText("取消").setSubmitText("完成").build();
        pvOptions.setPicker(mccNameData);
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.show();
    }


    private void updateMcc(final String orderId, final String mccCode) {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.UPDATE_MCC)
                .headers(PublicParam.getParam())
                .params(Params.updateMcc(orderId, mccCode))
                .build().execute(new GenericsCallback<PublicRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
            }

            @Override
            public void onResponse(PublicRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {

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


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.open_member_tx:
                IToast.getIToast().showIToast("开通会员");
                break;
            case R.id.btn_com:
                keyboardHide(findViewById(R.id.layout));
                keyPop1 = new KeyPop1(context, new KeyPop1.KeyPopListener() {
                    @Override
                    public void onClick(String pwd) {
                        keyPop1.dismiss();
                        payPwd = pwd;
                        comPlain();
                    }

                    @Override
                    public void diss() {
                        keyPop1.dismiss();
                    }
                });
                keyPop1.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                break;
        }
    }


    private void comPlain() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.CREATE_REPAY_PLAN)
                .headers(PublicParam.getParam())
                .params(Params.createPlanParams(channelId, card.getId(), money, parentOrder, pc, allFee, payPwd))
                .build().execute(new GenericsCallback<PublicRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(PublicRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mIntent = new Intent(context, HKPlainOkActivity.class);
                        mIntent.putExtra("resCode", response.getCode());
                        mIntent.putExtra("resMessage", response.getMsg());
                        mIntent.putExtra("money", money);
                        mIntent.putExtra("card", card);
                        startActivity(mIntent);
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


}
