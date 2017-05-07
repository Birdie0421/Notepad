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
    public static final String TABLE_NAME_NOTES = "note";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_NOTE_TITLE = "title";
    public static final String COLUMN_NAME_NOTE_CONTENT = "content";
    public static final String COLUMN_NAME_NOTE_CATEGORY= "category";
    public static final String COLUMN_NAME_NOTE_DATE = "date";

    public static final String COLUMN_NAME_NOTE_CATEGORY_NORMAL= "normal";


    public NotesDB(Context context){
        super(context,"note",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_NAME_NOTES + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME_NOTE_TITLE +" TEXT NOT NULL DEFAULT\"\","
                + COLUMN_NAME_NOTE_CONTENT + " TEXT NOT NULL DEFAULT\"\","
                + COLUMN_NAME_NOTE_CATEGORY + " TEXT NOT NULL DEFAULT\"normal\","
                + COLUMN_NAME_NOTE_DATE + " TEXT NOT NULL DEFAULT\"\"" + ")";
        Log.d("SQL", sql);
        db.execSQL(sql);
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
