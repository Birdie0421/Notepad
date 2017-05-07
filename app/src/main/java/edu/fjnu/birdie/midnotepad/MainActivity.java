package edu.fjnu.birdie.midnotepad;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fjnu.birdie.midnotepad.Utils.DatabaseManager;
import edu.fjnu.birdie.midnotepad.Utils.NoteCursorAdapter;
import edu.fjnu.birdie.midnotepad.Utils.NotesDB;

public class MainActivity extends AppCompatActivity implements OnScrollListener,
        OnItemClickListener , OnItemLongClickListener {

    public static final String CATEGORY_DELETED = "deleted";
    public static final String CATEGORY_NORMAL = "normal";
    public static final String CATEGORY_IMPORTANT = "important";
    public static final String CATEGORY_MEMO = "memo";
    public static final String CATEGORY_NOTE = "note";
    public static final String CATEGORY_SCHEDULE = "schedule";


    private String[] category = new String[] { "默认", "重要", "备忘", "笔记", "日程" };

    private Context mContext;
    private ListView listview;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> datalist;
    FloatingActionButton  addNote;
    private TextView tv_content;
    private NotesDB DB;
    private SQLiteDatabase dbread;
    public SearchView searchView;
    DatabaseManager dbManager;
    public String setCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_content=(TextView)findViewById(R.id.tv_content);
        listview = (ListView)findViewById(R.id.notelist);
        datalist = new ArrayList<Map<String,Object>>();
        addNote = (FloatingActionButton) findViewById(R.id.btn_add_note);
        mContext = this;

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                noteEdit.ENTER_STATE = 0;
                Intent intent = new Intent(mContext ,noteEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info","");
                bundle.putString("info_title","");
                intent.putExtras(bundle);
                startActivityForResult(intent ,1);
            }
        });

        DB = new NotesDB(this);
        dbManager = new DatabaseManager(this);
        dbread = DB.getReadableDatabase();
        RefreshNotesList();

        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);

    }

    //刷新页面
    public void RefreshNotesList(){
        int size = datalist.size();
        if(size>0){
            datalist.removeAll(datalist);
            simpleAdapter.notifyDataSetChanged();
            listview.setAdapter(simpleAdapter);
        }
        String sql = "select * from note where category !='"+CATEGORY_DELETED+"'";
        Cursor cursor = dbManager.executeSql(sql, null);
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
        //        R.layout.listview, cursor, new String[] {"title","content","date"},
        //       new int[] { R.id.tv_title,R.id.tv_content,R.id.tv_date }, 0);
        NoteCursorAdapter adapter = new NoteCursorAdapter(this,
                R.layout.listview, cursor, new String[] {"title","content","date"},
                new int[] { R.id.tv_title,R.id.tv_content,R.id.tv_date });
        listview.setAdapter(adapter);


    }

    //从数据库里读数据
    private  List<Map<String,Object>> getData(){
        Cursor cursor = dbread.query("note",null,"content!=\"\"",null,null,null,null);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("tv_title",title);
            map.put("tv_content",name);
            map.put("tv_date",date);
            datalist.add(map);
        }
        cursor.close();
        return datalist;
    }




    @Override
    public void onScroll(AbsListView arg0,int arg1,int arg2,int arg3){

    }

    @Override
    public void onScrollStateChanged(AbsListView arg0,int arg1){
        switch (arg1){
            case SCROLL_STATE_FLING:
                Log.i("main", "用户在手指离开屏幕之前，由于用力的滑了一下，视图能依靠惯性继续滑动");
            case SCROLL_STATE_IDLE:
                Log.i("main", "视图已经停止滑动");
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i("main", "手指没有离开屏幕，试图正在滑动");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
        noteEdit.ENTER_STATE = 1;
        Log.d("Onclick_STATE",noteEdit.ENTER_STATE + "");
        Log.d("position",arg2+"");


        Cursor content =  (Cursor) listview.getItemAtPosition(arg2);
        String content1 = content.getString(content.getColumnIndex("content"));
        Cursor title =   (Cursor) listview.getItemAtPosition(arg2);
        String title1 = title.getString( title.getColumnIndex("title"));
        Cursor id = (Cursor) listview.getItemAtPosition(arg2);
        String No = id.getString( title.getColumnIndex("_id"));

        Log.d("Content",content1);
        Log.d("Title",title1);
        Log.d("_Id",No);
        //使用这个操作会删除内容相同的不同条目
       // Cursor c = dbread.query("note",null,"content="+"'"+content1+"'",null,null,null,null);
      //  while (c.moveToNext()){
      //      String No = c.getString(c.getColumnIndex("_id"));
            Log.d("TEXT",No);
            Intent myIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("info",content1);
            bundle.putString("info_title",title1);
            noteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            myIntent.setClass(MainActivity.this,noteEdit.class);
            startActivityForResult(myIntent,1);

      //  }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==2){
            RefreshNotesList();
        }
    }

    //长按删除
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
        final int n = arg2;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除");
        builder.setMessage("确认删除?");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Cursor content =  (Cursor) listview.getItemAtPosition(n);
                String content1 = content.getString(content.getColumnIndex("content"));
                String id = content.getString(content.getColumnIndex("_id"));

                //String content = listview.getItemAtPosition(n)+"";
               // String content1= content.substring(content.indexOf("=")+1,content.indexOf(","));
           //     Cursor c = dbread.query("note",null,"content="+"'"+content1+"'",null,null,null,null);
           //     while(c.moveToNext()){
           //         String id = c.getString(c.getColumnIndex("_id"));
                    String setCategory = "update note set category ='" + CATEGORY_DELETED + "' where _id=" + id;
                  //  String sql_del = "update note set content='' where _id = "+id;
                    Log.d("DELETE",setCategory);
                    dbread.execSQL(setCategory);
                    RefreshNotesList();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        String SearchContent = getIntent().getStringExtra(SearchManager.QUERY);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.d("toSearch",query);
//                searchData(query);
                Log.d("Searching",query);
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("word",query);
                Log.d("Searching","intent");
                startActivity(intent);
                Log.d("Searching","intentstart");
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_search: {
                Log.d("Search","start");
                break;
            }
            case R.id.action_settings:{
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
            case R.id.action_backup:{

            }
            case R.id.action_aboutus:{

            }
        }

        return super.onOptionsItemSelected(item);
    }



//    //设置分组
//    public void addCategory(){
//        //Toast.makeText(this,"add_catagory",Toast.LENGTH_SHORT).show();
//        //{ "默认", "重要", "备忘", "笔记", "日程" };
//
//            Cursor content =  (Cursor) listview.getItemAtPosition(n);
//            String content1 = content.getString(content.getColumnIndex("content"));
//            String id = content.getString(content.getColumnIndex("_id"));
//
//            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
//            builder.setTitle("设置分组");
//            builder.setSingleChoiceItems(category, 0, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    int choose = which;
//                    switch (which) {
//                        case 0: {
//                            setCategory = "update note set category ='" + CATEGORY_NORMAL + "' where _id=" + id;
//                            Log.d("EXE", setCategory);
//                            break;
//                        }
//                        case 1: {
//                            setCategory = "update note set category ='" + CATEGORY_IMPORTANT + "' where _id=" + id;
//                            Log.d("EXE", setCategory);
//                            break;
//                        }
//                        case 2: {
//                            setCategory = "update note set category ='" + CATEGORY_MEMO + "' where _id=" + id;
//                            Log.d("EXE", setCategory);
//                            break;
//                        }
//                        case 3: {
//                            setCategory = "update note set category ='" + CATEGORY_NOTE + "' where _id=" + id;
//                            Log.d("EXE", setCategory);
//                            break;
//                        }
//                        case 4: {
//                            setCategory = "update note set category ='" + CATEGORY_SCHEDULE + "' where _id=" + id;
//                            Log.d("EXE", setCategory);
//                            break;
//                        }
//                    }
//                }
//            });
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dbread.execSQL(setCategory);
//                }
//            });
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            builder.create();
//            builder.show();
//    }


}
