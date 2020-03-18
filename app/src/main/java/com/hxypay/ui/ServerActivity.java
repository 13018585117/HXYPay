package com.hxypay.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.util.QRCodeUtil;

import static com.hxypay.util.Constants.BASE_URL;

public class ServerActivity extends BaseActivity {
    private ImageView mQrcodeImg;
    private TextView mWxNoText;
    private String wxNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        initView();
    }
    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "微信客服", 0,null);
        mQrcodeImg = (ImageView) findViewById(R.id.wx_qrcode_img);
        mWxNoText = (TextView) findViewById(R.id.wx_no_tx);

        wxNo = getIntent().getExtras().getString("wxNo");
        mWxNoText.setText("好信用管家");
        Glide.with(this).load(wxNo).into(mQrcodeImg);
//        createEwm(wxNo);

    }


    private void createEwm(final String url) {

        // 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        final Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.wx);
        // 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = QRCodeUtil.createQRImage(url, 500, 500, null);
                if (bitmap != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mQrcodeImg.setImageBitmap(bitmap);
                            mQrcodeImg.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();
    }

}
