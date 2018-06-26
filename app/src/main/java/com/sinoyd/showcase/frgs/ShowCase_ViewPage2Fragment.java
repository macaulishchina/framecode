package com.sinoyd.showcase.frgs;

import android.os.Bundle;

import com.sinoyd.R;
import com.sinoyd.frame.frgs.SinoBaseFragment;

/**
 * 作者： 王一凡
 * 创建时间： 2017/12/14
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.frgs
 */
public class ShowCase_ViewPage2Fragment extends SinoBaseFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.sc_viewpage2_fragment);

        initView();

    }
    void initView() {
        getNbBar().hide();
    }
}
