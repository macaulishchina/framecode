package com.sinoyd.frame.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import com.sinoyd.frame.model.FrmFragmentParamModel;

import java.util.List;

/**
 * 作者： 王一凡
 * 创建时间： 2017/12/14
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.adapter
 */
public class Frm_FragmentAdapter extends FragmentPagerAdapter {
    public Fragment[] frms;
    public List<FrmFragmentParamModel> params;

    /**
     * 构造函数，不带参数
     *
     * @param fm
     * @param frms
     */
    public Frm_FragmentAdapter(FragmentManager fm, Fragment[] frms) {
        super(fm);
        this.frms = frms;
        this.params = null;
    }

    /**
     * 构造函数，带参数List<FragmentParamModel>
     *
     * @param fm
     * @param frms
     * @param params
     */
    public Frm_FragmentAdapter(FragmentManager fm, Fragment[] frms, List<FrmFragmentParamModel> params) {
        super(fm);
        this.frms = frms;
        this.params = params;
    }

    @Override
    public Fragment getItem(int position) {
        //传递参数
        if (params != null) {
            for (FrmFragmentParamModel m : params) {
                if (m.position == position) {
                    //暂时只支持单参数传值
                    Bundle bundle = new Bundle();
                    bundle.putString(m.paramName, m.paramValue);
                    frms[position].setArguments(bundle);
                    break;
                }
            }
        }
        return frms[position];
    }

    @Override
    public int getCount() {
        return frms.length;
    }
}
