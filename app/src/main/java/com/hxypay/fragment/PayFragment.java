package com.hxypay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.response.BusOrderFormInfoRes;
import com.hxypay.ui.ServerActivity;
import com.hxypay.ui.ServerMainActivity;
import com.hxypay.util.Utils;

import java.util.List;

public class PayFragment extends BaseFragment {


    private List<BusOrderFormInfoRes.Data> data;
    private String stateContent ="";

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_pay_bus;
    }

    @Override
    protected void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView rv_pay = findViewById(R.id.rv_pay);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_pay.setLayoutManager(linearLayoutManager);
        data = (List<BusOrderFormInfoRes.Data>) getArguments().getSerializable("data");
        MAdapter mAdapter = new MAdapter();
        rv_pay.setAdapter(mAdapter);
    }

    private class MAdapter extends RecyclerView.Adapter<MAdapter.ViewHodle>{
        @NonNull
        @Override
        public ViewHodle onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View inflate = LinearLayout.inflate(viewGroup.getContext(), R.layout.viewpager_bus_order_form, null);
            ViewHodle viewHodle = new ViewHodle(inflate);
            return viewHodle;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHodle viewHodle, int i) {
            BusOrderFormInfoRes.Data data = PayFragment.this.data.get(i);
            viewHodle.tv_bankName.setText(data.getBankName());
            viewHodle.tv_commodity.setText(data.getGoodsName());
//            viewHodle.tv_date.setText(data.get);
            viewHodle.tv_integral.setText(data.getIntegral());
            viewHodle.tv_money.setText(data.getPrice());
            String status = data.getStatus();
            viewHodle.ll_reject.setVisibility(View.GONE);
            //0和3是审核中、1：审核通过、2、审核失败
            if (status.equals("0")|| (status.equals("3"))){
                stateContent = "审核中";
            }else if (status.equals("1")){
                stateContent = "审核通过";
            }else if (status.equals("2")){
                stateContent = "审核失败";
                viewHodle.ll_reject.setVisibility(View.VISIBLE);
                viewHodle.tv_reject.setText(data.getAuditOpinion());
            }
            viewHodle.tv_state.setText(stateContent);
            viewHodle.but_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.Copy(viewHodle.tv_dingdan.getText().toString().trim());
                    IToast.getIToast().showIToast("复制了单号");
                }
            });
            viewHodle.but_kf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ServerMainActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data ==null?0:data.size();
        }

        public class ViewHodle extends RecyclerView.ViewHolder{
            TextView tv_bankName,tv_commodity,tv_date,tv_integral,tv_money,tv_state,tv_dingdan,tv_reject;
            LinearLayout ll_reject;
            Button but_kf,but_copy;
            public ViewHodle(@NonNull View itemView) {
                super(itemView);
                tv_bankName = itemView.findViewById(R.id.tv_bankName);
                tv_commodity = itemView.findViewById(R.id.tv_commodity);
                tv_date = itemView.findViewById(R.id.tv_date);
                tv_integral = itemView.findViewById(R.id.tv_integral);
                tv_money = itemView.findViewById(R.id.tv_money);
                tv_state = itemView.findViewById(R.id.tv_state);
                tv_dingdan = itemView.findViewById(R.id.tv_dingdan);
                tv_reject = itemView.findViewById(R.id.tv_reject);
                ll_reject = itemView.findViewById(R.id.ll_reject);
                but_kf = itemView.findViewById(R.id.but_kf);
                but_copy = itemView.findViewById(R.id.but_copy);
            }
        }
    }
}
