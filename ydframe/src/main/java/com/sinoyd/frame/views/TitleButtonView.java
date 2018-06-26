package com.sinoyd.frame.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：标题左侧按钮布局
 */
public class TitleButtonView extends ImageView {
    public TitleButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case 0:
                this.setAlpha(0.5F);
                break;
            case 1:
                this.setAlpha(1.0F);
            case 2:
            case 3:
            default:
                break;
            case 4:
                this.setAlpha(1.0F);
        }

        return super.onTouchEvent(event);
    }
}
