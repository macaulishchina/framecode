package com.sinoyd.frame.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/8
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.views 不滑动ListView视图类
 */
public class FrmUnScrollListView extends ListView {
    public FrmUnScrollListView(Context context) {
        super(context);
    }

    public FrmUnScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrmUnScrollListView(Context context, AttributeSet attrs, int  defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {

            return true;  //禁止GridView滑动

        }
        return super.dispatchTouchEvent(ev);

    }

    //不出现滚动条
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
