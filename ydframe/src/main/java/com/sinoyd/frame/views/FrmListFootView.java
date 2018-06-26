package com.sinoyd.frame.views;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinoyd.frame.util.DensityUtil;
import com.sinoyd.frame.util.ResManager;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/7
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.views 框架list分页下方加载样式
 */
public class FrmListFootView extends LinearLayout{
    Context context;
    int height = 45;
    int dialogWidth = 25;
    String message = "正在加载...";
    int defaultColor = 0xff666666;

    public FrmListFootView(Context context) {
        super(context);
        this.context = context;
        this.setGravity(17);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(new AbsListView.LayoutParams(-1, DensityUtil.dip2px(context, (float)this.height)));
        ImageView ivProgress = new ImageView(context);
        ivProgress.setLayoutParams(new LayoutParams(DensityUtil.dip2px(context, (float)this.dialogWidth), DensityUtil.dip2px(context, (float)this.dialogWidth)));
        Animation animation = AnimationUtils.loadAnimation(context, ResManager.getAnimInt("loading_animation_clockwise"));
        ivProgress.startAnimation(animation);
        ivProgress.setImageResource(ResManager.getDrawableInt("img_common_progress"));
        ivProgress.setColorFilter(this.defaultColor);
        TextView tvMessage = new TextView(context);
        tvMessage.setTextSize(2, 15.0F);
        tvMessage.setTextColor(this.defaultColor);
        LayoutParams tvMessageParams = new LayoutParams(-2, -2);
        tvMessageParams.setMargins(DensityUtil.dip2px(context, 10.0F), 0, 0, 0);
        tvMessage.setLayoutParams(tvMessageParams);
        tvMessage.setText(this.message);
        this.addView(ivProgress);
        this.addView(tvMessage);
    }

    public void setLoadingMsg(String msg) {
        this.message = msg;
    }
}
