package com.sinoyd.hybird.actys;

import android.os.Bundle;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/16
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.hybird.actys
 */
public class Hybird_MainActivity extends SinoBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout(R.layout.hy_main_activity);

        // 关闭当前界面的右滑关闭功能
        openSlideFinish(false);

        getNbBar().hide();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
