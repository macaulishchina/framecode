package com.sinoyd.frame.util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.util
 */
public class PhoneUtil {
    public PhoneUtil() {
    }

    public static boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getDeviceId(Context con) {
        TelephonyManager tm = (TelephonyManager)con.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId() != null && !"".equals(tm.getDeviceId())?tm.getDeviceId():getMacAddress(con);
    }

    public static String getMacAddress(Context context) {
        try {
            ArrayList e = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var3 = e.iterator();

            while(var3.hasNext()) {
                NetworkInterface nif = (NetworkInterface)var3.next();
                if(nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if(macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    byte[] var9 = macBytes;
                    int var8 = macBytes.length;

                    for(int var7 = 0; var7 < var8; ++var7) {
                        byte b = var9[var7];
                        res1.append(String.format("%02X:", new Object[]{Byte.valueOf(b)}));
                    }

                    if(res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }

                    return res1.toString();
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return "";
    }

    public static int getPhoneWidth(Context con) {
        WindowManager wm = (WindowManager)con.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getPhoneHeight(Context con) {
        WindowManager wm = (WindowManager)con.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static void invokeSystemBrowser(Context con, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        con.startActivity(intent);
    }

    public static void sendMsgUsePhoneSelf(Context con, String phonenum, String body) {
        Uri smsToUri = Uri.parse("smsto:" + phonenum);
        Intent mIntent = new Intent("android.intent.action.SENDTO", smsToUri);
        mIntent.putExtra("sms_body", body);
        con.startActivity(mIntent);
    }

    public static PackageInfo getPackageInfo(Context con) {
        PackageManager packageManager = con.getPackageManager();
        PackageInfo packInfo = null;

        try {
            packInfo = packageManager.getPackageInfo(con.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException var4) {
            var4.printStackTrace();
        }

        return packInfo;
    }

    public static String getPackageName(Context con) {
        return getPackageInfo(con).packageName;
    }

    public static String getVersionName(Context con) {
        PackageManager packageManager = con.getPackageManager();
        PackageInfo packInfo = null;

        try {
            packInfo = packageManager.getPackageInfo(con.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException var4) {
            var4.printStackTrace();
        }

        String version = packInfo.versionName;
        return version;
    }

    public static boolean isTablet(Context con) {
        return (con.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static void cancelNotify(String id, String type, Context con) {
        NotificationManager manger = (NotificationManager)con.getSystemService(Context.NOTIFICATION_SERVICE);
        if(!TextUtils.isEmpty(id) && !TextUtils.isEmpty(type)) {
            int notifyid = 1;

            try {
                notifyid = Integer.parseInt(id) + Integer.parseInt(type);
            } catch (NumberFormatException var6) {
                var6.printStackTrace();
            }

            manger.cancel(notifyid);
        } else {
            manger.cancelAll();
        }

    }

    public static void cancelAllNotify(Context con) {
        cancelNotify((String)null, (String)null, con);
    }
}
