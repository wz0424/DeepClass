package com.example.deepclass.main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

    private ImageView image = null;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        TextView textview=(TextView)findViewById(R.id.textview);
        image = (ImageView)findViewById(R.id.image);

//实现动画效果：透明度
        ObjectAnimator alpha=ObjectAnimator.ofFloat(textview,"alpha",0,1);
        alpha.setDuration(5000);
        alpha.start();

        ObjectAnimator alpha2=ObjectAnimator.ofFloat(image,"alpha",0,1);
        alpha2.setDuration(5000);//动画实现时间
        alpha2.start();

//动画完以后跳到主页面
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Intent intent=new Intent(Welcome.this,MainActivity.class);
                                          startActivity(intent);
                                          finish();
                                      }        },
                7000);//动画持续时间


    }


}
