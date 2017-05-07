package edu.fjnu.birdie.midnotepad.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseManager{

    NotesDB mDbHelper = null;
    //操作数据库的实例
    static SQLiteDatabase mDb = null;
    Context mContext = null;

    public DatabaseManager(Context context) {
        mContext = context;
        mDbHelper = NotesDB.getInstance(mContext);
        mDb= mDbHelper.getReadableDatabase();
    }

    /**
     * 建立表
     * SQLite内部只支持NULL,INTEGER,REAL(浮点),TEXT(文本),BLOB(大二进制)5种数据类型
     * 建立表成功了，返回true
     */
    public boolean executeSql(String sql){
        try {
            mDb.execSQL(sql);
            return true;
        }
        catch(SQLiteException e){
            return false;
        }
    }

    public Cursor executeSql(String sql,String[]args){
        try {
            return mDb.rawQuery(sql, args);
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接
     */
    public void closeDBhelper() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

}