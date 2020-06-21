package com.example.deepclass.main;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deepclass.databasectrl.MyApplication;
import com.example.deepclass.databasectrl.UserInfo;

import org.litepal.LitePal;

import java.util.List;

public class Grade extends AppCompatActivity {
    public  static final String TABLE_NAME="answer";
    public static final String table_name="choice_question";
    private TextView show_grade;
    String tel;
    List<UserInfo> userInfos;



    protected void onCreate(Bundle savedInstanceState) {
        QuestionDetailActivity.DBHelper  helper =new QuestionDetailActivity.DBHelper(this);
        SQLiteDatabase database=helper.getWritableDatabase();//这是用户答案 正确答案  标识的数据库

        QuestionCreateActivity.MyDBHelper  myhelper =new QuestionCreateActivity.MyDBHelper(this);
        SQLiteDatabase data_base=myhelper.getWritableDatabase();//这是标题 内容 时间 abcd 正确答案的数据库
        Cursor cursor2 = data_base.query(table_name, null, null, null, null, null, null);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grade);
        show_grade=(TextView)findViewById(R.id.show_grade);

        Intent in=getIntent();
        int count= in.getIntExtra("count_que",2);
        System.out.println("题目个数："+count+"\n");


        double final_result=0;//最后的结果
        double solu=0;//每道题多少分
        double full=100.0;

        MyApplication myapp=(MyApplication)getApplication();
        tel = myapp.getPhone_number();
        userInfos = LitePal.where("tel=?",tel).find(UserInfo.class);

        Cursor cursor=database.query(TABLE_NAME,null,null,null,null,null,null);
        String results="";
        cursor.moveToFirst();
        while (!(cursor.getString(0).equals(tel))){
            cursor.moveToNext();
        }
        if(count!=0) {
            solu = full / count;
            System.out.println("每题分值" + solu + "\n");
            int i;
            for (i = 0; i < count; i++) {
                // cursor.moveToPosition(i);
                results += (i + 1) + "\n id：" + cursor.getString(0) + "\n   正确答案：" + cursor.getString(1) + "\n";
                results += "   用户答案：" + cursor.getString(2) + "\n";
                if (cursor.getString(1).equals(cursor.getString(2))) {
                    final_result = final_result + solu;
                }
                cursor.moveToNext();
            }
            System.out.println("初始化分数：" + final_result);
            System.out.println("答题结果：" + results);
            if (count % 2 != 0&&final_result != 0 && final_result != 100) {
                final_result = final_result + 1;
            }
            show_grade.setText("您的成绩为" + (int) final_result);
            UserInfo myGrade = new UserInfo();
            myGrade.setGrade((int)final_result);
            myGrade.updateAll("tel=?",tel);
        }

        //显示题目和答案





        String [] moblieArray=new String[count];
        cursor.moveToFirst();
        while (!(cursor.getString(0).equals(tel))){
            cursor.moveToNext();
        }
        cursor2.moveToFirst();
        for(int m=0;m<count;m++){
            String q="";
            String result="";
            q="题目为："+cursor2.getString(1)+"\n"+"内容为："+cursor2.getString(2)+"\n"
                    +"A："+cursor2.getString(4)+"\n"+"B："+cursor2.getString(5)+"\n"
                    +"C："+cursor2.getString(6)+"\n"+"D："+cursor2.getString(7)+"\n";
            result="正确答案为："+cursor.getString(1)+"\n"+"用户答案为："+cursor.getString(2)+"\n";
            cursor.moveToNext();
            cursor2.moveToNext();
            moblieArray[m]=q+"\n"+result;
            System.out.println(q+"\n"+result);
        }
        cursor.close();
        database.close();
        cursor2.close();
        data_base.close();
        ArrayAdapter adapter=new ArrayAdapter(this,R.layout.cell_list,moblieArray);
        ListView listView=findViewById(R.id.list_again);
        listView.setAdapter(adapter);

    }
}
