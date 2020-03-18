package com.hxypay.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.bean.MoreZBBean;

import java.util.List;


public class MoreZBListAdapter extends BaseAdapter {

    private List<MoreZBBean> list;
    private Context context;

    public MoreZBListAdapter(Context context, List<MoreZBBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<MoreZBBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_more_zb_item, null);
            holder = new ViewHolder();
            holder.itemImg = (ImageView) convertView.findViewById(R.id.img);
            holder.itemDesc = (TextView) convertView.findViewById(R.id.desc_tx);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MoreZBBean datas = list.get(position);
        holder.itemImg.setBackgroundResource(datas.getImgId());
        holder.itemDesc.setText(datas.getDesc());
        return convertView;
    }

    private class ViewHolder {
        ImageView itemImg;
        TextView itemDesc;
    }

}
