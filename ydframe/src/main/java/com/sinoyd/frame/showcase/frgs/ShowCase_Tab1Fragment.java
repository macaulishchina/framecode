package com.sinoyd.frame.showcase.frgs;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.frgs.SinoBaseFragment;

import butterknife.InjectView;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/12
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.frgs
 */
public class ShowCase_Tab1Fragment extends SinoBaseFragment {
    @InjectView(R.id.tv_note)
    TextView tv_note;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setLayout(R.layout.sc_tab_fragment);

        initView();

    }

    void initView(){
        getNbBar().setNBTitle("页面一");
        getNbBar().nbBack.setVisibility(View.GONE);

        tv_note.setText("第一个页面.");
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
