package com.sinoyd.frame.showcase.actys;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sinoyd.R;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.views.FrmListFootView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 作者： 王一凡
 * 创建时间： 2017/12/13
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.actys
 */
public class ShowCase_PagingListDemo extends SinoBaseActivity implements AbsListView.OnScrollListener{
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeLayout;

    @InjectView(R.id.lv)
    ListView listView;

    private FrmListFootView footLoadView;

    private List<String> dataList;

    private ArrayAdapter<String> adapter;

    private int currentPageIndex = 1;
    public  int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout(R.layout.sc_paginglist_demo);

        initView();
    }

    private void initView(){
        getNbBar().setNBTitle("分页列表");

        mSwipeLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //上拉刷新,初始化数据
                currentPageIndex = 1;
                getData();
            }
        });

        footLoadView = new FrmListFootView(getActivity());
        listView.setOnScrollListener(this);

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        mSwipeLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },  2000);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() - 1 && view.getCount() >= pageSize * currentPageIndex) {
                //下拉加载更多
                ++currentPageIndex;
                getData();
            }
        }
    }

    void getData(){
        if(currentPageIndex == 1) {
            dataList.clear();
        }


        for(int i=0; i<20; i++){
            dataList.add(20*(currentPageIndex-1)+i+"");
        }


        if (dataList.size() < pageSize * currentPageIndex) {
            if (listView.getFooterViewsCount() > 0) {
                listView.removeFooterView(footLoadView);
            }
        } else {
            if (listView.getFooterViewsCount() < 1) {
                listView.addFooterView(footLoadView);
            }
        }

        mSwipeLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
