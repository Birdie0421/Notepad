package edu.fjnu.birdie.midnotepad.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by edge0 on 2017/4/11.
 */

public class NotesDB extends SQLiteOpenHelper {

    private static NotesDB mInstance = null;

    public NotesDB(Context context){
        super(context,"note",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE " + NotePad.Notes.TABLE_NAME_NOTES + "("
                + NotePad.Notes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NotePad.Notes.COLUMN_NAME_NOTE_TITLE +" TEXT NOT NULL DEFAULT\"\","
                + NotePad.Notes.COLUMN_NAME_NOTE_CONTENT + " TEXT NOT NULL DEFAULT\"\","
                + NotePad.Notes.COLUMN_NAME_NOTE_CATEGORY + " TEXT NOT NULL DEFAULT\"normal\","
                + NotePad.Notes.COLUMN_NAME_NOTE_DATE + " TEXT NOT NULL DEFAULT\"\"" + ")";
        Log.d("SQL", sql);
        db.execSQL(sql);

        //初始化数据 ==提供帮助信息
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0,int arg1 ,int arg2){

    }

    public static synchronized NotesDB getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NotesDB(context);
        }
        return mInstance;
    }

}
