package com.hxypay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.response.PriviewPlainRes;

import java.util.List;
import java.util.Map;


public class HKPreviewPlainListAdapter extends BaseAdapter {

    private Context context;
    private List<PriviewPlainRes.Info1> list;
    private Map<Integer,String> selectMap;

    public HKPreviewPlainListAdapter(Context context, List<PriviewPlainRes.Info1> list,Map<Integer,String> selectMap) {
        this.context = context;
        this.list = list;
        this.selectMap = selectMap;
    }

    public void setData(List<PriviewPlainRes.Info1> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_hk_plain_preview_item, null);
            holder = new ViewHolder();
            holder.itemRQ = (TextView) convertView.findViewById(R.id.rq_tx);
            holder.itemTime1 = (TextView) convertView.findViewById(R.id.time1_tx);
            holder.itemType1 = (TextView) convertView.findViewById(R.id.type1_tx);
            holder.itemMoney1 = (TextView) convertView.findViewById(R.id.money1_tx);
            holder.itemFee = (TextView) convertView.findViewById(R.id.fee_tx);

            holder.mccTx = (TextView) convertView.findViewById(R.id.mcc_tx);
            holder.mSelectMccBtn = (Button) convertView.findViewById(R.id.btn_select);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PriviewPlainRes.Info1 datas = list.get(position);

        String time = datas.getPayTime();
        if (!TextUtils.isEmpty(time) && time.length() >= 10) {
            holder.itemRQ.setText(time.substring(0, 10));
            holder.itemTime1.setText(time.substring(10, time.length()));
        } else {
            holder.itemRQ.setText(time);
            holder.itemTime1.setText(time);
        }

        String type = datas.getType();
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("1")) {
                holder.itemType1.setText("还款");
                holder.itemMoney1.setText("+￥" + datas.getAmount());
                holder.itemMoney1.setTextColor(Color.parseColor("#333333"));
                holder.itemFee.setVisibility(View.INVISIBLE);

                holder.mccTx.setVisibility(View.GONE);
                holder.mSelectMccBtn.setVisibility(View.GONE);

            }
            if (type.equals("2")) {
                holder.itemType1.setText("消费");
                holder.itemMoney1.setText("-￥" + datas.getAmount());
                holder.itemMoney1.setTextColor(Color.parseColor("#e72b2a"));
                holder.itemFee.setVisibility(View.VISIBLE);
                holder.itemFee.setText("手续费" + datas.getFeeRate());

                holder.mccTx.setVisibility(View.VISIBLE);
                if (selectMap!=null) {
                    if (TextUtils.isEmpty(selectMap.get(position))) {
                        holder.mccTx.setText(datas.getMccName());
                    } else {
                        holder.mccTx.setText(selectMap.get(position));
                    }
                }else {
                    holder.mccTx.setText(datas.getMccName());
                }
                holder.mSelectMccBtn.setVisibility(View.VISIBLE);

                holder.mSelectMccBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        IToast.getIToast().showIToast(datas.getFormNo());
                        mccSelectListener.mccSelect(holder.mccTx, datas.getFormNo(),position);
                    }
                });
            }
        }
        return convertView;
    }


    private class ViewHolder {
        TextView itemRQ;
        TextView itemTime1, itemType1, itemMoney1, itemFee;

        private TextView mccTx, mSelectMccBtn;
    }


    private MccSelectListener mccSelectListener;

    public interface MccSelectListener {
        void mccSelect(TextView mccText, String orderId,int position);
    }

    public MccSelectListener getMccSelectListener() {
        return mccSelectListener;
    }

    public void setMccSelectListener(MccSelectListener mccSelectListener) {
        this.mccSelectListener = mccSelectListener;
    }
}
