package com.sinoyd.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.frgs.SinoBaseFragment;
import com.sinoyd.frame.util.DownloadUtil;
import com.sinoyd.frame.util.PhoneUtil;
import com.sinoyd.webview.action.WebloaderAction;
import com.sinoyd.webview.bridge.BridgeImpl;
import com.sinoyd.webview.bridge.JSBridge;
import com.sinoyd.webview.config.WebConfig;

import butterknife.InjectView;

import static android.app.Activity.RESULT_OK;
import static com.sinoyd.frame.util.AppUtil.getApplicationContext;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/13
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.webview
 */
public class FrmMojsFragment extends SinoBaseFragment {
    @InjectView(R.id.wv)
    WebView wv;

    //页面恢复时，是否刷新页面
    private boolean isResumeRefreshPage = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTheme(R.style.ActionSheetStyleIOS7);

        setLayout(R.layout.frmwebview);

        initView();

    }

    private void initView() {

        setUpWebViewDefaults();

        //注册MOJS的api
        JSBridge.register(WebloaderAction.BRIDGE_API_NAME, BridgeImpl.class);

        //设置导航栏显隐
        boolean isShowNBBar = getActivity().getIntent().getBooleanExtra(WebConfig.SHOW_NAVIGATION, true);
        if (!isShowNBBar) {
            getNbBar().hide();
        }

        //设置导航栏标题
        String pageTitle = getActivity().getIntent().getStringExtra(WebloaderAction.PAGE_TITLE);
        if (!TextUtils.isEmpty(pageTitle)) {
            getNbBar().setNBTitle(pageTitle);
        }

        //设置导航栏返回按钮
        boolean isShowBackButton = getActivity().getIntent().getBooleanExtra(WebConfig.SHOW_BACK_BUTTON, true);
        if (!isShowBackButton) {
            getNbBar().nbBack.setVisibility(View.GONE);
        }

        //设置右滑关闭页面
        boolean sildeFinish = getActivity().getIntent().getBooleanExtra(WebConfig.SLIDE_FINISH, true);
        if (!sildeFinish) {
            ((SinoBaseActivity)getActivity()).openSlideFinish(false);
        }

        //设置导航栏右上角文字按钮
        String nbRightText = getActivity().getIntent().getStringExtra(WebConfig.NBRIGHT_TEXT);
        if (!TextUtils.isEmpty(nbRightText)) {
            getNbBar().nbRightText.setVisibility(View.VISIBLE);
            getNbBar().nbRightText.setText(nbRightText);
        }

        //加载页面
        String pageUrl = getActivity().getIntent().getStringExtra(WebloaderAction.PAGE_URL);
        if(!TextUtils.isEmpty(pageUrl)) {
            wv.loadUrl(pageUrl);
        }

        //fragment传值
        if(getArguments()!=null) {
            //设置导航栏显隐
            boolean isShowNBBar_fragment = getArguments().getBoolean(WebConfig.SHOW_NAVIGATION, true);
            if (!isShowNBBar_fragment) {
                getNbBar().hide();
            }

            //设置导航栏标题
            String pageTitl_fragmente = getArguments().getString(WebloaderAction.PAGE_TITLE);
            if (!TextUtils.isEmpty(pageTitl_fragmente)) {
                getNbBar().setNBTitle(pageTitl_fragmente);
            }

            //设置导航栏返回按钮
            boolean isShowBackButton_fragment = getArguments().getBoolean(WebConfig.SHOW_BACK_BUTTON, true);
            if (!isShowBackButton_fragment) {
                getNbBar().nbBack.setVisibility(View.GONE);
            }

            //设置导航栏右上角文字按钮
            String nbRightText_fragment = getArguments().getString(WebConfig.NBRIGHT_TEXT);
            if (!TextUtils.isEmpty(nbRightText_fragment)) {
                getNbBar().nbRightText.setVisibility(View.VISIBLE);
                getNbBar().nbRightText.setText(nbRightText_fragment);
            }

            //加载页面
            String pageUrl_fragment = getArguments().getString(WebloaderAction.PAGE_URL);
            if (!TextUtils.isEmpty(pageUrl_fragment)) {
                wv.loadUrl(pageUrl_fragment);
            }
        }


    }

    @Override
    public void onNBRight() {
        super.onNBRight();
        wv.loadUrl("javascript:onNBRight()");
    }


    /**
     * 设置webview
     */
    private void setUpWebViewDefaults() {
        WebSettings settings = wv.getSettings();
        String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + " MOJS/" + PhoneUtil.getVersionName(getContext()));
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        settings.setDomStorageEnabled(true);

        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideProgress();
            }

        });

        /**
         * 写onshowFileChooser方法
         */
        wv.setWebChromeClient(new WebChromeClient() {
            //关键代码，以下函数是没有API文档的，所以在Eclipse中会报错，如果添加了@Override关键字在这里的话。

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            }

            //For Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.i("wyf","【mojs调用JsBridge信息:】"+message);
                result.confirm(JSBridge.callJava(FrmMojsFragment.this, view, message));
                return true;
            }
        });

        wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                new DownloadUtil(getContext(), url).start();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String jsonStr = data.getStringExtra(WebloaderAction.RESULT_DATA);
                wv.loadUrl("javascript:onPageResult('" + requestCode + "','"+jsonStr+"')");
            }
        }
    }

    /**
     * 设置容器重新显示时页面是否重载接口
     *
     * @param isResumeRefreshPage
     */
    public void setIsResumeRefreshPage(boolean isResumeRefreshPage) {
        this.isResumeRefreshPage = isResumeRefreshPage;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResumeRefreshPage) {
            wv.reload();
        }
    }
}
