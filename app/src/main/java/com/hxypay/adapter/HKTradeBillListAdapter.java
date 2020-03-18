package com.hxypay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.response.HKTradeBillRes;

import java.util.List;


public class HKTradeBillListAdapter extends BaseAdapter {

    private List<HKTradeBillRes.Info1> list;
    private Context context;
    private HKTradeBillRes.Info1 datas;

    public HKTradeBillListAdapter(Context context, List<HKTradeBillRes.Info1> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<HKTradeBillRes.Info1> list) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_trade_bills_item, null);
            holder = new ViewHolder();
            holder.delImg = (ImageView) convertView.findViewById(R.id.del_img);
            holder.itemHkPlain = (TextView) convertView.findViewById(R.id.hkjh_tx);
            holder.hk_zzj = (TextView) convertView.findViewById(R.id.hk_zzj);
            holder.itemHKState = (TextView) convertView.findViewById(R.id.hk_state_tx);
            holder.itemMoney = (TextView) convertView.findViewById(R.id.money_tx);
            holder.itemCount = (TextView) convertView.findViewById(R.id.count_tx);
            holder.itemStartTime = (TextView) convertView.findViewById(R.id.text_start_time);
            holder.itemEndTime = (TextView) convertView.findViewById(R.id.text_end_time);
            holder.but_stop = convertView.findViewById(R.id.but_stop);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        datas = list.get(position);
        holder.itemHkPlain.setText("手续费：￥" + datas.getFee());
        if (!TextUtils.isEmpty(datas.getReserve())){
            holder.hk_zzj.setText("周转金：￥"+datas.getReserve());
        }else {
            holder.hk_zzj.setText("周转金：￥"+"0.00");
        }
        if (datas.getState().contains("失败")){
            holder.but_stop.setVisibility(View.GONE);
        }else if (datas.getState().contains("还款中")){
            holder.but_stop.setVisibility(View.VISIBLE);
        }
        holder.but_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancellPlainListener!=null) {
                    cancellPlainListener.cancelPlain(list.get(position).getId());
                }
            }
        });
        holder.itemHKState.setText(datas.getState());
        holder.itemMoney.setText(datas.getRepaymoney());
        holder.itemCount.setText(datas.getRepayment());
        holder.itemStartTime.setText(datas.getBeginTime());
        holder.itemEndTime.setText(datas.getEndTime());
        String type  = datas.getType();
       /* if (!TextUtils.isEmpty(type)) {
            if (type.equals("0") || type.equals("1")) {
                holder.delImg.setVisibility(View.VISIBLE);
                holder.delImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancellPlainListener.cancelPlain(datas.getId());
                    }
                });
            } else {
                holder.delImg.setVisibility(View.GONE);
            }
        }*/
        return convertView;
    }





    private class ViewHolder {
        TextView itemHkPlain, itemHKState, itemMoney, itemCount, itemStartTime, itemEndTime,hk_zzj;
        ImageView delImg ;
        Button but_stop;
    }

    public interface ICancellPlainListener {
        void cancelPlain(String planId);
    }

    private ICancellPlainListener cancellPlainListener;

    public ICancellPlainListener getCancellPlainListener() {
        return cancellPlainListener;
    }

    public void setCancellPlainListener(ICancellPlainListener cancellPlainListener) {
        this.cancellPlainListener = cancellPlainListener;
    }
}
