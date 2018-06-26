package com.sinoyd.showcase.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.util.DateUtil;
import com.sinoyd.frame.views.swipelistview.OnClickItemEvent;
import com.sinoyd.frame.views.swipelistview.OnClickSwipeListBtn;

import java.util.Date;
import java.util.List;

/**
 * 作者： 王一凡
 * 创建时间： 2017/12/14
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.showcase.adapter
 */
public class ShowCase_SwipeList_Adapter extends BaseAdapter {
    private Context con;
    private List<String> data;
    private OnClickItemEvent onItemClick;
    private OnClickSwipeListBtn onClickSwipeListBtn;

    public ShowCase_SwipeList_Adapter(Context context, List<String> list,OnClickSwipeListBtn onClick) {
        this.con = context;
        this.data = list;
        this.onClickSwipeListBtn = onClick;
    }

    public void setOnItemClickEvent(OnClickItemEvent click){
        this.onItemClick = click;
    }

    public void setData(List<String> list){
        this.data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(con).inflate(R.layout.sc_swipeitem_item, null);
        }
        LinearLayout ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
        TextView tv_item = (TextView) convertView.findViewById(R.id.tv_item);
        Button delete = (Button) convertView.findViewById(R.id.delete);

        //   String ProblemCode = mData.get(position).get("ProblemCode").toString();
        tv_item.setText(data.get(position));


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickSwipeListBtn!=null){
                    onClickSwipeListBtn.onClickBtn(position);
                }
            }
        });

        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onClickItem(position);
                }
            }
        });

        return convertView;
    }
}
