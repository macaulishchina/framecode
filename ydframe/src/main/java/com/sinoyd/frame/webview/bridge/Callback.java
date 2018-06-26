package com.sinoyd.frame.webview.bridge;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.sinoyd.frame.actys.SinoBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by Jim on 2016/9/28.
 * JSBridge回调
 */
public class Callback {
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static final String CALLBACK_JS_FORMAT = "javascript:JSBridge._handleMessageFromNative(%s);";
    private String mPort;
    private WeakReference<WebView> mWebViewRef;

    public Callback(WebView view, String port) {
        mWebViewRef = new WeakReference<>(view);
        mPort = port;
    }


    public void apply(JSONObject jsonObject) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("responseId", mPort);
        object.putOpt("responseData", jsonObject);
        final String execJs = String.format(CALLBACK_JS_FORMAT, String.valueOf(object));
        //如果activity已经关闭则不回调
        if (mWebViewRef != null && mWebViewRef.get() != null ) {
            if( mWebViewRef.get().getContext() instanceof SinoBaseActivity){
                if (((SinoBaseActivity) mWebViewRef.get().getContext()).getActivity().isFinishing()){
                    return;
                }
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebViewRef.get().loadUrl(execJs);
                }
            });
        }
    }
}
