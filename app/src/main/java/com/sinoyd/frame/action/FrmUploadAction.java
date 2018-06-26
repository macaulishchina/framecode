package com.sinoyd.frame.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.sinoyd.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者： 王一凡
 * 创建时间： 2017/10/31
 * 版权： 江苏远大信息股份有限公司
 * 描述： 附件上传逻辑控制
 */
public class FrmUploadAction {
    public static final String Attach_INTENT = "attach_key";

    public static final int OpenCamera_REQUESTCODE = 0;

    public static final int OpenPhoto_REQUESTCODE = 1;

    public FrmUploadAction(){

    }

    /**
     * 打开摄像头拍照
     * @param activity
     * @param file
     */
    public static void openCamera(Activity activity, File file) {
        //拍照
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, OpenCamera_REQUESTCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过手机相册获取图片
     * @param activity
     */
    public static void openPic(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, OpenPhoto_REQUESTCODE);
    }

    /**
     * 获取指定文件大小(b)
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 根据文件类型获取文件图标
     * @param filename
     * @return
     */
    public Bitmap getImgByFileName(Context context, String filename) {
        String dotName = "article";
        if (!TextUtils.isEmpty(filename) && filename.contains(".")){
            dotName = filename.substring(filename.lastIndexOf(".") + 1);
        }
        Bitmap icon = null;
        InputStream in = null;
        try {
            in = context.getResources().getAssets().open("imgRes/attachicon/ico_" + dotName + "_large@2x.png");
            icon = BitmapFactory.decodeStream(in);
            if (icon == null) {
                in = context.getResources().getAssets().open("imgRes/attachicon/ico_article_large@2x.png");
                icon = BitmapFactory.decodeStream(in);
            }
        } catch (IOException e) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_netdisk_article);
        }
        return icon;
    }

    public String getFileNameByUrl(String url){
        String filename = "default";
        if (!TextUtils.isEmpty(url) && url.contains("/")){
            filename = url.substring(url.lastIndexOf("/") + 1);
        }
        return filename;
    }
}
