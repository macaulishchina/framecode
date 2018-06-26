package com.sinoyd.webview;

import android.content.DialogInterface;
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
import com.sinoyd.frame.util.DownloadUtil;
import com.sinoyd.frame.util.PhoneUtil;
import com.sinoyd.frame.views.FrmActionSheet;
import com.sinoyd.webview.action.WebloaderAction;
import com.sinoyd.webview.bridge.BridgeImpl;
import com.sinoyd.webview.bridge.JSBridge;
import com.sinoyd.webview.config.WebConfig;

import butterknife.InjectView;

/**
 * 作者： 王一凡
 * 创建时间： 2018/3/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.webview
 */
@Deprecated
public class FrmWebViewActivity extends SinoBaseActivity implements FrmActionSheet.MenuItemClickListener{
    @InjectView(R.id.wv)
    WebView wv;

    /**
     * 页面恢复时，是否刷新页面
     */
    private boolean isResumeRefreshPage = false;

    private String[] items;

    private ValueCallback filePathCallback;

    private ValueCallback<Uri[]> mFilePathCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTheme(R.style.ActionSheetStyleIOS7);

     //   getNbBar().hide();

        setLayout(R.layout.frmwebview);

        initView();
    }

    private void initView() {


        setUpWebViewDefaults();

        //wv.addJavascriptInterface(new EJSobj(this, wv), "android");

        //注册MOJS的api
        JSBridge.register(WebloaderAction.BRIDGE_API_NAME, BridgeImpl.class);

        //设置导航栏显隐
        boolean isShowNBBar = getIntent().getBooleanExtra(WebConfig.SHOW_NAVIGATION, true);
        if (!isShowNBBar) {
            getNbBar().hide();
        }

        //设置导航栏标题
        String pageTitle = getIntent().getStringExtra(WebloaderAction.PAGE_TITLE);
        if (!TextUtils.isEmpty(pageTitle)) {
            getNbBar().setNBTitle(pageTitle);
        }

        //设置导航栏返回按钮
        boolean isShowBackButton = getIntent().getBooleanExtra(WebConfig.SHOW_BACK_BUTTON, true);
        if (!isShowBackButton) {
            getNbBar().nbBack.setVisibility(View.GONE);
        }

        //设置右滑关闭页面
        boolean sildeFinish = getIntent().getBooleanExtra(WebConfig.SLIDE_FINISH, true);
        if (!sildeFinish) {
            openSlideFinish(false);
        }

        //设置导航栏右上角文字按钮
        String nbRightText = getIntent().getStringExtra(WebConfig.NBRIGHT_TEXT);
        if (!TextUtils.isEmpty(nbRightText)) {
            getNbBar().nbRightText.setVisibility(View.VISIBLE);
            getNbBar().nbRightText.setText(nbRightText);
        }

        //加载页面
        String pageUrl = getIntent().getStringExtra(WebloaderAction.PAGE_URL);
        wv.loadUrl(pageUrl);

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
                filePathCallback = uploadMsg;
                showSelect();
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                filePathCallback = uploadMsg;
                showSelect();
            }

            //For Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mFilePathCallback = filePathCallback;

                String[] acceptTypes = new String[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    acceptTypes = fileChooserParams.getAcceptTypes();
                }
                showSelect();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.i("wyf","【mojs调用JsBridge信息:】"+message);
                result.confirm(JSBridge.callJava(FrmWebViewActivity.this, view, message));
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

    /**
     * 显示要弹出的选择项
     */
    private void showSelect() {
//        if (items == null || TextUtils.isEmpty(items[0])) {
        items = new String[]{"拍照", "相册", "录像"};
//        }
        final FrmActionSheet menuView = new FrmActionSheet(getActivity());
        menuView.setCancelButtonTitle("取消");// before add items
        menuView.addItems(items);
        menuView.setItemClickListener(this);
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                filePathCallback(null);
            }
        });
        menuView.showMenu();
    }

    /**
     * 将选择的数据回调给file控件
     *
     * @param results 回调数据
     */
    private void filePathCallback(Uri[] results) {
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
        if (filePathCallback != null) {
            filePathCallback.onReceiveValue(results == null ? null : results[0]);
            filePathCallback = null;
        }
    }

    @Override
    public void onItemClick(int index) {
        if (items != null) {
            String title = items[index];
            if ("相册".equals(title)) {
                //photoSelector.requestPhotoPick(getActivity(), IMAGE_REQUEST_CODE);
            } else if ("拍照".equals(title)) {
                //photoSelector.requestSysCamera(getActivity(), CAMERA_REQUEST_CODE);
            } else if ("文件".equals(title)) {
                //FileChooseActivity.goFileChooseActivity(getActivity(), FILE_REQUEST_CODE, ThemeConfig.getCurrentSelectedTheme().topbarImage, ThemeConfig.getCurrentSelectedTheme().topbarFilterColor);
            } else if ("录像".equals(title)) {
                //Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                //startActivityForResult(intent, VIDEO_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    protected void onResume() {
        super.onResume();
        if (isResumeRefreshPage) {
            wv.reload();
        }
    }
}
