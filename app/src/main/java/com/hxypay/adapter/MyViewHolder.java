package com.hxypay.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class MyViewHolder {
    private Context mContext;
    private int postion;
    private View mConvertView;
    private SparseArray<View> mViews;
    public MyViewHolder(Context context, View convertView, ViewGroup parent,int layoutId,int postion){
        this.mContext = context;
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mViews = new SparseArray<View>();
        this.postion = postion;
        mConvertView.setTag(this);
    }

    public static MyViewHolder get(Context context, View converView, ViewGroup parent, int layoutid, int postion){
        if (converView == null){
            MyViewHolder myViewHolder = new MyViewHolder(context, converView, parent, layoutid, postion);
            return myViewHolder;
        }
        else {
            MyViewHolder holder = (MyViewHolder) converView.getTag();
            holder.postion = postion;
            return holder;
        }
    }
    public View getConvertView(){
        return mConvertView;
    }

    /**
     * @param viewId 控件ID
     * @param <T>
     * @return 通过id获取控件
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if (view ==null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    public MyViewHolder getTextVie(int id,String  tv){
        TextView mTextView = getView(id);
        mTextView.setText(tv);
        return this;
    }

    public MyViewHolder getImageResoure(int id,int color){
        ImageView mImageView = getView(id);
        mImageView.setImageResource(color);
        return this;
    }

    public MyViewHolder getImageBitmap(int id, Bitmap bitmap){
        ImageView img = getView(id);
        img.setImageBitmap(bitmap);
        return this;
    }
    public MyViewHolder getImageUrl(int id, String url){
        ImageView img = getView(id);
        Glide.with(mContext).load(url).into(img);
        return this;
    }
    public MyViewHolder getImageUrl2(ImageView img, String url){ ;
        Glide.with(mContext).load(url).into(img);
        return this;
    }
}
