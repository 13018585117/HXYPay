package com.hxypay.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类
 */
public class FileUtil {
    public static final String TAG = "fileUtil";


    public static boolean hasSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 如果sd卡不存在，就保存在手机存储中
     *
     * @param context
     */
//    public static void FileCache(Context context) {
//        // 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
//        // 没有SD卡就放在系统的缓存目录中
//        File cacheDir = null;
//        if (hasSDCard()) {
//            cacheDir = new File(Constants.DIR_PICTURE);
//        } else {
//            cacheDir = context.getCacheDir();
//        }
//        if (!cacheDir.exists()) {
//            cacheDir.mkdirs();
//        }
//    }

//	/**
//	 * 删除SD卡或者手机的缓存图片和目录
//	 */
//	public void deleteFile() {
//		File dirFile = new File(Constants.SIGN_PICTURE);
//		if (!dirFile.exists()) {
//			return;
//		}
//		if (dirFile.isDirectory()) {
//			String[] children = dirFile.list();
//			for (int i = 0; i < children.length; i++) {
//				new File(dirFile, children[i]).delete();
//			}
//		}
//
//		dirFile.delete();
//	}

    /**
     * 获取SD卡路径
     *
     * @return /sdcard/
     */
    public static String getSDCardPath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().toString() + "/";
        }
        return null;
    }

    /**
     * 创建文件夹
     *
     * @param dirPath
     */
    public static String creatDir2SDCard(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {// 判断文件是否存在
            file.mkdirs();
        }
        return dirPath;
    }

    /**
     * 创建文件
     * <p>
     * 如果是/sdcard/download/123.doc则只需传入filePath=download/123.doc
     *
     * @param filePath 文件路径
     * @return 创建文件的路径
     * @throws IOException
     */
    public static String creatFile2SDCard(String filePath) throws IOException {
        // 无论传入什么值 都是从根目录开始 即/sdcard/+filePath
        // 创建文件路径包含的文件夹
        String filedir = creatDir2SDCard(getFileDir(filePath));
        String fileFinalPath = filedir + getFileName(filePath);
        File file = new File(fileFinalPath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return fileFinalPath;
    }

    /**
     * 获取文件目录路径
     *
     * @param filePath
     * @return
     */
    private static String getFileDir(String filePath) {
        if (filePath.startsWith(getSDCardPath())) {
            return filePath.replace(getFileName(filePath), "");
        }
        return getSDCardPath() + filePath.replace(getFileName(filePath), "");
    }

    /**
     * 获取文件名
     *
     * @param filePath
     * @return
     */
    private static String getFileName(String filePath) {
        int index = 0;
        String tempName = "";
        if ((index = filePath.lastIndexOf("/")) != -1) {
            // 如果有后缀名才
            tempName = filePath.substring(index + 1);
        }
        return tempName.contains(".") ? tempName : "";
    }


    public static boolean delDir(File f) {
        try {
            if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
                if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                    f.delete();
                } else {// 若有则把文件放进数组，并判断是否有下级目录
                    File delFile[] = f.listFiles();
                    int i = f.listFiles().length;
                    for (int j = 0; j < i; j++) {
                        if (delFile[j].isDirectory()) {
                            delDir(delFile[j]);// 递归调用del方法并取得子目录路径
                        }
                        delFile[j].delete();
                    }// 删除文件
                    f.delete();
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取手机内置SD卡路径
     */
    public static String getSDCardPath(Context c) {
        File sdDir = null;
        String sdStatus = Environment.getExternalStorageState();
        boolean sdCardExist = sdStatus
                .equals(Environment.MEDIA_MOUNTED);

        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }
        return "";
    }

    /**
     * bitma转file
     *
     * @param bitmap
     * @return
     */
    public static File saveBitmapFile(Context c, Bitmap bitmap) {
        File file = new File(getSDCardPath(c) + File.separator + "curr.jpg");//将要保存图片的路径
        try {
            file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 删除file
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            }
        } else {

        }
    }

    /**
     * 判断SD卡是否存在
     *
     * @return boolean
     */
    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static void createNewDir(String dir) {
        if (!checkSDCard()) {
            return;
        }
        if (null == dir) {
            return;
        }
        File f = new File(dir);
        if (!f.exists()) {
            String[] pathSeg = dir.split(File.separator);
            String path = "";
            for (String temp : pathSeg) {
                if (TextUtils.isEmpty(temp)) {
                    path += File.separator;
                    continue;
                } else {
                    path += temp + File.separator;
                }
                File tempPath = new File(path);
                if (tempPath.exists() && !tempPath.isDirectory()) {
                    tempPath.delete();
                }
                tempPath.mkdirs();
            }
        } else {
            if (!f.isDirectory()) {
                f.delete();
                f.mkdirs();
            }
        }
    }


}
