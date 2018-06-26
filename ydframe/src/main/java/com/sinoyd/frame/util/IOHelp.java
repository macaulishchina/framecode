package com.sinoyd.frame.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：输入/输出工具处理类
 */
public class IOHelp {
    private static final String[][] MIME_MapTable = new String[][]{{".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"}, {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"}, {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"}, {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"}, {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, {".xls", "application/vnd.ms-excel"}, {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"}, {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"}, {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"}, {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"}, {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"}, {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"}, {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"}, {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"}, {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"}, {".ppt", "application/vnd.ms-powerpoint"}, {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, {".prop", "text/plain"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"}, {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/x-zip-compressed"}, {"", "*/*"}};

    public IOHelp() {
    }

    public static String File2Base64(File f) {
        byte[] b = File2Byte(f);
        return Base64.encodeToString(b, 0);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] obj2bytes(Object obj) throws IOException {
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        return fos.toByteArray();
    }

    public static Object bytes2obj(byte[] b) throws ClassNotFoundException, IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }

    public static void TextFileContinueWrite(String path, String log) {
        try {
            PrintWriter e = new PrintWriter(new BufferedOutputStream(new FileOutputStream(path, true)));
            e.write("\r\n");
            e.write(log);
            e.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static byte[] InputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream e = new ByteArrayOutputStream();

            int ch;
            while((ch = is.read()) != -1) {
                e.write(ch);
            }

            byte[] imgdata = e.toByteArray();
            e.close();
            return imgdata;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static byte[] File2Byte(File f) {
        try {
            FileInputStream e = new FileInputStream(f);
            return InputStreamToByte(e);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static void FoldCreate(String path) {
        File f = new File(path);
        if(!f.exists()) {
            f.mkdirs();
        }

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
        }

    }

    public static void copyFile(String assfilename, String destpathname) throws IOException {
        FileInputStream fis = new FileInputStream(assfilename);
        FileOutputStream fos = new FileOutputStream(destpathname);
        byte[] data = new byte[1024];

        int count;
        while((count = fis.read(data)) != -1) {
            fos.write(data, 0, count);
        }

        fos.flush();
        fos.close();
        fis.close();
    }

    public static String readtxt(String path) {
        try {
            BufferedReader e = new BufferedReader(new FileReader(path));
            String str = "";

            for(String r = e.readLine(); r != null; r = e.readLine()) {
                str = str + r;
            }

            return str;
        } catch (Exception var4) {
            return "";
        }
    }

    public static void writeText2Path(String path, String msg) {
        try {
            FileOutputStream e = new FileOutputStream(path);
            e.write(msg.getBytes());
            e.flush();
            e.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void byte2file(byte[] b, String filepath) {
        saveFile(b, filepath);
    }

    public static boolean saveFile(byte[] b, String filepath) {
        try {
            File e = new File(filepath);
            File pdir = new File(e.getParent());
            if(!pdir.exists()) {
                pdir.mkdirs();
            }

            FileOutputStream out = new FileOutputStream(filepath);
            out.write(b);
            out.close();
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean saveBitmap(Bitmap bm, String filepath) {
        try {
            File e = new File(filepath);
            File pdir = new File(e.getParent());
            if(!pdir.exists()) {
                pdir.mkdirs();
            } else if(e.exists()) {
                e.delete();
            }

            FileOutputStream out = new FileOutputStream(e);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static void copyFileTemp(String src, String dest) {
        BufferedInputStream is = null;
        BufferedOutputStream os = null;
        String temp = src.substring(src.lastIndexOf("/"));
        dest = dest + temp;

        try {
            is = new BufferedInputStream(new FileInputStream(src));
            os = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] e = new byte[1024];
            boolean len = false;

            try {
                int len1;
                while((len1 = is.read(e)) != -1) {
                    os.write(e, 0, len1);
                }

                os.flush();
            } catch (IOException var32) {
                var32.printStackTrace();
            } finally {
                if(is != null) {
                    try {
                        is.close();
                    } catch (IOException var31) {
                        var31.printStackTrace();
                    }
                }

            }
        } catch (FileNotFoundException var34) {
            var34.printStackTrace();
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException var30) {
                    var30.printStackTrace();
                }
            }

        }

    }

    public static void copyFileFromAss2Dir(Context con, String assfilename, String destpathname) throws IOException {
        InputStream fis = con.getResources().getAssets().open(assfilename);
        FileOutputStream fos = new FileOutputStream(destpathname);
        byte[] data = new byte[1024];

        int count;
        while((count = fis.read(data)) != -1) {
            fos.write(data, 0, count);
        }

        fos.flush();
        fos.close();
        fis.close();
    }

    public static String do_exec(String cmd) {
        String s = "/n";

        try {
            Process e = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(e.getInputStream()));

            for(String line = null; (line = in.readLine()) != null; s = s + line + "/n") {
                ;
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return cmd + "result=" + s;
    }

    public static String throwException2String(Throwable ex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        ex.printStackTrace(printStream);
        byte[] data = baos.toByteArray();
        String info = new String(data);
        Object data1 = null;
        printStream.close();

        try {
            baos.close();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return info;
    }

    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream ex = new ObjectOutputStream(bos);
            ex.writeObject(obj);
            ex.flush();
            bytes = bos.toByteArray();
            ex.close();
            bos.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return bytes;
    }

    public static Object toObject(byte[] bytes) {
        if(bytes == null) {
            return null;
        } else {
            Object obj = null;

            try {
                ByteArrayInputStream ex = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(ex);
                obj = ois.readObject();
                ois.close();
                ex.close();
            } catch (IOException var4) {
                var4.printStackTrace();
            } catch (ClassNotFoundException var5) {
                var5.printStackTrace();
            }

            return obj;
        }
    }

    private static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0) {
            return type;
        } else {
            String end = fName.substring(dotIndex, fName.length()).toLowerCase();
            if(end == "") {
                return type;
            } else {
                for(int i = 0; i < MIME_MapTable.length; ++i) {
                    if(end.equals(MIME_MapTable[i][0])) {
                        type = MIME_MapTable[i][1];
                    }
                }

                return type;
            }
        }
    }

    public static void openFile(Context con, File file) {
        try {
            Intent e = new Intent();
            e.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            e.setAction("android.intent.action.VIEW");
            String type = getMIMEType(file);
            e.setDataAndType(Uri.fromFile(file), type);
            con.startActivity(e);
        } catch (Exception var4) {
            Log.i("sinoyd","未知文件类型");
        }

    }

    public static double getDirSize(File file) {
        if(!file.exists()) {
            return 0.0D;
        } else if(!file.isDirectory()) {
            double var8 = (double)file.length() / 1024.0D;
            return var8;
        } else {
            File[] size = file.listFiles();
            double size1 = 0.0D;
            File[] var7 = size;
            int var6 = size.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                File f = var7[var5];
                size1 += getDirSize(f);
            }

            return size1;
        }
    }

    public static String getFileSize(String fileSize) {
        if(fileSize == null) {
            return "";
        } else {
            Pattern pattern = Pattern.compile("[0-9]+.?[0-9]*");
            if(pattern.matcher(fileSize).matches()) {
                fileSize = formetFileSize(Long.parseLong(fileSize));
            }

            return fileSize;
        }
    }

    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if(fileS == 0L) {
            fileSizeString = "0B";
        } else if(fileS < 1024L) {
            fileSizeString = df.format((double)fileS) + "B";
        } else if(fileS < 1048576L) {
            fileSizeString = df.format((double)fileS / 1024.0D) + "K";
        } else if(fileS < 1073741824L) {
            fileSizeString = df.format((double)fileS / 1048576.0D) + "M";
        } else {
            fileSizeString = df.format((double)fileS / 1.073741824E9D) + "G";
        }

        return fileSizeString;
    }

    public static void scanImage(Context context, String filename, MediaScannerConnection.OnScanCompletedListener listener) {
        MediaScannerConnection.scanFile(context, new String[]{filename}, (String[])null, listener);
    }

    public static void doOpenFile(Context con, String attachfilepath) {
        File file = new File(attachfilepath);
        if(!file.exists()) {
            ToastUtil.showShort(con, "附件不存在");
        } else if(!attachfilepath.contains(".")) {
            ToastUtil.showShort(con, "未知文件类型");
        } else {
            try {
                if(attachfilepath.contains(".doc") || attachfilepath.contains(".docx")) {
                    Intent suffix1 = new Intent("android.intent.action.VIEW");
                    suffix1.setClassName("com.yozo.office", "emo.main.MainAppProxy");
                    suffix1.setData(Uri.fromFile(file));
                    suffix1.putExtra("readOnly", false);
                    con.startActivity(suffix1);
                    return;
                }
            } catch (Exception var16) {
                ;
            }

            String suffix = attachfilepath.substring(attachfilepath.lastIndexOf("."), attachfilepath.length()).toLowerCase();
            Intent intent = new Intent("android.intent.action.VIEW");
            String param = "file://" + attachfilepath;
            Uri uri = Uri.parse(param);
            String type = null;
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(attachfilepath));
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
                } catch (ActivityNotFoundException var14) {
                    ToastUtil.showShort(con, "文件不能打开");
                }
            } else {
                try {
                    Intent e1 = null;
                    if(!suffix.equals(".jpg") && !suffix.equals(".png") && !suffix.equals(".gif") && !suffix.equals(".bmp")) {
                        if(suffix.equals(".pdf")) {
                            e1 = IOService.getPdfFileIntent(attachfilepath);
                        } else if(!suffix.equals(".doc") && !suffix.equals(".docx") && !suffix.equals(".wps")) {
                            if(!suffix.equals(".xls") && !suffix.equals(".xlsx")) {
                                if(!suffix.equals(".mp3") && !suffix.equals(".wma") && !suffix.equals(".mp4") && !suffix.equals(".wav")) {
                                    if(suffix.equals(".txt")) {
                                        e1 = IOService.getTextFileIntent(attachfilepath, false);
                                    } else if(suffix.equals(".html")) {
                                        e1 = IOService.getHtmlFileIntent(attachfilepath);
                                    } else if(!suffix.equals(".ppt") && !suffix.equals(".pptx")) {
                                        if(suffix.toLowerCase().contains(".rar") || suffix.toLowerCase().contains(".zip")) {
                                            try {
                                                openFile(con, new File(attachfilepath));
                                            } catch (ActivityNotFoundException var13) {
                                                try {
                                                    Uri uri1 = Uri.parse("market://search?q=rar");
                                                    Intent it = new Intent("android.intent.action.VIEW", uri1);
                                                    con.startActivity(it);
                                                } catch (ActivityNotFoundException var12) {
                                                    ;
                                                }
                                            }

                                            return;
                                        }

                                        if(suffix.toLowerCase().endsWith(".cebx")) {
                                            e1 = new Intent("com.founder.apabi.BOOKREADING", Uri.fromFile(new File(attachfilepath)));
                                        }
                                    } else {
                                        e1 = IOService.getPptFileIntent(attachfilepath);
                                    }
                                } else {
                                    e1 = IOService.getAudioFileIntent(attachfilepath);
                                }
                            } else {
                                e1 = IOService.getExcelFileIntent(attachfilepath, con);
                            }
                        } else {
                            e1 = IOService.getWordFileIntent(attachfilepath);
                        }
                    } else {
                        e1 = IOService.getImageFileIntent(attachfilepath);
                    }

                    con.startActivity(e1);
                } catch (Exception var15) {
                    ToastUtil.showShort(con, "没有适合的程序打开此文件");
                }
            }

        }
    }

    public static String getMimeType(File file) {
        String extension = getExtension(file);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    private static String getExtension(final File file) {
        String suffix = "";
        String name = file.getName();
        final int idx = name.lastIndexOf(".");
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }
}
