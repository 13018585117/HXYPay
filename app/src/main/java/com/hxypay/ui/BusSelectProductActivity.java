package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.CommonAdapter;
import com.hxypay.adapter.MyViewHolder;
import com.hxypay.bean.KeyConfig;
import com.hxypay.customview.IToast;
import com.hxypay.response.BusCardListInfoRes;
import com.hxypay.response.BusCommodityListInfoRes;
import com.hxypay.util.Constants;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.util.SharedPreStorageMgr;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BusSelectProductActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private List<BusCommodityListInfoRes.Data> data;
    private String id;
    private ListView lv_selectProduct;
    private SwipeRefreshLayout srl_bus;
    private CommonAdapter<BusCommodityListInfoRes.Data> commonAdapter;
    private int positionCard;
    private String userCurrLevelId = "1"; //用户等级
    private String userCurrLevelName = ""; //用户等级名
    private String moneyLeve = "0.00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product__bus);
        initView();
        requestCommodityLists();
    }

    private void initView() {
        ButterKnife.bind(this);
        initHeadView(this, R.drawable.icon_back_r, "请选择一个产品", 0,null);
        userCurrLevelId = SharedPreStorageMgr.getIntance().getStringValue(KeyConfig.USERCURRLEVELID);
        userCurrLevelName = SharedPreStorageMgr.getIntance().getStringValue(KeyConfig.USERCURRLEVELNAME);
        //银行卡id;
        id = getIntent().getStringExtra("id");
        positionCard = getIntent().getIntExtra("position", 0);
        lv_selectProduct = findViewById(R.id.lv_selectProduct);
        srl_bus = findViewById(R.id.srl_bus);
        initAdapter();
        lv_selectProduct.setAdapter(commonAdapter);
        lv_selectProduct.setOnItemClickListener(this);
        lv_selectProduct.setOnScrollListener(new OnScrollListener());

        srl_bus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestCommodityLists();
            }
        });
    }

    private void initAdapter() {
        commonAdapter = new CommonAdapter<BusCommodityListInfoRes.Data>(this, data, R.layout.item_bus_select_product) {
            @Override
            public void convert(MyViewHolder holder, BusCommodityListInfoRes.Data data, int position) {
                holder.getImageUrl2((ImageView) holder.getView(R.id.iv_picture), data.getPicture());
                holder.getTextVie(R.id.tv_thoroughfare, data.getName());
//                0：秒结，1：核算结账，2，T+1结账
                if (data.getSettlementType().trim().equals("0")) {
                    holder.getTextVie(R.id.tv_explain, data.getIntegral()+"积分起兑,秒结");
                } else if (data.getSettlementType().trim().equals("1")) {
                    holder.getTextVie(R.id.tv_explain, data.getIntegral()+"积分起兑,核算结账");
                } else if (data.getSettlementType().trim().equals("2")) {
                    holder.getTextVie(R.id.tv_explain, data.getIntegral()+"积分起兑,T+1结账");
                } else {
                    holder.getTextVie(R.id.tv_explain, data.getIntegral()+"");
                }
                if (userCurrLevelId.equals("1")){
                    if (TextUtils.isEmpty(data.getMenberMaxExchangePrice())) {
                        moneyLeve ="0.00";
                    }else {
                        moneyLeve = data.getMenberMaxExchangePrice();
                    }
                }else if (userCurrLevelId.equals("2")){
                    moneyLeve = data.getMemberPrice();
                }else if (userCurrLevelId.equals("3")){
                    holder.getTextVie(R.id.tv_money, data.getAgentPrice()+"元/万分");
                    moneyLeve = data.getAgentPrice();
                }else {
                    moneyLeve ="0.00";
                }
                holder.getTextVie(R.id.tv_money, moneyLeve+"元/万分");
                holder.getTextVie(R.id.tv_grade, "当前级别："+userCurrLevelName);
            }
        };
    }

    private void requestCommodityLists() {
        showDialog("加载中...");
        Map<String, String> map = Params.merchantInfoParams();
        map.put("id",id);
        OkHttpUtils.get()
                .url(Constants.busCommodityList)
                .headers(PublicParam.getParam())
                .params(map)
                .build().execute(new GenericsCallback<BusCommodityListInfoRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(BusCommodityListInfoRes response, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        data = response.getData();
                        commonAdapter.setUpdateData(data);
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                    }
                } else {
                    IToast.getIToast().showIToast(response.getMsg());
                }
            }
        });
    }

    //列表条目点击事件；
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (data!=null){
            String goodsId = data.get(position).getId();
            Intent intent = new Intent(context, BusCommodityDetailActivity.class);
            intent.putExtra("id",goodsId);
            intent.putExtra("position", positionCard);
            intent.putExtra("moneyLeve",((TextView) view.findViewById(R.id.tv_money)).getText().toString());
            startActivity(intent);
        }
    }

    //滚到监听
    private class OnScrollListener implements AbsListView.OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState){
                //闲置 // 滚动结束
                case OnScrollListener.SCROLL_STATE_IDLE:
                    // 如果滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                        srl_bus.setEnabled(false);
                    }else if (view.getFirstVisiblePosition() == 0){
                        // 滚动到顶部
                        srl_bus.setEnabled(true);
                    }else {
                        srl_bus.setEnabled(false);
                    }
                    break;
                // 正在滚动
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    break;
                // 开始滚动
                case OnScrollListener.SCROLL_STATE_FLING:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
}
