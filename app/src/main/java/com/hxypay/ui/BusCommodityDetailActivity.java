package com.hxypay.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.BusCouponAdapter;
import com.hxypay.bean.KeyConfig;
import com.hxypay.customview.IToast;
import com.hxypay.pulltorefresh.view.HeadView;
import com.hxypay.response.BusCommodityDetailInfoRes;
import com.hxypay.response.BusCommodityListInfoRes;
import com.hxypay.response.ImgPathRes;
import com.hxypay.response.UploadImgRes;
import com.hxypay.util.Base64BitmapUtil;
import com.hxypay.util.Constants;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.util.SharedPreStorageMgr;
import com.hxypay.view.ProgressWebView;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;

public class BusCommodityDetailActivity extends BaseActivity {

    private String goodsId;
    private SwipeRefreshLayout srl_bus;
    private BusCommodityDetailInfoRes.Data data;
    private int positionCard;
    @BindView(R.id.iv_picture)
    ImageView iv_picture;
    @BindView(R.id.tv_thoroughfare)
    TextView tv_thoroughfare;
    @BindView(R.id.tv_explain)
    TextView tv_explain;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_grade)
    TextView tv_grade;
    ProgressWebView web_exchangeExplain;
    @BindView(R.id.rv_coupon)
    RecyclerView rv_coupon;
    @BindView(R.id.et_preferential)
    EditText et_preferential;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    private final int REQUEST_SYSTEM_PIC = 1011;

    private List<ImgPathRes> bitmaps = new ArrayList<>();
    private BusCouponAdapter bitmapsAdapter;
    private String userCurrLevelId = "1"; //用户等级
    private String userCurrLevelName = ""; //用户等级名
    private String moneyLeve;
    private File file;
    private String base64FontStr = "data:image/jpg;base64,";
    private StringBuffer data_jpg = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_detail__bus);
        initView();
        requestCommodityDetail();
    }

    private void initView() {
        ButterKnife.bind(this);
        initHeadView(this, R.drawable.icon_back_r, "", 0,null);
        userCurrLevelId = SharedPreStorageMgr.getIntance().getStringValue(KeyConfig.USERCURRLEVELID);
        userCurrLevelName = SharedPreStorageMgr.getIntance().getStringValue(KeyConfig.USERCURRLEVELNAME);
        moneyLeve = getIntent().getStringExtra("moneyLeve");
        goodsId = getIntent().getStringExtra("id");
        positionCard = getIntent().getIntExtra("position", 0);
        srl_bus = findViewById(R.id.srl_bus);
        srl_bus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestCommodityDetail();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_coupon.setLayoutManager(linearLayoutManager);
        bitmapsAdapter = new BusCouponAdapter(bitmaps);
        rv_coupon.setAdapter(bitmapsAdapter);
        bitmapsAdapter.OnCloseClick(new BusCouponAdapter.IListenerClick() {
            @Override
            public void OnCloseClick(int i) {
                bitmaps.remove(i);
                bitmapsAdapter.setData(bitmaps);
            }
        });
        web_exchangeExplain = new ProgressWebView(this, srl_bus);
        frameLayout.addView(web_exchangeExplain);
    }

    private void requestCommodityDetail() {
        showDialog("加载中...");
        Map<String, String> map = Params.merchantInfoParams();
        map.put("goodsId",goodsId);
        OkHttpUtils.get()
                .url(Constants.busCommodityDetails)
                .headers(PublicParam.getParam())
                .params(map)
                .build().execute(new GenericsCallback<BusCommodityDetailInfoRes>(new JsonGenericsSerializator()) {

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(BusCommodityDetailInfoRes response, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        data = response.getData().get(0);
                        initHeadView(BusCommodityDetailActivity.this, R.drawable.icon_back_r, data.getBankName()+"", 0,"算一算");
                        Glide.with(context).load(data.getPicture()).into(iv_picture);
                        tv_thoroughfare.setText(data.getName());
                        // 0：秒结，1：核算结账，2，T+1结账
                        if (data.getSettlementType().trim().equals("0")) {
                            tv_explain.setText( data.getIntegral()+"积分起兑,秒结");
                        } else if (data.getSettlementType().trim().equals("1")) {
                            tv_explain.setText(data.getIntegral()+"积分起兑,核算结账");
                        } else if (data.getSettlementType().trim().equals("2")) {
                            tv_explain.setText(data.getIntegral()+"积分起兑,T+1结账");
                        } else {
                            tv_explain.setText( data.getIntegral()+"");
                        }

                        tv_money.setText( moneyLeve);
                        tv_grade.setText( "当前级别："+userCurrLevelName);

                        web_exchangeExplain.loadDataWithBaseURL(null, data.getReminder(), "text/html", "utf-8", null);

                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                    }
                } else {
                    IToast.getIToast().showIToast(response.getMsg());
                }
            }
        });
    }

    /**
     *  提交订单；
     * @param picture 图片地址 用逗号隔开（不用头部）
     * @param goodsId  商品id
     * @param exchangeMessage  兑换券 多个用三个*号隔开
     * @param qty 数量
     */
    private void orderSubmission(String picture,String goodsId,String exchangeMessage,String qty){
        showDialog("加载中...");
        Map<String, String> map = Params.merchantInfoParams();
        map.put("userId","7bd4d7a6-fd2b-4f21-b429-142e2cb7621e");
        map.put("picture",picture);
        map.put("goodsId",goodsId);
        map.put("exchangeMessage",exchangeMessage);
        map.put("qty",qty);
        map.put("type","2");
        OkHttpUtils.post()
                .url(Constants.busOrderSubmission)
                .headers(PublicParam.getParam())
                .params(map)
                .build().execute(new GenericsCallback<BusCommodityDetailInfoRes>(new JsonGenericsSerializator()) {

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(BusCommodityDetailInfoRes response, int id) {
                dismissDialog();
                if (response != null) {
                    //0000：成功，0001：失败,0002：商品已下架
                    if (response.getCode().equals("0000")){
                        IToast.getIToast().showIToast(response.getMsg());
                    }else if (response.getCode().equals("0001")){
                        IToast.getIToast().showIToast(response.getMsg());
                    }else if (response.getCode().equals("0002")){
                        IToast.getIToast().showIToast(response.getMsg());
                    }else {
                        IToast.getIToast().showIToast(response.getMsg());
                    }
                } else {
                    IToast.getIToast().showIToast(response.getMsg());
                }
            }
        });
    }

    @OnClick(R.id.tv_right_img)
    public void tv_right_img(){
        IToast.getIToast().showIToast("算一算");
    }

    @OnClick(R.id.iv_kf)
    public void iv_kf(){
        mIntent = new Intent(context,ServerMainActivity.class);
        startActivity(mIntent);
    }

    @OnClick(R.id.ll_addConvolutionCode)
    public void ll_addConvolutionCode(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //打开系统相册
            openAlbum();
        }
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SYSTEM_PIC);//打开系统相册

    }

    //提交订单；
    @OnClick(R.id.but_submit)
    public void but_submit(){
        String et_p = et_preferential.getText().toString().trim();
        String[] split = et_p.split(",");
        et_p = et_p.replace(",","***");
        int size = bitmaps.size();
        if (TextUtils.isEmpty(et_p) && size<=0){
            IToast.getIToast().showIToast("请上传券码");
            return;
        }
        for (int i =0;i<bitmaps.size();i++){
            if (i==0){
                data_jpg.append(bitmaps.get(i).getNetworkImgPath());
            }else {
                data_jpg.append(","+bitmaps.get(i).getNetworkImgPath());
            }
        }
        orderSubmission(data_jpg.toString(),goodsId,et_p,(split.length+bitmaps.size())+"");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SYSTEM_PIC && resultCode == RESULT_OK && null != data) {
            if (Build.VERSION.SDK_INT >= 19) {
                handleImageOnKitkat(data);
            } else {
                handleImageBeforeKitkat(data);
            }
        }
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片

    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            String bank = Base64BitmapUtil.bitmapToBase64(bitmap);
            uploadPictures(imagePath,base64FontStr+bank);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * @param
     * @param file
     *  上传图片；
     */
    private void uploadPictures(final String imgPath, String file) {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.busUploadImg)
                .headers(PublicParam.getParam())
                .params(Params.uploadImg(file))
                .build().execute(new GenericsCallback<UploadImgRes>(new JsonGenericsSerializator()) {

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(UploadImgRes response, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        ImgPathRes imgPathRes = new ImgPathRes();
                        imgPathRes.setNativeImgPath(imgPath);
                        imgPathRes.setNetworkImgPath(response.getData());
                        bitmaps.add(imgPathRes);
                        bitmapsAdapter.setData(bitmaps);
                        IToast.getIToast().showIToast(response.getMsg());
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                    }
                } else {
                    IToast.getIToast().showIToast(response.getMsg());
                }
            }
        });
    }

}
