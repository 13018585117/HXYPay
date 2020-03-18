package com.hxypay.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.io.File;
import java.io.FileOutputStream;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
//import cn.sharesdk.tencent.qq.QQ;
//import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareUtil {

    private static final ShareUtil instance = new ShareUtil();

    private ShareUtil() {
    }

    public static ShareUtil getInstance() {
        return instance;
    }

    public void showShare1(final Context context, String platform, final int shareType, final String shareUrl, final String shareTitle, final String shareContent, final Bitmap logoBitmap) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams shareParams) {
                if (platform.getName().equals(Wechat.NAME)) {
                    shareParams.setShareType(shareType);//Platform.SHARE_WEBPAGE;
                    shareParams.setTitle(shareTitle);
                    shareParams.setText(shareContent);
                    shareParams.setImageData(logoBitmap);
                    shareParams.setUrl(shareUrl);
                }
                if (platform.getName().equals(WechatMoments.NAME)) {
                    shareParams.setShareType(shareType);
                    shareParams.setTitle(shareTitle);
                    shareParams.setText(shareContent);
                    shareParams.setImageData(logoBitmap);
                    shareParams.setUrl(shareUrl);
                }

//                if (platform.getName().equals(QQ.NAME)) {
//                    saveImageToGallery(context, logoBitmap, "logo");
//                    shareParams.setTitle("立即注册" + context.getResources().getString(R.string.app_name));
//                    shareParams.setTitleUrl(shareUrl);
//                    shareParams.setText("银联收款，完美还款，体验智能信用卡管理!");
//                    shareParams.setImagePath(Environment.getExternalStorageDirectory() + "/JHPay/" + "logo");
//                }
//                if (platform.getName().equals(QZone.NAME)) {
//                    saveImageToGallery(context, logoBitmap, "logo");
//                    shareParams.setTitle("立即注册" + context.getResources().getString(R.string.app_name));
//                    shareParams.setTitleUrl(shareUrl);
//                    shareParams.setText("银联收款，完美还款，体验智能信用卡管理!");
//                    shareParams.setImagePath(Environment.getExternalStorageDirectory() + "/JHPay/" + "logo");
//                    shareParams.setSite(context.getResources().getString(R.string.app_name));
//                    shareParams.setSiteUrl(shareUrl);
//                }
            }
        });
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        //启动分享
        oks.show(context);
    }



    //保存图片；
    public void saveImageToGallery(final Context context, Bitmap bitMap, String fileName) {// E0F2FE

        if (!FileUtil.hasSDCard()) {
            IToast.getIToast().showIToast("Sorry！保存失败");
            return;
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "ZBPay");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            IToast.getIToast().showIToast("Sorry！保存失败");
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (Exception e) {
            IToast.getIToast().showIToast("Sorry！保存失败");
            e.printStackTrace();
        }
        IToast.getIToast().showIToast("保存成功");
        // 最后通知图库更新
        // 通知图库更新
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            mediaScanIntent.setData(uri);
                            context.sendBroadcast(mediaScanIntent);
                        }
                    });
        } else {
            String relationDir = file.getParent();
            File file1 = new File(relationDir);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + appDir.getAbsolutePath())));
        }

//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir.getAbsolutePath())));
    }


    public void showShare2(final Context context, String platform, final String imgPath, final String shareUrl) {
//        saveImageToGallery(myShot(CustomSharePicActivity.this));
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams shareParams) {
                if (platform.getName().equals(Wechat.NAME)) {
                    shareParams.setShareType(Platform.SHARE_IMAGE);
                    shareParams.setTitle("立即注册" + context.getString(R.string.app_name));
                    shareParams.setText("体验新时代智能还款！告别逾期，钱包休息！");
                    shareParams.setImagePath(imgPath);// 确保SDcard下面存在此张图片
                }
                if (platform.getName().equals(WechatMoments.NAME)) {
                    shareParams.setShareType(Platform.SHARE_IMAGE);
                    shareParams.setTitle("立即注册" + context.getString(R.string.app_name));
                    shareParams.setText("体验新时代智能还款！告别逾期，钱包休息！");
                    shareParams.setImagePath(imgPath);// 确保SDcard下面存在此张图片
                }
//                if (platform.getName().equals(QQ.NAME)) {
//                    shareParams.setImagePath(imgPath);// 确保SDcard下面存在此张图片
//                }
//                if (platform.getName().equals(QZone.NAME)) {
//                    shareParams.setText(context.getString(R.string.app_name));
//                    shareParams.setImagePath(imgPath);// 确保SDcard下面存在此张图片
//                    shareParams.setSite(context.getString(R.string.app_name));
//                    shareParams.setSiteUrl(shareUrl);
//                }
            }
        });
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        //启动分享
        oks.show(context);
    }
}
