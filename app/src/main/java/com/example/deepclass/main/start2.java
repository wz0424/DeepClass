package com.example.deepclass.main;


import android.database.sqlite.SQLiteDatabase;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.deepclass.main.R;
import com.example.deepclass.tools.MyHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class start2 extends AppCompatActivity implements View.OnClickListener {
    //定义登录按钮
    Button btn;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start2);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        id=getIntent().getStringExtra("id");
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        SQLiteDatabase db;
        MyHelper ggg = new MyHelper(start2.this);
        db = ggg.getWritableDatabase();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ggg.AP(db, "attendanceP", id,df.format(new Date()));
        Toast.makeText(start2.this, "开始打卡！", Toast.LENGTH_LONG).show();
    }
}
