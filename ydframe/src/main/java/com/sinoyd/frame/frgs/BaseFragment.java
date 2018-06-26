package com.sinoyd.frame.frgs;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sinoyd.frame.config.ResConfig;
import com.sinoyd.frame.views.BaseNavigationBar;
import com.sinoyd.frame.views.InfoStatusItem;

import butterknife.ButterKnife;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/6
 * 版权： 江苏远大信息股份有限公司
 * 描述： 逻辑基类Fragment 整合布局样式及页面基础定义方法
 */
public class BaseFragment extends Fragment implements BaseNavigationBar.NBBarAction{
    private BaseNavigationBar nbBar;
    private View rootView;
    private FrameLayout baseContent;
    private InfoStatusItem statusItem;
    private ProgressDialog progress;

    public BaseFragment() {
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(ResConfig.getBaseLayoutId(), container, false);
        this.baseContent = (FrameLayout)this.rootView.findViewById(ResConfig.getBaseContentId());
        this.nbBar = new BaseNavigationBar(this.rootView, this);
        this.statusItem = new InfoStatusItem(this.rootView);
        this.progress = new ProgressDialog(this.getContext());
        return this.rootView;
    }

    public void onResume() {
        super.onResume();
        this.initNB();
    }

    public BaseNavigationBar getNbBar() {
        return this.nbBar;
    }

    public View getRootView() {
        return this.rootView;
    }

    public View findViewById(int id) {
        return this.getRootView().findViewById(id);
    }

    public void initNB() {
    }

    public void showStatus(int resid, String info) {
        this.baseContent.setVisibility(View.GONE);
        this.getStatusItem().setStatusIcon(resid);
        this.getStatusItem().setStatusInfo(info);
        this.getStatusItem().show();
    }

    public void hideStatus() {
        this.baseContent.setVisibility(View.VISIBLE);
        this.getStatusItem().hide();
    }

    public InfoStatusItem getStatusItem() {
        return this.statusItem;
    }

    public void setLayout(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        View view = inflater.inflate(layoutId, (ViewGroup)null);
        this.setLayout(view);
    }

    public void setLayout(View view) {
        this.baseContent.addView(view);
        ButterKnife.inject(this, this.baseContent);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onNBBack() {
    }

    public void onNBRight() {
    }

    public void onHiddenChanged(boolean hidden) {
        if(hidden) {
            this.onInVisible();
        } else {
            this.onVisible();
        }

    }

    public void onVisible() {
    }

    public void onInVisible() {
    }

    @SuppressLint({"Override"})
    public Context getContext() {
        return this.getActivity();
    }

    public FrameLayout getBaseContent() {
        return this.baseContent;
    }

    public void showLoading() {
        this.progress.show();
    }

    public void hideLoading() {
        this.progress.hide();
    }

    public void showProgress() {
        this.showLoading();
    }

    public void hideProgress() {
        this.hideLoading();
    }
}
