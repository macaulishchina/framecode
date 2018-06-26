package com.sinoyd.frame.views;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinoyd.frame.util.ResManager;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：标题栏控制类
 */
public class BaseNavigationBar {
    public TitleButtonView nbBack;
    public TitleButtonView nbRight;
    public TextView nbRightText;
    public TextView nbTitle;
    public FrameLayout nbCustomLayout;
    public RelativeLayout root;
    public FrameLayout navLeftCustom;
    public FrameLayout navRightCustom;

    public BaseNavigationBar(View rootView, final BaseNavigationBar.NBBarAction action) {
        this.nbBack = (TitleButtonView)rootView.findViewById(ResManager.getIdInt("nbBack"));
        this.nbRight = (TitleButtonView)rootView.findViewById(ResManager.getIdInt("nbRight"));
        this.nbRightText = (TextView)rootView.findViewById(ResManager.getIdInt("nbRightText"));
        this.nbTitle = (TextView)rootView.findViewById(ResManager.getIdInt("nbTitle"));
        this.nbCustomLayout = (FrameLayout)rootView.findViewById(ResManager.getIdInt("nbCustomLayout"));
        this.navLeftCustom = (FrameLayout)rootView.findViewById(ResManager.getIdInt("navLeftCustom"));
        this.navRightCustom = (FrameLayout)rootView.findViewById(ResManager.getIdInt("navRightCustom"));
        this.nbBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                action.onNBBack();
            }
        });
        this.nbRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                action.onNBRight();
            }
        });
        this.nbRightText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                action.onNBRight();
            }
        });
        this.root = (RelativeLayout)rootView.findViewById(ResManager.getIdInt("nbRoot"));
    }

    public void hide() {
        this.root.setVisibility(View.GONE);
    }

    public void show() {
        this.root.setVisibility(View.VISIBLE);
    }

    public void addNBCustomView(View view) {
        this.nbTitle.setVisibility(View.GONE);
        this.nbCustomLayout.setVisibility(View.VISIBLE);
        this.nbCustomLayout.addView(view);
    }

    public View getNBCustomView() {
        View view = null;
        if(this.nbCustomLayout.getChildCount() > 0) {
            view = this.nbCustomLayout.getChildAt(0);
        }

        return view;
    }

    public void setNbBackImage(int resid) {
        this.nbBack.setImageResource(resid);
    }

    public void setNBBackground(int resid) {
        if(this.root != null) {
            this.root.setBackgroundResource(resid);
        }

    }

    public void setNBTitle(CharSequence title) {
        this.nbCustomLayout.setVisibility(View.GONE);
        this.nbTitle.setVisibility(View.VISIBLE);
        if(title!=null) {
            this.nbTitle.setText(title);
        }else{
            this.nbTitle.setText("");
        }
    }

    public interface NBBarAction {
        void onNBBack();

        void onNBRight();
    }
}
