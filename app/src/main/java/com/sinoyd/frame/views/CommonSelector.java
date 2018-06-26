package com.sinoyd.frame.views;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.sinoyd.R;
import com.sinoyd.frame.adapter.Frm_CommonSelectorAdapter;
import com.sinoyd.frame.model.FrmCommonSelectModel;


import java.util.List;




/**
 * 作者： 王一凡
 * 创建时间： 2017/10/24
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.baoshuiqu.view
 */
public class CommonSelector {
    public PopupWindow popupWindow;
    public View popupWindowView;
    private Activity activity;
    private View anchor;
    private int width = 0;
    private int customLayout = 0;
    private int customItemLayout = 0;
    public OnSelectClickListener onSelect;

    ListView selectLv;
    List<FrmCommonSelectModel> commonList;



    public CommonSelector(Activity activity, View v, List<FrmCommonSelectModel> list, OnSelectClickListener callback){
        this.activity = activity;
        this.anchor = v;
        this.width = ViewGroup.LayoutParams.MATCH_PARENT;
        this.customLayout = R.layout.frm_common_selector;
        this.customItemLayout = R.layout.frm_common_selector_item;
        this.commonList = list;
        this.onSelect = callback;
        initPop();
    }

    /**
     * 初始化下拉选择器（使用默认样式）
     * @param activity
     * @param v
     * @param width
     * @param list
     * @param callback
     */
    public CommonSelector(Activity activity, View v, int width, List<FrmCommonSelectModel> list, OnSelectClickListener callback){
        this.activity = activity;
        this.anchor = v;
        this.width = width;
        this.customLayout = R.layout.frm_common_selector;
        this.customItemLayout = R.layout.frm_common_selector_item;
        this.commonList = list;
        this.onSelect = callback;
        initPop();
    }

    /**
     * 初始化下拉选择器（自定义样式）
     * @param activity
     * @param v
     * @param width
     * @param layout
     * @param itemLayout
     * @param list
     * @param callback
     */
    public CommonSelector(Activity activity, View v, int width, int layout, int itemLayout, List<FrmCommonSelectModel> list, OnSelectClickListener callback){
        this.activity = activity;
        this.anchor = v;
        this.width = width;
        this.customLayout = layout;
        this.customItemLayout = itemLayout;
        this.commonList = list;
        this.onSelect = callback;
        initPop();
    }

    public void initPop(){
        popupWindowView = activity.getLayoutInflater().inflate(customLayout, null);
        popupWindow = new PopupWindow(popupWindowView, width, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOnDismissListener(new PopupDismissListener());
        initEvent();
    }

    private void initEvent(){
        selectLv = (ListView)popupWindowView.findViewById(R.id.select_lv);
        Frm_CommonSelectorAdapter commonAdapter = new Frm_CommonSelectorAdapter(activity, commonList ,customItemLayout);
        selectLv.setAdapter(commonAdapter);
        selectLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                onSelect.onCommonItemSelect(position);
            }
        });

    }

    public void showPop(){
        if(popupWindow.isShowing()){
            return;
        }
        if(commonList.size()>5){
            popupWindow.setHeight(560);
        }
        //显示位置
        popupWindow.showAsDropDown(anchor);
        //backgroundAlpha(0.5f);
    }

    public void dismissPop(){
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，将背景透明度复原
     *
     */
    class PopupDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
            //选择回调
           // selectCallBack.onColorChange(fontColor,fontSize);
        }

    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 公共选择器接口
     */
    public interface OnSelectClickListener {
        void onCommonItemSelect(int position);
    }
}
