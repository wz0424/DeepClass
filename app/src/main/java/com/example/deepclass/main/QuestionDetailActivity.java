package com.example.deepclass.main;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepclass.databasectrl.DataQuery;
import com.example.deepclass.databasectrl.MyApplication;
import com.example.deepclass.databasectrl.UserInfo;
import com.example.deepclass.myutil.ToastUtil;

import org.litepal.LitePal;

import java.util.List;

public class QuestionDetailActivity extends AppCompatActivity {

    private CheckBox mCbA, mCbB, mCbC, mCbD;
    private TextView mTvTitle, mTvDesp;
    private Button mBtnSubmit;
    private String rightAsw;

    DBHelper helper;


    List<UserInfo> userInfos;
    String tel;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        helper=new DBHelper(this);
        helper.getWritableDatabase();

        initControl();
        setData();


        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication myapp=(MyApplication)getApplication();
                tel = myapp.getPhone_number();
                userInfos = LitePal.where("tel=?",tel).find(UserInfo.class);
                System.out.println("标识: "+tel);

                String userAnswer = getAnswer();
                // ToastUtil.showTextToas(getApplicationContext(),""+userAnswer);
                System.out.println(""+userAnswer);
                insert(userAnswer,rightAsw,tel);
                System.out.println("insert ok");
                //ToastUtil.showTextToas(getApplicationContext(),"insert  ok");
                query();

                Intent intent=new Intent(QuestionDetailActivity.this,QuestionListActivity.class);
                //intent.setAction("list");
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initControl(){
        mTvTitle = findViewById(R.id.qstd_tv_title);
        mTvDesp = findViewById(R.id.qstd_tv_desp);
        mCbA = findViewById(R.id.asw_cb_a);
        mCbB = findViewById(R.id.asw_cb_b);
        mCbC = findViewById(R.id.asw_cb_c);
        mCbD = findViewById(R.id.asw_cb_d);
        mBtnSubmit = findViewById(R.id.asw_btn_submit);

        cbChangeListener(mCbA);
        cbChangeListener(mCbB);
        cbChangeListener(mCbC);
        cbChangeListener(mCbD);
    }

    @SuppressLint("SetTextI18n")
    private void setData(){
        DataQuery dataQuery = new DataQuery(this);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos",-1);
        String[] allInfo = dataQuery.queryData(pos);
        String id = allInfo[0];
        String qstTitle = allInfo[1];
        String qstDesp = allInfo[2];
        String optionA = allInfo[4];
        String optionB = allInfo[5];
        String optionC = allInfo[6];
        String optionD = allInfo[7];
        rightAsw = allInfo[8];

        mTvTitle.setText(qstTitle);
        mTvDesp.setText(qstDesp);
        mCbA.setText("A、" + optionA);
        mCbB.setText("B、" + optionB);
        mCbC.setText("C、" + optionC);
        mCbD.setText("D、" + optionD);

    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void  cbChangeListener(final CheckBox checkBox){

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked())
                    checkBox.setBackground(getDrawable(R.drawable.cb_check_style));
                else
                    checkBox.setBackground(getDrawable(R.drawable.cb_style));
            }
        });
    }


    private String getAnswer() {

        boolean isa = mCbA.isChecked();
        boolean isb = mCbB.isChecked();
        boolean isc = mCbC.isChecked();
        boolean isd = mCbD.isChecked();

        String answer = "";

        if(isa) answer += "A";
        if(isb) answer += "B";
        if(isc) answer += "C";
        if(isd) answer += "D";

        return answer;
    }



    static class DBHelper extends SQLiteOpenHelper {
        //数据库版本号
        public static final int DATABASE_VERSION=5;

        //数据库名称
        public static final String DATABASE_NAME="grade.db";
        public static  final String TABLE_NAME="answer";
        public DBHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //创建数据表
            String CREATE_TABLE_STUDENT="CREATE TABLE "+ TABLE_NAME+"("
                    +" id "+"varchar ,"
                    +"rightAnswer"+" varchar,"
                    +"userAnswer"+" varchar)";
            db.execSQL(CREATE_TABLE_STUDENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //如果旧表存在，删除，所以数据将会消失
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);

            //再次创建表
            onCreate(db);
        }
    }
    public  void insert(String userAnswer,String rightAnswer,String id){

        String TABLE_NAME="answer";
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",id);
        values.put("rightAnswer",rightAnswer);
        values.put("userAnswer",userAnswer);
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    private void query() {
        SQLiteDatabase  db=helper.getReadableDatabase();
        String TABLE_NAME="answer";
        Cursor cursor=db.query(TABLE_NAME,null,null,null,null,null,null);
        if(cursor.getCount()==0)
            ToastUtil.showTextToas(getApplicationContext(),"当前没有答案");
        else{
            String results="";
            cursor.moveToFirst();
            int count = cursor.getCount();
            int i;
            for(i=0;i<count;i++){
                cursor.moveToPosition(i);
                results += (i + 1) + "id："+cursor.getString(0)+"\n   正确答案："+cursor.getString(1)+"\n";
                results+="   用户答案："+cursor.getString(2);
            }
            System.out.println(""+results);
        }
        cursor.close();
        db.close();
    }

}
