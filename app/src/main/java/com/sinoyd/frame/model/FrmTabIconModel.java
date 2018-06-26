package com.sinoyd.frame.model;

import android.app.Fragment;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/6
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.model
 */
public class FrmTabIconModel {
    public String title;
    public int normalIcon;
    public int selectedIcon;
    public Fragment fragment;

    public FrmTabIconModel(String title, int normalIcon, int selectedIcon, Fragment fragment) {
        this.title = title;
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
        this.fragment = fragment;
    }
}
