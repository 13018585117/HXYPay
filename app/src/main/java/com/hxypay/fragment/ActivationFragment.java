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
import android.widget.Toast;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.adapter.ActivationListAdapter;
import com.hxypay.adapter.FragmentExpandableListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.response.FriendsRes;
import com.hxypay.ui.FriendsActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class ActivationFragment extends BaseFragment {

    private ExpandableListView mListView;
    private FragmentExpandableListAdapter mAdapter;
    private static final int MY_PERMISSION_REQUEST_CALL_PHONE = 0;
    private String phone;
    private List<FriendsRes.Info1> indirect;
    private List<FriendsRes.Info1> direct;

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



    private void initData(){
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
                        if (response.getData().getUser()!=null) {
                            indirect = response.getData().getUser().getIndirect();
                            direct = response.getData().getUser().getDirect();
                            String datas[][] = new String[1][];
                            HashMap<Integer, Object> listItem = new HashMap<>();
                            for (int i = 0; i < direct.size(); i++) {
                                listItem.put(i, direct.get(i));
                            }
                            listItem.get(1);
                        }
                        mAdapter = new FragmentExpandableListAdapter(getActivity(), direct, indirect);
                        mListView.setAdapter(mAdapter);
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
