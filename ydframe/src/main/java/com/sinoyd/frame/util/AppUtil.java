package com.sinoyd.frame.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.sinoyd.frame.application.BaseApplication;
import com.sinoyd.frame.config.ResConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：应用工具类
 */
public class AppUtil {
    public AppUtil() {
    }

    /**
     * 获取当前Application实例
     * @return
     */
    public static Application getApplicationContext() {
        return BaseApplication.getInstance();
    }

    /**
     * 获取本地文件存放路径
     * @return
     */
    public static String getStoragePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/sinoyd";
    }

    /**
     * 获取框架标题栏高度
     * @param context 上下文
     * @return 通知栏高度
     */
    public static int getTitleBarHeight(Context context){
        int id = ResConfig.getTitleBarHeight();
        int statusBarHeight = context.getResources().getDimensionPixelSize(id);context.getResources().getDimensionPixelOffset(id);
        return statusBarHeight;
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mWidth = dm.widthPixels;
        return mWidth;
    }

    /**
     * 获取当前app版本
     * @return
     */
    public static String getAppVersion(){
        return PhoneUtil.getVersionName(AppUtil.getApplicationContext());
    }

    /**
     * MD5加密
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
