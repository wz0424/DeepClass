package com.example.deepclass.main;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deepclass.databasectrl.MyApplication;
import com.example.deepclass.databasectrl.UserInfo;
import com.example.deepclass.tools.MyHelper;

import org.litepal.LitePal;

import java.util.List;

public class StudentLand extends AppCompatActivity implements View.OnClickListener {
    //定义登录的姓名和ID号和学号
    String id, password, tel;
    //定义登录按钮
    Button btn_Login,btn_Reg;
    //定义输入的文本域
    EditText et_ID, et_password;
    //定义图片的路径
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_land);
        btn_Login = (Button) findViewById(R.id.login);
        btn_Login.getBackground().setAlpha(200);
        btn_Login.setOnClickListener(this);
        et_ID = (EditText) findViewById(R.id.ID);
        et_password = (EditText) findViewById(R.id.password);
        btn_Reg=(Button)findViewById(R.id.register);
        btn_Reg.getBackground().setAlpha(200);
        btn_Reg.setOnClickListener(this);
        readRequest();
        MyHelper helper=new MyHelper(StudentLand.this);
    }
    //登录按钮被点击
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.register){
            Intent in=new Intent(this, register.class);
            startActivity(in);
        }
        else if(v.getId()==R.id.login) {
            id = et_ID.getText().toString().trim();
            password = et_password.getText().toString().trim();
            if (id.equals("") || password.equals("")) {
                Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder sum = new StringBuilder();
                SQLiteDatabase db;
                MyHelper ggg = new MyHelper(StudentLand.this);
                db = ggg.getWritableDatabase();
                String select = "id=?";
                String[] columns = new String[]{"password", "name", "identity"};
                String[] selectionArgs = new String[]{id};
                Cursor cursor = db.query("information", columns,
                        select, selectionArgs, null, null, null);
                List<UserInfo> users= LitePal.where("num=?",id).find(UserInfo.class);
                if (cursor.getCount() == 0) {
                    Toast.makeText(this, "尚未注册", Toast.LENGTH_SHORT).show();
                    cursor.close();
                } else {
                    cursor.moveToFirst();
                    String pwd = cursor.getString(0);
                    if (pwd.equals(password)) {
                        if(users.size()!=0){
                            tel=users.get(0).getTel();
                            MyApplication myphone_num=(MyApplication)getApplication();
                            myphone_num.setPhone_number(tel);
                        }
                        Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                        String name = cursor.getString(1);
                        String identity = cursor.getString(2);
                        String ID=cursor.getString(0);
                        if (identity.equals("我是老师")) {
                            Fragment_3.ID=id;
                            Fragment_3.Name=name;
                            Intent i = new Intent(this, TeacherMainActivity.class);
                            i.putExtra("id", id);
                            startActivity(i);
                        } else {
                            Fragment_3_2.ID=id;
                            Fragment_3_2.Name=name;
                            Intent i = new Intent(this, StudentMainActivity.class);
                            i.putExtra("id", id);
                            startActivity(i);
                        }
                        cursor.close();
                    } else {
                        Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                        cursor.close();
                    }
                }

            }
        }else{
            System system = null;
            system.exit(0);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void readRequest() {             //获取相机拍摄读写权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
}

