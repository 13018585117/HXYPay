package com.hxypay.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hxypay.R;
import com.hxypay.pulltorefresh.util.DisplayUtil;
import com.hxypay.response.ChannelRes;

import java.util.List;


public class HKChannelListAdapter extends BaseAdapter {

    private Context context;
    private List<ChannelRes.Info1> list;
    private int[] imgs = {R.drawable.qudao1,R.drawable.qudao2,R.drawable.qudao3,R.drawable.qudao4};
    private JsonParser parser;

    public HKChannelListAdapter(Context context, List<ChannelRes.Info1> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<ChannelRes.Info1> list) {
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_hk_channel_item, null);
            holder = new ViewHolder();
            holder.itemName = (TextView) convertView.findViewById(R.id.channel_name_tx);
            holder.ll_bg = convertView.findViewById(R.id.ll_bg);
            holder.ll_content = convertView.findViewById(R.id.ll_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChannelRes.Info1 datas = list.get(position);
        holder.itemName.setText(datas.getPay_classname());
        //Json的解析类对象
        if (parser==null) {
            parser = new JsonParser();
        }
        Drawable drawable = context.getResources().getDrawable(R.drawable.qudao_yuan);
        drawable.setBounds(0, 0, 20, 20);// 设置图片宽高；
         LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(DisplayUtil.dp2Px(context,12),0,0,DisplayUtil.dp2Px(context,3));
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(datas.array).getAsJsonArray();
        holder.ll_content.removeAllViews();
        for (JsonElement element :jsonArray){
            TextView textView = new TextView(context);
            textView.setText(element.toString());
            textView.setLayoutParams(layoutParams);
            textView.setCompoundDrawablePadding(DisplayUtil.dp2Px(context,7));
            textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            textView.setTextSize(12);
            textView.setCompoundDrawables(drawable,null,null,null);
            holder.ll_content.addView(textView);
        }
        try {
            if (imgs.length>position){
                holder.ll_bg.setBackgroundResource(imgs[position]);
            }else {
                holder.ll_bg.setBackgroundResource(imgs[position  % imgs.length]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.ll_bg.setBackgroundResource(imgs[3]);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView itemName;
        LinearLayout ll_bg,ll_content;
    }

}
