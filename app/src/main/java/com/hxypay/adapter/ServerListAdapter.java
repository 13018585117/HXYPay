package com.hxypay.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.response.ServerRes;

import java.util.List;


public class ServerListAdapter extends BaseAdapter {

    private List<ServerRes.Info> list;
    private Context context;

    public ServerListAdapter(Context context, List<ServerRes.Info> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<ServerRes.Info> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_server_main_item, null);
            holder = new ViewHolder();
            holder.itemLogo = (ImageView) convertView.findViewById(R.id.qq_wx_img);
            holder.itemName = (TextView) convertView.findViewById(R.id.no_tx);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ServerRes.Info datas = list.get(position);
        if(datas.getType().equals("1")){//qq
            holder.itemLogo.setBackgroundResource(R.drawable.qq);
            holder.itemName.setText("QQ客服："+datas.getInformation());
            holder.img.setBackgroundResource(R.drawable.row);
        }
        if(datas.getType().equals("2")){//qq
            holder.itemLogo.setBackgroundResource(R.drawable.wx);
            holder.itemName.setText("微信客服："+"好信用管家");
            holder.img.setBackgroundResource(R.drawable.row);
        }
        if(datas.getType().equals("3")){//qq
            holder.itemLogo.setBackgroundResource(R.drawable.call_tele);
            holder.itemName.setText("客服电话："+datas.getInformation());
            holder.img.setBackgroundResource(R.drawable.call);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView itemLogo,img;
        TextView itemName;
    }

}
