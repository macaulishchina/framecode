package com.sinoyd.frame.frgs;

import android.os.Bundle;

import com.sinoyd.frame.config.ResConfig;
import com.sinoyd.frame.config.ThemeConfig;
import com.sinoyd.frame.util.LogUtil;
import com.sinoyd.frame.views.LoadingDialog;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/6
 * 版权： 江苏远大信息股份有限公司
 * 描述： 业务基类Fragment 二次封装逻辑基类方法
 */
public class SinoBaseFragment extends BaseFragment{

    LoadingDialog loadingDialog;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.writeLogThread("进入模类Fragment－>", getClass().getName());
    }

    @Override
    public void onVisible() {
        super.onVisible();
        LogUtil.writeLogThread("恢复进入模块Fragment－>", getNbBar().nbTitle.getText().toString());
    }

    @Override
    public void onInVisible() {
        super.onInVisible();
        LogUtil.writeLogThread("离开模块Fragment－>", getNbBar().nbTitle.getText().toString());
    }
    @Override
    public void initNB() {

        ThemeConfig.setDefaultViewConfig(getNbBar(), getActivity());

    }

    public void showNetStatus() {
        showStatus(ResConfig.getStatusWifiId(), "糟了，网络好像有问题...");
    }

    public void showNetStatus(int icon,String info) {
        showStatus(icon, info);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.show(getActivity(), "", true, true, null);
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
