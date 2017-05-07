package edu.fjnu.birdie.midnotepad.Utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import edu.fjnu.birdie.midnotepad.R;

/**
 * Created by edge0 on 2017/4/24.
 */
public class NoteCursorAdapter extends SimpleCursorAdapter {

    public static final String CATEGORY_NORMAL = "normal";
    public static final String CATEGORY_IMPORTANT = "important";
    public static final String CATEGORY_MEMO = "memo";
    public static final String CATEGORY_NOTE = "note";
    public static final String CATEGORY_SCHEDULE = "schedule";

    public static final int COLOR_NORMAL = 0xff90a4ae;
    public static final int COLOR_IMPORTANT = 0xffc62828;
    public static final int COLOR_MEMO = 0xfffbc02d;
    public static final int COLOR_NOTE = 0xff26c6da;
    public static final int COLOR_SCHEDULE = 0xff5c6bc0;


    private Cursor _cursor;
    private Context _context;
    public NoteCursorAdapter(Context context, int layout, Cursor c,
                                 String[] from, int[] to) {
        super(context, layout, c, from, to);
        _cursor = c;
        _context = context;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        AvatarImageView imageView = (AvatarImageView) view.findViewById(R.id.img_group);
        //imageView.setTextAndColorSeed("H","hello");
        String Head = cursor.getString(cursor.getColumnIndex("content")).substring(0,1);
        String Category = cursor.getString(cursor.getColumnIndex("category"));
//        imageView.setTextAndColor(Head, AvatarImageView.COLORS[1]);
//        Log.d("setImage","H");

        switch(Category){
            case CATEGORY_NORMAL:{
                imageView.setTextAndColor(Head, COLOR_NORMAL);
                //Log.d("NormalsetImage","H");
                break;
            }
            case CATEGORY_IMPORTANT:{
                imageView.setTextAndColor(Head, COLOR_IMPORTANT);
                //Log.d("ImportentsetImage","H");
                break;
            }
            case CATEGORY_MEMO:{
                imageView.setTextAndColor(Head,COLOR_MEMO);
                //Log.d("MEMOsetImage","H");
                break;
            }
            case CATEGORY_NOTE:{
                imageView.setTextAndColor(Head, COLOR_NOTE);
                //Log.d("NOTEsetImage","H");
                break;
            }
            case CATEGORY_SCHEDULE:{
                imageView.setTextAndColor(Head, COLOR_SCHEDULE);
                //Log.d("SCHEDULEsetImage","H");
                break;
            }
            default:{
                imageView.setTextAndColor(Head, AvatarImageView.COLORS[0]);
                //Log.d("defaultsetImage","H");
                break;
            }
        }

        super.bindView(view, context, cursor);
    }



}
