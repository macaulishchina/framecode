package com.sinoyd.frame.config;

import com.sinoyd.frame.util.ResManager;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：资源文件-反射 名称配置类
 */
public class ResConfig {
    public ResConfig(){

    }

    public static int getBaseLayoutId() {
        return ResManager.getLayoutInt("frmbase");
    }

    public static int getBaseContentId() {
        return ResManager.getIdInt("baseContent");
    }

    public static int getStatusWifiId() {
        return ResManager.getIdInt("frm_wifi_round");
    }

    public static int getToastBgColor() {
        return ResManager.getDrawableInt("toastbg");
    }

    public static int getToastWarningBgColor() {
        return ResManager.getDrawableInt("toastworningbg");
    }

    public static int getTitleBarHeight() {
        return ResManager.getDimenInt("frame_nb_height");
    }
}
