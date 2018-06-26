package com.sinoyd.frame.showcase.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.model.FrmCommonSelectModel;
import com.sinoyd.frame.util.DensityUtil;
import com.sinoyd.frame.views.CommonSelector;

import java.util.ArrayList;
import java.util.List;

import static com.sinoyd.R.id.select;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/13
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.views
 */
public class ShowCaseOperateDialog extends Dialog implements View.OnClickListener{
    private FrameLayout selectbar;

    private TextView tv_select;

    private EditText et_single;

    private EditText et_multi;

    private TextView btn_submit;

    private Context mContext;
    private OnCloseListener listener;

    CommonSelector gradeSelector;

    public ShowCaseOperateDialog(Context context, int themeResId, OnCloseListener closeListener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = closeListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sc_operate_dialog);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView(){
        selectbar = (FrameLayout)findViewById(select);
        tv_select = (TextView)findViewById(R.id.tv_select);
        et_single = (EditText)findViewById(R.id.et_single);
        et_multi = (EditText)findViewById(R.id.et_multi);
        btn_submit = (TextView)findViewById(R.id.btn_submit);

        selectbar.setOnClickListener(this);
        btn_submit.setOnClickListener(this);


        final List<FrmCommonSelectModel> gradeList = new ArrayList<>();
        FrmCommonSelectModel commonModel = new FrmCommonSelectModel("1", "一级");
        gradeList.add(commonModel);
        commonModel = new FrmCommonSelectModel("2", "二级");
        gradeList.add(commonModel);
        commonModel = new FrmCommonSelectModel("3", "三级");
        gradeList.add(commonModel);
        commonModel = new FrmCommonSelectModel("4", "四级");
        gradeList.add(commonModel);
        gradeSelector = new CommonSelector((Activity) mContext, selectbar, DensityUtil.dip2px(mContext, 180), gradeList, new CommonSelector.OnSelectClickListener() {
            @Override
            public void onCommonItemSelect(int position) {
                tv_select.setText(gradeList.get(position).item_value);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case select:
                if(gradeSelector!=null) {
                    gradeSelector.showPop();
                }
                break;
            case R.id.btn_submit:
                if(listener != null){
                    String select = tv_select.getText().toString();
                    String single = et_single.getText().toString();
                    String multi = et_multi.getText().toString();
                    listener.onClick(this, true, select, single, multi);
                }
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm, String select, String single, String multi);
    }
}
