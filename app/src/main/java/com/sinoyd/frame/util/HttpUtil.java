package com.sinoyd.frame.util;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sinoyd.frame.action.FrmCommonAction;
import com.sinoyd.frame.application.FrmApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述： 网络请求工具类
 */
public class HttpUtil {
    private final static int CONNENT_TIMEOUT = 20000;
    private final static int READ_TIMEOUT = 20000;
    private final static String Authorization_Type = "Bearer ";//验证类型

    /**
     * @function trustAllHosts
     * @Description 信任所有主机-对于任何证书都不做检查
     */
    private static void trustAllHosts() {
        TrustManager[] arrayOfTrustManager = new TrustManager[1];
        //实现自己的信任管理器类
        arrayOfTrustManager[0] = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }

        };
        try {
            SSLContext localSSLContext = SSLContext.getInstance("TLS");
            localSSLContext.init(null, arrayOfTrustManager, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(localSSLContext.getSocketFactory());
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
    static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * @param httpsurl
     * @param data
     * @return
     * @function
     * @Description https post/get方法，返回值是https请求
     */
    public static JsonObject request(String httpsurl, String data, int requestType) {
        String logRequest = httpsurl;
        if (data != null) {
            logRequest += " [请求参数：]" + data;
        }
        Log.i("wyf","[请求地址：]" + logRequest);
        LogUtil.writeLogThread("[请求地址：]", logRequest);

        int responseCode = -1;
        String result = null;
        HttpURLConnection http = null;
        URL url;
        try {
            url = new URL(httpsurl);
            // 判断是http请求还是https请求
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                http = (HttpsURLConnection) url.openConnection();
                ((HttpsURLConnection) http).setHostnameVerifier(DO_NOT_VERIFY);// 对所有主机都进行确认
            } else {
                http = (HttpURLConnection) url.openConnection();
            }

            http.setConnectTimeout(CONNENT_TIMEOUT);// 设置超时时间
            http.setReadTimeout(READ_TIMEOUT);
            //http.setRequestProperty("User-Agent", "App-Android/0.1 "+ getUserAgent());
            //H5+项目 添加头Authorization验证
            if(!TextUtils.isEmpty(FrmCommonAction.getUserToken())){
                http.addRequestProperty("Authorization", Authorization_Type + FrmCommonAction.getUserToken());
            }
            if(requestType == FrmCommonAction.GET) {
                http.setRequestMethod("GET");// 设置请求类型为get
                http.setDoInput(true);
            } else if(requestType == FrmCommonAction.DELETE) {
                http.setRequestMethod("DELETE");// 设置请求类型为delete
            } else if(requestType == FrmCommonAction.POST){
                http.setRequestMethod("POST");// 设置请求类型为post
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("Content-Type", "application/json");
                http.setRequestProperty("Accept", "application/json");
                DataOutputStream out = new DataOutputStream(http.getOutputStream());
                out.writeBytes(cn2Unicode(data));
                out.flush();
                out.close();
            }else if(requestType == FrmCommonAction.PUT){
                http.setRequestMethod("PUT");// 设置请求类型为put
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("Content-Type", "application/json");
                http.setRequestProperty("Accept", "application/json");
                DataOutputStream out = new DataOutputStream(http.getOutputStream());
                out.writeBytes(cn2Unicode(data));
                out.flush();
                out.close();
            }

            // 判断返回状态
            responseCode = http.getResponseCode();
            BufferedReader in = null;
            if (responseCode >= 200 && responseCode < 300) {
                in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(http.getErrorStream()));
            }
            String temp = in.readLine();
            while (temp != null) {
                if (result != null) {
                    result += temp;
                }else {
                    result = temp;
                }
                temp = in.readLine();
            }
            in.close();
            http.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = convert(result);
        //拼接返回Json数据
        JsonObject returnJsonObject = null;
        try {
            returnJsonObject = new JsonObject();
            returnJsonObject.addProperty("code", responseCode);
            if (result != null) {
                JsonObject resultJsonObject = new JsonParser().parse(dealWithBs(result)).getAsJsonObject();
                returnJsonObject.add("body", resultJsonObject);
            }
            Log.i("wyf", "[返回数据：]" + returnJsonObject.toString());
            LogUtil.writeLogThread("[返回数据：]", returnJsonObject.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return returnJsonObject;

    }

    /**
     * 处理异常返回值，防止json解析出错
     * @param bs
     * @return
     */
    private static String dealWithBs(String bs){
        if (bs.startsWith("[") && bs.endsWith("]")) {
            bs = "{\"data\":" + bs + "}";
        }
        return bs;
    }

    /**
     * 中文转化为ASCII编码，防止请求参数中出现编码错误
     * #该方法已过时#
     * @param str
     * @return
     */
    @Deprecated
    private static String cnToUnicode(String str){
        String result="";
        for (int i = 0; i < str.length(); i++){
            int chr1 = (char) str.charAt(i);
            if(chr1>='\u4e00' && chr1<='\u9fa5'){
                //汉字范围 \u4e00-\u9fa5 (中文)
                result+="\\u" + Integer.toHexString(chr1);
            }else{
                result+=str.charAt(i);
            }
        }
        return result;
    }

    /**
     * 中文转化为ASCII编码，防止请求参数中出现编码错误
     * @param str
     * @return
     */
    public static String cn2Unicode(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if((c>='\u0391' && c<='\uffe5') || (c>='\u0080' && c<='\u00FF')) {
                //汉字范围 \u0391-\uffe5 (中文含标点符号) || 罗马字符\u0080-\u00FF
                sb.append("\\u");
                j = (c >>> 8); //取出高8位
                tmp = Integer.toHexString(j);
                if (tmp.length() == 1) {
                    sb.append("0");
                }
                sb.append(tmp);
                j = (c & 0xFF); //取出低8位
                tmp = Integer.toHexString(j);
                if (tmp.length() == 1) {
                    sb.append("0");
                }
                sb.append(tmp);
            }else{
                sb.append(c);
            }
        }
        return (new String(sb));
    }

    /**
     * ASCII编码转化为中文
     * @param utfString
     * @return
     */
    private static String convert(String utfString){
        if(utfString != null) {
            StringBuilder sb = new StringBuilder();
            int i = -1;
            int pos = 0;
            while ((i = utfString.indexOf("\\u", pos)) != -1) {
                sb.append(utfString.substring(pos, i));
                if (i + 5 < utfString.length()) {
                    pos = i + 6;
                    sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
                }
            }
            sb.append(utfString.substring(pos));
            return sb.toString();
        }
        return null;
    }

    /**
     * 获取http头部userAgent信息
     * @return
     */
    private static String getUserAgent() {
        String userAgent = System.getProperty("http.agent");
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
