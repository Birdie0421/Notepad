package edu.fjnu.birdie.midnotepad.Utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public static final String CATEGORY_DELETED = "deleted";

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


    //设置头标签
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
                //imageView.setImageResource(R.drawable.ic_stat_list_gray);
                //imageView.set
                //Log.d("NormalsetImage","H");
                break;
            }
            case CATEGORY_IMPORTANT:{
                imageView.setTextAndColor(Head, COLOR_IMPORTANT);
                //imageView.setImageResource(R.drawable.ic_stat_priority_high_red);
                //Log.d("ImportentsetImage","H");
                break;
            }
            case CATEGORY_MEMO:{
                imageView.setTextAndColor(Head,COLOR_MEMO);
                //imageView.setImageResource(R.drawable.ic_stat_access_alarm_yellow);
                //Log.d("MEMOsetImage","H");
                break;
            }
            case CATEGORY_NOTE:{
                imageView.setTextAndColor(Head, COLOR_NOTE);
                //imageView.setImageResource(R.drawable.ic_stat_lock_outline_blue);
                //imageView.setImageResource(R.drawable.ic_stat_edit_cyan);
                //Log.d("NOTEsetImage","H");
                break;
            }
            case CATEGORY_SCHEDULE:{
                imageView.setTextAndColor(Head, COLOR_SCHEDULE);
                //imageView.setImageResource(R.drawable.ic_stat_storage_blue);
                //Log.d("SCHEDULEsetImage","H");
                break;
            }
            case CATEGORY_DELETED:{
                imageView.setTextAndColor(Head, AvatarImageView.COLORS[0]);
                //imageView.setImageResource(R.drawable.ic_stat_delete_brown);
                //Log.d("ImportentsetImage","H");
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

//
//    @Override
//    public View getView(int position, View convertView, final ViewGroup parent) {
//        mInflater = (LayoutInflater) contxet
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.ordercar_pager_item, null);
//            holder = new ViewHolder();
//            holder.tvDate = (TextView) convertView.findViewById(R.id.item_date);
//            holder.time1 = (TextView) convertView.findViewById(R.id.item_time1);
//            holder.time2 = (TextView) convertView.findViewById(R.id.item_time2);
//            holder.time3 = (TextView) convertView.findViewById(R.id.item_time3);
//
//            holder.num1 = (TextView) convertView.findViewById(R.id.item_num1);
//            holder.num2 = (TextView) convertView.findViewById(R.id.item_num2);
//            holder.num3 = (TextView) convertView.findViewById(R.id.item_num3);
//
//            holder.btn1 = (Button) convertView.findViewById(R.id.item_btn1);
//            holder.btn2 = (Button) convertView.findViewById(R.id.item_btn2);
//            holder.btn3 = (Button) convertView.findViewById(R.id.item_btn3);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//    }


    }
