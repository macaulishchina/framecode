package com.sinoyd.frame.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinoyd.R;
import com.sinoyd.frame.util.DensityUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/11
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.views 图片轮播视图
 */
public class ImageIndicatorView extends RelativeLayout {
    /**
     * ViewPager
     */
    private ViewPager viewPager;
    /**
     * anchor container
     */
    private LinearLayout indicateLayout;

    /**
     * page vies list
     */
    private List<View> viewList = new ArrayList<View>();

    private Handler refreshHandler;

    /**
     * item changed listener
     */
    private OnItemChangeListener onItemChangeListener;

    /**
     * item clicked listener
     */
    private OnItemClickListener onItemClickListener;
    /**
     * page total count
     */
    private int totalCount = 0;
    /**
     * current page
     */
    private int currentIndex = 1;

    /**
     * cycle list arrow anchor
     */
    public static final int INDICATE_ARROW_ROUND_STYLE = 0;

    /**
     * user guide anchor
     */
    public static final int INDICATE_USERGUIDE_STYLE = 1;

    /**
     * INDICATOR style
     */
    private int indicatorStyle = INDICATE_ARROW_ROUND_STYLE;

    /**
     * latest scroll time
     */
    private long refreshTime = 0l;

    /**
     * context
     */
    private Context context;

    /**
     * item changed callback
     */
    public interface OnItemChangeListener {
        void onPosition(int position, int totalCount);
    }

    /**
     * item clicked callback
     */
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public ImageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public ImageIndicatorView(Context context) {
        super(context);
        this.init(context);
    }

    /**
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.frm_image_indicator_layout, this);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        this.indicateLayout = (LinearLayout) findViewById(R.id.image_indicater_layout);
        this.viewPager.addOnPageChangeListener(new PageChangeListener());
        this.refreshHandler = new ScrollIndicateHandler(ImageIndicatorView.this);
    }

    /**
     * get ViewPager object
     */
    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * get current index
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /**
     * git view count
     */
    public int getTotalCount() {
        return this.totalCount;
    }

    /**
     * get latest scroll time
     */
    public long getRefreshTime() {
        return this.refreshTime;
    }

    /**
     * add single View
     *
     * @param view
     */
    public void addViewItem(View view) {
        final int position = viewList.size();
        view.setOnClickListener(new ItemClickListener(position));
        this.viewList.add(view);
    }

    /**
     * 单击事件
     */
    private class ItemClickListener implements OnClickListener {
        private int position = 0;

        public ItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.OnItemClick(view, position);
            }
        }
    }

    /**
     * 添加滚动layout
     */
    public void setupLayoutByDrawable(final Integer resArray[]) {
        if (resArray == null)
            throw new NullPointerException();

        this.setupLayoutByDrawable(Arrays.asList(resArray));
    }

    /**
     * 添加滚动图片item（本地图片）
     */
    public void setupLayoutByDrawable(final List<Integer> resList) {
        if (resList == null)
            throw new NullPointerException();

        final int len = resList.size();
        if (len > 0) {
            for (int index = 0; index < len; index++) {
                final View pageItem = new ImageView(getContext());
                pageItem.setBackgroundResource(resList.get(index));
                addViewItem(pageItem);
            }
        }
    }

    /**
     * 添加滚动图片item（网络图片）
     */
    public void setupLayoutByImageUrl(List<String> urlList) {
        this.viewList.clear();
        //设置头尾缓冲图片
        List<String> cacheUrlList = new ArrayList<>();
        if(urlList.size()>1){
            cacheUrlList.add(urlList.get(urlList.size()-1));
            for (String url : urlList) {
                cacheUrlList.add(url);
            }
            cacheUrlList.add(urlList.get(0));
        }else{
            cacheUrlList = urlList;
        }

        for (String url : cacheUrlList) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(url, imageView, mDisplayImageOptions);
            addViewItem(imageView);
        }
    }

    /**
     * set show item current
     *
     * @param index
     *            postion
     */
    public void setCurrentItem(int index) {
        this.currentIndex = index;
    }

    /**
     * set anchor style, default INDICATOR_ARROW_ROUND_STYLE
     *
     * @param style
     *            INDICATOR_USERGUIDE_STYLE or INDICATOR_ARROW_ROUND_STYLE
     */
    public void setIndicateStyle(int style) {
        this.indicatorStyle = style;
    }

    /**
     *  add OnItemChangeListener
     *
     * @param onItemChangeListener  callback
     */
    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        if (onItemChangeListener == null) {
            throw new NullPointerException();
        }
        this.onItemChangeListener = onItemChangeListener;
    }

    /**
     * add setOnItemClickListener
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * show
     */
    public void show() {
        indicateLayout.removeAllViews();
        this.totalCount = viewList.size();
        final LayoutParams params = (LayoutParams) indicateLayout.getLayoutParams();
        if (INDICATE_USERGUIDE_STYLE == this.indicatorStyle) {// user guide
            params.bottomMargin = 45;
        }
        this.indicateLayout.setLayoutParams(params);
        //init anchor
        int dotSize = totalCount>2 ? totalCount-2 : 0;
        for (int index = 0; index < dotSize; index++) {
            final View indicater = new ImageView(getContext());
            indicater.setAlpha(0.5f);
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context,8), DensityUtil.dip2px(context,8));
            dotParams.setMargins(0,0,DensityUtil.dip2px(context,8),0);
            this.indicateLayout.addView(indicater, index, dotParams);
        }
        this.refreshHandler.sendEmptyMessage(currentIndex);
        // set data for viewpager
        MyPagerAdapter adapter = new MyPagerAdapter(this.viewList);
        this.viewPager.setAdapter(adapter);
        this.viewPager.setCurrentItem(currentIndex, false);
    }

    /**
     * deal page change
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int index) {
            currentIndex = index;
            refreshHandler.sendEmptyMessage(index);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE){
                handleSetCurrentItem(viewPager.getCurrentItem());
            }
        }

        private void handleSetCurrentItem(final int position) {
            final int lastPosition = viewPager.getAdapter().getCount() - 1;
            if (position == 0) {
                viewPager.setCurrentItem(lastPosition == 0 ? 0 : lastPosition - 1, false);
            } else if (position == lastPosition) {
                viewPager.setCurrentItem(1, false);
            }
        }
    }

    /**
     * refresh indicate anchor
     */
    protected void refreshIndicateView() {
        this.refreshTime = System.currentTimeMillis();

        //当前显示的原点位置
        if(totalCount < 3){
            return;
        }
        int currentImageView;
        if(currentIndex==0){
            currentImageView = totalCount-3;
        }else if(currentIndex == totalCount-1){
            currentImageView = 0;
        }else{
            currentImageView = currentIndex-1;
        }

        //改变当前图标背景
        for (int index = 0; index < totalCount-2; index++) {
            final ImageView imageView = (ImageView) this.indicateLayout.getChildAt(index);
            if (currentImageView == index) {
                imageView.setBackgroundResource(R.drawable.frm_indicator_dot_focus);
            } else {
                imageView.setBackgroundResource(R.drawable.frm_indicator_dot);
            }
        }

        //通知UI
        if (this.onItemChangeListener != null) {
            try {
                this.onItemChangeListener.onPosition(currentImageView, this.totalCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ScrollIndicateHandler
     */
    private static class ScrollIndicateHandler extends Handler {
        private final WeakReference<ImageIndicatorView> scrollIndicateViewRef;

        public ScrollIndicateHandler(ImageIndicatorView scrollIndicateView) {
            this.scrollIndicateViewRef = new WeakReference<ImageIndicatorView>(
                    scrollIndicateView);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageIndicatorView scrollIndicateView = scrollIndicateViewRef.get();
            if (scrollIndicateView != null) {
                scrollIndicateView.refreshIndicateView();
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<View> pageViews = new ArrayList<View>();

        public MyPagerAdapter(List<View> pageViews) {
            this.pageViews = pageViews;
        }

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
            if(arg1 > pageViews.size() - 1) return;
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(ViewGroup arg0, int position) {
            ((ViewPager) arg0).addView(pageViews.get(position));
            return pageViews.get(position);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    private DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
            .cacheOnDisk(true).build();
}
