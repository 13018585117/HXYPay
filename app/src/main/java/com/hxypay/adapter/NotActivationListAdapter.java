package com.hxypay.adapter;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.response.FriendsRes;
import com.hxypay.util.VerifyRuler;

import java.util.List;


public class NotActivationListAdapter extends BaseAdapter {

    private List<FriendsRes.Info1> list;
    private Context context;

    public NotActivationListAdapter(Context context, List<FriendsRes.Info1> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<FriendsRes.Info1> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_friends_item, null);
            holder = new ViewHolder();
            holder.itemTele = (TextView) convertView.findViewById(R.id.tele_tx);
            holder.itemTime = (TextView) convertView.findViewById(R.id.time_tx);
            holder.tv_name_yes = (TextView) convertView.findViewById(R.id.tv_name_yes);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FriendsRes.Info1 datas = list.get(position);
        holder.itemTele.setText(VerifyRuler.teleShow(datas.getPhoneNum().trim()));
        holder.itemTime.setText(VerifyRuler.timeShow(datas.getCreated().trim()));
        if (TextUtils.isEmpty(datas.getLoginId())||datas.getLoginId().equals("1")) {
            holder.tv_name_yes.setText("未实名");
            holder.tv_name_yes.setTextColor(context.getResources().getColor(R.color.text_e72b2a_color));
        }else {
            holder.tv_name_yes.setText(VerifyRuler.nameShow(datas.getLoginId().trim()));
            holder.tv_name_yes.setTextColor(context.getResources().getColor(R.color.text_333333_color));

        }
        return convertView;
    }

    private class ViewHolder {
        TextView itemTele,itemTime,tv_name_yes;
    }

}
