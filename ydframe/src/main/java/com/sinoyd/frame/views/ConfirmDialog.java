package com.sinoyd.frame.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.sinoyd.R;

/**
 * 作者： 王一凡
 * 创建时间： 2017/12/1
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.baoshuiqu.view
 */
public class ConfirmDialog extends Dialog implements View.OnClickListener{
    private TextView tv_title;
    private TextView tv_content;

    private TextView btn_confirm;
    private TextView btn_cancel;

    private Context mContext;
    private Activity activity;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public ConfirmDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ConfirmDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public ConfirmDialog(Context context, int themeResId, String title, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }

    protected ConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public ConfirmDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public ConfirmDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public ConfirmDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_confirm_dialog);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView(){
        tv_title = (TextView)findViewById(R.id.title);
        tv_content = (TextView)findViewById(R.id.content);

        tv_title.setText(title);
        tv_content.setText(Html.fromHtml(content));

        btn_confirm = (TextView)findViewById(R.id.btn_confirm);
        btn_cancel = (TextView)findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                this.dismiss();
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
            case R.id.btn_cancel:
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }
}
