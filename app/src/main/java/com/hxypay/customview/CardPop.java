package com.hxypay.customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxypay.R;
import com.hxypay.response.CreditRes;
import com.hxypay.ui.CardAddOcrActivity;

import java.util.List;

public class CardPop extends PopupWindow {
    private LinearLayout mCloseLayout;
    private ListView mListView;
    private Button Btn;
    private CardListAdapter mAdapter;
    private List<CreditRes.Info1> mData;

    public CardPop(final Context context, int h, final List<CreditRes.Info1> mData, final ICardListener iCardListener) {
        final View view = LayoutInflater.from(context).inflate(R.layout.card_pop, null);
        this.mData = mData;
        LinearLayout allLayout = (LinearLayout) view.findViewById(R.id.popLayout);
        ViewGroup.LayoutParams lp = allLayout.getLayoutParams();
        lp.height = h / 2 + 150;
        allLayout.setLayoutParams(lp);

        mCloseLayout = (LinearLayout) view.findViewById(R.id.close_layout);
        mCloseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mListView = (ListView) view.findViewById(R.id.list_data);

        Btn = (Button) view.findViewById(R.id.add_card);
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent mIntent = new Intent(context, CardAddOcrActivity.class);
                context.startActivity(mIntent);
            }
        });
        mAdapter = new CardListAdapter(context, mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iCardListener.card(CardPop.this, mData.get(position));
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

    public interface ICardListener {
        void card(CardPop dialog, CreditRes.Info1 card);
    }


    class CardListAdapter extends BaseAdapter {

        private List<CreditRes.Info1> list;
        private Context context;

        public CardListAdapter(Context context, List<CreditRes.Info1> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<CreditRes.Info1> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.card_pop_item, null);
                holder = new ViewHolder();
                holder.bankLogo = (ImageView) convertView.findViewById(R.id.bank_logo_img);
                holder.mSelectImg = (ImageView) convertView.findViewById(R.id.select_img);

                holder.bankName = (TextView) convertView.findViewById(R.id.bank_name_tx);
                holder.bankRq = (TextView) convertView.findViewById(R.id.card_rq_tx);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CreditRes.Info1 card = list.get(position);
            Glide.with(context).load(card.getBankLogo()).into(holder.bankLogo);
            String cardNo = card.getAccount();
            String dCardNo = "";
            if (!TextUtils.isEmpty(cardNo)) {
                dCardNo = "(" + cardNo.substring(cardNo.length() - 4, cardNo.length()) + ")";
            }
            holder.bankName.setText(card.getBankName() + dCardNo);
            holder.bankRq.setText("账单日：" + card.getBillDay() + "  还款日：" + card.getRepaymentDay());
            return convertView;
        }

        private class ViewHolder {
            ImageView bankLogo;
            TextView bankName, bankRq;
            ImageView mSelectImg;
        }

    }
}
