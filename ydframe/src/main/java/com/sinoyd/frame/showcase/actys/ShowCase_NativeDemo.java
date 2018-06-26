package com.sinoyd.frame.showcase.actys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.util.DateUtil;
import com.sinoyd.webview.action.WebloaderAction;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 作者： 王一凡
 * 创建时间： 2018/3/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.actys
 */
public class ShowCase_NativeDemo extends SinoBaseActivity {
    @InjectView(R.id.close)
    TextView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout(R.layout.sc_native_activity);

        getNbBar().setNBTitle("HelloNative");

    }

    @OnClick(R.id.close)
    public void onClick(View view) {
        //###与H5页面交互，参数名必须为WebloaderAction.RESULT_DATA###否则会造成H5页面找不到返回参数！
        Intent intent = new Intent();
        intent.putExtra(WebloaderAction.RESULT_DATA, DateUtil.getCurrentDateByFormat("yyyy-MM-dd HH:mm"));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
