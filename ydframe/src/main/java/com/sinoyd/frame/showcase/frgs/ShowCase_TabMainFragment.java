package com.sinoyd.frame.showcase.frgs;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sinoyd.R;
import com.sinoyd.frame.model.FrmTabIconModel;
import com.sinoyd.frame.views.FrmTabbar;
import com.sinoyd.webview.action.WebloaderAction;
import com.sinoyd.webview.config.WebConfig;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/12
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.frgs
 */
public class ShowCase_TabMainFragment extends Fragment implements FrmTabbar.FrmTabbarListener{
    public FrmTabbar tabbar;
    public int index = 0;

    public FrmTabIconModel[] tabModels = new FrmTabIconModel[]{
            new FrmTabIconModel("首页", R.drawable.sc_tabicon_first, R.drawable.sc_tabicon_first_select, new ShowCase_Tab1Fragment()),
            new FrmTabIconModel("审批", R.drawable.sc_tabicon_second, R.drawable.sc_tabicon_second_select, new ShowCase_Tab2Fragment()),
            new FrmTabIconModel("点位", R.drawable.sc_tabicon_three, R.drawable.sc_tabicon_three_select, new ShowCase_Tab3Fragment())
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frmmainfragment, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.llTabbar);

        for (int i = 0; i < tabModels.length; i++) {
            ll.addView(inflater.inflate(R.layout.frmtabitem, null), lp);
        }

        tabbar = new FrmTabbar(ll, tabModels);
        tabbar.setNormalColor("#445D67");//未选中字体颜色
        tabbar.setSelectedColor("#00B9FF");//选中字体颜色
        tabbar.setTabbarListener(this);
        tabbar.create();


        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index", 1);
        }
        //设置默认选择的tab页面为1
        setDefaultIndex(index);

        return view;
    }

    /**
     * 设置默认tab页面
     * @param index
     */
    public void setDefaultIndex(int index) {
        setItem();
        tabbar.changeSelectedIcon(index);
    }

    /**
     * 更改tab图标角标
     * @param index
     * @param value
     */
    public void changeTips(int index, String value) {
        tabbar.setItemTipsValue(index, value);
    }

    @Override
    public void tabbarItemClickListener(int index) {
        this.index = index;
        setItem();
    }

    /**
     * tab切换事件
     */
    private void setItem() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment tabFragment = tabModels[index].fragment;

        if(index == 0 && !tabFragment.isAdded()) {
            //为首页H5页面传递url地址
            Bundle args = new Bundle();
            args.putString(WebloaderAction.PAGE_URL, "http://192.168.30.135:8080/h5/hybird/mo_show_page.html");
            args.putBoolean(WebConfig.SHOW_NAVIGATION, false);
            tabFragment.setArguments(args);
        }

        Fragment fragment = getFragmentManager().findFragmentByTag(tabFragment.getClass().getName());
        if (fragment == null) {
            transaction.add(R.id.frgContent, tabFragment, tabFragment.getClass().getName());
        } else {
            transaction.show(fragment);
        }

        for (int i = 0; i < tabModels.length; i++) {
            if (i != index) {
                Fragment otherFragment = getFragmentManager().findFragmentByTag(tabModels[i].fragment.getClass().getName());
                if (otherFragment != null) {
                    transaction.hide(otherFragment);
                }
            }
        }
        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
