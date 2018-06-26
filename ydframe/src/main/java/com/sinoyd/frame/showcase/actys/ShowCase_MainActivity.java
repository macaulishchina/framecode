package com.sinoyd.frame.showcase.actys;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sinoyd.R;
import com.sinoyd.frame.actys.FrmAttachUploadActivity;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.util.ToastUtil;
import com.sinoyd.webview.FrmWebLoader;
import com.sinoyd.webview.action.WebloaderAction;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import butterknife.InjectView;

/**
 * 作者： 王一凡
 * 创建时间： 2017/12/13
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.actys
 */
public class ShowCase_MainActivity extends SinoBaseActivity {
    @InjectView(R.id.lv)
    ListView lv;

    private static int gotoScanActivity = 10000;

    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout(R.layout.sc_main_activity);

        openSlideFinish(false);

        initView();
    }

    private void initView(){
        getNbBar().setNBTitle("ShowCase");//设置标题文字
        getNbBar().nbBack.setVisibility(View.GONE);//将默认左侧返回箭头隐藏

        String[] titles =getResources().getStringArray(R.array.showcase_unit);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, titles);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(getContext(), FrmWebLoader.class);
                        intent.putExtra(WebloaderAction.PAGE_URL, "http://192.168.30.135:8080/h5/");
                        intent.putExtra(WebloaderAction.PAGE_TITLE, "mojs演示");
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getContext(), ShowCase_PagingListDemo.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getContext(), ShowCase_SwipeListDemo.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getContext(), ShowCase_SegmentedDemo.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getContext(), ShowCase_ViewPageDemo.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getContext(), ShowCase_BottomTapDemo.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(getContext(), ShowCase_SlideMenuDemo.class);
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(getContext(), ShowCase_ShowDialogDemo.class);
                        startActivity(intent);
                        break;
                    case 10:
                        intent = new Intent(getContext(), ShowCase_ImageIndicatorDemo.class);
                        startActivity(intent);
                        break;
                    case 11:
                        intent = new Intent(getContext(), CaptureActivity.class);
                        startActivityForResult(intent, gotoScanActivity);
                        break;
                    case 12:
                        intent = new Intent(getContext(), FrmAttachUploadActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        ToastUtil.showWorning(ShowCase_MainActivity.this,"建设中...");
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == gotoScanActivity) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                ToastUtil.showShort(this,"扫描结果：" + scanResult);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitApp();
        }
        return false;
    }

    public void ExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showShort(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            ShowCase_InitActivity.activity.finish();
            finish();
        }

    }
}
