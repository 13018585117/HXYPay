package com.hxypay.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.bean.RQBean;

import java.util.ArrayList;
import java.util.List;

public class RQPop extends PopupWindow {

    private GridView mGd;
    private Button Btn;
    private RQListAdapter mAdapter;
    private List<RQBean> rqList;

    public RQPop(final Context context, final List<RQBean> rqList, final IRQListener irqListener) {
        final View view = LayoutInflater.from(context).inflate(R.layout.rq_pop, null);
        this.rqList = rqList;
        Btn = (Button) view.findViewById(R.id.btn_com);
        mGd = (GridView) view.findViewById(R.id.gridView);
        mAdapter = new RQListAdapter(context, rqList);
        mGd.setAdapter(mAdapter);
        mGd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean selectFlag = rqList.get(position).isTextSelectFlag();
                rqList.get(position).setTextSelectFlag(!selectFlag);
                mAdapter.setData(rqList);
            }
        });

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> selectNoList = new ArrayList<String>();
                selectNoList.clear();
                for (int i = 0; i < rqList.size(); i++) {
                    if (rqList.get(i).isTextSelectFlag()) {
                        selectNoList.add(rqList.get(i).getTextNo());
                    }
                }
                if (selectNoList == null || selectNoList.size() == 0) {
                    IToast.getIToast().showIToast("请选择还款日期");
                    return;
                }
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < selectNoList.size(); i++) {
                    if(i ==0){
                        sb.append(selectNoList.get(0));
                    }else{
                        sb.append(","+selectNoList.get(i));
                    }
                }
                irqListener.rq(RQPop.this, sb.toString());
            }
        });

        this.setContentView(view);
        this.setOutsideTouchable(true);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.operation_anim_style);
        ColorDrawable dw = new ColorDrawable(0x66797979);
        this.setBackgroundDrawable(dw);

        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.popLayout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public interface IRQListener {
        void rq(RQPop dialog, String rq);
    }


    class RQListAdapter extends BaseAdapter {

        private List<RQBean> list;
        private Context context;

        public RQListAdapter(Context context, List<RQBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<RQBean> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.rq_pop_item, null);
                holder = new ViewHolder();
                holder.bt = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bt.setText(list.get(position).getTextNo());
            if (list.get(position).isTextSelectFlag()) {
                holder.bt.setBackgroundResource(R.drawable.btn_cicle_red);
                holder.bt.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.bt.setBackgroundResource(R.drawable.btn_cicle_white);
                holder.bt.setTextColor(Color.parseColor("#333333"));
            }
            return convertView;
        }

        private class ViewHolder {
            TextView bt;
        }

    }
}
