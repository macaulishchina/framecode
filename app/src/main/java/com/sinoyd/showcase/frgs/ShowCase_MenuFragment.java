package com.sinoyd.showcase.frgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinoyd.R;
import com.sinoyd.frame.action.FrmCommonAction;
import com.sinoyd.frame.frgs.SinoBaseFragment;
import com.sinoyd.showcase.actys.ShowCase_SlideMenuDemo;

import butterknife.OnClick;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/13
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.frgs
 */
public class ShowCase_MenuFragment extends SinoBaseFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNbBar().hide();

        setLayout(R.layout.sc_menu_fragment);
    }

    @OnClick({R.id.version,R.id.about,R.id.exit})
    public void click(View v){
        ((ShowCase_SlideMenuDemo) getActivity()).hideLeftMenu();
    }
}
