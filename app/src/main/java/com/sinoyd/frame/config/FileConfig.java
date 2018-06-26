package com.sinoyd.frame.config;

import android.util.Log;

import com.sinoyd.frame.util.AppUtil;
import com.sinoyd.frame.util.DateUtil;

import java.io.File;
import java.util.Date;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：
 */
public class FileConfig {
    public static final String TAG = FileConfig.class.getName();

    public static void createFolers(String[] folders) {

        for (String folder : folders) {
            File f = new File(AppUtil.getStoragePath() + "/" + folder);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        //clearLocalLogs();
    }

    /**
     * 初始化文件夹
     * attach：附件目录，保存各模块下载、上传的附件
     * update：更新包保存路径
     * log：日志文件保存路径
     */
    public static void initFolders() {
        createFolers(new String[]{"attach", "update", "log", "upload"});
    }

    //清理5天前的日志
    static void clearLocalLogs() {
        File f = new File(AppUtil.getStoragePath() + "/log");
        File[] files = f.listFiles();
        if(files!=null) {
            for (File file : files) {
                if (file.exists() && file.isFile()) {
                    String FileName = file.getName().substring(0, file.getName().indexOf("."));
                    String todayName = DateUtil.convertDate(new Date(), "yyyy-MM-dd");
                    Date fileDate = DateUtil.convertString2Date(FileName, "yyyy-MM-dd");
                    Date todayDate = DateUtil.convertString2Date(todayName, "yyyy-MM-dd");
                    if (fileDate != null && todayDate != null) {
                        if (todayDate.getTime() - fileDate.getTime() >= 86400000 * 5) {
                            file.delete();
                        }
                    }
                }
            }
        }
    }
}
