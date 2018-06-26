package com.sinoyd.showcase.actys;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.util.ToastUtil;
import com.sinoyd.frame.views.ConfirmDialog;
import com.sinoyd.showcase.views.ShowCaseOperateDialog;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/13
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.actys
 */
public class ShowCase_ShowDialogDemo extends SinoBaseActivity {
    @InjectView(R.id.tv_content)
    TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout(R.layout.sc_showdialog_activity);

        getNbBar().setNBTitle("通用对话框");
    }

    @OnClick(R.id.tv_confirm)
    public void showConfirm(){
        new ConfirmDialog(getActivity(), R.style.frm_dialog, "自定义标题", "您正在使用远大移动开发框架<br/><br/><font color='#8A8A88'>该内容支持HTML文本</font>", new ConfirmDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if(confirm){
                    ToastUtil.showShort(ShowCase_ShowDialogDemo.this,"点击 确认");
                }
            }
        }).show();
    }

    @OnClick(R.id.tv_operate)
    public void showOperate(){
        //自定义对话框（带操作控件）
        new ShowCaseOperateDialog(getContext(), R.style.frm_dialog, new ShowCaseOperateDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm, String select, String single, String multi) {
                if(confirm){
                    tv_content.setText(Html.fromHtml("单行输入："+single+"<br/><br/>"+"单项选择："+select+"<br/><br/>"+"多行输入："+multi));
                }
            }
        }).show();
    }
}
