package com.hxypay.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.CommonAdapter;
import com.hxypay.adapter.MyViewHolder;
import com.hxypay.response.KCePingJiLuResponseDTO;
import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KaCePing_JiLu_Activity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.lv_jilu)
    ListView lv_jilu;
    private  List<KCePingJiLuResponseDTO.ListData> list = new ArrayList();
    private Adapter adapter;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_kaceping_jilu);
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {
        adapter = new Adapter(KaCePing_JiLu_Activity.this, list, R.layout.item_kacepingjilu);
        lv_jilu.setAdapter(adapter);
//        requestData(false);
    }

    private class Adapter extends CommonAdapter<KCePingJiLuResponseDTO.ListData> {
        public Adapter(Context context, List list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        public void convert(MyViewHolder holder, KCePingJiLuResponseDTO.ListData kCePingJiLuResponseDTO, int posittion) {
            try {
                ImageView iv_bankLogo = holder.getView(R.id.iv_bankLogo);
                TextView tv_bankName = holder.getView(R.id.tv_bankName);
                TextView tv_name = holder.getView(R.id.tv_name);
                TextView tv_idCard = holder.getView(R.id.tv_idCard);
                TextView tv_lastTime = holder.getView(R.id.tv_lastTime);
                Glide.with(KaCePing_JiLu_Activity.this).load(kCePingJiLuResponseDTO.getBankLogo()).into(iv_bankLogo);
                String bankCard = kCePingJiLuResponseDTO.getBankCard();
                tv_bankName.setText(kCePingJiLuResponseDTO.getBankName()+"("+  bankCard.substring((bankCard.length() - 4), bankCard.length())+")");
                tv_name.setText(kCePingJiLuResponseDTO.getName());
                tv_idCard.setText(kCePingJiLuResponseDTO.getIdCard());
                tv_lastTime.setText(kCePingJiLuResponseDTO.getLastTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

  /*  private void requestData(boolean isRefurbish) {
        String url = "merchantCollectMoneyAction/queryHistoryKCPCard.action";
        AccessPersonalInformationRequestDTO requestDTO = new AccessPersonalInformationRequestDTO();
        requestDTO.setMobilePhone("123456");
        String jsondata = MyGson.toJson(requestDTO);
        Map<String, String> params = new HashMap<String, String>();
        params.put(BuildConfig.REQUESE_DATA, jsondata);
        new NetCotnent(mHandler, 2, params,isRefurbish).execute(url);
    }

    private class NetCotnent extends NetAsyncTask {
        private String result;
        private int mType = 1;
        private Map<String, String> mParams;
        private boolean isRefurbish;

        public NetCotnent(Handler uiHandler, int type,
                          Map<String, String> params,boolean isRefurbish) {
            super(uiHandler);
            this.mType = type;
            this.mParams = params;
            this.isRefurbish = isRefurbish;
            setDialogId(1);// 默认样式
        }

        @Override
        protected void handlePreExecute() {

        }

        @Override
        protected String handleNetworkProcess(String... arg0) throws Exception {
            result = httptask.getRequestbyPOST(ActionOfUrl.JsonAction.POST_URL, arg0[0],
                    mParams);
            return result != null ? HANDLE_SUCCESS : HANDLE_FAILED;
        }


        @Override
        protected void handleResult() {
            if (result != null && !"".equals(result)) {
                switch (mType){
                    case 2:
                        try {
                            KCePingJiLuResponseDTO responseDTO = (KCePingJiLuResponseDTO) MyGson.fromJson(mContext, result, KCePingJiLuResponseDTO.class);
                            if (responseDTO.getRetCode()==0){
                                list = responseDTO.getList();
                                adapter.setData(list);
                                if (isRefurbish) {
                                    refreshLayout.finishRefresh(true*//*,false*//*);//传入false表示加载失败
                                    Toast.makeText(KaCePing_JiLu_Activity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                                }
                            }else if (responseDTO.getRetCode()== -1){
                                if (isRefurbish) {
                                    refreshLayout.finishRefresh(false*//*,false*//*);//传入false表示加载失败
                                    Toast.makeText(KaCePing_JiLu_Activity.this, "没有新记录", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(KaCePing_JiLu_Activity.this, "没有记录", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                if (isRefurbish) {
                                    refreshLayout.finishRefresh(false*//*,false*//*);//传入false表示加载失败;
                                    Toast.makeText(KaCePing_JiLu_Activity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(KaCePing_JiLu_Activity.this, "请求失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (isRefurbish) {
                                refreshLayout.finishRefresh(false*//*,false*//*);//传入false表示加载失败
                                Toast.makeText(KaCePing_JiLu_Activity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
            }
        }
    }*/


    @Override
    protected void onResume() {
        super.onResume();
//        refreshLayout.autoRefresh();
//设置 Header 为 全屏水滴 样式
        WaveSwipeHeader waveSwipeHeader = new WaveSwipeHeader(this);
        waveSwipeHeader.setPrimaryColors(getResources().getColor(R.color.app_theme_color));
        refreshLayout.setEnableHeaderTranslationContent(true);
        refreshLayout.setRefreshHeader(waveSwipeHeader);
//设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                requestData(true);

            }
        });
    }
}
