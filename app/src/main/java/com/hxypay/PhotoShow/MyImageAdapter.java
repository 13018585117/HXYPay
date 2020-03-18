package com.hxypay.PhotoShow;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hxypay.R;
import com.hxypay.customview.CardSettingPop;
import com.hxypay.customview.IToast;
import com.hxypay.customview.SavePop;
import com.hxypay.util.MediaScanner;
import com.hxypay.util.ShareUtil;
import com.hxypay.util.Utils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.EasyPermissions;

public class MyImageAdapter extends PagerAdapter {
    public static final String TAG = MyImageAdapter.class.getSimpleName();
    private List<String> imageUrls;
    private Activity activity;
    private MediaScanner mediaScanner;
    private View view;
    private String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public MyImageAdapter(List<String> imageUrls, Activity activity, MediaScanner mediaScanner,View view) {
        this.imageUrls = imageUrls;
        this.activity = activity;
        this.mediaScanner = mediaScanner;
        this.view = view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        String url = imageUrls.get(position);
        final ImageView imageView = new ImageView(activity);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(activity).load(imageUrls.get(position)).into(imageView);
        container.addView(imageView);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final SavePop mCardSettingPop = new SavePop(activity, new SavePop.SaveListener() {
                    @Override
                    public void saveCard(SavePop dialog) {
                        saveImg(imageUrls.get(position),position);
                    }
                });
                mCardSettingPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);

                return false;
            }
        });
        return imageView;
    }

    @Override
    public int getCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private void saveImg(final String url, final int posion){
        getPermission();
        try {
            Glide.with(activity).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                    returnBitMap(bitmap,posion);
                    Log.e("保存",111+"");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            IToast.getIToast().showIToast("保存失败");
        }
    }
    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            //已经打开权限
//            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(activity, "需要获取您的相册使用权限", 1, permissions);
        }
    }
    public Bitmap returnBitMap(final Bitmap bitmap , final int index) {
        try {
            ShareUtil.getInstance().saveImageToGallery(activity, bitmap, index+"source_material"+ System.currentTimeMillis() +".jpg");
            String[] filePaths = new String[]{Environment.getExternalStorageDirectory().getPath()+ File.separator +"HXYPay"};
            String[] mimeTypes = new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg")};
            mediaScanner.scanFiles(filePaths,mimeTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
