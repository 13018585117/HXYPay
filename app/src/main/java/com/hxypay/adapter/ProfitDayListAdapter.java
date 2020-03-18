package com.hxypay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.response.ProfitDayRes;
import com.hxypay.util.VerifyRuler;

import java.util.List;


public class ProfitDayListAdapter extends BaseAdapter {

    private List<ProfitDayRes.Info1> list;
    private Context context;

    public ProfitDayListAdapter(Context context, List<ProfitDayRes.Info1> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<ProfitDayRes.Info1> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_profit_day_item, null);
            holder = new ViewHolder();
            holder.itemTele = (TextView) convertView.findViewById(R.id.tele_tx);
            holder.itemMoney = (TextView) convertView.findViewById(R.id.money_tx);
            holder.itemTime = (TextView) convertView.findViewById(R.id.time_tx);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ProfitDayRes.Info1 datas = list.get(position);
        holder.itemTele.setText(VerifyRuler.teleShow(datas.getPhoneNum()));
        holder.itemMoney.setText(datas.getType() + "+" + datas.getMoney());
        holder.itemTime.setText(datas.getCreated());
        return convertView;
    }

    private class ViewHolder {
        TextView itemTele, itemMoney, itemTime;
    }

}
