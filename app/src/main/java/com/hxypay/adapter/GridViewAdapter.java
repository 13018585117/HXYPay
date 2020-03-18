package com.hxypay.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hxypay.R;

public class GridViewAdapter extends BaseAdapter{

    private final LayoutInflater mInflater;
    private Context mContext;
    private List<String> mList;
    public GridViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setDevicesList(List<String> devicesList) {
        this.mList = devicesList;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_gv_share_material, null);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_gv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList!=null && position < mList.size()) {
            Glide.with(mContext).load(mList.get(position)).into(holder.mImageView);
        } else {
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView mImageView;
    }
}
