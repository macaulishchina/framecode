package com.sinoyd.frame.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinoyd.frame.util.ResManager;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：超时视图
 */
public class InfoStatusItem {
    View statusItem;
    ImageView ivStatus;
    TextView tvStatus;

    public InfoStatusItem(View rootView) {
        this.statusItem = rootView.findViewById(ResManager.getIdInt("statusItem"));
        this.ivStatus = (ImageView)rootView.findViewById(ResManager.getIdInt("ivStatus"));
        this.tvStatus = (TextView)rootView.findViewById(ResManager.getIdInt("tvStatus"));
    }

    public void setStatusIcon(int resid) {
        this.ivStatus.setImageResource(resid);
    }

    public void setStatusInfo(CharSequence info) {
        this.tvStatus.setText(info);
    }

    public ImageView getIvStatus() {
        return this.ivStatus;
    }

    public TextView getTvStatus() {
        return this.tvStatus;
    }

    public void hide() {
        this.statusItem.setVisibility(View.GONE);
    }

    public void show() {
        this.statusItem.setVisibility(View.VISIBLE);
    }
}
