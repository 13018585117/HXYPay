package com.hxypay.fragment;


import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.adapter.CreditListAdapter;
import com.hxypay.customview.CardSettingPop;
import com.hxypay.customview.IToast;
import com.hxypay.customview.TipDialog;
import com.hxypay.response.CreditRes;
import com.hxypay.response.PublicRes;
import com.hxypay.ui.CardMgrActivity;
import com.hxypay.ui.CardUpdateActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;


public class CreditCardFragment extends BaseFragment {

    private ListView mListView;
    private CreditListAdapter mAdapter;
    private List<CreditRes.Info1> mData = new ArrayList<CreditRes.Info1>();
    private CreditRes.Info1 myCreditCard;
    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_cards;
    }

    @Override
    protected void initViews() {
        mListView = (ListView) findViewById(R.id.list_data);

        mAdapter = new CreditListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null && mData.size() > 0) {
                    Intent mIntent = new Intent(getActivity(), CardMgrActivity.class);
                    mIntent.putExtra("card", mData.get(position));
                    startActivity(mIntent);
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                myCreditCard = mData.get(position);

                CardSettingPop mCardSettingPop = new CardSettingPop(getActivity(), new CardSettingPop.ICardSettingListener() {
                    @Override
                    public void updateCard(CardSettingPop dialog) {
                        dialog.dismiss();
                        Intent mIntent = new Intent(getActivity(), CardUpdateActivity.class);
                        mIntent.putExtra("card", myCreditCard );
                        startActivity(mIntent);
                    }

                    @Override
                    public void deleteCard(CardSettingPop dialog) {
                        dialog.dismiss();
                        delCardTipDilog(myCreditCard.getAccount());
                    }
                });
                mCardSettingPop.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);

                return true;
            }
        });
    }

    private void delCardTipDilog(String cardNo){
        TipDialog tipDialog = new TipDialog(getActivity(), "提示", "您确定删除卡片：" + cardNo, new String[]{"取消", "确认"}, new TipDialog.OnCustomDialogListener() {
            @Override
            public void ok(TipDialog dialog) {
                dialog.dismiss();
                deleteCard();
            }

            @Override
            public void cancle(TipDialog dialog) {
                dialog.dismiss();
            }
        });
        tipDialog.show();
    }

    private void deleteCard(){
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.DEL_CARD)
                .headers(PublicParam.getParam())
                .params(Params.delCardParams(myCreditCard.getId()))
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
                        IToast.getIToast().showIToast("删除成功");
                        initData();
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
    public void onResume() {
        super.onResume();
        initData();
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

}
