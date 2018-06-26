package com.sinoyd.frame.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;

/**
 * 作者： 王一凡
 * 创建时间： 2017/10/30
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.util
 */
public class IOService {
    private String tag = "FileDialog";
    public static final boolean D = false;

    public IOService() {
    }

    public static boolean checkNet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if(info != null && info.isConnected() && info.getState() == State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception var3) {
            ;
        }

        return false;
    }

    public static void AlertWithLayout(Context con, int layout, int icon) {
        LayoutInflater inflater = LayoutInflater.from(con);
        View view = inflater.inflate(layout, (ViewGroup)null);
        AlertDialog ad = (new AlertDialog.Builder(con)).setTitle("欢迎登录").setView(view).setPositiveButton("确定", (DialogInterface.OnClickListener)null).setNegativeButton("取消", (DialogInterface.OnClickListener)null).create();
        ad.show();
    }

    public static void Call(Context con, String phonenum) {
        String[] dualSimTypes = new String[]{"subscription", "Subscription", "com.android.phone.extra.slot", "phone", "com.android.phone.DialingMode", "simId", "simnum", "phone_type", "simSlot"};
        Intent intent = (new Intent("android.intent.action.CALL")).setFlags(268435456);
        intent.setData(Uri.parse("tel:" + phonenum));

        for(int i = 0; i < dualSimTypes.length; ++i) {
            intent.putExtra(dualSimTypes[i], 2);
        }

        con.startActivity(intent);
    }

    public static void SendMsg(Context con, String phonenum, String shortmsg) {
        PhoneUtil.sendMsgUsePhoneSelf(con, phonenum, shortmsg);
    }

    public static Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    public static Intent getImageFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    public static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        Uri uri2;
        if(paramBoolean) {
            uri2 = Uri.parse(param);
            intent.setDataAndType(uri2, "text/plain");
        } else {
            uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }

        return intent;
    }

    public static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(67108864);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    public static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(67108864);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    public static Intent getChmFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    public static Intent getExcelFileIntent(String param, Context con) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    public static Intent getPptFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    public static Intent getRARFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-rar-compressed");
        intent.setAction("com.asrazpaid");
        System.out.println("uncompass RAR");
        return intent;
    }

    public static void doOpenFile2(Context con, String attachfilepath) {
        String suffix = attachfilepath.substring(attachfilepath.lastIndexOf("."), attachfilepath.length()).toLowerCase();
        Intent intent = new Intent("android.intent.action.VIEW");
        String param = "file://" + attachfilepath;
        Uri uri = Uri.parse(param);
        String type = null;
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(attachfilepath));
        System.out.println("filetype:" + type);
        if(type == null) {
            String e = attachfilepath.toLowerCase();
            if(!e.endsWith("mp3") && !e.endsWith("wav") && !e.endsWith("wma")) {
                if(e.endsWith("apk")) {
                    type = "application/vnd.android.package-archive";
                }
            } else {
                type = "audio/*";
            }
        }

        if(type != null && !suffix.equals(".rar")) {
            intent.setDataAndType(uri, type);

            try {
                ((Activity)con).startActivityForResult(intent, 1);
            } catch (ActivityNotFoundException var8) {
                ToastUtil.showShort(con, "文件不能打开");
            }
        } else {
            try {
                Intent e1 = null;
                if(!suffix.equals(".jpg") && !suffix.equals(".png") && !suffix.equals(".gif") && !suffix.equals(".bmp")) {
                    if(suffix.equals(".pdf")) {
                        e1 = getPdfFileIntent(attachfilepath);
                    } else if(!suffix.equals(".doc") && !suffix.equals(".docx")) {
                        if(!suffix.equals(".xls") && !suffix.equals(".xlsx")) {
                            if(!suffix.equals(".mp3") && !suffix.equals(".wma") && !suffix.equals(".mp4") && !suffix.equals(".wav")) {
                                if(suffix.equals(".txt")) {
                                    e1 = getTextFileIntent(attachfilepath, false);
                                } else if(suffix.equals(".html")) {
                                    e1 = getHtmlFileIntent(attachfilepath);
                                } else if(!suffix.equals(".ppt") && !suffix.equals(".pptx")) {
                                    if(suffix.equals(".rar") || suffix.equals(".zip")) {
                                        e1 = getRARFileIntent(attachfilepath);
                                    }
                                } else {
                                    e1 = getPptFileIntent(attachfilepath);
                                }
                            } else {
                                e1 = getAudioFileIntent(attachfilepath);
                            }
                        } else {
                            e1 = getExcelFileIntent(attachfilepath, con);
                        }
                    } else {
                        e1 = getWordFileIntent(attachfilepath);
                    }
                } else {
                    e1 = getImageFileIntent(attachfilepath);
                }

                con.startActivity(e1);
            } catch (Exception var9) {
                ToastUtil.showShort(con, "没有适合的程序打开此文件");
            }
        }

    }

    public static void playMusic(Context con, String filename) throws IOException {
        MediaPlayer m = new MediaPlayer();
        AssetFileDescriptor descriptor = con.getAssets().openFd(filename);
        m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
        descriptor.close();
        m.prepare();
        m.start();
    }

    public static void deleteFile(File file) {
        if(file.exists()) {
            if(file.isFile()) {
                file.delete();
            } else if(file.isDirectory()) {
                File[] files = file.listFiles();

                for(int i = 0; i < files.length; ++i) {
                    deleteFile(files[i]);
                }
            }

            file.delete();
        } else {
            System.out.println("所删除的文件不存在！\n");
        }

    }

    public static void intentView(Activity con, Class<?> disClass) {
        Intent intent = new Intent(con, disClass);
        con.startActivity(intent);
    }

    public static void intentViewFinish(Activity con, Class<?> disClass) {
        Intent intent = new Intent(con, disClass);
        con.startActivity(intent);
        con.finish();
    }

    public static void intentViewWithTitle(Activity con, Class<?> disClass, String title) {
        Intent intent = new Intent(con, disClass);
        intent.putExtra("viewtitle", title);
        con.startActivity(intent);
    }

    public static void startApp(Context con, String packageName, String className, String param) {
        try {
            Intent e = new Intent("android.intent.action.MAIN");
            e.addCategory("android.intent.category.LAUNCHER");
            ComponentName cn = new ComponentName(packageName, className);
            e.setComponent(cn);
            e.putExtra("param", param);
            con.startActivity(e);
        } catch (Exception var6) {
            ToastUtil.showShort(con, "应用未安装");
        }

    }

    public static void startApp(Context con, String packageName, String param) {
        try {
            Intent e = con.getPackageManager().getLaunchIntentForPackage(packageName);
            e.putExtra("param", param);
            con.startActivity(e);
        } catch (Exception var4) {
            ToastUtil.showShort(con, "应用未安装");
        }

    }
}
