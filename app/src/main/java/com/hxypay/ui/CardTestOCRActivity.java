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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.response.BankCardRes;
import com.hxypay.response.CT1Res;
import com.hxypay.response.IdCardFontRes;
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

public class CardTestOCRActivity extends BaseActivity {

    private ImageView mIdFontImg, mBankImg;
    private Button mComBtn;

    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String IMAGE_FILE_NAME = "cardTestPic.jpg";
    private Bitmap mBitmap;

    private int index = 1;//1正面  2反面 3卡
    private Bitmap mIdFontBitmap, mBankBitmap;
    private String idFont = "", bank = "";
    private String idFontUrl = "", bankUrl = "";
    private String base64FontStr = "data:image/jpg;base64,";
    private String idName, idNo, cardNo;
    private Intent intent;
    private Uri pictureUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_test_ocr);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "卡测评", 0,null);

        mIdFontImg = (ImageView) findViewById(R.id.id_font_img);
        mBankImg = (ImageView) findViewById(R.id.bank_img);

        mComBtn = (Button) findViewById(R.id.btn_com);

        mIdFontImg.setOnClickListener(this);
        mBankImg.setOnClickListener(this);
        mComBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_font_img:
                index = 1;
              /*  intent = new Intent(CardTestOCRActivity.this, CropActivity.class);
                intent.putExtra(CameraConfig.RATIO_WIDTH, 855);
                intent.putExtra(CameraConfig.RATIO_HEIGHT, 541);
                intent.putExtra(CameraConfig.PERCENT_LARGE, 0.8f);
                intent.putExtra(CameraConfig.MASK_COLOR, 0x2f000000);
                intent.putExtra(CameraConfig.RECT_CORNER_COLOR, 0xff00ff00);
                intent.putExtra(CameraConfig.TEXT_COLOR, 0xffffffff);
                intent.putExtra(CameraConfig.HINT_TEXT, "请将方框对准证件拍照");
                intent.putExtra(CameraConfig.IMAGE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath()+"/CameraCardCrop/"+System.currentTimeMillis()+".jpg");
                startActivityForResult(intent, 0x22);*/
                takePhoto();
                break;
            case R.id.bank_img:
                index = 3;
            /*    intent = new Intent(CardTestOCRActivity.this, CropActivity.class);
                intent.putExtra(CameraConfig.RATIO_WIDTH, 855);
                intent.putExtra(CameraConfig.RATIO_HEIGHT, 541);
                intent.putExtra(CameraConfig.PERCENT_LARGE, 0.8f);
                intent.putExtra(CameraConfig.MASK_COLOR, 0x2f000000);
                intent.putExtra(CameraConfig.RECT_CORNER_COLOR, 0xff00ff00);
                intent.putExtra(CameraConfig.TEXT_COLOR, 0xffffffff);
                intent.putExtra(CameraConfig.HINT_TEXT, "请将方框对准证件拍照");
                intent.putExtra(CameraConfig.IMAGE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath()+"/CameraCardCrop/"+System.currentTimeMillis()+".jpg");
                startActivityForResult(intent, 0x01);*/
                takePhoto();
                break;
            case R.id.btn_com:
//                if (TextUtils.isEmpty(idFontUrl)) {
//                    IToast.getIToast().showIToast("请上传身份证正面照");
//                    return;
//                }
//                if (TextUtils.isEmpty(bankUrl)) {
//                    IToast.getIToast().showIToast("请上传银行卡正面照");
//                    return;
//                }
                cardTestGetMoney();
                break;
        }
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

    private void cardTestGetMoney() {
        OkHttpUtils.post()
                .url(Constants.CARD_TEST_MONEY_AND_ORDERID)
                .headers(PublicParam.getParam())
                .params(Params.cardTestMoneyAndOrderId("1"))
                .build().execute(new GenericsCallback<CT1Res>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(CT1Res response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mIntent = new Intent(context, CardTestOCRInfoActivity.class);
                        mIntent.putExtra("idName", idName);
                        mIntent.putExtra("idNo", idNo);
                        mIntent.putExtra("cardNo", cardNo);
                        mIntent.putExtra("money", response.getData().getMoney());

//                        mIntent.putExtra("idName", "111");
//                        mIntent.putExtra("idNo", "111");
//                        mIntent.putExtra("cardNo", "111");
//                        mIntent.putExtra("money", "111");

                        startActivity(mIntent);
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



    private void imageCapture() {
        Intent intent;
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode== -1) {
            showUploadImage(pictureUri);
          /*  if (requestCode == 0x22) {
                String path = data.getStringExtra(CameraConfig.IMAGE_PATH);
                Uri uri = Uri.parse("file://" + path);
                mIdFontImg.setImageURI(uri);
                showUploadImage(uri);
            }
            if (requestCode == 0x01) {
                String path = data.getStringExtra(CameraConfig.IMAGE_PATH);
                Uri uri = Uri.parse("file://" + path);
                mBankImg.setImageURI(uri);
                showUploadImage(uri);
            }*/
        }
    }

    private void showUploadImage(Uri imageUri) {
        if (imageUri != null) {
            if (index == 1) {
                mIdFontBitmap = getBitmapFormUri(this, imageUri);

                String path = PictureUtil.getAbsoluteImagePath(CardTestOCRActivity.this, imageUri);
                int jd = PictureUtil.readPictureDegree(path);
                mIdFontBitmap = PictureUtil.rotaingImageView(jd,mIdFontBitmap);


                mIdFontImg.setBackground(new BitmapDrawable(mIdFontBitmap));
                idFont = Base64BitmapUtil.bitmapToBase64(mIdFontBitmap);
                uploadIdCardFontImg(base64FontStr + idFont);
            }
            if (index == 3) {
                mBankBitmap = getBitmapFormUri(this, imageUri);

                String path = PictureUtil.getAbsoluteImagePath(CardTestOCRActivity.this, imageUri);
                int jd = PictureUtil.readPictureDegree(path);
                mBankBitmap = PictureUtil.rotaingImageView(jd,mBankBitmap);

                mBankImg.setBackground(new BitmapDrawable(mBankBitmap));
                bank = Base64BitmapUtil.bitmapToBase64(mBankBitmap);
                uploadBankCardImg(base64FontStr + bank);
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


    public void uploadIdCardFontImg(String fileBase64) {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.ID_CARD_FONT_IMG)
                .headers(PublicParam.getParam())
                .params(Params.uploadImg(fileBase64))
                .build().execute(new GenericsCallback<IdCardFontRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(IdCardFontRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        idFontUrl = response.getData().getImages();
                        idName = response.getData().getUsername();
                        idNo = response.getData().getIdCard();
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


    public void uploadBankCardImg(String fileBase64) {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.BANK_CARD_IMG)
                .headers(PublicParam.getParam())
                .params(Params.uploadImg(fileBase64))
                .build().execute(new GenericsCallback<BankCardRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(BankCardRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        cardNo = response.getData().getBankCard();
                        bankUrl = response.getData().getImages();
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

}
