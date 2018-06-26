package com.sinoyd.frame.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sinoyd.frame.util.AppUtil;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：sinyd框架数据库结构创建（不允许修改）
 */
public class FrmDBOpenHelper extends SQLiteOpenHelper {
    public static final int VERSION = 2;
    public static final String DBNAME = "sinoydframe";
    private static FrmDBOpenHelper instance;

    public static FrmDBOpenHelper getInstance() {
        if(instance == null) {
            instance = new FrmDBOpenHelper(AppUtil.getApplicationContext());
        }

        return instance;
    }

    private FrmDBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.i("======数据库创建======", "onCreate");
        db.execSQL("CREATE TABLE IF NOT EXISTS Frame_Config (Key TEXT NOT NULL PRIMARY KEY,Value TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Frame_SearchRecord (RowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,KeyWord TEXT,UserGuid TEXT,OperationDate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Frame_Resc (Key TEXT NOT NULL PRIMARY KEY,Value BLOB)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Frame_Operation (RowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,OperationName TEXT,OperatorId TEXT,OperateDate TEXT,APPVersion TEXT,SystemVersion TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Frame_AppInfo (ModuleId TEXT NOT NULL PRIMARY KEY,ModuleName TEXT,Type TEXT,ClassName TEXT,PackageName TEXT,ImageUrl TEXT,WebUrl TEXT,AppUrl TEXT,Params TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Frame_AppInfo_Manage(RowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,ModuleId TEXT NOT NULL,Recommend INTEGER,UserRecommend INTEGER,OperatorId Text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Frame_Log (RowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,LogInfo TEXT,LogDate TEXT,LogType TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Frame_DownLoad (RowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,DownLoadId Long,Name Text,LocalPath Text,Type Text,FileSize Text,Status INTEGER,Url Text,FileDir Text,FileType Text,PercentStr Text,UserGuid Text,Mark Text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Frame_Collection(RowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, UserGuid TEXT NOT NULL, MsgGuid TEXT, Title TEXT, DateTime TEXT,Publisher TEXT, Type TEXT ,URL TEXT, Remark TEXT, Flag TEXT , CollectionTime TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2) {
            this.onCreate(db);
        }

    }
}
