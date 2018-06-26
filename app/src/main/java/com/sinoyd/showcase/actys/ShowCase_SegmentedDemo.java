package com.sinoyd.showcase.actys;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.util.ToastUtil;
import com.sinoyd.frame.views.FrmSegmentedGroup;

import butterknife.InjectView;

/**
 * 作者： 王一凡
 * 创建时间： 2017/12/14
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.actys
 */
public class ShowCase_SegmentedDemo extends SinoBaseActivity implements RadioGroup.OnCheckedChangeListener{
    @InjectView(R.id.segmented_group)
    FrmSegmentedGroup segmentedGroup;

    @InjectView(R.id.segmented_group2)
    FrmSegmentedGroup segmented_group2;

    @InjectView(R.id.tvshow)
    TextView tvshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout(R.layout.sc_segmented_demo);

        initView();
    }

    void initView(){
        getNbBar().setNBTitle("标签示例");

        segmentedGroup.setOnCheckedChangeListener(this);
        segmentedGroup.check(R.id.button_11);

        segmented_group2.setOnCheckedChangeListener(this);
        segmented_group2.check(R.id.button_22);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.button_11:
                tvshow.setText("切换标签1");
                break;
            case R.id.button_12:
                tvshow.setText("切换标签2");
                break;
            case R.id.button_21:
                tvshow.setText("切换标签3");
                break;
            case R.id.button_22:
                tvshow.setText("切换标签4");
                break;
            case R.id.button_23:
                tvshow.setText("切换标签5");
                break;
            default:
                // Nothing to do
        }
    }
}
