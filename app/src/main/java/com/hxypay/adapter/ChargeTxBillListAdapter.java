package com.hxypay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxypay.R;
import com.hxypay.response.ChargeTxBillRes;

import java.util.List;


public class ChargeTxBillListAdapter extends BaseAdapter {

    private List<ChargeTxBillRes.Info1> list;
    private Context context;

    public ChargeTxBillListAdapter(Context context, List<ChargeTxBillRes.Info1> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<ChargeTxBillRes.Info1> list) {
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_bills_item, null);
            holder = new ViewHolder();
            holder.logoImg = (ImageView) convertView.findViewById(R.id.logo_img);
            holder.stateImg = (ImageView) convertView.findViewById(R.id.state_img);
            holder.itemBankName = (TextView) convertView.findViewById(R.id.bank_name_tx);
            holder.itemState = (TextView) convertView.findViewById(R.id.state_tx);
            holder.itemMoney = (TextView) convertView.findViewById(R.id.money_tx);
            holder.itemCardNo = (TextView) convertView.findViewById(R.id.card_no_tx);
            holder.itemFee = (TextView) convertView.findViewById(R.id.fee_tx);
            holder.itemTime = (TextView) convertView.findViewById(R.id.time_tx);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ChargeTxBillRes.Info1 datas = list.get(position);
        Glide.with(context).load(datas.getLogo()).into(holder.logoImg);
        //  0：处理中 1：成功 2：失败
        String state = datas.getState();
        switch (Integer.parseInt(state)) {
            case 0:
                holder.stateImg.setBackgroundResource(R.drawable.clz);
                holder.itemState.setText("处理中");
                break;
            case 1:
                holder.stateImg.setBackgroundResource(R.drawable.success);
                holder.itemState.setText("成功");
                break;
            case 2:
                holder.stateImg.setBackgroundResource(R.drawable.fail);
                holder.itemState.setText("失败");
                break;
        }
        holder.itemBankName.setText(datas.getType());
        holder.itemMoney.setText("￥ " + datas.getMoney());
        holder.itemCardNo.setText("尾号(" + datas.getAccount() + ")");
        holder.itemFee.setVisibility(View.VISIBLE);
        holder.itemFee.setText("手续费：" + datas.getFee());
        holder.itemTime.setText(datas.getCreated());
        return convertView;
    }

    private class ViewHolder {
        ImageView logoImg, stateImg;
        TextView itemBankName, itemState, itemMoney, itemCardNo, itemFee, itemTime;
    }

}
