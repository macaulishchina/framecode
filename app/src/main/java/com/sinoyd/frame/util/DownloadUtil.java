package com.sinoyd.frame.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 作者： 王一凡
 * 创建时间： 2017/10/30
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.util
 */
public class DownloadUtil extends Thread{
    private String urlStr;
    private String filePath;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private long total;
    private HttpClient httpClient;
    private Context con;
    private boolean isStop = false;

    /**
     * 普通文件下载
     * @param con
     * @param urlStr
     */
    public DownloadUtil(Context con, String urlStr) {
        this.urlStr = urlStr;
        String filename = urlStr.substring(urlStr.lastIndexOf("/")+1);
        this.filePath = AppUtil.getStoragePath() + "/attach/" + filename;
        this.con = con;
        this.progressDialog = new ProgressDialog(con);
        this.progressDialog.setProgressStyle(1);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                DownloadUtil.this.isStop = true;
            }
        });
        File f = new File(filePath);
        File fdir = new File(f.getParent());
        if(!fdir.exists()) {
            fdir.mkdirs();
        }

    }

    /**
     * apk更新下载
     * @param con
     * @param urlStr
     * @param version
     */
    public DownloadUtil(Context con, String urlStr, String version) {
        this.urlStr = urlStr;
        this.filePath = AppUtil.getStoragePath()+"/attach/v"+version+".apk";
        this.con = con;
        this.progressDialog = new ProgressDialog(con);
        this.progressDialog.setProgressStyle(1);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                DownloadUtil.this.isStop = true;
            }
        });
        File f = new File(filePath);
        File fdir = new File(f.getParent());
        if(!fdir.exists()) {
            fdir.mkdirs();
        }

    }

    public DownloadUtil setCancelable(boolean cancelable) {
        if(this.progressDialog != null) {
            this.progressDialog.setCancelable(cancelable);
        }

        return this;
    }

    public void run() {
        try {
            this.handler.post(new Runnable() {
                public void run() {
                    DownloadUtil.this.progressDialog.show();
                }
            });
            this.httpClient = new DefaultHttpClient();
            this.httpClient.getParams().setParameter("http.protocol.handle-redirects", Boolean.valueOf(true));
            HttpGet e = new HttpGet(this.urlStr);
            HttpResponse f1 = this.httpClient.execute(e);
            if(f1.getStatusLine().getStatusCode() != 200) {
                throw new Exception("文件下载失败:" + f1.getStatusLine().getStatusCode());
            }

            HttpEntity entity = f1.getEntity();
            final long fileLength = entity.getContentLength();
            if(fileLength == 0L) {
                throw new Exception("服务器文件为空");
            }

            InputStream input = entity.getContent();
            FileOutputStream output = new FileOutputStream(this.filePath);
            this.total = 0L;
            byte[] data = new byte[1024];

            while(true) {
                int count;
                if((count = input.read(data)) != -1) {
                    if(!this.isStop) {
                        this.total += (long)count;
                        output.write(data, 0, count);
                        this.handler.post(new Runnable() {
                            public void run() {
                                DownloadUtil.this.progressDialog.setProgress((int)(DownloadUtil.this.total * 100L / fileLength));
                            }
                        });
                        continue;
                    }

                    if(!e.isAborted()) {
                        e.abort();
                        this.httpClient.getConnectionManager().shutdown();
                    }
                }

                output.flush();
                output.close();
                input.close();
                break;
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            LogUtil.Log2Storage(IOHelp.throwException2String(var10));
            File f = new File(this.filePath);
            if(f.exists()) {
                f.delete();
            }

            return;
        }

        this.handler.post(new Runnable() {
            public void run() {
                if(DownloadUtil.this.progressDialog != null && DownloadUtil.this.progressDialog.isShowing()) {
                    DownloadUtil.this.progressDialog.dismiss();
                }

                if(DownloadUtil.this.isStop) {
                    File f = new File(DownloadUtil.this.filePath);
                    if(f.exists()) {
                        f.delete();
                    }
                } else {
                    IOHelp.doOpenFile(DownloadUtil.this.con, DownloadUtil.this.filePath);
                }

            }
        });
    }
}
