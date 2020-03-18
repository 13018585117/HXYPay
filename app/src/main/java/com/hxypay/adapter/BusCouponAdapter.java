package com.hxypay.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hxypay.R;
import com.hxypay.response.ImgPathRes;
import com.liulishuo.okdownload.core.interceptor.Interceptor;

import java.util.List;

public class BusCouponAdapter extends RecyclerView.Adapter<BusCouponAdapter.MViewHoader> {
    List<ImgPathRes> data;
    private IListenerClick iListenerClick;
    private Context context;
    public BusCouponAdapter(List<ImgPathRes> data){
        this.data = data;
    }

    public void setData(List<ImgPathRes> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MViewHoader onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_bus_coupon, viewGroup, false);
        MViewHoader mViewHoader = new MViewHoader(view);
        return mViewHoader;
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHoader mViewHoader, final int i) {
        Glide.with(context).load(data.get(i).getNativeImgPath()).into(mViewHoader.iv_icom);
        mViewHoader.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iListenerClick!=null){
                    iListenerClick.OnCloseClick(i); //回调；
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class MViewHoader extends RecyclerView.ViewHolder{
        ImageView iv_icom,iv_close;
        public MViewHoader(@NonNull View itemView) {
            super(itemView);
            iv_icom = itemView.findViewById(R.id.iv_icom);
            iv_close = itemView.findViewById(R.id.iv_close);
        }
    }

    public interface IListenerClick {
        public void OnCloseClick(int i);
    }

    public void OnCloseClick(IListenerClick iListenerClick){
        this.iListenerClick = iListenerClick;
    }
}
