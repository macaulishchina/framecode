package com.sinoyd.frame.util;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：日志工具类
 */
public class LogUtil {
    public LogUtil() {
    }

    public static void frm_log(Class cls, String log) {
        Log.i(cls.getName(), "[远大移动框架A7]->" + log);
    }

    public synchronized static void writeLogThread(final String tag, final String log){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log2Storage(tag + log);
            }
        }).start();
    }

    public static void Log2Storage(String log) {
        try {
            String e = AppUtil.getStoragePath() + "/log";
            IOHelp.FoldCreate(e);
            String logName = DateUtil.convertDate(new Date(), "yyyy-MM-dd") + ".log";
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(e + "/" + logName, true)));
            String dataTag = "[" + DateUtil.convertDate(new Date(), DateUtil.DateFormat_24) + "]";
            pw.write(dataTag);
            pw.write(log);
            pw.write("\r\n");
            pw.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }
}
