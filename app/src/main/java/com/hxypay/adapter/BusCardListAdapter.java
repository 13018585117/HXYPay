package com.hxypay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hxypay.R;
import com.hxypay.response.BusCardListInfoRes;
import com.hxypay.ui.BusSelectProductActivity;

import java.util.List;

public class BusCardListAdapter extends RecyclerView.Adapter<BusCardListAdapter.MviewHolder> {
    private List<BusCardListInfoRes.Data> lists;
    private Context context;
    public BusCardListAdapter(List<BusCardListInfoRes.Data> lists){
        this.lists = lists;
    }
    public void setData(List<BusCardListInfoRes.Data> lists){
        this.lists = lists;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context ==null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_bus_cardlist,viewGroup,false);
        MviewHolder mviewHolder = new MviewHolder(view);
        return mviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MviewHolder mviewHolder, final int position) {
        Glide.with(context).load(lists.get(position).getPicture()).into(mviewHolder.card_picture);
        //大于0时候就是秒结
        if (Integer.parseInt(lists.get(position).getSettlemenTypeCount())>0){
            mviewHolder.iv_miaojie.setVisibility(View.VISIBLE);
        }else {
            mviewHolder.iv_miaojie.setVisibility(View.GONE);
        }
        mviewHolder.cardName.setText(""+lists.get(position).getName());
        mviewHolder.cardContent.setText("最高"+lists.get(position).getMaxExchangePrice()+"元/万分");
        mviewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusSelectProductActivity.class);
                intent.putExtra("id",lists.get(position).getId());
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists==null?0:lists.size();
    }


    public class MviewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout layout;
        private ImageView card_picture,iv_miaojie;
        private TextView cardName,cardContent;
        public MviewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.rv_layout);
            card_picture = itemView.findViewById(R.id.card_picture);
            iv_miaojie = itemView.findViewById(R.id.iv_miaojie);
            cardName = itemView.findViewById(R.id.cardName);
            cardContent = itemView.findViewById(R.id.cardContent);
        }
    }


}
