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
import com.hxypay.response.NewsRes;

import java.util.List;


public class NewsListAdapter extends BaseAdapter {

    private List<NewsRes.Info1> list;
    private Context context;

    public NewsListAdapter(Context context, List<NewsRes.Info1> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<NewsRes.Info1> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_news_item, null);
            holder = new ViewHolder();
            holder.itemLogo = (ImageView) convertView.findViewById(R.id.img);
            holder.itemTitle = (TextView) convertView.findViewById(R.id.title_tx);
            holder.itemTime = (TextView) convertView.findViewById(R.id.time_tx);
            holder.itemContent = (TextView) convertView.findViewById(R.id.content_tx);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NewsRes.Info1 datas = list.get(position);
        holder.itemLogo.setVisibility(View.VISIBLE);
        Glide.with(context).load(datas.getImages()).into(holder.itemLogo);
        holder.itemTitle.setText(datas.getTitle());
        holder.itemTime.setText(datas.getCreated());
        holder.itemContent.setText(datas.getText());
        return convertView;
    }

    private class ViewHolder {
        ImageView itemLogo;
        TextView itemTitle,itemTime,itemContent;
    }

}
