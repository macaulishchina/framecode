package com.sinoyd.frame.showcase.actys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.util.ToastUtil;
import com.sinoyd.frame.views.ImageIndicatorView;
import com.sinoyd.showcase.model.ShowCaseNotices;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/16
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.actys
 */
public class ShowCase_ImageIndicatorDemo extends SinoBaseActivity implements ImageIndicatorView.OnItemChangeListener{
    @InjectView(R.id.indicate_view)
    ImageIndicatorView indicate_view;

    @InjectView(R.id.indicate_tv)
    TextView indicate_tv;

    List<ShowCaseNotices> notices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout(R.layout.sc_imageindicator_activity);

        getNbBar().setNBTitle("图片轮播");

        initData();
    }

    void initData(){
        notices = new ArrayList<>();

        ShowCaseNotices m = new ShowCaseNotices();
        m.image = "https://om.envchina.com/storage/media/kiso4bgPp3uvSqdDp9LvRlL6CdVAYu6p9LmqFWtb.jpeg";
        m.title = "通知一";
        notices.add(m);

        m = new ShowCaseNotices();
        m.image = "https://om.envchina.com/storage/media/c4a154381f251f10fbc98ba89b2997a8.jpeg";
        m.title = "通知二";
        notices.add(m);

        m = new ShowCaseNotices();
        m.image = "https://om.envchina.com/storage/media/946720a6ddbff3d43193a5f6808c7e91.jpeg";
        m.title = "通知三";
        notices.add(m);

        initIndicate(notices);

    }

    void initIndicate(final List<ShowCaseNotices> notices){
        indicate_view.setOnItemChangeListener(this);

        List<String> URL_LIST = new ArrayList<>();
        for(ShowCaseNotices m: notices){
            if(!TextUtils.isEmpty(m.image)) {
                URL_LIST.add(m.image);
            }else{
                URL_LIST.add("");
            }
        }

        this.indicate_view.setupLayoutByImageUrl(URL_LIST);
        this.indicate_view.setOnItemClickListener(new ImageIndicatorView.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                int realPosition = 0;
                if(position > 0) {
                    realPosition = position - 1;
                }
                ToastUtil.showShort(ShowCase_ImageIndicatorDemo.this,"当前点击:第"+(realPosition+1)+"个图片");
            }
        });
        this.indicate_view.setCurrentItem(1);//默认显示第一页
        this.indicate_view.show();
    }

    @Override
    public void onPosition(int position, int totalCount) {
        indicate_tv.setText(notices.get(position).title);
    }
}
