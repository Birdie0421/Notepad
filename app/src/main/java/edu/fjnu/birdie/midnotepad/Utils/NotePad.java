package edu.fjnu.birdie.midnotepad.Utils;

import android.provider.BaseColumns;

/**
 * Created by edge0 on 2017/5/16.
 */
public final class NotePad {

    //分类
    public static final String CATEGORY_DELETED = "deleted";
    public static final String CATEGORY_NORMAL = "normal";
    public static final String CATEGORY_IMPORTANT = "important";
    public static final String CATEGORY_MEMO = "memo";
    public static final String CATEGORY_NOTE = "note";
    public static final String CATEGORY_SCHEDULE = "schedule";

    //public static final String EDIT_BACKGROUND_COLOR =

    private NotePad() {
    }

    public static final class Notes implements BaseColumns {

        public static final String TABLE_NAME_NOTES = "note";

        public static final String COLUMN_NAME_NOTE_TITLE = "title";
        public static final String COLUMN_NAME_NOTE_CONTENT = "content";
        public static final String COLUMN_NAME_NOTE_CATEGORY= "category";
        public static final String COLUMN_NAME_NOTE_DATE = "date";

    }




}
