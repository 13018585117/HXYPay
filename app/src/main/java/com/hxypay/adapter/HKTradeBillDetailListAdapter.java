package com.hxypay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.response.HKTradeBillDetailRes;

import java.util.List;


public class HKTradeBillDetailListAdapter extends BaseAdapter {

    private List<HKTradeBillDetailRes.Info1> list;
    private Context context;
    private String bankStr;

    public HKTradeBillDetailListAdapter(Context context, String bankStr,List<HKTradeBillDetailRes.Info1> list) {
        this.context = context;
        this.list = list;
        this.bankStr= bankStr;
    }

    public void setData(String bankStr,List<HKTradeBillDetailRes.Info1> list) {
        this.list = list;
        this.bankStr= bankStr;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (list == null) ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_trade_bill_detail_item, null);
            holder = new ViewHolder();
            holder.itemMoney = (TextView) convertView.findViewById(R.id.money_tx);
            holder.itemFee = (TextView) convertView.findViewById(R.id.fee_tx);
            holder.itemCardNo = (TextView) convertView.findViewById(R.id.card_no_tx);
            holder.itemState = (TextView) convertView.findViewById(R.id.state_tx);
            holder.itemTime = (TextView) convertView.findViewById(R.id.time_tx);
            holder.tv_errer_tishi = (TextView) convertView.findViewById(R.id.tv_errer_tishi);
            holder.ll_errer = convertView.findViewById(R.id.ll_errer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final HKTradeBillDetailRes.Info1 datas = list.get(position);
        String type = datas.getType();
        if (type.equals("1")) {//还款
            holder.itemFee.setVisibility(View.INVISIBLE);

            holder.itemMoney.setText("+￥" + datas.getAmount());
            holder.itemMoney.setTextColor(Color.parseColor("#f5212d"));

            holder.itemState.setText(datas.getState());
            holder.itemState.setTextColor(Color.parseColor("#f5212d"));
        }
        if (type.equals("2")) {
            holder.itemFee.setVisibility(View.VISIBLE);
            holder.itemFee.setText("￥" + datas.getFeeRate());

            holder.itemMoney.setText("-￥" + datas.getAmount());
            holder.itemMoney.setTextColor(Color.parseColor("#09bb07"));

            holder.itemState.setText(datas.getState());
            holder.itemState.setTextColor(Color.parseColor("#09bb07"));
        }
        if (datas.getState().contains("失败")){
            holder.ll_errer.setVisibility(View.VISIBLE);
            holder.tv_errer_tishi.setText(datas.getMessage());
        }

        holder.itemCardNo.setText(bankStr);


        holder.itemTime.setText(datas.getPayTime());
        return convertView;
    }

    private class ViewHolder {
        TextView itemMoney, itemFee, itemCardNo, itemState, itemTime,tv_errer_tishi;
        LinearLayout ll_errer;
    }

}
