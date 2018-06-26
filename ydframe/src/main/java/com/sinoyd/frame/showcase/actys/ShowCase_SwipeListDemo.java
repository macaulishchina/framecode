package com.sinoyd.frame.showcase.actys;

import android.os.Bundle;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.util.ToastUtil;
import com.sinoyd.frame.views.swipelistview.OnClickItemEvent;
import com.sinoyd.frame.views.swipelistview.OnClickSwipeListBtn;
import com.sinoyd.frame.views.swipelistview.SwipeListView;
import com.sinoyd.showcase.adapter.ShowCase_SwipeList_Adapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 作者： 王一凡
 * 创建时间： 2017/12/14
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.actys
 */
public class ShowCase_SwipeListDemo extends SinoBaseActivity implements OnClickItemEvent,OnClickSwipeListBtn {
    @InjectView(R.id.lv_item_task)
    SwipeListView lv_item_task;

    private List<String> dataList;
    private ShowCase_SwipeList_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setLayout(R.layout.sc_swipelist_demo);

        initView();

    }

    void initView(){
        getNbBar().setNBTitle("侧滑删除列表");

        dataList = new ArrayList<>();

        for(int i=0; i<20; i++){
            dataList.add("第"+i+"条数据");
        }

        adapter = new ShowCase_SwipeList_Adapter(getContext(), dataList, this);
        adapter.setOnItemClickEvent(this);
        lv_item_task.setAdapter(adapter);



    }

    @Override
    public void onClickItem(int position) {
        ToastUtil.showShort(this,"点击："+position);
    }

    @Override
    public void onClickBtn(int position) {
        dataList.remove(position);
        lv_item_task.closeAllItems();
        adapter.setData(dataList);
        adapter.notifyDataSetChanged();
    }
}
