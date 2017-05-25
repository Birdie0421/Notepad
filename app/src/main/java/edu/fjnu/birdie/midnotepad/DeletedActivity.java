package edu.fjnu.birdie.midnotepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.idescout.sql.SqlScoutServer;

import edu.fjnu.birdie.midnotepad.Utils.DatabaseManager;
import edu.fjnu.birdie.midnotepad.Utils.NoteCursorAdapter;
import edu.fjnu.birdie.midnotepad.Utils.NotePad;
import edu.fjnu.birdie.midnotepad.Utils.NotesDB;

public class DeletedActivity extends AppCompatActivity implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    private ListView deletedview;
    private NotesDB DB;
    private SQLiteDatabase dbread;
    //DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SqlScoutServer.create(this, getPackageName());

        //设置返回按钮:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        deletedview = (ListView) findViewById(R.id.deletedlist);


        DB = new NotesDB(this);
        //dbManager = new DatabaseManager(this);
        dbread = DB.getReadableDatabase();

        ShowDeletedList();

        deletedview.setOnItemClickListener(this);
        deletedview.setOnItemLongClickListener(this);
        deletedview.setOnScrollListener(this);


    }


    public void ShowDeletedList(){
        isDeletedNull();
        String  sql = "select * from note where category ='"+ NotePad.CATEGORY_DELETED+"'";
        Cursor cursor = dbread.rawQuery(sql, null);
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
        //SimpleCursor 最后一个参数 0
        NoteCursorAdapter adapter = new NoteCursorAdapter(this,
                R.layout.listview, cursor, new String[] {"title","content","date"},
                new int[] { R.id.tv_title,R.id.tv_content,R.id.tv_date });
        deletedview.setAdapter(adapter);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
        noteEdit.ENTER_STATE = 1;
        Log.d("Onclick_STATE",noteEdit.ENTER_STATE+"");

        Cursor content =  (Cursor) deletedview.getItemAtPosition(arg2);
        String content1 = content.getString(content.getColumnIndex("content"));
        Cursor title =   (Cursor) deletedview.getItemAtPosition(arg2);
        String title1 = title.getString( title.getColumnIndex("title"));
        String No = title.getString( title.getColumnIndex("_id"));
        Log.d("Content",content1);

        Log.d("TEXT",No);
        Intent myIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("info",content1);
        bundle.putString("info_title",title1);
        noteEdit.id = Integer.parseInt(No);
        myIntent.putExtras(bundle);
        myIntent.setClass(DeletedActivity.this,noteEdit.class);
        startActivityForResult(myIntent,1);

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
    public boolean onItemLongClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
        final int n = arg2;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.choose_delete_recovery,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        String action[]= getResources()
                                    .getStringArray(R.array.choose_value_delete_recovery);
                        switch(action[which]){

                            case "recovery":{
                                Cursor content = (Cursor) deletedview.getItemAtPosition(n);
                                String id = content.getString(content.getColumnIndex("_id"));
                                String recovery = "update note set category ='" + NotePad.CATEGORY_NORMAL + "' where _id=" + id;
                                dbread.execSQL(recovery);
                                Toast.makeText(DeletedActivity.this, "恢复 "+content.getString(content.getColumnIndex("title")), Toast.LENGTH_SHORT).show();
                                ShowDeletedList();
                                break;
                            }
                            case "drop":{
                                Cursor content = (Cursor) deletedview.getItemAtPosition(n);
                                String id = content.getString(content.getColumnIndex("_id"));
                                String recovery = "delete from note where _id=" + id;
                                dbread.execSQL(recovery);
                                Toast.makeText(DeletedActivity.this, "彻底删除"+content.getString(content.getColumnIndex("title")), Toast.LENGTH_SHORT).show();
                                ShowDeletedList();
                                break;
                            }
                        }
                    }
                });
        builder.create();
        builder.show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==2){
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_deleted, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
            case R.id.ab_delete_forever:{
                Toast.makeText(DeletedActivity.this, "清空回收站", Toast.LENGTH_SHORT).show();
                delete_forever();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //清空回收站操作
    public void delete_forever() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("清空回收站");
        builder.setMessage("确认清空回收站?");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            String setCategory = "delete from note where category ='" + NotePad.CATEGORY_DELETED + "'";
            dbread.execSQL(setCategory);
            ShowDeletedList();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
    }


    public boolean isDeletedNull(){
        String sql = "select * from note where category ='"+ NotePad.CATEGORY_DELETED+"'";
        Log.d("sql",sql);
        //Cursor c = dbManager.executeSql(sql, null);
        Cursor c = dbread.rawQuery(sql, null);
        int number = c.getCount();
        Log.d("Note number",number+"");
        if(number == 0){
            ListView listView = (ListView)findViewById(R.id.deletedlist);
            TextView textView = (TextView)findViewById(R.id.deleted_text);
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            return true;
        }else{
            ListView listView = (ListView)findViewById(R.id.deletedlist);
            TextView textView = (TextView)findViewById(R.id.deleted_text);
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            return false;
        }
    }



}
