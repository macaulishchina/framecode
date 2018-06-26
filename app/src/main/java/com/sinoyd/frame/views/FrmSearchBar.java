package com.sinoyd.frame.views;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sinoyd.frame.util.AppUtil;
import com.sinoyd.frame.util.ResManager;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/7
 * 版权： 江苏远大信息股份有限公司
 * 描述： 框架搜索栏控制类
 */
public class FrmSearchBar implements View.OnClickListener, View.OnKeyListener {
    EditText etKeyWord;
    Button btSearch;
    ImageView ivClear;
    LinearLayout llSearch;
    FrmSearchBar.SearchActionListener listener;

    public FrmSearchBar(View rootView, FrmSearchBar.SearchActionListener listener) {
        this.listener = listener;
        this.llSearch = (LinearLayout)rootView.findViewById(ResManager.getIdInt("ll_search"));
        this.etKeyWord = (EditText)rootView.findViewById(ResManager.getIdInt("etKeyWord"));
        this.btSearch = (Button)rootView.findViewById(ResManager.getIdInt("btSearch"));
        this.ivClear = (ImageView)rootView.findViewById(ResManager.getIdInt("ivClear"));
        if(this.ivClear != null) {
            this.ivClear.setOnClickListener(this);
        }

        this.btSearch.setOnClickListener(this);
        this.etKeyWord.setOnKeyListener(this);
    }

    public void onClick(View v) {
        if(v == this.btSearch) {
            hideInputKeyboard(this.etKeyWord);
            this.listener.search(this.etKeyWord.getText().toString());
        } else if(v == this.ivClear) {
            this.etKeyWord.setText("");
        }

    }

    public void hideSearch() {
        this.llSearch.setVisibility(View.GONE);
    }

    public void showSearch() {
        this.llSearch.setVisibility(View.VISIBLE);
    }

    public void clearKeyWord() {
        this.etKeyWord.setText("");
    }

    public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
        if(arg2.getAction() == 0 && arg1 == 66) {
            hideInputKeyboard(this.etKeyWord);
            this.listener.search(this.etKeyWord.getText().toString());
        }

        return false;
    }

    public interface SearchActionListener {
        void search(String var1);
    }

    public static void hideInputKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) AppUtil.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }
}
