package com.hxypay.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.customview.KeyPop1;
import com.hxypay.response.PublicRes;
import com.hxypay.response.UploadImgRes;
import com.hxypay.util.Base64BitmapUtil;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PictureUtil;
import com.hxypay.util.PublicParam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class RealNameActivity extends BaseActivity {

    private EditText mIdNameEt, mIdNoEt, mBankNoEt;
    private EditText mTeleEt, mAuthEt;
    private TextView mPayPwdText;//支付密码
    private ImageView mIdFontImg, mIdBackImg, mBankImg;
    private Button mSmsCodeBtn, mComBtn;

    private String idName, idNo, bankNo, tele, auth, payPwd;

    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String IMAGE_FILE_NAME = "cardPic.jpg";
    private Bitmap mBitmap;

    private int index = 1;//1正面  2反面 3卡
    private Bitmap mIdFontBitmap, mIdBackBitmap, mBankBitmap;
    private String idFont = "", idBack = "", bank = "";
    private String idFontUrl = "", idBackUrl = "", bankUrl = "";
    private String base64FontStr = "data:image/jpg;base64,";

    private KeyPop1 keyPop1;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.AUTH_CODE_S:
                    mSmsCodeBtn.setEnabled(false);
                    break;
                case 0:
                    mSmsCodeBtn.setText("获取验证码");
                    mSmsCodeBtn.setEnabled(true);
                    break;
                default:
                    mSmsCodeBtn.setText(msg.what + "s重新获取");
                    mSmsCodeBtn.setEnabled(false);
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "实名认证", 0,null);
        mIdNameEt = (EditText) findViewById(R.id.id_name_et);
        mIdNoEt = (EditText) findViewById(R.id.id_no_et);
        mBankNoEt = (EditText) findViewById(R.id.bank_no_et);
        mTeleEt = (EditText) findViewById(R.id.tele_et);
        mAuthEt = (EditText) findViewById(R.id.auth_et);
        mPayPwdText = (TextView) findViewById(R.id.pay_pwd_text);

        mIdFontImg = (ImageView) findViewById(R.id.id_font_img);
        mIdBackImg = (ImageView) findViewById(R.id.id_back_img);
        mBankImg = (ImageView) findViewById(R.id.bank_img);

        mSmsCodeBtn = (Button) findViewById(R.id.btn_sms_code);
        mComBtn = (Button) findViewById(R.id.btn_com);

        mPayPwdText.setOnClickListener(this);
        mIdFontImg.setOnClickListener(this);
        mIdBackImg.setOnClickListener(this);
        mBankImg.setOnClickListener(this);
        mSmsCodeBtn.setOnClickListener(this);
        mComBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pay_pwd_text:
                keyPop1 = new KeyPop1(context, new KeyPop1.KeyPopListener() {
                    @Override
                    public void onClick(String pwd) {
                        keyPop1.dismiss();
                        payPwd = pwd;
                        mPayPwdText.setText("******");
                    }

                    @Override
                    public void diss() {
                        keyPop1.dismiss();
                    }
                });
                keyPop1.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.id_font_img:
                index = 1;
                takePhoto();
                break;
            case R.id.id_back_img:
                index = 2;
                takePhoto();
                break;
            case R.id.bank_img:
                index = 3;
                takePhoto();
                break;
            case R.id.btn_sms_code:
                tele = mTeleEt.getText().toString();
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号");
                    return;
                }
                getSmsCode();
                break;
            case R.id.btn_com:
                idName = mIdNameEt.getText().toString();
                idNo = mIdNoEt.getText().toString();
                bankNo = mBankNoEt.getText().toString();
                tele = mTeleEt.getText().toString();
                auth = mAuthEt.getText().toString();
                if (TextUtils.isEmpty(idName)) {
                    IToast.getIToast().showIToast("请输入持卡人身份证姓名");
                    return;
                }
                if (TextUtils.isEmpty(idNo)) {
                    IToast.getIToast().showIToast("请输入持卡人身份证号码");
                    return;
                }
                if (TextUtils.isEmpty(bankNo)) {
                    IToast.getIToast().showIToast("请输入储蓄卡号");
                    return;
                }
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(auth)) {
                    IToast.getIToast().showIToast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(payPwd)) {
                    IToast.getIToast().showIToast("请设置支付密码");
                    return;
                }
                if (TextUtils.isEmpty(idFontUrl)) {
                    IToast.getIToast().showIToast("请上传身份证正面照");
                    return;
                }
                if (TextUtils.isEmpty(idBackUrl)) {
                    IToast.getIToast().showIToast("请上传身份证反面照");
                    return;
                }
                if (TextUtils.isEmpty(bankUrl)) {
                    IToast.getIToast().showIToast("请上传银行卡正面照");
                    return;
                }
                realName();
                break;
        }
    }

    private void realName() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.REAL_NAME_AUTH)
                .headers(PublicParam.getParam())
                .params(Params.merRealNameAuthParams(tele, auth, idName, idNo, bankNo, idFontUrl, idBackUrl, bankUrl, payPwd, "1"))
                .build().execute(new GenericsCallback<PublicRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(PublicRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        IToast.getIToast().showIToast("认证成功");
                        finish();
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }


    private void takePhoto() {
        PictureUtil.mkdirOSCRootDirectory();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 权限还没有授予，进行申请
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300); // 申请的 requestCode 为 300
        } else {
            // 权限已经申请，直接拍照
//            mPhotoPop.dismiss();
            imageCapture();
        }
    }

    private void imageCapture() {
        Intent intent;
        Uri pictureUri;
        //getMyPetRootDirectory()得到的是Environment.getExternalStorageDirectory() + File.separator+"MyPet"
        //也就是我之前创建的存放的文件夹
        File pictureFile = new File(PictureUtil.getJHRootDirectory(), IMAGE_FILE_NAME);
        // 判断当前系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //这一句非常重要
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //""中的内容是随意的，但最好用package名.provider名的形式，清晰明了
            pictureUri = FileProvider.getUriForFile(this,
                    "com.hxypay.fileprovider", pictureFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureUri = Uri.fromFile(pictureFile);
        }
        // 去拍照,拍照的结果存到oictureUri对应的路径中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        Log.e("take photo", "before take photo" + pictureUri.toString());
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void picGallery() {
        // 文件权限申请
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 权限还没有授予，进行申请
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200); // 申请的 requestCode 为 200
        } else {
            // 如果权限已经申请过，直接进行图片选择
//            mPhotoPop.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            // 判断系统中是否有处理该 Intent 的 Activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GET);
            } else {
                Toast.makeText(context, "未找到图片查看器", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mPhotoPop.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                    } else {
                        Toast.makeText(context, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    mPhotoPop.dismiss();
                }
                break;
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mPhotoPop.dismiss();
                    imageCapture();
                } else {
//                    mPhotoPop.dismiss();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相册选取
                case REQUEST_IMAGE_GET:
                    Uri uri = PictureUtil.getImageUri(this, data);
                    showUploadImage(uri);
                    //startPhotoZoom(uri);
//                    mBitmap = getBitmapFormUri(this, uri);
//                    if (index == 1) {
//                        mIdFontBitmap = mBitmap;
//                        mIdFontImg.setBackgroundDrawable(new BitmapDrawable(mBitmap));
//                    }
//                    if (index == 2) {
//                        mIdBackBitmap = mBitmap;
//                        mIdBackImg.setBackgroundDrawable(new BitmapDrawable(mBitmap));
//                    }
//                    if (index == 3) {
//                        mIdHandBitmap = mBitmap;
//                        mIdHandImg.setImageBitmap(mBitmap);
//                    }
//                    if (index == 4) {
//                        mBankBitmap = mBitmap;
//                        mBankImg.setImageBitmap(mBitmap);
//                    }
                    break;
                // 拍照
                case REQUEST_IMAGE_CAPTURE:
                    File pictureFile = new File(PictureUtil.getJHRootDirectory(), IMAGE_FILE_NAME);
                    Uri pictureUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pictureUri = FileProvider.getUriForFile(this,
                                "com.hxypay.fileprovider", pictureFile);
                    } else {
                        pictureUri = Uri.fromFile(pictureFile);
                    }
                    showUploadImage(pictureUri);
//                    mBitmap = getBitmapFormUri(this, pictureUri);
//                    if (index == 1) {
//                        mIdFontBitmap = mBitmap;
//                        mIdFontImg.setImageBitmap(mBitmap);
//                    }
//                    if (index == 2) {
//                        mIdBackBitmap = mBitmap;
//                        mIdBackImg.setImageBitmap(mBitmap);
//                    }
//                    if (index == 3) {
//                        mIdHandBitmap = mBitmap;
//                        mIdHandImg.setImageBitmap(mBitmap);
//                    }
//                    if (index == 4) {
//                        mBankBitmap = mBitmap;
//                        mBankImg.setImageBitmap(mBitmap);
//                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void showUploadImage(Uri imageUri) {
        if (imageUri != null) {
            if (index == 1) {
                mIdFontBitmap = getBitmapFormUri(this, imageUri);
                mIdFontImg.setBackground(new BitmapDrawable(mIdFontBitmap));
                idFont = Base64BitmapUtil.bitmapToBase64(mIdFontBitmap);
                uploadImg(base64FontStr + idFont);
            }
            if (index == 2) {
                mIdBackBitmap = getBitmapFormUri(this, imageUri);
                mIdBackImg.setBackground(new BitmapDrawable(mIdBackBitmap));
                idBack = Base64BitmapUtil.bitmapToBase64(mIdBackBitmap);
                uploadImg(base64FontStr + idBack);
            }
            if (index == 3) {
                mBankBitmap = getBitmapFormUri(this, imageUri);
                mBankImg.setBackground(new BitmapDrawable(mBankBitmap));
                bank = Base64BitmapUtil.bitmapToBase64(mBankBitmap);
                uploadImg(base64FontStr + bank);
            }
//            Glide.with(this).load(imageUri.toString()).thumbnail(0.3f).into(image);
        }
    }

    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) {
        Bitmap bitmap = null;
        try {
            InputStream input = ac.getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            int originalWidth = onlyBoundsOptions.outWidth;
            int originalHeight = onlyBoundsOptions.outHeight;
            if ((originalWidth == -1) || (originalHeight == -1))
                return null;
            //图片分辨率以480x800为标准
            float hh = 800f;//这里设置高度为800f
            float ww = 480f;//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (originalWidth / ww);
            } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
                be = (int) (originalHeight / hh);
            }
            if (be <= 0)
                be = 1;
            //比例压缩
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = be;//设置缩放比例
            bitmapOptions.inDither = true;//optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            input = ac.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    public void uploadImg(String fileBase64) {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.UPLOAD_IMG)
                .headers(PublicParam.getParam())
                .params(Params.uploadImg(fileBase64))
                .build().execute(new GenericsCallback<UploadImgRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(UploadImgRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (index == 1) {
                            IToast.getIToast().showIToast("身份证正面照上传成功");
                            idFontUrl = response.getData();
                        }
                        if (index == 2) {
                            IToast.getIToast().showIToast("身份证反面照上传成功");
                            idBackUrl = response.getData();
                        }
                        if (index == 3) {
                            IToast.getIToast().showIToast("银行卡正面照上传成功");
                            bankUrl = response.getData();
                        }
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }


    public void getSmsCode() {
        showDialog("加载中...");
        count();
        OkHttpUtils.post()
                .url(Constants.SMS_CODE)
                .headers(PublicParam.getParam())
                .params(Params.smsCodeParams(tele))
                .build().execute(new GenericsCallback<PublicRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(PublicRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        IToast.getIToast().showIToast("短信验证码已发送至：" + tele);
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }

    private void count() {
        mSmsCodeBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = Constants.AUTH_CODE_S; i >= 0; i--) {
                    handler.sendMessage(handler.obtainMessage(i));
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
