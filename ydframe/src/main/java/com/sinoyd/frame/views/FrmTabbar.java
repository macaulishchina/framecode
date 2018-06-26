package com.sinoyd.frame.views;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinoyd.frame.model.FrmTabIconModel;
import com.sinoyd.frame.model.FrmTabItemViewModel;

import java.util.ArrayList;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/6
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.views 主页下方Tab栏控制类
 */
public class FrmTabbar {
    private LinearLayout llTabbar;
    private FrmTabIconModel[] tabModels;
    public ArrayList<FrmTabItemViewModel> itemsList = new ArrayList();
    private String normalColor = "#999999";
    private String selectedColor = "#333333";
    private FrmTabbarListener tabbarListener;

    public FrmTabbar(View v, FrmTabIconModel[] tabModels) {
        this.tabModels = tabModels;
        this.llTabbar = (LinearLayout)v;
    }

    public void create() {
        for(int i = 0; i < this.llTabbar.getChildCount(); i++) {
            RelativeLayout item = (RelativeLayout)this.llTabbar.getChildAt(i);
            final int j = i;
            item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FrmTabbar.this.tabbarItemClick(j);
                }
            });
            FrmTabItemViewModel viewModel = new FrmTabItemViewModel();

            for(int frmTabIconModel = 0; frmTabIconModel < item.getChildCount(); ++frmTabIconModel) {
                View v = item.getChildAt(frmTabIconModel);
                if(v instanceof ImageView) {
                    viewModel.ivIcon = (ImageView)v;
                } else if(v instanceof TextView) {
                    viewModel.tvTitle = (TextView)v;
                } else if(v instanceof RelativeLayout) {
                    RelativeLayout rlTipsArea = (RelativeLayout)v;
                    viewModel.tvTips = (TextView)rlTipsArea.getChildAt(0);
                }
            }

            FrmTabIconModel var8 = this.tabModels[i];
            viewModel.tvTitle.setText(var8.title);
            viewModel.ivIcon.setImageResource(var8.normalIcon);
            this.itemsList.add(viewModel);
        }

    }

    public void setNormalColor(String color) {
        this.normalColor = color;
    }

    public void setSelectedColor(String color) {
        this.selectedColor = color;
    }

    public void tabbarItemClick(int index) {
        this.changeSelectedIcon(index);
        if(this.tabbarListener != null) {
            this.tabbarListener.tabbarItemClickListener(index);
        }

    }

    public void changeSelectedIcon(int index) {
        for(int i = 0; i < this.llTabbar.getChildCount(); ++i) {
            FrmTabIconModel frmTabIconModel = this.tabModels[i];
            ((FrmTabItemViewModel)this.itemsList.get(i)).ivIcon.setImageResource(frmTabIconModel.normalIcon);
            ((FrmTabItemViewModel)this.itemsList.get(i)).tvTitle.setTextColor(Color.parseColor(normalColor));
        }
        ((FrmTabItemViewModel)this.itemsList.get(index)).ivIcon.setImageResource(this.tabModels[index].selectedIcon);
        ((FrmTabItemViewModel)this.itemsList.get(index)).tvTitle.setTextColor(Color.parseColor(selectedColor));
    }

    public void setItemTipsValue(int index, String tips) {
        if(tips != null && tips.trim().length() != 0 && !"0".equals(tips.trim())) {
            if(tips.trim().length() > 2) {
                tips = "99";
            }
            ((FrmTabItemViewModel)this.itemsList.get(index)).tvTips.setVisibility(View.VISIBLE);
            ((FrmTabItemViewModel)this.itemsList.get(index)).tvTips.setText(tips);
        } else {
            ((FrmTabItemViewModel)this.itemsList.get(index)).tvTips.setVisibility(View.GONE);
        }

    }

    public void setTabbarListener(FrmTabbarListener tabbarListener) {
        this.tabbarListener = tabbarListener;
    }

    public interface FrmTabbarListener {
        void tabbarItemClickListener(int var1);
    }
}
