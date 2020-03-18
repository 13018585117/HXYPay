package com.hxypay.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.TabFragmentPagerAdapter;
import com.hxypay.fragment.TradeChargeTxBillFragment;
import com.hxypay.fragment.TradeHKBillFragment;
import com.hxypay.response.CreditRes;

import java.util.ArrayList;
import java.util.List;

/**
 * 还款记录 和 提现记录
 */
public class TradeBillActivity extends BaseActivity {

    private TabLayout mViewpagerTab;
    private ViewPager mNewsViewpager, viewpager_yh;

    private TradeHKBillFragment f1;
    private TradeChargeTxBillFragment f2;

    private List<CreditRes.Info1> cards;
    public static CreditRes.Info1 card;

    /*viewPager的*/
    private ImageView img;
    private ImageView itemBankLogo;
    private ImageView itemBankLogo1;
    private TextView itemUpdate;
    private TextView itemBankName;
    private TextView itemBankType;
    private TextView getItemBankNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_bill);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cards!=null){
            cards.clear();
            cards = null;
        }
        if (card!=null){
            card = null;
        }
        if (mNewsViewpager!=null) {
            mNewsViewpager.removeAllViews();
            mNewsViewpager = null;
        }
        if (viewpager_yh!=null) {
            viewpager_yh.removeAllViews();
            viewpager_yh = null;
        }
        if (f1!=null) {
            f1.onDestroy();
            f1= null;
        }
        if (f2!=null) {
            f2.onDestroy();;
            f2= null;
        }

    }

    private void initView() {
        cards = (List<CreditRes.Info1>) getIntent().getSerializableExtra("cards");
        card = cards.get(0);
        initHeadView(this, R.drawable.icon_back_r, cards.get(0).getBankName(), 0, null);

        initFragmentView();
    }


    private void setCardView(CreditRes.Info1 dd) {
        Glide.with(context).load(dd.getBankBack()).into(img);
        Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo);
        Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo1);
        itemBankName.setText(dd.getBankName());
        itemBankType.setText("信用卡");
        String cardNo = dd.getAccount();
        getItemBankNo.setText(" ****    ****    ****    " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
        itemUpdate.setVisibility(View.GONE);
    }


    private void initFragmentView() {
        //找到控件
        mViewpagerTab = findViewById(R.id.home_viewpager_tab);
        mNewsViewpager = findViewById(R.id.home_viewpager);
        viewpager_yh = findViewById(R.id.viewpager_yh);
        //fragment列表  
        List<Fragment> list_fragment = new ArrayList<>();
        //tab名的列表
        List<String> list_Title = new ArrayList<>();
        f1 = new TradeHKBillFragment();
        f2 = new TradeChargeTxBillFragment();

        list_fragment.add(f1);
        list_fragment.add(f2);

        list_Title.add("还款计划");
        list_Title.add("信用卡收款");

        //设置名称
        for (int i = 0; i < list_Title.size(); i++) {
            mViewpagerTab.addTab(mViewpagerTab.newTab().setText(list_Title.get(i)));
        }
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(
                getSupportFragmentManager(), list_fragment, list_Title
        );
        //viewpager 加载adapter
        mNewsViewpager.setAdapter(adapter);
        //TableLayout加载viewpager
        mViewpagerTab.setupWithViewPager(mNewsViewpager);
        viewpager_yh.addOnPageChangeListener(new PageChangeListener());
        Vp_Adapter vp_adapter = new Vp_Adapter();
        viewpager_yh.setAdapter(vp_adapter);
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            card = cards.get(i);
            initHeadView(TradeBillActivity.this, R.drawable.icon_back_r, cards.get(i).getBankName(), 0, null);
            f1.mPullToRefreshLayout.autoRefresh();
            f2.mPullToRefreshLayout.autoRefresh();
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    private class Vp_Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return cards == null ? 0 : cards.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.vp_item_trade_bill, container, false);
            img = (ImageView) view.findViewById(R.id.img);
            itemBankLogo = (ImageView) view.findViewById(R.id.bank_logo_img);
            itemBankLogo1 = (ImageView) view.findViewById(R.id.bank_logo_img1);
            itemUpdate = (TextView) view.findViewById(R.id.update_tx);
            itemBankName = (TextView) view.findViewById(R.id.bank_name_tx);
            itemBankType = (TextView) view.findViewById(R.id.bank_type_tx);
            getItemBankNo = (TextView) view.findViewById(R.id.bank_card_tx);
            setCardView(cards.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
