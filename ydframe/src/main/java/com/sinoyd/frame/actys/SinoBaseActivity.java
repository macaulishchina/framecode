package com.sinoyd.frame.actys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.sinoyd.frame.config.ResConfig;
import com.sinoyd.frame.config.ThemeConfig;
import com.sinoyd.frame.util.LogUtil;
import com.sinoyd.frame.views.LoadingDialog;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：业务基类 二次封装逻辑基类方法
 */
public class SinoBaseActivity extends BaseActivity{
    FrameLayout baseContent;

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //禁止设备横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setDefaultTitle();
        baseContent = (FrameLayout) findViewById(ResConfig.getBaseContentId());

        LogUtil.writeLogThread("进入模块Activity－>", getLocalClassName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.writeLogThread("恢复进入模块Activity－>", getNbBar().nbTitle.getText().toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.writeLogThread("离开模块Activity－>", getNbBar().nbTitle.getText().toString());
    }

    @Override
    public void setLayout(View view) {
        super.setLayout(view);
    }

    /**
     * 设置默认标题，从上一个页面传递
     */
    public void setDefaultTitle() {
        String defaultTitle = getIntent().getStringExtra("viewtitle");
        if (defaultTitle != null) {
            getNbBar().setNBTitle(defaultTitle);
        }
    }

    /**
     * 显示网络超时状态视图
     */
    public void showNetStatus() {
        showStatus(ResConfig.getStatusWifiId(), "糟了，网络好像有问题...");
    }

    @Override
    public void initNB() {
        ThemeConfig.setDefaultViewConfig(getNbBar(), getActivity());
    }

    public BaseActivity getBaseActivity() {
        return this;
    }

    @Override
    public void showProgress() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.show(getContext(), "", true, true, null);
        }else if (!loadingDialog.isShowing()){
            loadingDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        try {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {
        showProgress();
    }

    @Override
    public void hideLoading() {
        hideProgress();
    }
}
