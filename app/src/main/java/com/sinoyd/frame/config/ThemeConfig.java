package com.sinoyd.frame.config;

import android.app.Activity;
import android.graphics.Color;

import com.sinoyd.frame.util.ResManager;
import com.sinoyd.frame.views.BaseNavigationBar;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述： 主题控制类
 */
public class ThemeConfig {

    public ThemeConfig(){
        //主题控制构造方法
    }

    /**
     * 设置标题栏默认样式，可以自定义修改
     * @param nbBar
     * @param activity
     */
    public static void setDefaultViewConfig(BaseNavigationBar nbBar, Activity activity){
        nbBar.nbBack.setImageResource(ResManager.getDrawableInt("frm_nav_back"));
        //**************** 标题背景使用纯色 ****************
        int themeColor =  activity.getResources().getColor(ResManager.getColorInt("theme_color"));
        nbBar.root.setBackgroundColor(themeColor);
        //**************** 标题背景使用图片 ****************
        //nbBar.root.setBackgroundResource(ResManager.getDrawableInt("theme_title_bg"));
        int topFilterColor = activity.getResources().getColor(ResManager.getColorInt("white"));
        nbBar.nbRight.setColorFilter(topFilterColor);
        nbBar.nbBack.setColorFilter(topFilterColor);
        nbBar.nbRightText.setTextColor(topFilterColor);
        nbBar.nbTitle.setTextColor(topFilterColor);
    }
}
