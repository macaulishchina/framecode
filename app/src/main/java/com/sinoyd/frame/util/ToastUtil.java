package com.sinoyd.frame.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sinoyd.frame.config.ResConfig;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：Toast工具类
 */
public class ToastUtil {
    public Context context;

    private ToastUtil(Context context, CharSequence message, int duration, int bgColor) {
        try {
            if(message == null || context == null) {
                return;
            }

            this.context = context;
            Toast e = new Toast(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(ResManager.getLayoutInt("frmtoast"), (ViewGroup)null);
            TextView tv = (TextView)v.findViewById(ResManager.getIdInt("messageItem"));
            tv.setBackgroundResource(bgColor);
            tv.setText(message);
            e.setDuration(duration);
            e.setView(v);
            e.show();
        } catch (Exception var9) {
            Toast.makeText(context, message, duration).show();
        }

    }

    public static void show(Context context, CharSequence message, int duration, int bgColor) {
        if(message != null) {
            new ToastUtil(context, message, duration, bgColor);
        }
    }

    public static void showShort(Context context, CharSequence message) {
        show(context, message, 0, ResConfig.getToastBgColor());
    }

    public static void showLong(Context context, CharSequence message) {
        show(context, message, 1, ResConfig.getToastBgColor());
    }

    public static void showWorning(Context context, CharSequence message) {
        show(context, message, 0, ResConfig.getToastWarningBgColor());
    }
}
