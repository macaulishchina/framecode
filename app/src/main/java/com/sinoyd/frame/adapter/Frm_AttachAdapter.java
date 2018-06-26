package com.sinoyd.frame.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinoyd.R;
import com.sinoyd.frame.action.FrmUploadAction;
import com.sinoyd.frame.model.FrmAttachments;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 王一凡
 * 创建时间： 2018/3/30
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.adapter
 */
public class Frm_AttachAdapter extends BaseAdapter {
    List<FrmAttachments> mdata = new ArrayList<>();
    Context context;
    private FrmUploadAction uploadAction;

    public Frm_AttachAdapter(List<FrmAttachments> mdata, Context mcon) {
        this.mdata = mdata;
        this.context = mcon;
        uploadAction = new FrmUploadAction();
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public FrmAttachments getItem(int i) {
        return mdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.frm_attach_item, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        FrmAttachments model = getItem(position);
        if(!TextUtils.isEmpty(model.AttFileName)) {
            //本地上传附件
            holder.iv_icon.setImageBitmap(uploadAction.getImgByFileName(context, model.AttFileName));
            holder.tv_name.setText(model.AttFileName);
        }else if(!TextUtils.isEmpty(model.id)){
            //网络获取附件
            holder.iv_icon.setImageBitmap(uploadAction.getImgByFileName(context, model.id));
            holder.tv_name.setText(uploadAction.getFileNameByUrl(model.id));
        }
        holder.tv_length.setText(model.DateTime);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_length;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.ivAttachIcon);
            tv_name = (TextView) view.findViewById(R.id.tvFileName);
            tv_length = (TextView) view.findViewById(R.id.tvFileLength);
            view.setTag(this);
        }
    }
}
