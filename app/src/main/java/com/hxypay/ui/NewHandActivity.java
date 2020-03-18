package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.CommonAdapter;
import com.hxypay.adapter.MyViewHolder;
import com.hxypay.response.NewHeadRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewHandActivity extends BaseActivity {
    @BindView(R.id.lv_newHand)
    ListView lv_newHand;
    private CommonAdapter<NewHeadRes.Inif> inifCommonAdapter;
    List<NewHeadRes.Inif> datas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hand);
        ButterKnife.bind(this);
        initHeadView(this, R.drawable.icon_back_r, "新手教程", 0,null);
        initView();
        initData();
    }

    private void initView() {
        inifCommonAdapter = new CommonAdapter<NewHeadRes.Inif>(this, datas, R.layout.item_new_hand) {
            @Override
            public void convert(MyViewHolder holder, NewHeadRes.Inif inif, int position) {
                holder.getTextVie(R.id.tv_title,inif.getName());
                holder.getTextVie(R.id.tv_content,inif.getRemarks());
                holder.getImageUrl(R.id.iv_icon2, inif.getIcon());
            }
        };
        lv_newHand.setAdapter(inifCommonAdapter);
        lv_newHand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (datas!=null&&datas.size()>=position) {
                    mIntent = new Intent(context, LongGraphWBViewActivity.class);
                    mIntent.putExtra("title", datas!=null?datas.get(position).getName():"新手教程");
                    mIntent.putExtra("content",datas!=null?datas.get(position).getRemarks():"新手教程");
                    mIntent.putExtra("url", datas.get(position).getSrc());
                    startActivity(mIntent);
                }
            }
        });
    }

    private void initData() {
         showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.NEW_HAND)
                .headers(PublicParam.getParam())
                .params(Params.popNew())
                .build().execute(new GenericsCallback<NewHeadRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
            }

            @Override
            public void onResponse(NewHeadRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData()!=null){
                            datas = response.getData();
                            inifCommonAdapter.setUpdateData(datas);
                        }

                    } else {
                        GoLogin.goLogin(response.getCode());
                    }
                }
            }
        });
    }
}
