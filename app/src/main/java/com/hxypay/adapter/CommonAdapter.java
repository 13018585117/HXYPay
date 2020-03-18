package com.hxypay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> list;
    private int layoutId;
    public CommonAdapter(Context context, List<T>list,int layoutId){
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }
    public void setUpdateData(List<T> list){
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = MyViewHolder.get(context, convertView, parent, layoutId, position);
        convert(holder,getItem(position),position);
        return holder.getConvertView();
    }

    public abstract void convert(MyViewHolder holder,T t,int position);

}
