package com.sinoyd.frame.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sinoyd.frame.application.BaseApplication;
import com.sinoyd.frame.application.FrmApplication;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/6
 * 版权： 江苏远大信息股份有限公司
 * 描述： json解析工具类
 */
public class JsonHelp {
    public JsonHelp(){

    }
    public static ArrayList DocumentJson(JsonArray objectArray, Class<?> c, String itemTitle) {
        ArrayList list = new ArrayList();
        Gson gson = new Gson();
        Iterator var8 = objectArray.iterator();

        while(var8.hasNext()) {
            JsonElement je = (JsonElement)var8.next();
            JsonObject object = je.getAsJsonObject();
            if(!(itemTitle==null||itemTitle.isEmpty())){
                object = object.get(itemTitle).getAsJsonObject();
            }
            String tempStr = object.toString();

            try {
                Object e = c.newInstance();
                e = gson.fromJson(tempStr, e.getClass());
                list.add(e);
            } catch (InstantiationException var10) {
                var10.printStackTrace();
            } catch (IllegalAccessException var11) {
                var11.printStackTrace();
            }
        }

        return list;
    }

    public static Object DocumentJson(JsonObject jsonObject, Class<?> c) {
        Gson gson = new Gson();
        Object nc = null;

        try {
            nc = c.newInstance();
            Class e = nc.getClass();
            nc = gson.fromJson(jsonObject.toString(), e);
        } catch (Exception var5) {
            var5.printStackTrace();
            nc = null;
        }

        return nc;
    }

    public static String DocumentJson(JsonObject jsonObject, String key) {
        String value = "";

        try {
            if(jsonObject != null && jsonObject.get(key) != null) {
                value = jsonObject.get(key).getAsString();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return value;
    }

    public static boolean checkResult(Object obj) {
        JsonObject jsonObject = (JsonObject) obj;
        if (jsonObject == null) {
            ToastUtil.showShort(FrmApplication.getContext(), "网络访问超时");
            return false;
        }
        if (jsonObject.has("code")) {
            int code = jsonObject.get("code").getAsInt();
            if (code == -1) {
                //请求超时
                ToastUtil.showShort(FrmApplication.getContext(), "网络访问超时");
                return false;
            } else if (code >= 200 && code < 300) {
                //成功
                return true;
            } else {
                //失败
                if (jsonObject.has("body")) {
                    JsonObject body = (JsonObject) jsonObject.get("body");
                    if (body.has("message")) {
                        String info = body.get("message").getAsString();
                        ToastUtil.showShort(FrmApplication.getContext(), info);
                        return false;
                    } else if (body.has("error")) {
                        String info = body.get("error").getAsString();
                        ToastUtil.showShort(FrmApplication.getContext(), info);
                        return false;
                    } else {
                        ToastUtil.showShort(FrmApplication.getContext(), "网络访问失败");
                        return false;
                    }
                } else {
                    ToastUtil.showShort(FrmApplication.getContext(), "网络访问失败");
                    return false;
                }
            }
        } else {
            ToastUtil.showShort(FrmApplication.getContext(), "网络访问失败，请联系管理员");
            return false;
        }
    }
}
