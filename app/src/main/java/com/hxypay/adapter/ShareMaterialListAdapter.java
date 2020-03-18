package com.hxypay.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hxypay.R;
import com.hxypay.response.ShareMaterialRes;
import com.hxypay.view.MyGridView;

import java.util.List;
import java.util.zip.Inflater;

public class ShareMaterialListAdapter extends BaseAdapter {
    List<ShareMaterialRes.Info1> datas;
    Context context;
    private ItemListener mItemListener;
    private LayoutInflater mInflater;
    private ClipboardManager clipboardManager;
    public ShareMaterialListAdapter(Context context, List<ShareMaterialRes.Info1> datas){
        this.datas = datas;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    public void setUpdateData(List<ShareMaterialRes.Info1> list){
        this.datas = list;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_share_material, null);
            holder.mMyGridView = (MyGridView) convertView.findViewById(R.id.gv_img);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.fz_bt = (Button) convertView.findViewById(R.id.fz_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//注意：这是重要的地方
//鉴于我们想让每个item下的GridView都能独立，因此只能通过new 出适配器来
//单独定义每个item，这样才能让每个listview的item内容都有不同的GridView
        final List<String> imgs = datas.get(position).getImg();
        holder.tv_content.setText(datas.get(position).getContent());
        final GridViewAdapter devicesAdapter = new GridViewAdapter(context);
        devicesAdapter.setDevicesList(imgs);
        holder.mMyGridView.setAdapter(devicesAdapter);
        holder.tv_content.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                copyContent(position);
                return false;
            }
        });
        holder.fz_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyContent(position);
            }
        });
        holder.mMyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//监听最后一个item（position==list.size（）-1），
//动态更改其作用功能（比如增加一条数据，或者blablabla...）
                    mItemListener.onAddClick(imgs,position);

            }
        });

        return convertView;
    }

    private void copyContent(int position) {
        if (clipboardManager==null) {
            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        }
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("推广素材内容：",datas.get(position).getContent());
        //添加ClipData对象到剪切板中
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "成功复制内容", Toast.LENGTH_SHORT).show();
    }


    static class ViewHolder {
        MyGridView mMyGridView;
        TextView tv_content;
        Button fz_bt;
    }
   public interface ItemListener{
        void onAddClick(List<String> url,int position);
   }
   public void setOnGridViewItenClick(ItemListener itenClick){
        mItemListener = itenClick;
   }

}
