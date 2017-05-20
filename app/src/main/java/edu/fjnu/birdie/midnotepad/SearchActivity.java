package edu.fjnu.birdie.midnotepad;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import edu.fjnu.birdie.midnotepad.Utils.DatabaseManager;
import edu.fjnu.birdie.midnotepad.Utils.NoteCursorAdapter;
import edu.fjnu.birdie.midnotepad.Utils.NotesDB;

public class SearchActivity extends AppCompatActivity implements OnScrollListener,
                         OnItemClickListener , OnItemLongClickListener {

    public static final String CATEGORY_DELETED = "deleted";

    private ListView searchview;
    private NotesDB DB;
    private SQLiteDatabase dbread;
    DatabaseManager dbManager;

    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle myBundle = this.getIntent().getExtras();
        word = myBundle.getString("word");
        Log.d("SearchWord",word);

        //设置返回按钮:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchview = (ListView) findViewById(R.id.searchlist);


        DB = new NotesDB(this);
        dbManager = new DatabaseManager(this);
        dbread = DB.getReadableDatabase();

        ShowSearchList();

        searchview.setOnItemClickListener(this);
        searchview.setOnItemLongClickListener(this);
        searchview.setOnScrollListener(this);


    }

    //执行搜索
    /**
     *select * from note where context like %searcherFilter% ;
     select * from note where content like '%e%'
     * Cursor cursor = db.query(note , null, "context like ？", new String[]{"%"+searcherFilter+"%"}, null, null, null);
     */
    public void ShowSearchList(){
        isSearchNull();
        String  sql = "select * from note where category !='"+CATEGORY_DELETED+"' and content like ?";
        Cursor cursor = dbread.rawQuery(sql, new String[]{"%"+word+"%"});
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
        //SimpleCursor 最后一个参数 0
        NoteCursorAdapter adapter = new NoteCursorAdapter(this,
                R.layout.listview, cursor, new String[] {"title","content","date"},
                new int[] { R.id.tv_title,R.id.tv_content,R.id.tv_date });
        searchview.setAdapter(adapter);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
        noteEdit.ENTER_STATE = 1;
        Log.d("Onclick_STATE",noteEdit.ENTER_STATE+"");

        Cursor content =  (Cursor) searchview.getItemAtPosition(arg2);
        String content1 = content.getString(content.getColumnIndex("content"));
        Cursor title =   (Cursor) searchview.getItemAtPosition(arg2);
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
        myIntent.setClass(SearchActivity.this,noteEdit.class);
        startActivityForResult(myIntent,1);
    }

    @Override
    public void onScroll(AbsListView arg0,int arg1,int arg2,int arg3){

    }

    @Override
    public void onScrollStateChanged(AbsListView arg0,int arg1){
//        switch (arg1){
//            case SCROLL_STATE_FLING:
//                Log.i("main", "用户在手指离开屏幕之前，由于用力的滑了一下，视图能依靠惯性继续滑动");
//            case SCROLL_STATE_IDLE:
//                Log.i("main", "视图已经停止滑动");
//            case SCROLL_STATE_TOUCH_SCROLL:
//                Log.i("main", "手指没有离开屏幕，试图正在滑动");
//        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
        Toast.makeText(this,"删除请在首页列表中进行",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==2){
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean isSearchNull(){
        String sql = "select * from note where category !='"+CATEGORY_DELETED+"' and content like ?";
        Cursor c = dbread.rawQuery(sql, new String[]{"%"+word+"%"});
        Log.d("sql",sql);
        int number = c.getCount();
        Log.d("Note number",number+"");
        if(number == 0){
            ListView listView = (ListView)findViewById(R.id.searchlist);
            TextView textView = (TextView)findViewById(R.id.search_text);
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            return true;
        }else{
            ListView listView = (ListView)findViewById(R.id.searchlist);
            TextView textView = (TextView)findViewById(R.id.search_text);
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            return false;
        }
    }


}
