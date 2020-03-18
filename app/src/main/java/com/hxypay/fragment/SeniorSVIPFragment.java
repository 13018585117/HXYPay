package com.hxypay.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.adapter.FragmentExpandableListAdapter;
import com.hxypay.adapter.NotActivationListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.response.FriendsRes;
import com.hxypay.ui.FriendsActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;

public class SeniorSVIPFragment extends BaseFragment {

    private ExpandableListView mListView;
    private NotActivationListAdapter mAdapter;
    private static final int MY_PERMISSION_REQUEST_CALL_PHONE = 0;
    private String phone;
    private List<FriendsRes.Info1> direct;
    private List<FriendsRes.Info1> indirect;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_friends;
    }

    @Override
    protected void initViews() {
        mListView = findViewById(R.id.list_data);
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    if (direct!=null) {
                        phone = direct.get(childPosition).getPhoneNum();
                    }
                }else if (groupPosition == 1){
                    if (indirect!=null) {
                        phone = indirect.get(childPosition).getPhoneNum();
                    }
                }
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_REQUEST_CALL_PHONE);
                } else {
                    callPhone();
                }
                return true;
            }
        });

        initData();
    }
    private void initData() {
//        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.MY_MERCHANT_LIST)
                .headers(PublicParam.getParam())
                .params(Params.myMerchantParams())
                .build().execute(new GenericsCallback<FriendsRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(FriendsRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData().getSVIP()!=null) {
                            direct = response.getData().getSVIP().getDirect();
                            indirect = response.getData().getSVIP().getIndirect();
                        }
                        FragmentExpandableListAdapter info1FragmentExpandableListAdapter = new FragmentExpandableListAdapter(getActivity(), direct, indirect);
                        mListView.setAdapter(info1FragmentExpandableListAdapter);
                        mListView.expandGroup(0);
                        mListView.expandGroup(1);
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


    //只有第一次进行权限询问的时候才会调用这个方法，所以只有第一次调用的时候才会出现“哪里” 之后拨打电话都是这里
    //如果把这里的callPhone注释掉的话 第一次询问以后不会打电话
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                //Permission Denied
                IToast.getIToast().showIToast("请手动打开拨打电话权限");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callPhone(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }
    @Override
    public void onPause() {
        super.onPause();
        dismissDialog();
    }
}
