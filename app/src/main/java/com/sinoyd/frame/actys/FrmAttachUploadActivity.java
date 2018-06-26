package com.sinoyd.frame.actys;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sinoyd.R;
import com.sinoyd.frame.action.FrmUploadAction;
import com.sinoyd.frame.adapter.Frm_AttachAdapter;
import com.sinoyd.frame.model.FrmAttachments;
import com.sinoyd.frame.task.BaseTask;
import com.sinoyd.frame.util.AppUtil;
import com.sinoyd.frame.util.DateUtil;
import com.sinoyd.frame.util.IOHelp;
import com.sinoyd.frame.util.JsonHelp;
import com.sinoyd.frame.util.ToastUtil;
import com.sinoyd.frame.views.FrmActionSheet;
import com.sinoyd.webview.action.WebloaderAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 作者： 王一凡
 * 创建时间： 2018/3/30
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.actys
 */
public class FrmAttachUploadActivity extends SinoBaseActivity implements FrmActionSheet.MenuItemClickListener, AdapterView.OnItemClickListener, BaseTask.BaseTaskCallBack{
    @InjectView(R.id.lv_attach)
    ListView lvAttach;
    @InjectView(R.id.btn_add)
    Button btnAdd;

    private List<FrmAttachments> attachmentsList;//所有附件
    private Frm_AttachAdapter madapter;
    private String folderPath = AppUtil.getStoragePath()+"/upload";

    private File attachFile;//拍摄照片

    private static final int Upload_ID = 1;
    private static final int Download_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNbBar().setNBTitle("附件上传");

        setLayout(R.layout.frm_attach_activity);

        initView();

    }

    void initView(){
        //添加主题
        getActivity().setTheme(R.style.ActionSheetStyleIOS7);
        //标题栏右侧按钮
        getNbBar().nbRightText.setText("完成");
        getNbBar().nbRightText.setVisibility(View.VISIBLE);
        getNbBar().setNBTitle("附件上传");

        attachmentsList = new ArrayList<>();
        if(getIntent().hasExtra(FrmUploadAction.Attach_INTENT)) {
            attachmentsList = (List<FrmAttachments>) getIntent().getSerializableExtra(FrmUploadAction.Attach_INTENT);
        }

        madapter = new Frm_AttachAdapter(attachmentsList, getActivity());
        lvAttach.setAdapter(madapter);
        lvAttach.setOnItemClickListener(this);

    }

    @Override
    public void onNBRight() {
        super.onNBRight();
        if(attachmentsList.size()>0) {
            List<File> fileList = new ArrayList<>();

            JSONArray jsonArray = new JSONArray();//返回上一个页面的json参数
            for(FrmAttachments m :attachmentsList){
                if(!TextUtils.isEmpty(m.AttachPath)) {
                    File file = new File(m.AttachPath);
                    fileList.add(file);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("file_name",m.AttFileName);
                        object.put("file_path",m.AttachPath);
                        object.put("file_size",m.size);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jsonArray.put(object);
                }
            }

            if(fileList.size()>0) {
                //*******************自定义上传附件逻辑*******************
                //showLoading();
            }


            Intent intent = new Intent();
            intent.putExtra(WebloaderAction.RESULT_DATA, jsonArray.toString());
            setResult(RESULT_OK, intent);
            finish();

        }else{
            finish();
        }
    }

    @Override
    public void refresh(final int taskId, final Object obj) {
        hideLoading();
        if(JsonHelp.checkResult(obj)) {
            if (taskId == Upload_ID) {

            } else if (taskId == Download_ID) {

            }
        }

    }

    @OnClick(R.id.btn_add)
    public void onClick() {
        FrmActionSheet menuView = new FrmActionSheet(getActivity());
        menuView.setCancelButtonTitle("取消");
        //   menuView.addItems("拍照", "相册", "录音", "录像");
        menuView.addItems("本地相册", "拍照");
        menuView.setItemClickListener(this);
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
    }

    @Override
    public void onItemClick(int index) {
        switch (index) {
            case 0:
                //相册
                FrmUploadAction.openPic(this);
                break;
            case 1:
                //拍照
                attachFile = new File(folderPath, DateUtil.convertDate(new Date(), "yyyyMMddHHmmss") + ".jpg");
                FrmUploadAction.openCamera(this, attachFile);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FrmAttachments item = attachmentsList.get(i);
        if (!TextUtils.isEmpty(item.AttachPath)) {
            //打开本地
            String filePath = item.AttachPath;
            IOHelp.openFile(this, new File(filePath));
        }else if(!TextUtils.isEmpty(item.id)){
            //打开网络
            //*******************自定义下载附件逻辑*******************
            //showLoading();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FrmUploadAction.OpenCamera_REQUESTCODE:
                if (resultCode == RESULT_OK) {
                    addAttachFile(decodeFile(attachFile.getAbsolutePath()));
                }
                break;
            case FrmUploadAction.OpenPhoto_REQUESTCODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.MediaColumns.DATA};
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                            String path = cursor.getString(column_index);
                            addAttachFile(decodeFile(path));
                        }
                    }
                }
                break;
        }
    }

    //添加附件
    private void addAttachFile(String path) {
        attachFile = new File(path);
        if (attachFile.exists()) {
            long fileSize = FrmUploadAction.getFileSize(attachFile);//b
            if (fileSize <= 10 * 1024 * 1024) {
                FrmAttachments mtemp = new FrmAttachments();
                mtemp.AttachPath = path;
                mtemp.AttFileName = attachFile.getName();
                String time = DateUtil.convertDate(new Date(attachFile.lastModified()), "yyyy-MM-dd HH:mm:ss");
                mtemp.FileDate = time;
                mtemp.DateTime = time;
                BigDecimal b1 = new BigDecimal(fileSize);
                BigDecimal b2 = new BigDecimal(1024 * 1024);
                mtemp.size = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue()+"MB";
                attachmentsList.add(mtemp);
                madapter.notifyDataSetChanged();
            } else {
                ToastUtil.showShort(getContext(), "文件大小不可超过10M!");
            }
        } else {
            ToastUtil.showShort(getContext(), "您选择的文件不存在!");
        }
    }

    /**
     * 对图片进行压缩
     * @param srcPath
     * @return
     */
    public String decodeFile(String srcPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 获取这个图片的宽和高
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options); //此时返回bm为空

        int be = options.outWidth / 720;
        if (be < 1) {
            be = 1;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = be;

        bitmap = BitmapFactory.decodeFile(srcPath, options);
        String photoName = DateUtil.convertDate(new Date(), "yyyyMMddHHmss") + "s.jpg";
        String sfullPath = folderPath + "/" + photoName;
        File sfullFile = new File(sfullPath);

        try {
            FileOutputStream out = new FileOutputStream(sfullFile);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //删除拍摄照片
            //IOHelp.deleteFile(new File(srcPath));
        }

        return sfullPath;
    }
}
