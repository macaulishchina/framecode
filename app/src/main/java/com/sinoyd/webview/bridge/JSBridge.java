package com.sinoyd.webview.bridge;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.sinoyd.webview.action.WebloaderAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： 王一凡
 * 创建时间： 2018/3/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.webview
 */
public class JSBridge {
    private static Map<String, HashMap<String, Method>> exposedMethods = new HashMap<>();

    public static void register(String exposedName, Class<? extends IBridge> clazz) {
        if (!exposedMethods.containsKey(exposedName)) {
            try {
                exposedMethods.put(exposedName, getAllMethod(clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static HashMap<String, Method> getAllMethod(Class injectedCls) throws Exception {
        HashMap<String, Method> mMethodsMap = new HashMap<>();
        Method[] methods = injectedCls.getDeclaredMethods();
        for (Method method : methods) {
            String name;
            if (method.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC) || (name = method.getName()) == null) {
                continue;
            }
            Class[] parameters = method.getParameterTypes();
            if (null != parameters && parameters.length == 4) {
                if (parameters[1] == WebView.class && parameters[2] == JSONObject.class && parameters[3] == Callback.class) {
                    mMethodsMap.put(name, method);
                }
            }
        }
        return mMethodsMap;
    }

    public static String callJava(Object webLoader, WebView webView, String uriString) {
        String methodName = "";
        String apiName = "";
        String param = "{}";
        String port = "";
        if (!TextUtils.isEmpty(uriString) && uriString.startsWith(WebloaderAction.MOJS_SCHEME)) {
            Uri uri = Uri.parse(uriString);
            apiName = uri.getHost();
//            if (webLoader instanceof BaseWebLoaderFragment) {
//                apiName += "_fragment";
//            }
            param = uri.getQuery();
            port = uri.getPort() + "";
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                methodName = path.replace("/", "");
            }
        }

        if (exposedMethods.containsKey(apiName)) {
            HashMap<String, Method> methodHashMap = exposedMethods.get(apiName);

            if (methodHashMap != null && methodHashMap.size() != 0 && methodHashMap.containsKey(methodName)) {
                Method method = methodHashMap.get(methodName);
                if (method != null) {
                    try {
                        method.invoke(null, webLoader, webView, new JSONObject(param), new Callback(webView, port));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            //未注册API
            try {
                new Callback(webView, port).apply(BridgeImpl.getJSONObject(0, apiName + "未注册", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取调用成功后callback返回数据json对象
     *
     * @return
     */
    public static JSONObject getSuccessJSONObject() {
        return getJSONObject(1, "", null);
    }

    /**
     * 获取调用成功后callback返回数据json对象
     * @param result
     * @return
     */
    public static JSONObject getSuccessJSONObject(JSONObject result) {
        return getJSONObject(1, "", result);
    }

    /**
     * 获取调用失败后callback返回数据json对象
     * @return
     */
    public static JSONObject getFailJSONObject() {
        return getJSONObject(0, "API调用失败", null);
    }

    /**
     * 获取callback返回数据json对象
     *
     * @param code   1：成功 0：失败 2:下拉刷新回传code值 3:页面刷新回传code值
     * @param msg    描述
     * @param result
     * @return
     */
    public static JSONObject getJSONObject(int code, String msg, JSONObject result) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            if (!TextUtils.isEmpty(msg)) {
                object.put("msg", msg);
            }
            object.putOpt("result", result == null ? "" : result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
