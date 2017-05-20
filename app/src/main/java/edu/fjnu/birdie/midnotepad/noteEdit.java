package edu.fjnu.birdie.midnotepad;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.fjnu.birdie.midnotepad.Utils.NotesDB;

public class noteEdit extends AppCompatActivity {

    public static final String TABLE_NAME_NOTES = "note";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_NOTE_TITLE = "title";
    public static final String COLUMN_NAME_NOTE_CONTENT = "content";
    public static final String COLUMN_NAME_NOTE_DATE = "date";

    public static final String CATEGORY_NORMAL = "normal";
    public static final String CATEGORY_IMPORTANT = "important";
    public static final String CATEGORY_MEMO = "memo";
    public static final String CATEGORY_NOTE = "note";
    public static final String CATEGORY_SCHEDULE = "schedule";
    public static final String CATEGORY_DELETED = "deleted";


    private String[] category = new String[] { "默认", "重要", "备忘", "笔记", "日程" };

    //private TextView tv_edit_date;
    //private Button btn_save1;
    //private Button btn_cancel;

    private EditText et_content;
    private EditText et_title;
    private FloatingActionButton btn_addImage;
    private NotesDB DB;
    private SQLiteDatabase dbread;
    private Bitmap bitmap;
    public static int ENTER_STATE = 0;
    public static int CHANGE_STATE = 0;
    public static String last_content;
    public static String last_title;
    public static int id;
    public String setCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置返回按钮:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //4.4标题栏显示时间 //这个版本打算只兼容5.0以上 所以暂时不考虑4.4
        //tv_edit_date = (TextView)findViewById(R.id.tv_edit_date);
        //Date date = new Date();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        //String dateString = sdf.format(date);
        //tv_edit_date.setText(dateString);

        et_content = (EditText)findViewById(R.id.et_content);
        et_title   = (EditText)findViewById(R.id.et_title);

        //使焦点默认在编辑内容上,点击标题栏才能编辑标题
        et_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_title.setFocusableInTouchMode(true);
                return false;
            }
        });

        //新建文本时调用软键盘,如果是打开原来存在的文本默认不打开软键盘
        //在Manifest中添加android:windowSoftInputMode="stateHidden"使得虚拟键盘不会自动弹出
        if(ENTER_STATE == 0){
            Log.d("KeyBoard","VISIBLE");
            Log.d("ENTER_STATE",ENTER_STATE+"");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        DB = new NotesDB(this);
        dbread = DB.getReadableDatabase();

        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        last_title = myBundle.getString("info_title");

        Log.d("LAST_CONTENT",last_content);
        Log.d("LAST_TITLE",last_title);

        //将内容中的图片加载出来
        SpannableString ss = new SpannableString(last_content);
        Pattern p= Pattern.compile("<img>.*<img>");
        Matcher m=p.matcher(last_content);
        while(m.find()){
            String image=m.group();
            String path=image.substring(5,image.length()-5);
            Bitmap bm = BitmapFactory.decodeFile(path);
                Log.d("path",path);
            if(bm==null)
            {
                Log.d("bm","null");
            }
            Bitmap rbm = resizeImage(bm);
            ImageSpan span = new ImageSpan(this, rbm);
            ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        et_content.setText(ss);

        et_title.setText(last_title);
        //et_content.setText(last_content);
        //内容编辑栏的光标处于文字之后
        et_content.setSelection(et_content.getText().toString().length());






                btn_addImage=(FloatingActionButton)findViewById(R.id.btn_add_image);
        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage  = new Intent("android.intent.action.GET_CONTENT");
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("image/*");
                startActivityForResult(getImage, 1);
                Log.d("ImageState",1+"");

            }
        });

//        //兼容4.4实现保存和取消
//        btn_save1 = (Button) findViewById(R.id.btn_save_old);
//        btn_save1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               saveData();
//            }
//        });
//        btn_cancel = (Button) findViewById(R.id.btn_cancel);
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //5.0以上使用toolbar来进行保存和取消
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public void saveData(){
        String content = et_content.getText().toString();
        String title = et_title.getText().toString();
        Log.d("Type",content);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateNum = sdf.format(date);
        String sql;
        String sql_count = "SELECT COUNT(*) FROM note";
        SQLiteStatement statement = dbread.compileStatement(sql_count);
        long count = statement.simpleQueryForLong();
        Log.d("COUNT",count+"");
        Log.d("ENTER_STATE",ENTER_STATE+"");



        //新建记事
        if(ENTER_STATE == 0){
            if(!content.equals("")){
                //如果标题为空,自动添加标题
                if(title.equals("")){
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd-HH-mm");
                    String dateNum1 = sdf.format(date);
                    //title = "新建记事"+ dateNum1;
                     title = "新建记事";
                    //title = "新建记事";
                }
                //insert into note values("count,'title','content','dataNum')
//                sql = "insert into " +NotesDB.TABLE_NAME_NOTES
//                        +" values("+count+","+"'"+ title +"'"+","+"'"+ content +"'"+","+"'"+ dateNum + "')";
                //insert into note(_id,title,content,date) values("count,'title','content','dataNum')
                //使用下述的方法不能输入 ' 这个符号  要使用Insert();
                sql = "insert into " +NotesDB.TABLE_NAME_NOTES +"(" + COLUMN_NAME_ID + " ,"
                        + COLUMN_NAME_NOTE_TITLE +","
                        + COLUMN_NAME_NOTE_CONTENT + " ,"
                        + COLUMN_NAME_NOTE_DATE + ")"
                        +" values("+count+","+"'"+ title +"'"+","+"'"+ content +"'"+","+"'"+ dateNum + "')";
                Log.d("LOG",sql);
                //dbread.execSQL(sql);
                //insert方法插入
                ContentValues values  = new ContentValues();
                //values.put(COLUMN_NAME_ID ,count);
                values.put(COLUMN_NAME_NOTE_TITLE ,title);
                values.put(COLUMN_NAME_NOTE_CONTENT ,content);
                values.put(COLUMN_NAME_NOTE_DATE ,dateNum);
                dbread.insert(TABLE_NAME_NOTES ,null,values);
                Log.d("LOG",sql);
            }else {
                Toast.makeText(this,"内容为空,笔记未保存",Toast.LENGTH_SHORT).show();
            }
        }else{//修改记事
            //使用这个方法不能输入 '
            Log.d("EXE","executed");
            String updatesql = "update note set content ='"+ content +"' where _id=" + id;
            String updatesqltitle = "update note set title ='"+ title +"' where _id=" + id;
            Log.d("EXE",updatesql);
            //dbread.execSQL(updatesql);
            //dbread.execSQL(updatesqltitle);
            //使用update方法
            ContentValues values  = new ContentValues();;
            values.put(COLUMN_NAME_NOTE_TITLE ,title);
            values.put(COLUMN_NAME_NOTE_CONTENT ,content);
            values.put(COLUMN_NAME_NOTE_DATE,dateNum);
            String where = "_id="+id;
            dbread.update(TABLE_NAME_NOTES ,values ,where, null);
        }
        Intent data = new Intent();
        setResult(2,data);
        finish();
    }

    //设置分组
    public void addCategory(){
        //Toast.makeText(this,"add_catagory",Toast.LENGTH_SHORT).show();
        //{ "默认", "重要", "备忘", "笔记", "日程" };
        if(ENTER_STATE == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("设置分组");
            builder.setSingleChoiceItems(category, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int choose = which;
                    switch (which) {
                        case 0: {
                            setCategory = "update note set category ='" + CATEGORY_NORMAL + "' where _id=" + id;
                            Log.d("EXE", setCategory);
                            break;
                        }
                        case 1: {
                            setCategory = "update note set category ='" + CATEGORY_IMPORTANT + "' where _id=" + id;
                            Log.d("EXE", setCategory);
                            break;
                        }
                        case 2: {
                            setCategory = "update note set category ='" + CATEGORY_MEMO + "' where _id=" + id;
                            Log.d("EXE", setCategory);
                            break;
                        }
                        case 3: {
                            setCategory = "update note set category ='" + CATEGORY_NOTE + "' where _id=" + id;
                            Log.d("EXE", setCategory);
                            break;
                        }
                        case 4: {
                            setCategory = "update note set category ='" + CATEGORY_SCHEDULE + "' where _id=" + id;
                            Log.d("EXE", setCategory);
                            break;
                        }
                    }
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbread.execSQL(setCategory);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create();
            builder.show();
        }else{
            Toast.makeText(this,"请先保存笔记",Toast.LENGTH_SHORT).show();
        }
    }
    //确认退出
    public void confirmCancel(){
        //如果标题和内容都未做改动,则直接退出,
        //如果标题或内容做了改动,则提示为保存是否退出
        if (et_content.getText().toString().equals(last_content)
                && et_title.getText().toString().equals(last_title)){
            CHANGE_STATE = 0;
        }else {
            CHANGE_STATE = 1;
        }
        if (CHANGE_STATE == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("确认退出?");
            builder.setMessage("内容未保存,确认退出?");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create();
            builder.show();
        }else{
            finish();
        }
    }

    //重写返回键,使得返回键也弹出确认保存信息
    @Override
    public void onBackPressed() {
        confirmCancel();
        //super.onBackPressed();
    }


    //为保存和取消设置监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.ab_save : {
                saveData();
                break;
            }
            case android.R.id.home:{
                //finish();
                //加入确认退出提示
                confirmCancel();
                break;
            }
            case R.id.ab_add_category:{
                addCategory();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Uri originalUri = intent.getData();
                //String Imgpath = getRealFilePath(this,originalUri);//得到图片真实路径
                String Imgpath = getPath(this,originalUri);
                Uri realUri = Uri.parse("file://"+Imgpath);//真实路径转化为Uri
                String realPath = "content://media/"+Imgpath;

                Log.d("originalUri",originalUri.toString() );
                Log.d("imgpath",Imgpath);
                Log.d("realUri",realUri.toString());
                Log.d("realPath",realPath);

                try {
                    //Bitmap originalBitmap = BitmapFactory.decodeStream(resolver
                    //        .openInputStream(realUri));
                    Bitmap originalBitmap = BitmapFactory.decodeFile(Imgpath);

                    Log.d("imageUri",originalUri.toString() );
                    if(originalBitmap != null) {
                        bitmap = resizeImage(originalBitmap);
                    }else{
                        Log.d("ob","null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    insertIntoEditText(getBitmapMime(bitmap, realUri));
                } else {
                    Toast.makeText(noteEdit.this, "获取图片失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (bitmap != null) {
        }
    }

    //===============================图片设置部分=====================================

    //获得图片真实地址
    //http://blog.csdn.net/huangyanan1989/article/details/17263203
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    //下面的方法对com.android.providers.media.documents 不能支持
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //设置Spannable String
    private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
        String path = "<img>"+uri.getPath()+"<img>";
        SpannableString ss = new SpannableString(path);
        ImageSpan span = new ImageSpan(this, pic);
        ss.setSpan(span, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    //将SS插入EditText
    private void insertIntoEditText(SpannableString ss) {
        insertEnter();//图片添加到新的一行
        Editable et = et_content.getText();// 先获取Edittext中的内容
        int start = et_content.getSelectionStart();
        et.insert(start, ss);// 设置ss要添加的位置
        et_content.setText(et);// 把et添加到Edittext中
        et_content.setSelection(start + ss.length());// 设置Edittext中光标在最后
        Log.d("Text",et_content.getText().toString());
        insertEnter();//添加完图片后换行
    }
    //排版问题,插入EditText时在图片的上下一行加入空格;
    private  void insertEnter(){
        Editable et = et_content.getText();
        int start = et_content.getSelectionStart();
        String enter = "\n";
        et.insert(start,enter);
        et_content.setText(et);
        et_content.setSelection(start + enter.length());
    }
    //压缩图片
    public Bitmap resizeImage(Bitmap bitmap)
    {
        if(bitmap != null) {

            Bitmap BitmapOrg = bitmap;
            int width = BitmapOrg.getWidth();
            int height = BitmapOrg.getHeight();
            int newWidth = 480;
            int newHeight = 800;
            float scale =  ((float) newWidth) / width;
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            //matrix.postScale(scaleWidth, scaleHeight);
            matrix.postScale(scale, scale);//比例不变
            // if you want to rotate the Bitmap
            // matrix.postRotate(45);
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;

        }else{
            return null;
        }
    }




}

