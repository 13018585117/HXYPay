package com.hxypay.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.adapter.CommonAdapter;
import com.hxypay.adapter.MyViewHolder;
import com.hxypay.response.AnnouncementRes;

import java.util.List;

public class NewDialog extends Dialog {
    private Context context;
    private TextView contentTx,tv_date,tv_headTitle;
    private LinearLayout mCloseLayout,return_layout,ll_news;
    List<AnnouncementRes.Info1> info1;
    private ListView lv_content;
    private int start = 0;

    /**
     * @param context
     * @param info1  数据
     * @param start  1为全部信息，0位最新信息；
     */
    public NewDialog(Context context, List<AnnouncementRes.Info1> info1,int start) {
        // 设置背景style
        super(context, R.style.custom_dialog);
        this.context = context;
        this.info1 = info1;
        this.start = start;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dialog);
        initView();
    }

    private void initView() {
        mCloseLayout = (LinearLayout) findViewById(R.id.close_layout);
        return_layout = (LinearLayout) findViewById(R.id.return_layout);
        ll_news = (LinearLayout) findViewById(R.id.ll_news);
        contentTx = (TextView) findViewById(R.id.content_tx);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_headTitle = (TextView) findViewById(R.id.tv_headTitle);
        lv_content = (ListView) findViewById(R.id.lv_content);
        contentTx.setText(info1.get(0).getContent());
        tv_date.setText(info1.get(0).getCreated());
        initStart();
        final CommonAdapter<AnnouncementRes.Info1> info1CommonAdapter = new CommonAdapter<AnnouncementRes.Info1>(context, info1, R.layout.newdialog_item) {
            @Override
            public void convert(MyViewHolder holder, AnnouncementRes.Info1 info1, int position) {
                holder.getTextVie(R.id.tv_content_item,info1.getContent());
                holder.getTextVie(R.id.tv_date_item,info1.getCreated());
                holder.getTextVie(R.id.tv_title_item,info1.getTitle());
            }
        };
        lv_content.setAdapter(info1CommonAdapter);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contentTx.setText(info1.get(position).getContent());
                tv_headTitle.setText(info1.get(position).getTitle());
                tv_date.setText(info1.get(position).getCreated());
                lv_content.setVisibility(View.GONE);
                ll_news.setVisibility(View.VISIBLE);
                return_layout.setVisibility(View.VISIBLE);
            }
        });
        mCloseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_news.setVisibility(View.GONE);
                lv_content.setVisibility(View.VISIBLE);
                tv_headTitle.setText("公告信息");
                return_layout.setVisibility(View.GONE);
            }
        });
    }

    private void initStart() {
        if (start==1){
            //显示全部公告；
            ll_news.setVisibility(View.GONE);
            lv_content.setVisibility(View.VISIBLE);
            return_layout.setVisibility(View.GONE);
        }else if (start == 0){
            //只显示最新一条公告；
            lv_content.setVisibility(View.GONE);
            ll_news.setVisibility(View.VISIBLE);
            tv_headTitle.setText(info1.get(0).getTitle());
            return_layout.setVisibility(View.VISIBLE);
        }
    }
}
