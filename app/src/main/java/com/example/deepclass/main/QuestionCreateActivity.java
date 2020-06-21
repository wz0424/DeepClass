package com.example.deepclass.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepclass.databasectrl.UserInfo;
import com.example.deepclass.myutil.ToastUtil;

import java.util.List;

public class QuestionCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_title,et_description,et_a,et_b,et_c,et_d,et_time;
    private Button bt_add,bt_del,bt_query;
    private TextView tv_test;
    private CheckBox cb_a,cb_b,cb_c,cb_d;

    String tel;
    List<UserInfo> userInfos;
    String id;

    SQLiteDatabase db;
    MyDBHelper myDBHelper;
    private static final String dbName = "question.db";
    private static final String tableName="choice_question";
    private ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_create);

        /*MyApplication myapp=(MyApplication)getApplication();
        tel = myapp.getPhone_number();
        userInfos = DataSupport.where("tel=?",tel).find(UserInfo.class);*/

        initView();
    }

    private void initView() {
        et_title=findViewById(R.id.et_title);
        et_description=findViewById(R.id.et_description);
        et_time=findViewById(R.id.et_time);
        et_a=findViewById(R.id.et_a);
        et_b=findViewById(R.id.et_b);
        et_c=findViewById(R.id.et_c);
        et_d=findViewById(R.id.et_d);

        bt_add=findViewById(R.id.bt_add);
        bt_del=findViewById(R.id.bt_del);
        bt_query=findViewById(R.id.bt_query);


        tv_test=findViewById(R.id.tv_test);

        cb_a=findViewById(R.id.cb_a);
        cb_b=findViewById(R.id.cb_b);
        cb_c=findViewById(R.id.cb_c);
        cb_d=findViewById(R.id.cb_d);

        bt_add.setOnClickListener(this);
        bt_del.setOnClickListener(this);
        bt_query.setOnClickListener(this);



        myDBHelper=new MyDBHelper(this);
    }

    @Override
    public void onClick(View v) {
        String title=et_title.getText().toString();
        String description=et_description.getText().toString();
        String time=et_time.getText().toString();
        String a=et_a.getText().toString();
        String b=et_b.getText().toString();
        String c=et_c.getText().toString();
        String d=et_d.getText().toString();
        String answer=answer();

        switch (v.getId()){
            case R.id.bt_add:
                if(TextUtils.isEmpty(title))
                    //ToastUtil.showImageToas(getApplicationContext(),"显示文本+图片");
                    ToastUtil.showTextToas(getApplicationContext(),"请输入标题");
                else if(TextUtils.isEmpty(description))
                    ToastUtil.showTextToas(getApplicationContext(),"请输入内容");
                else if(TextUtils.isEmpty(time))
                    ToastUtil.showTextToas(getApplicationContext(),"请输入答题时间");
                else if(TextUtils.isEmpty(a))
                    ToastUtil.showTextToas(getApplicationContext(),"请输入选项A的描述");
                else if(TextUtils.isEmpty(b))
                    ToastUtil.showTextToas(getApplicationContext(),"请输入选项B的描述");
                else if(TextUtils.isEmpty(c))
                    ToastUtil.showTextToas(getApplicationContext(),"请输入选项C的描述");
                else if(TextUtils.isEmpty(d))
                    ToastUtil.showTextToas(getApplicationContext(),"请输入选项D的描述");
                else if(!cb_a.isChecked()&&!cb_b.isChecked()&&!cb_c.isChecked()&&!cb_d.isChecked()){
                    ToastUtil.showTextToas(getApplicationContext(),"请选择正确答案");
                }
                else{
                    ADDDATE(title,description,time,a,b,c,d,answer);
                    et_title.setText("");
                    et_description.setText("");
                    et_time.setText("");
                    et_a.setText("");
                    et_b.setText("");
                    et_c.setText("");
                    et_d.setText("");
                    cb_a.setChecked(false);
                    cb_b.setChecked(false);
                    cb_c.setChecked(false);
                    cb_d.setChecked(false);
                    break; }
            case R.id.bt_del:
                if(TextUtils.isEmpty(title))
                    ToastUtil.showTextToas(getApplicationContext(),"请输入标题");
                else{
                    delData(title);
                    break;}
            case R.id.bt_query:
                queryData();
                break;

        }
    }

    private void queryData() {
        db=myDBHelper.getReadableDatabase();
        Cursor cursor=db.query(tableName,null,null,null,null,null,null);
        if(cursor.getCount()==0)
            ToastUtil.showTextToas(getApplicationContext(),"当前没有试题");
        else{
            String results="";
            cursor.moveToFirst();
            int count = cursor.getCount();
            int i;
            for(i=0;i<count;i++){
                cursor.moveToPosition(i);
                results += (i + 1) + ".标题："+cursor.getString(1)+"\n   内容："+cursor.getString(2)+"\n";
                results+="答题时间："+cursor.getString(3);
                results+="   答案是："+cursor.getString(8);
                results+="\n   选项为：";
                results+="\n    A:"+cursor.getString(4);
                results+="\n    B:"+cursor.getString(5);
                results+="\n    C:"+cursor.getString(6);
                results+="\n    D:"+cursor.getString(7);
                results+="\n";
            }
            ToastUtil.showTextToas(getApplicationContext(),"总共有"+i--+"道试题");
            tv_test.setText(results);

        }
        cursor.close();
        db.close();

    }


    private void delData(String title) {
        db=myDBHelper.getWritableDatabase();
        db.delete(tableName,"title=?",new String[]{title});
        ToastUtil.showTextToas(getApplicationContext(),"成功删除了标题为："+title+"试题");
        db.close();
    }




    private void addDate(String title, String description,String time, String a, String b, String c, String d,String answer) {
        db=myDBHelper.getWritableDatabase();
        values=new ContentValues();
        values.put("id",id);
        values.put("title",title);
        values.put("description",description);
        values.put("time",time);
        values.put("a",a);
        values.put("b",b);
        values.put("c",c);
        values.put("d",d);
        values.put("answer",answer);
        db.insert(tableName,null,values);
        ToastUtil.showTextToas(getApplicationContext(),"成功添加试题");
        db.close();
    }
    private void ADDDATE(String title, String description, String time,String a, String b, String c, String d, String answer) {
        boolean flag = true;
        db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            addDate(title, description, time,a, b, c, d, answer);
        } else {
            while (cursor.moveToNext()) {

                if (cursor.getString(1).equals(title) && cursor.getString(2).equals(description)
                        && cursor.getString(4).equals(a) && cursor.getString(5).equals(b)
                        && cursor.getString(6).equals(c) && cursor.getString(7).equals(d)) {
                    ToastUtil.showTextToas(getApplicationContext(),"该题目已存在，不可重复添加");
                    flag = false;
                    break;
                }
            }
            if (flag) addDate(title, description,time, a, b, c, d, answer);
        }
        cursor.close();
        db.close();
    }

    private String answer() {

        boolean isa=cb_a.isChecked();
        boolean isb=cb_b.isChecked();
        boolean isc=cb_c.isChecked();
        boolean isd=cb_d.isChecked();

        String answer = "";

        if(isa) answer += "A";
        if(isb) answer += "B";
        if(isc) answer += "C";
        if(isd) answer += "D";

        return answer;
    }




    static class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(@Nullable Context context) {
            super(context, dbName, null, 3);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists "+tableName+"("
                    +" id varchar ,"
                    +"title varchar,"
                    +"description varchar,"
                    +"time  varchar,"
                    +"a varchar,"
                    +"b varchar,"
                    +"c varchar,"
                    +"d varchar,"
                    +"answer varchar)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists "+tableName);
        }
    }
}
