package com.sinoyd.frame.application;

import android.app.Application;

import com.sinoyd.frame.util.LogUtil;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：Application基类
 */
public class BaseApplication extends Application{
    private static Application instance;

    public BaseApplication() {
    }

    public static synchronized void initializeInstance(Application application) {
        if(instance == null) {
            instance = application;
        }

    }

    public static synchronized Application getInstance() {
        if(instance == null) {
            throw new IllegalStateException("Application没有初始化");
        } else {
            return instance;
        }
    }

    public void onCreate() {
        super.onCreate();
        initializeInstance(this);
        LogUtil.frm_log(this.getClass(), "Application启动");
    }
}
