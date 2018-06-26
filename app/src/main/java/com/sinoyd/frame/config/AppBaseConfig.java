package com.sinoyd.frame.config;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述： app基础信息配置（自定义修改）
 */
public class AppBaseConfig {

    /**
     * 接口地址（个性化配置 必须修改）
     * @return
     */
    public static String getDefaultURL() {
        return "https://om.envchina.com/api";
    }

    /**
     * 图片地址（个性化配置 必须修改）
     * @return
     */
    public static String getDefaultImageURL() {
        return "https://om.envchina.com/storage/";
    }
}
