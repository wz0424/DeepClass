package com.example.deepclass.main;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import androidx.annotation.RequiresApi;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.deepclass.databasectrl.UserInfo;
import com.example.deepclass.tools.MyHelper;

public class register extends Activity implements View.OnClickListener {
    //定义登录的姓名和ID号
    String id,name,password,tel,identity;
    //定义登录按钮
    Button btn_register;
    //定义输入的文本域
    EditText et_ID, et_name ,et_tel,et_password;
    RadioGroup rg;
    RadioButton rb;
    //定义图片的路径
    private String imagePath = null;

    private Bitmap bp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.getBackground().setAlpha(200);
        btn_register.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.name);
        et_ID = (EditText) findViewById(R.id.ID);
        et_password = (EditText) findViewById(R.id.password);
        et_tel = (EditText) findViewById(R.id.phone);
        rg=(RadioGroup)findViewById(R.id.rg);
    }
    //检查登录是否有效
    boolean checkNum(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (!(ch >= '0' && ch <= '9')) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        name = et_name.getText().toString().trim();
        id = et_ID.getText().toString().trim();
        password = et_password.getText().toString().trim();
        int select=rg.getCheckedRadioButtonId();
        rb=(RadioButton)findViewById(select);
        identity=rb.getText().toString();
        tel = et_tel.getText().toString().trim();

        UserInfo user =new UserInfo();
        user.setNum(id);
        user.setName(name);
        user.setPassword(password);
        if(identity.equals("我是学生")){
            user.setIs_teacher(false);
            Log.d("hhhhh", "xueshen");
        }
        else {
            user.setIs_teacher(true);
            Log.d("hhhhh", "laoshi");
        }
        user.setTel(tel);
        user.setTotalTime(0);
        user.setGrade(0);
        if (name.equals("") || id.equals("")) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
        } else if (!(checkNum(id) && checkNum(tel))) {
            Toast.makeText(this, "数据非法", Toast.LENGTH_SHORT).show();
        } else if (password.length()<=6) {
        Toast.makeText(this, "密码需大于六位", Toast.LENGTH_SHORT).show();
        } else if (!isMobileNO(tel)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
        } else if (id.length()!=10) {
            Toast.makeText(this, "请输入正确的学号", Toast.LENGTH_SHORT).show();
        }  else {
            SQLiteDatabase db;
            MyHelper ggg = new MyHelper(register.this);
            db = ggg.getWritableDatabase();
            ggg.Register(db, "information", id, name,password,tel,identity);
            user.save();
            Toast.makeText(register.this, "注册成功", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, StudentLand.class);
            startActivity(i);
        }
    }
    //正则表达式，判断手机号格式
    private boolean isMobileNO(String mobiles) {
        String telRegex = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16([5,6])|(17[0-8])|(18[0-9]))|(19[1,8,9]))\\d{8}$";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }
}

