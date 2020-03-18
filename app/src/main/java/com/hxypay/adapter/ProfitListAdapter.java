package com.hxypay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.response.ProfitRes;

import java.util.List;


public class ProfitListAdapter extends BaseAdapter {

    private List<ProfitRes.Info1> list;
    private Context context;

    public ProfitListAdapter(Context context, List<ProfitRes.Info1> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<ProfitRes.Info1> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_profit_item, null);
            holder = new ViewHolder();
            holder.itemTime = (TextView) convertView.findViewById(R.id.time_tx);
            holder.itemMoney = (TextView) convertView.findViewById(R.id.money_tx);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            ProfitRes.Info1 datas = list.get(position);
            holder.itemTime.setText(datas.getDays());
            holder.itemMoney.setText("￥ " + datas.getMoney());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView itemTime, itemMoney;
    }

}
