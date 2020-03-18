package com.hxypay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxypay.R;
import com.hxypay.response.DepositRes;

import java.util.List;


public class DepositListAdapter extends BaseAdapter {

    private List<DepositRes.Info1> list;
    private Context context;

    public DepositListAdapter(Context context, List<DepositRes.Info1> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<DepositRes.Info1> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_cards_item, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.itemBankLogo = (ImageView) convertView.findViewById(R.id.bank_logo_img);
            holder.itemBankLogo1 = (ImageView) convertView.findViewById(R.id.bank_logo_img1);
            holder.itemUpdate = (TextView) convertView.findViewById(R.id.update_tx);
            holder.itemBankName = (TextView) convertView.findViewById(R.id.bank_name_tx);
            holder.itemBankType = (TextView) convertView.findViewById(R.id.bank_type_tx);
            holder.getItemBankNo = (TextView) convertView.findViewById(R.id.bank_card_tx);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DepositRes.Info1 datas = list.get(position);
        holder.img.setBackgroundResource(R.drawable.card_shape_bg);
        Glide.with(context).load(datas.getBankLogo()).into(holder.itemBankLogo);
        Glide.with(context).load(datas.getBankLogo()).into(holder.itemBankLogo1);
        holder.itemBankName.setText(datas.getBankName());
        holder.itemBankType.setText("储蓄卡");
        String cardNo = datas.getAccount();
        holder.getItemBankNo.setText(" ****    ****    ****    " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
        holder.itemUpdate.setVisibility(View.VISIBLE);
        return convertView;
    }


    private class ViewHolder {
        LinearLayout layouty;
        ImageView img,itemBankLogo, itemBankLogo1;
        TextView itemUpdate,itemBankName, itemBankType, getItemBankNo;
    }

}
