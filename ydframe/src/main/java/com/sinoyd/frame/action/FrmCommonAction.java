package com.sinoyd.frame.action;

import com.google.gson.JsonObject;
import com.sinoyd.frame.config.AppBaseConfig;
import com.sinoyd.frame.config.FrmKeysConfig;
import com.sinoyd.frame.db.FrmDBService;
import com.sinoyd.frame.util.HttpUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：程序主控制类 #处理网络请求、获取用户登陆信息#
 */
public class FrmCommonAction {
    /**
     * POST请求标志
     */
    public static final int POST = 1;
    /**
     * GET请求标志
     */
    public static final int GET = 2;
    /**
     * PUT请求标志
     */
    public static final int PUT = 3;
    /**
     * DELETE请求标志
     */
    public static final int DELETE = 4;

    /**
     * 判断用户是否登录
     * @return
     */
    public static boolean isUserLogin() {
        return FrmDBService.getConfigValue(FrmKeysConfig.ISLogin).equals("1");
    }

    /**
     * 设置用户已登陆
     * @return
     */
    public static void setUserLogin() {
       FrmDBService.setConfigValue(FrmKeysConfig.ISLogin, "1");
    }

    /**
     * 设置用户登出
     * @return
     */
    public static void setUserLogout() {
        FrmDBService.setConfigValue(FrmKeysConfig.ISLogin, "");
        FrmDBService.setConfigValue(FrmKeysConfig.UserToken, "");
        FrmDBService.setConfigValue(FrmKeysConfig.UserName, "");
        FrmDBService.setConfigValue(FrmKeysConfig.RoleId, "");
    }

    /**
     * 获取用户当前token
     * @return
     */
    public static String getUserToken(){
        return FrmDBService.getConfigValue(FrmKeysConfig.UserToken);
    }

    @Deprecated
    public static JsonObject requestPost(JsonObject paras, String method) {
        return request(paras, method, AppBaseConfig.getDefaultURL(), FrmCommonAction.POST);
    }

    @Deprecated
    public static JsonObject requestGet(JsonObject paras, String method) {
        return request(paras, method, AppBaseConfig.getDefaultURL(), FrmCommonAction.GET);
    }

    /**
     * 基于HttpURLConnection的网络请求通用方法
     * @param paras 请求携带参数
     * @param method 请求方法名称
     * @param requestType GET/POST/PUT
     * @return
     */
    public static JsonObject request(JsonObject paras, String method, int requestType) {
        return request(paras, method, AppBaseConfig.getDefaultURL(), requestType);
    }

    /**
     * 基于HttpURLConnection的网络请求通用方法
     * @param paras 请求携带参数
     * @param method 请求方法名称
     * @param url 请求地址
     * @param requestType GET/POST/PUT
     * @return
     */
    public static JsonObject request(JsonObject paras, String method, String url, int requestType) {
        JsonObject bs = null;
        if (!url.endsWith(method)) {
            url += "/" + method;
        }
        try {
            if(requestType == POST || requestType == PUT){
                //POST、PUT请求
                String requestBody = "";
                if(paras != null){
                    requestBody = paras.toString();
                }
                bs = HttpUtil.request(url, requestBody, requestType);
            } else if(requestType == GET || requestType == DELETE) {
                //GET、DELETE请求
                if (paras != null) {
                    url += "?";
                    Set set = paras.entrySet();
                    Iterator sIterator = set.iterator();
                    while (sIterator.hasNext()) {
                        Map.Entry me = (Map.Entry) sIterator.next();
                        // 获得key
                        String key = me.getKey().toString();
                        // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                        String value = paras.get(key).getAsString();
                        if (!sIterator.hasNext()) {
                            //最后一个元素
                            url += (key + "=" + value);
                        } else {
                            url += (key + "=" + value + "&");
                        }
                    }
                }
                bs = HttpUtil.request(url, null, requestType);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return bs;
    }
}
