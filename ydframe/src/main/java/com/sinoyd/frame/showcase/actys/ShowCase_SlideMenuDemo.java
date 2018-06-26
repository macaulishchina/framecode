package com.sinoyd.frame.showcase.actys;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.showcase.frgs.ShowCase_MenuFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/12
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.actys
 */
public class ShowCase_SlideMenuDemo extends SinoBaseActivity {
    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout(R.layout.sc_slidemenu_activity);

        getNbBar().setNBTitle("侧拉菜单弹出");

        openSlideFinish(false);

        getFragmentManager().beginTransaction().add(R.id.id_left_menu, new ShowCase_MenuFragment()).commit();

    }

    @OnClick(R.id.tv_left)
    public void showLeft(){
        displayLeftMenu();
    }

    public void displayLeftMenu() {
        //如果为右方弹出，此处修改为Gravity.RIGHT
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void hideLeftMenu() {
        //如果为右方弹出，此处修改为Gravity.RIGHT
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }
}
