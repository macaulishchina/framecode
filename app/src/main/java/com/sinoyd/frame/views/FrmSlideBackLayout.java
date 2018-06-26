package com.sinoyd.frame.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sinoyd.frame.actys.BaseActivity;
import com.sinoyd.frame.util.DensityUtil;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/18
 * 版权： 江苏远大信息股份有限公司
 * 描述： 右滑关闭控制View #逻辑控制基于support.v4.ViewDragHelper，个性化修改，解决与scrollview冲突问题#
 */
public class FrmSlideBackLayout extends FrameLayout {
    /**
     * 左滑区域大小
     */
    private int LEFT_TIRGGER_SIZE = 0;
    /**
     * 水平滑动距离侦测值【建议50，数值越小越灵敏】
     */
    private final int X_MONITOR = 50;
    /**
     * 垂直滑动距离侦测值【建议50，数值越大允许误差范围越大】
     */
    private final int Y_MONITOR = 50;
    /**
     * 速度阈值【建议3~20之间，值越小，触发关闭越灵敏】
     */
    private final int SPEED_THRESHOLD_VALUE = 3;
    /**
     * 当前滑动速度
     */
    private int mSpeed = 0;
    /**
     * 屏幕的宽
     */
    private int mWidth;
    /**
     * 屏幕的高
     */
    private int mHeight;
    /**
     * 当前滑动距离
     */
    private int mSlideX;
    /**
     * 画笔，用来绘制阴影效果
     */
    private Paint mPaint;

    private BaseActivity mActivity;
    /**
     * 当前Activity的ViewGroup
     */
    private ViewGroup mViewGroup;
    /**
     * 最左侧的View
     */
    private View mLeftView;
    /**
     * Drag助手类
     */
    private FrmViewDragHelper mViewDragHelper;

    private int startX;
    private int startY;
    private int pointId;

    public FrmSlideBackLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        //必须是传入Activity
        mActivity = (BaseActivity) context;
        //构造ViewDragHelper
        mViewDragHelper = FrmViewDragHelper.create(this, new DragCallback());
        //设置从左边缘捕捉View
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

        //初始化画笔
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);

        //初始化左滑区域大小
        LEFT_TIRGGER_SIZE = DensityUtil.dip2px(mActivity, 50);
    }

    /**
     * 绑定ViewGroup
     */
    public void bind() {
        mViewGroup = (ViewGroup) mActivity.getWindow().getDecorView();
        mLeftView = mViewGroup.getChildAt(0);
        mViewGroup.removeView(mLeftView);
        this.addView(mLeftView);
        mViewGroup.addView(this);

        //计算屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将滑动事件交给ViewDrag处理
        //（注意：因经过拦截，此时MotionEvent不带有ACTION_DOWN事件，故在拦截前已经将DOWN信息传入）
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //绘制阴影,onDraw（）方法在ViewGroup中不一定会执行
        drawShadow(canvas);
        super.dispatchDraw(canvas);
    }

    private void drawShadow(Canvas canvas) {
        // 滑动百分比，以计算阴影透明度
        float slidePercentage = 1 - (float) mSlideX / (float) mWidth;
        // 透明的黑色阴影
        int color = Color.argb((int) (220 * slidePercentage), 0, 0, 0);

        canvas.save();
        //构造着色器
        Shader mShader = new LinearGradient(0, 0, mSlideX, 0, new int[]{color, color}, null, Shader.TileMode.REPEAT);
        //设置着色器
        mPaint.setShader(mShader);
        RectF rectF = new RectF(0, 0, mSlideX, mHeight);
        canvas.drawRect(rectF, mPaint);
        canvas.restore();
    }

    @Override
    public void computeScroll() {
        //持续滚动期间，不断刷新ViewGroup
        if (mViewDragHelper.continueSettling(true))
            invalidate();
    }

    /**
     * 获取当前滑动距离
     *
     * @param slideX
     */
    public void setmSlideX(int slideX) {
        mSlideX = slideX;
    }

    /**
     * 触摸监听
     */
    class DragCallback extends FrmViewDragHelper.Callback {

        @Override
        /**
         * 手指按下
         */
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }

        @Override
        /**
         * 手指抬起
         */
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //当前回调，松开手时触发，比较触发条件和当前的滑动距离
            int left = releasedChild.getLeft();
            if (left > mWidth * 0.5f || mSpeed > SPEED_THRESHOLD_VALUE) {
                //大于触发条件，滚出去...
                mViewDragHelper.settleCapturedViewAt(mWidth, 0);
            } else {
                //缓慢滑动的方法,小于触发条件，滚回去
                mViewDragHelper.settleCapturedViewAt(0, 0);
            }
            //需要手动调用更新界面的方法
            invalidate();

        }

        @Override
        /**
         * 发生改变
         */
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mSpeed = dx;
            mSlideX = left;
            //当滑动位置改变时，刷新View,绘制新的阴影位置
            invalidate();
            //当滚动位置到达屏幕最右边，则关掉Activity
            if (changedView == mLeftView && left >= mWidth) {
                mActivity.finishWithHideKeybord();
            }
        }

        @Override
        /**
         * X轴变化
         */
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //限制左右拖拽的位移
            left = left >= 0 ? left : 0;
            return left;
        }

        @Override
        /**
         * Y轴变化
         */
        public int clampViewPositionVertical(View child, int top, int dy) {
            //上下不能移动，返回0
            return 0;
        }

        @Override
        /**
         * 触摸边缘
         */
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            //触发边缘时，主动捕捉mRootView
            if (mSlideX == 0) {
                mViewDragHelper.captureChildView(mLeftView, pointerId);
            }
        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (ev.getX() < AppUtil.getScreenWidth(mActivity) / LEFT_TIRGGER_AREA
//                && ev.getY() > TITLE_HEIGHT) {
//            return true;//表示拦截，不分发到子视图触摸滑动事件
//        }
//        return false;
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX= (int) ev.getX();
                startY= (int) ev.getY();
                pointId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                //点击位置位于屏幕左方定义区域 & 水平滑动距离超过定义距离 & 垂直滑动距离超过定义距离
                if(startX < LEFT_TIRGGER_SIZE && (ev.getX()-startX) > X_MONITOR && Math.abs(ev.getY()-startY) < Y_MONITOR && mSlideX!=-1) {
                    //预备右滑关闭操作，初始点位
                    //（注意：如果不定义此方法将导致页面右滑功能失效，不允许修改）
                    mViewDragHelper.processTouchDownEvent(startX, startY, pointId);
                    //拦截事件，不向下分发，该滑动事件由当前View的onTouch处理
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


}
