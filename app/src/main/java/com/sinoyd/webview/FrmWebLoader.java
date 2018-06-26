package com.sinoyd.webview;

import android.content.res.Configuration;
import android.os.Bundle;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/13
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.webview
 */
public class FrmWebLoader extends SinoBaseActivity {
    public FrmMojsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNbBar().hide();

        setLayout(R.layout.frm_mojs_webloader);

        fragment = new FrmMojsFragment();
   //     fragment.model = WebloaderAction.getEJSModel(getActivity().getIntent());

        getFragmentManager().beginTransaction().add(R.id.frgContent, fragment).commit();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (fragment.wv!=null) {
            fragment.wv.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragment.wv!=null) {
            fragment.wv.onResume();
        }
    }

}
