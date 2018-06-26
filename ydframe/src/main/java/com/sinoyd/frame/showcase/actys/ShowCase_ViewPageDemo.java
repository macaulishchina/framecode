package com.sinoyd.frame.showcase.actys;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.adapter.Frm_FragmentAdapter;
import com.sinoyd.showcase.frgs.ShowCase_ViewPage1Fragment;
import com.sinoyd.showcase.frgs.ShowCase_ViewPage2Fragment;
import com.sinoyd.showcase.frgs.ShowCase_ViewPage3Fragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 作者： 王一凡
 * 创建时间： 2017/12/14
 * 版权： 江苏远大信息股份有限公司
 * 描述： ViewPage+Fragment展示
 */
public class ShowCase_ViewPageDemo extends SinoBaseActivity implements ViewPager.OnPageChangeListener{
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    @InjectView(R.id.demo1)
    LinearLayout demo1;
    @InjectView(R.id.demo2)
    LinearLayout demo2;
    @InjectView(R.id.demo3)
    LinearLayout demo3;
    @InjectView(R.id.tv_demo1)
    TextView tv_demo1;
    @InjectView(R.id.tv_demo2)
    TextView tv_demo2;
    @InjectView(R.id.tv_demo3)
    TextView tv_demo3;
    @InjectView(R.id.spzn_sc_line)
    LinearLayout line;

    private Fragment frms[] = {new ShowCase_ViewPage1Fragment(), new ShowCase_ViewPage2Fragment(), new ShowCase_ViewPage3Fragment()};
    View CurrentView;
    View[] lls = new View[3];
    private Frm_FragmentAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout(R.layout.sc_viewpage_demo);

        openSlideFinish(false);

        initView();
    }

    void initView(){
        getNbBar().setNBTitle("多页面滑动");

        //设置viewpage适配器，若当前页面为fragment，使用getChildFragmentManager(),否则会出现页面丢失的情况
        mPagerAdapter = new Frm_FragmentAdapter(getFragmentManager(), frms);
        mViewPager.setAdapter(mPagerAdapter);
        //设置缓冲区为前后各2张页面，此demo为3页面，即全部缓存。建议设置为全部缓存的方式（若为4页面，则此处设置setOffscreenPageLimit(3)保证全部缓冲）
        //support.v4.view.ViewPage组件存在先天性bug，若不设置该属性或setOffscreenPageLimit(0)，ViewPage仍然会缓存前后各1张页面
        //若项目涉及滑动刷新页面操作，建议使用滑动监听器完成操作#onPageSelected
        mViewPager.setOffscreenPageLimit(2);
        //设置ViewPage滑动监听
        mViewPager.setOnPageChangeListener(this);
        CurrentView = demo1;
        lls[0] = demo1;
        lls[1] = demo2;
        lls[2] = demo3;
        //设置默认页面
        setPage(0);
    }

    @OnClick({R.id.demo1, R.id.demo2, R.id.demo3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.demo1:
                setPage(0);
                break;
            case R.id.demo2:
                setPage(1);
                break;
            case R.id.demo3:
                setPage(2);
                break;
        }
    }

    private void setPage(int i) {
        startLineAnim(line, lls[i]);
        mViewPager.setCurrentItem(i);
        switch (i) {
            case 0:
                reset(tv_demo1);
                break;
            case 1:
                reset(tv_demo2);
                break;
            case 2:
                reset(tv_demo3);
                break;
        }
    }

    private void reset(View view) {
        tv_demo1.setEnabled(true);
        tv_demo2.setEnabled(true);
        tv_demo3.setEnabled(true);

        view.setEnabled(false);
    }

    /**
     * 点击动画颜色变化效果
     * @param line
     * @param flag
     */
    public void startLineAnim(View line, View flag) {
        CurrentView.setEnabled(true);
        flag.setEnabled(false);
        CurrentView = flag;
        ObjectAnimator anim = ObjectAnimator.ofFloat(line, "translationX", line.getX(), flag.getX());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.playTogether(anim);
        set.start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
