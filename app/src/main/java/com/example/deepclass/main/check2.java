package com.example.deepclass.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepclass.tools.MyHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class check2 extends AppCompatActivity implements View.OnClickListener{
    LinearLayout location,face;
    Button in;
    String id=null;
    Toast toa;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check2);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //给左上角添加返回箭头
        //getSupportActionBar().setTitle("签到");  //设置Title文字


        location=(LinearLayout)findViewById(R.id.location);
        location.setOnClickListener(this);

        face=(LinearLayout)findViewById(R.id.face);
        face.setOnClickListener(this);

        in =(Button) findViewById(R.id.in);
        in.setOnClickListener(this);

//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**Intent intent=new Intent(check2.this,location.class);
//                 /**ComponentName componentName=new ComponentName("com.example.ui","check_confirm");
//                 intent.setComponent(componentName);**/
//                //startActivity(intent);
//            }
//        });
//
//        face.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Intent intent=new Intent(check2.this,face.class);
//                /**ComponentName componentName=new ComponentName("com.example.ui","check_confirm");
//                 intent.setComponent(componentName);**/
//                //startActivity(intent);
//            }
//        });
//
//        in.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        id=getIntent().getStringExtra("id");
        MyHelper hhh = new MyHelper(check2.this);
    }
    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.in) {
            Intent in = new Intent(this, in.class);
            in.putExtra("id",id);
            startActivity(in);
        } else if (v.getId() == R.id.face) {


            SQLiteDatabase db;
            MyHelper ggg = new MyHelper(check2.this);
            db = ggg.getWritableDatabase();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String [] columns=new String []{"time"};
            Cursor cursor = db.query("attendanceP", columns,
                    null, null, null, null, null);
            if(cursor.getCount()==0) {
                toa= Toast.makeText(check2.this,"不在打卡时间内！",Toast.LENGTH_LONG);
                toa.show();
                cursor.close();
            }else{
                cursor.moveToLast();
                String time=cursor.getString(0);
                cursor.close();
                try {
                    Date t=df.parse(time);
                    Date now=df.parse(df.format(new Date()));
                    long from=t.getTime();
                    long to=now.getTime();
                    int minute=(int)((to-from)/(1000*60));
                    if(minute<=10){
                        Intent in = new Intent(this, opt.class);
                        in.putExtra("id",id);
                        startActivity(in);
                    }
                    else{
                        toa=Toast.makeText(check2.this,"不在打卡时间内！",Toast.LENGTH_LONG);
                        toa.show();
                    }
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        } else if (v.getId() == R.id.location) {
            SQLiteDatabase db;
            MyHelper ggg = new MyHelper(check2.this);
            db = ggg.getWritableDatabase();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String [] columns=new String []{"time,la,lo"};
            Cursor cursor = db.query("attendanceT", columns,
                    null, null, null, null, null);
            int a=cursor.getCount();
            if(cursor.getCount()==0) {
                toa=Toast.makeText(check2.this,"不在打卡时间内！",Toast.LENGTH_LONG);
                toa.show();
                cursor.close();
            }
            else{
                cursor.moveToLast();
                String time=cursor.getString(0);
                String la=cursor.getString(1);
                String lo=cursor.getString(2);
                cursor.close();
                try {
                    Date t=df.parse(time);
                    Date now=df.parse(df.format(new Date()));
                    long from=t.getTime();
                    long to=now.getTime();
                    int minute=(int)((to-from)/(1000*60));
                    if(minute<=10){
                        Intent in = new Intent(this, mapin.class);
                        in.putExtra("id",id);
                        in.putExtra("la",la);
                        in.putExtra("lo",lo);
                        startActivity(in);
                    }
                    else{
                        toa=Toast.makeText(check2.this,"不在打卡时间内！",Toast.LENGTH_LONG);
                        toa.show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.exit(0);
        }
    }

    // 返回上一界面
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return super.onSupportNavigateUp();
    }


}
