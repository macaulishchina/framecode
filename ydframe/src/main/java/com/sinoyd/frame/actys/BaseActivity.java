package com.sinoyd.frame.actys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.sinoyd.frame.config.ResConfig;
import com.sinoyd.frame.views.BaseNavigationBar;
import com.sinoyd.frame.views.FrmSlideBackLayout;
import com.sinoyd.frame.views.InfoStatusItem;

import butterknife.ButterKnife;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：逻辑基类-整合布局样式及定义页面通用交互方法
 */
public class BaseActivity extends Activity implements BaseNavigationBar.NBBarAction {
    private FrmSlideBackLayout mSlideBack;
    private BaseNavigationBar nbBar;
    private View rootView;
    private FrameLayout baseContent;
    private InfoStatusItem statusItem;
    ProgressDialog progress;

    public BaseActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定侧滑关闭控件
        mSlideBack = new FrmSlideBackLayout(this);
        mSlideBack.bind();
        //初始化默认布局
        LayoutInflater inflater = LayoutInflater.from(this);
        this.rootView = inflater.inflate(ResConfig.getBaseLayoutId(), (ViewGroup)null);
        this.setContentView(this.rootView);
        this.baseContent = (FrameLayout)this.rootView.findViewById(ResConfig.getBaseContentId());
        this.nbBar = new BaseNavigationBar(this.rootView, this);
        this.progress = new ProgressDialog(this.getContext());
        this.statusItem = new InfoStatusItem(this.rootView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.initNB();
    }

    public void initNB() {
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

    public BaseNavigationBar getNbBar() {
        return this.nbBar;
    }

    public void setLayout(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(layoutId, (ViewGroup)null);
        this.setLayout(view);
    }

    public void setLayout(View view) {
        this.baseContent.addView(view);
        ButterKnife.inject(this);
    }

    public Context getContext() {
        return this;
    }

    public Activity getActivity() {
        return this;
    }

    public void onNBBack() {
        finishWithHideKeybord();
    }

    public void onNBRight() {
    }

    public View getRootView() {
        return this.rootView;
    }

    public void finishWithHideKeybord() {
        //hideKeybord();
        super.finish();
    }

    private void hideKeybord(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isActive()){
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void openSlideFinish(boolean open) {
        mSlideBack.setmSlideX(open ? 0 : -1);
    }
}
