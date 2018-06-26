package com.sinoyd.frame.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.sinoyd.frame.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：框架sqllite控制类
 */
public class FrmDBService {
    public FrmDBService() {
    }

    public static synchronized void setConfigValue(String key, String value) {
        setSecurityValue(key, value);
    }

    public static void deleteConfigValue(String key) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        db.execSQL("delete from Frame_Resc where key = ?", new String[]{key});
    }

    public static synchronized String getConfigValue(String key) {
        return getSecurityValue(key);
    }

    public static synchronized void setConfigBlob(String key, byte[] bytes) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        db.execSQL("delete from Frame_Resc where key = ?", new String[]{key});
        ContentValues cv = new ContentValues();
        cv.put("Key", key);
        cv.put("Value", bytes);
        db.insert("Frame_Resc", (String)null, cv);
    }

    public static synchronized byte[] getConfigBlob(String key) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.rawQuery("select value from Frame_Resc where key=?", new String[]{key});

        byte[] b;
        for(b = null; cursor.moveToNext(); b = cursor.getBlob(0)) {
            ;
        }

        cursor.close();
        return b;
    }

    private static void setSecurityValue(String key, String value) {
        try {
            String e = SimpleCrypto.encrypt(key, value);
            setConfigBlob(key, e.getBytes());
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private static String getSecurityValue(String key) {
        String securityStr = "";

        try {
            byte[] e = getConfigBlob(key);
            if(e != null) {
                String pswEncode = new String(e);
                securityStr = SimpleCrypto.decrypt(key, pswEncode);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return securityStr;
    }

    public static void saveLog(String logType, String loginfo) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        db.execSQL("INSERT INTO Frame_Log(LogInfo,LogDate,LogType) values(?,?,?)", new Object[]{loginfo, DateUtil.getCurrentTime(), logType});
    }

    public static String getTopErrorLog() {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT loginfo FROM Frame_log WHERE logtype = \'ERR\' order by logdate desc", (String[])null);

        String logInfo;
        for(logInfo = ""; cursor.moveToNext(); logInfo = cursor.getString(0)) {
            ;
        }

        db.execSQL("delete from frame_log where logtype = \'ERR\'");
        cursor.close();
        return logInfo;
    }

    public static void insertSearchRecord(String keyWord, String userGuid) {
        String opeDate = DateUtil.getCurrentTime();
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        db.execSQL("DELETE FROM Frame_SearchRecord WHERE UserGuid = ? and KeyWord = ?", new String[]{userGuid, keyWord});
        db.execSQL("INSERT INTO Frame_SearchRecord(KeyWord,UserGuid,OperationDate) values(?,?,?)", new String[]{keyWord, userGuid, opeDate});
    }

    public static void deleteAllSearchByUserGuid(String userGuid) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        db.execSQL("DELETE FROM Frame_SearchRecord WHERE UserGuid = ?", new String[]{userGuid});
    }

    public static List<String> getSearchRecordByUserGuid(String userGuid) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT KeyWord FROM Frame_SearchRecord WHERE UserGuid = ? order by OperationDate desc", new String[]{userGuid});
        ArrayList list = new ArrayList();

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }

        cursor.close();
        return list;
    }

    public static List<Map<String, String>> getLocalCollections(String userGuid, int pageSize, int pageIndex) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT MsgGuid,Title,DateTime,Publisher,Type,URL,Remark,Flag,CollectionTime FROM Frame_Collection WHERE UserGuid = ?  limit " + pageSize + " offset " + (pageIndex - 1) * pageSize, new String[]{userGuid});
        ArrayList list = new ArrayList();
        String[] unames = cursor.getColumnNames();

        while(cursor.moveToNext()) {
            HashMap map = new HashMap();
            String[] var11 = unames;
            int var10 = unames.length;

            for(int var9 = 0; var9 < var10; ++var9) {
                String name = var11[var9];
                String value = cursor.getString(cursor.getColumnIndex(name));
                if(!TextUtils.isEmpty(value)) {
                    map.put(name, value);
                }
            }

            list.add(map);
        }

        cursor.close();
        return list;
    }

    public static int saveLocalCollection(String UserGuid, String MsgGuid, String Title, String DateTime, String Publisher, String Type, String URL, String Remark, String Flag, String CollectionTime) {
        if(isCollection(UserGuid, MsgGuid)) {
            delLocalCollection(UserGuid, MsgGuid);
        }

        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserGuid", UserGuid);
        values.put("MsgGuid", MsgGuid);
        values.put("Title", Title);
        values.put("DateTime", DateTime);
        values.put("Publisher", Publisher);
        values.put("Type", Type);
        values.put("URL", URL);
        values.put("Remark", Remark);
        values.put("Flag", Flag);
        values.put("CollectionTime", CollectionTime);
        long rowid = db.insert("Frame_Collection", "MsgGuid", values);
        return rowid > -1L?1:0;
    }

    public static int delLocalCollection(String UserGuid, String MsgGuid) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        String whereClause = "UserGuid=? and MsgGuid=?";
        String[] whereArgs = new String[]{UserGuid, MsgGuid};
        db.delete("Frame_Collection", whereClause, whereArgs);
        return 1;
    }

    public static int delAllLocalCollections(String UserGuid) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        String whereClause = "UserGuid=?";
        String[] whereArgs = new String[]{UserGuid};
        db.delete("Frame_Collection", whereClause, whereArgs);
        return 1;
    }

    public static boolean isCollection(String UserGuid, String MsgGuid) {
        SQLiteDatabase db = FrmDBOpenHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT MsgGuid FROM Frame_Collection WHERE UserGuid = ? and MsgGuid = ?", new String[]{UserGuid, MsgGuid});
        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
}
