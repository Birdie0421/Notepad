# Notepad

![图标.jpg](http://upload-images.jianshu.io/upload_images/6080696-d491610f8067dddf.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### 简述:
---
记事本的第一个版本,需要完善的还有很多,完成了基本的增删改查的功能,以及在正文中插入图片,对内容进行分类,根据内容查找等基本功能,对于删除添加了回收站机制,可在回收站中对已删除的内容进行恢复或者永久删除
暂时只支持**Android5.0**以上的设备运行,后续版本将会对4.4版本进行兼容,对4.4以下的设备未做兼容打算

![关于.jpg](http://upload-images.jianshu.io/upload_images/6080696-b7ac809f3c03020e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 主界面

![主界面.jpg](http://upload-images.jianshu.io/upload_images/6080696-f87c1c72124bf7db.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 编辑界面


![编辑界面.jpg](http://upload-images.jianshu.io/upload_images/6080696-e90fbf325c64cee0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---
### 功能点:

#### 基础功能
- 对记事的增删改
- 添加时间戳
- 查询内容

#### 拓展功能

- 对记事进行分类
- 在记事中添加图片
- 一些界面美化以及人性化细节设置


---

### 基础功能

##### 对记事的增删改
使用了数据库辅助类SQLiteOpenHelper来创建数据库
 ###### 数据的插入
使用dbHelper封装insert方法


插入数据         

```
ContentValues values  = new ContentValues();
values.put(COLUMN_NAME_NOTE_TITLE ,title);
values.put(COLUMN_NAME_NOTE_CONTENT ,content);
values.put(COLUMN_NAME_NOTE_DATE ,dateNum);
dbread.insert(TABLE_NAME_NOTES ,null,values);
```



原先使用的是execSQL()方法来插入数据,但是有一个问题就是输入 **'** 这个符号使,执行语句就会出错,

 原先的插入方法

    sql = "insert into " +NotesDB.TABLE_NAME_NOTES +"(" 
                +COLUMN_NAME_ID + " ,"
                +COLUMN_NAME_NOTE_TITLE +","
                +COLUMN_NAME_NOTE_CONTENT + " ,"
                +COLUMN_NAME_NOTE_DATE + ")"
                +" values("+count+","+"'"+ title +"'"+","+"'"+ content +"'"+","+"'"+ dateNum + "')";
    Log.d("LOG",sql);
    dbread.execSQL(sql);

###### 数据的修改
使用dbHelper封装update方法

```
ContentValues values  = new ContentValues();; 
values.put(COLUMN_NAME_NOTE_TITLE ,title);    
values.put(COLUMN_NAME_NOTE_CONTENT ,content);  
values.put(COLUMN_NAME_NOTE_DATE,dateNum);
String where = "_id="+id;
dbread.update(TABLE_NAME_NOTES ,values ,where, null);
```

同上,使用数据库语句的execSQL()方法会因为输入 ' 而出错,此处不再列出

###### 数据的删除
本应用的删除分两步进行,第一步只是先把记事的属性改为已删除,并在回收站显示,第二部才是进行在数据库的删除

第一步
更改属性为删除


```
    Cursor content = c1;
    String id = c1.getString(content.getColumnIndex("_id"));
    String setCategory = "update note set category ='" +   CATEGORY_DELETED + "' where _id=" + id;
    Log.d("DELETE",setCategory);
    dbread.execSQL(setCategory);
```
第二步
在数据库中删除
```
    Cursor content = (Cursor) deletedview.getItemAtPosition(n);
    String id =content.getString(content.getColumnIndex("_id"));
    String recovery = "delete from note where _id=" + id;
    dbread.execSQL(recovery);
```
至此,本应用的核心功能增删改已经完成

下面是彻底删除一条记事的流程:

主界面长按选择删除


![主界面长按.jpg](http://upload-images.jianshu.io/upload_images/6080696-b275175e86616aa2.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


在弹出的提示中确认删除,此时,主界面列表已经不显示这条记录


![删除提示.jpg](http://upload-images.jianshu.io/upload_images/6080696-1f56fe4f1a239d2a.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


而在回收站中可以看到先前被删除的记录,长按则可以选择回复或者彻底删除


![回收站长按.jpg](http://upload-images.jianshu.io/upload_images/6080696-b6d4a18509d966ba.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


选择删除则弹出提示,这条记录"彻底删除"已在数据库中删除


![永久删除.jpg](http://upload-images.jianshu.io/upload_images/6080696-255dc0b5d31acead.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


逻辑的判断
进入编辑界面有两个途径,第一是点击新建按钮,第二是从记事列表进入,所以我们加入了一个属性,若是新建按钮则为0(调用插入函数),若是从记事列表则为1(调用修改函数).

```
if(ENTER_STATE == 0){
                noteInsert();
        else{
                noteUpdate();
            }
        }
```

---
##### 添加时间戳
首先要在显示的listview中加入一个TextView组件来显示这个时间,
使用SimpleDateFormat 可以把当前时间格式化成指定格式

```
Date date = new Date();

SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

String dateNum = sdf.format(date);
```


然后在数据库相对应的列中加入这个字符串即可

---
##### 查询内容
查询的方法在工具栏点击查询按钮即可调出searchview,然后输入所需查询的文字然后点击键盘的搜索即可查询内容包含关键词的记录

![查询1.jpg](http://upload-images.jianshu.io/upload_images/6080696-9d0ab6adb5de4b72.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

查询"在"的结果

![查询结果.jpg](http://upload-images.jianshu.io/upload_images/6080696-dd62753ca0db7f8f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

实现

具体搜索的实现代码不难,执行一下代码就可以将查询到的内容放到Cursor里,然后用适配器进行适配就可以在列表中显示了

```
 String  sql = "select * from note where category !='"+CATEGORY_DELETED+"' and content like ?";

Cursor cursor = dbread.rawQuery(sql, new String[]{"%"+word+"%"});
```



但是为了实现搜索栏,我们需要用到一个新的组件 *SearchView*

首先要在AndroidManifest的显示搜索栏的活动中加入
```
        <meta-data
            android:name="android.app.searchable"
            android:resource="@xml/searchable" />
        <meta-data
            android:name="android.app.default_searchable"
            android:value="edu.fjnu.birdie.notemd.MainActivity"/>	
```
并在显示搜索结果的活动中加入
```
     <intent-filter>           
     <action android:name="android.intent.action.SEARCH" />
     <category android:name="android.intent.category.DEFAULT" />
     </intent-filter>
```
在 onCreateOptionsMenu(Menu menu)函数中加入
```
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
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("word",query);
                startActivity(intent);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        
```
即可让搜索栏中输入的文字传到显示搜索结果的活动中,并在显示结果的搜索栏中进行dbread.rawQuery()并装配到listview就可以得到搜索结果

---
### 拓展功能

##### 对记事进行分类

此处的分类有"默认", "重要", "备忘", "笔记", "日程" ,用户不可自定义
此处一是作为分类,二也是为后续添加的功能留下接口(备忘接口添加闹钟提醒等,但这个版本只单纯的作为分类功能)
同时还有一个隐藏分类 删除
删除也是通过分类到删除分类并在select的时候去掉这个分类的记录

修改分类可以从主界面长按或者编辑界面的右上角分类按钮进行

![分类菜单.jpg](http://upload-images.jianshu.io/upload_images/6080696-ef7546053d032771.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

实现
```
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
....
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
```
使用一个AlertDialog弹出单选框,并根据选项的不同来加载不同的sql语句,并在确定后执行sql语句.

---
##### 在记事中添加图片

在编辑界面 点击右下角的按钮,就会打开相册,选择相册中的图片即可将图片加入到文本中

![插入图片1.jpg](http://upload-images.jianshu.io/upload_images/6080696-28fb95180ddc418b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

实现:

1.首先使用intent.getData得到uri 
2.然后调用BitmapFactory的解码函数decodeStream且要求的参数为流(Stream)，所以要用ContentResolver解析uri为流。 
3.接着通过一个resizeImage函数重新调整bitmap大小 
4.然后就是要把所得到的图片放到EditText里了

  首先要在AndroidManifest中加入权限

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
首先
对点击按钮的事件创建监听

```
Intent getImage  = new Intent("android.intent.action.GET_CONTENT");               
getImage.addCategory(Intent.CATEGORY_OPENABLE);
getImage.setType("image/*");
startActivityForResult(getImage, 1);
```

ACTION_GET_CONTENT是标准的Activity Action的一种，那什么是Activity Action呢，简单来说就是让用户选择一种特殊的数据并得到它。 

ACTION_GET_CONTENT可以让用户在运行的程序中取得数据，例如取照片，当然这里的运行的程序指的是手机上的文件管理器之类的。 

addCategory是要增加一个分类，增加一个什么分类呢？就是增加CATEGORY_OPENABLE，从字面意思值是增加一个可以打开的分类，也即是取得的uri要可以被ContentResolver解析，注意这里的分类即是执行的附加条件。

 
setType就是设置取得的数据类型为image，也即是取照片。 

```
protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri originalUri = intent.getData();
                String Imgpath = getPath(this,originalUri);
                Uri realUri = Uri.parse("file://"+Imgpath);//真实路径转化为Uri
                String realPath = "content://media/"+Imgpath;
                try {
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
```

将得到的图片转化成真实地址

下面是转化成图片的真实地址     参考文章:[Android4.4中获取资源路径问题](http://blog.csdn.net/huangyanan1989/article/details/17263203)

```
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
```

在TextView中显示图片

这里用到了Spannable和ImageSpan来在EditText中显示图片

这边使用了< img >标签标注图片的路径

```
//设置Spannable String
    private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
        String path = "<img>"+uri.getPath()+"<img>";
        SpannableString ss = new SpannableString(path);
        ImageSpan span = new ImageSpan(this, pic);
        ss.setSpan(span, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
```

```
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
```

插入图片时在图片上下各插入一个空行优化排版

```
//排版问题,插入EditText时在图片的上下一行加入空格;
    private  void insertEnter(){
        Editable et = et_content.getText();
        int start = et_content.getSelectionStart();
        String enter = "\n";
        et.insert(start,enter);
        et_content.setText(et);
        et_content.setSelection(start + enter.length());
    }
```

在文本中设置图片的显示大小

```
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
            matrix.postScale(scale, scale);//比例不变
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;

        }else{
            return null;
        }
    }
```

​	在EditText中加载图片

​	使用了正则表达式来识别< img >标签,并使用 ImageSpan来显示图片

```
/将内容中的图片加载出来
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
```





---

##### 一些界面美化以及人性化细节设置
本应用参照了Material Design,虽然并未完全规范,不过在界面上还是达到了相对应的简洁,在操作逻辑上也符合用户的使用
###### 界面美化
界面的演变

4.12 --雏形
内容:
- 通过ListView,Button,EditText等控件做出初始的界面
- 并通过内嵌数据库SQLite 完成对内容的增删改


![1g.jpg](http://upload-images.jianshu.io/upload_images/6080696-01e91b728bfbe883.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

4.13 --重新设计界面
内容:

- 对界面进行重新设计

![2g.jpg](http://upload-images.jianshu.io/upload_images/6080696-5df4d1f08d1a610a.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

为了使输入界面更加简洁,可以通过 在<EditText>中,加入

 ```   android:background="@null"```

去掉输入框下的横线


4.13 --Material Design
内容:
- 基于对界面的重新设计,加入了Material Design
  虽然并不是很规范,但会在后续慢慢完善
- 完成了搜索的基本逻辑,记事本的增删改查功能基本完善
- 同时对设置菜单,关于界面进行了初步的设计,但大部分功能都未实现

![3g_1.jpg](http://upload-images.jianshu.io/upload_images/6080696-b990b02b989a387c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![3g_2.jpg](http://upload-images.jianshu.io/upload_images/6080696-19db5f42b0d2c1b8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

后续的版本都是在第三次界面修改后基本没有太大的变化,主要实在功能上的变化

当前版本

![mg-1.jpg](http://upload-images.jianshu.io/upload_images/6080696-8ff7afb582110fe9.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


###### 人性化细节设置
- 虚拟键盘设置
- 自动补充标题
- 提醒设置
- 空界面提示



虚拟键盘设置
新建记事会自动弹出虚拟键盘,而二次编辑不弹出虚拟键盘,需要点击才会弹出键盘,因为作为记事类软件后续修改的频率远低于查看的频率,自动弹出键盘反而会降低用户体验
此外,光标自动聚焦在内容编辑处,标题在记事类软件中的存在性并不重要,若要编辑标题则需要点击标题栏
实现
```//使焦点默认在编辑内容上,点击标题栏才能编辑标题
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
            Log.d("ENTER_STATE",ENTER_STATE+"");            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } 
        
```
自动补充标题
若用户觉得标题不重要大可不填,将会自动生成标题
```  if(title.equals(&quot;&quot;)){
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd-HH-mm");
                    String dateNum1 = sdf.format(date);
                    //title = "新建记事"+ dateNum1;//自动添加为时间
                     title = "新建记事";//添加为新建记事
                }
```
提醒设置
在删除,编辑了内容未保存的情况下,空内容保存记录,都会弹出提示框来提示用户确保不会产生误操作
(在回收站中的删除没有提示,逻辑上如果你已经进到回收站并确认要删除那条被删除过的记录,应该不会是误操作)

未保存提示

![未保存提示.jpg](http://upload-images.jianshu.io/upload_images/6080696-53c3da29f775bd68.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

删除提示

![删除提示.jpg](http://upload-images.jianshu.io/upload_images/6080696-e7a77da48e4ce1b1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

空界面提示
在没有记录的首页,搜索不到结果的搜索页面,没有回收记录的回收站,不会因为没有记录而空在那里,而是会有一定的文字提醒

没有记录会提示点击右下角添加

![nullmain.jpg](http://upload-images.jianshu.io/upload_images/6080696-6445396d02a25b59.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

无搜索结果

![nullsearch.jpg](http://upload-images.jianshu.io/upload_images/6080696-71e7fb9570cc9a29.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

无回收文件

![nulldelete.jpg](http://upload-images.jianshu.io/upload_images/6080696-45540138f186abb9.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


实现
在Layout中同时放两个Item都是matchparents
然后通过判断select结果来判断是要显示Listview还是显示提示性文字
以MainActivity为例
```    //如果列表项为空,则显示背景和文字
    public boolean isNoteNull(){
        String sql = "select * from note where category !='"+CATEGORY_DELETED+"'";
        Log.d("sql",sql);
        Cursor c = dbManager.executeSql(sql, null);
        int number = c.getCount();
        Log.d("Note number",number+"");
        if(number == 0){
            ListView listView = (ListView)findViewById(R.id.notelist);
            TextView textView = (TextView)findViewById(R.id.main_text);
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            return true;
        }else{
            ListView listView = (ListView)findViewById(R.id.notelist);
            TextView textView = (TextView)findViewById(R.id.main_text);
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            return false;
        }
    }
```
