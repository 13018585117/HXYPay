package com.hxypay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.CommonAdapter;
import com.hxypay.adapter.MyViewHolder;
import com.hxypay.response.FellowBusinessRes;

import java.io.Serializable;
import java.util.List;

public class FellowBusinessDetailedDataActivity extends BaseActivity {

    private ListView lv_detailedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fellow_business_detailed_data);
        initView();
        initData();
    }

    private void initData() {
        //type 0为每月交易数据 1为好友交易数据；
        int type = getIntent().getIntExtra("type", 0);
        if (type==0) {
            List<FellowBusinessRes.Inif.MonthTotal.Detail> detail = (List<FellowBusinessRes.Inif.MonthTotal.Detail>) getIntent().getSerializableExtra("data");
            CommonAdapter<FellowBusinessRes.Inif.MonthTotal.Detail> detailCommonAdapter = new CommonAdapter<FellowBusinessRes.Inif.MonthTotal.Detail>(this, detail, R.layout.item_fellowbusiness_detailed_data) {
                @Override
                public void convert(MyViewHolder holder, FellowBusinessRes.Inif.MonthTotal.Detail detail, int position) {
                    TextView tv_item1 = holder.getView(R.id.tv_item1);
                    TextView tv_item2 = holder.getView(R.id.tv_item2);
                    TextView tv_item3 = holder.getView(R.id.tv_item3);
                    tv_item1.setText(detail.getDate());
                    tv_item3.setText("￥"+detail.getMoney());
                }
            };
            lv_detailedData.setAdapter(detailCommonAdapter);
        }else if (type == 1){
            List<FellowBusinessRes.Inif.Friend.Detail> detail = (List<FellowBusinessRes.Inif.Friend.Detail>) getIntent().getSerializableExtra("data");
            CommonAdapter<FellowBusinessRes.Inif.Friend.Detail> detailCommonAdapter = new CommonAdapter<FellowBusinessRes.Inif.Friend.Detail>(this, detail, R.layout.item_fellowbusiness_detailed_data) {
                @Override
                public void convert(MyViewHolder holder, FellowBusinessRes.Inif.Friend.Detail detail, int position) {
                    TextView tv_item1 = holder.getView(R.id.tv_item1);
                    TextView tv_item2 = holder.getView(R.id.tv_item2);
                    TextView tv_item3 = holder.getView(R.id.tv_item3);
                    tv_item1.setText(detail.getType());
                    tv_item2.setText("￥"+detail.getMoney());
                    tv_item3.setText(detail.getDate());
                }
            };
            lv_detailedData.setAdapter(detailCommonAdapter);
        }
    }

    private void initView() {
        initHeadView(this,R.drawable.icon_back_r,"详细数据",0,null);
        lv_detailedData = findViewById(R.id.lv_detailedData);
    }
}
