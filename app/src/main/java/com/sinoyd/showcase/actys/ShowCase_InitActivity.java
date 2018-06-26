package com.sinoyd.showcase.actys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.sinoyd.R;
import com.sinoyd.frame.action.FrmCommonAction;
import com.sinoyd.frame.actys.SinoBaseActivity;

/**
 * 作者： 王一凡
 * 创建时间： 2017/10/17
 * 版权： 江苏远大信息股份有限公司
 * 描述： ShowCase初始化页面
 */
public class ShowCase_InitActivity extends SinoBaseActivity {
    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNbBar().hide();

        setLayout(R.layout.sc_helloworld_activity);

        openSlideFinish(false);

        activity = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intentPage();
            }
        },  2000);
    }

    void intentPage() {
        Intent intent = new Intent(getActivity(), ShowCase_MainActivity.class);
        startActivity(intent);
    }
}
