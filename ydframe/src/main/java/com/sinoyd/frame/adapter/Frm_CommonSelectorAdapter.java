package com.sinoyd.frame.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.model.FrmCommonSelectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 王一凡
 * 创建时间： 2018/4/13
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.adapter
 */
public class Frm_CommonSelectorAdapter extends BaseAdapter {
    List<FrmCommonSelectModel> mdata = new ArrayList<>();
    Context context;
    int itemLayout = 0;

    public Frm_CommonSelectorAdapter(Context con, List<FrmCommonSelectModel> mdata, int layout) {
        this.context = con;
        this.mdata = mdata;
        this.itemLayout = layout;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public FrmCommonSelectModel getItem(int i) {
        return mdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context, itemLayout, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        FrmCommonSelectModel model = getItem(position);
        holder.item_value.setText(model.item_value);
        return convertView;
    }

    class ViewHolder {
        TextView item_value;

        public ViewHolder(View view) {
            item_value = (TextView) view.findViewById(R.id.itemvalue);
            view.setTag(this);
        }
    }
}
