package com.example.deepclass.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class StudentMainActivity extends AppCompatActivity implements View.OnClickListener {
    String id=null;
    //Fragment
    private Fragment_1_2 fragment1;
    private Fragment_2_2 fragment2;
    private Fragment_3_2 fragment3;

    //底端菜单栏LinearLayout
    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear3;
    Bundle bundle=new Bundle();

    //底端菜单栏Imageview
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;

    //底端菜单栏textView
    /**private TextView tv1;
     private TextView tv2;
     private TextView tv3;
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        id=getIntent().getStringExtra("id");
        bundle.putString("id",id);
        //初始化各个控件
        InitView();

        //初始化点击触发事件
        InitEvent();

        //初始化Fragment
        InitFragment(1);

        //将第一个图标设置为选中状态
        iv1.setImageResource(R.drawable.class2);
        //tv_first.setTextColor(getResources().getColor(R.color.colorTextViewPress));
    }

    //初始化控件
    private void InitView(){

        linear1 = findViewById(R.id.line1_stu);
        linear2 =  findViewById(R.id.line2_stu);
        linear3 = findViewById(R.id.line3_stu);

        iv1=  findViewById(R.id.iv1_stu);
        iv2 =  findViewById(R.id.iv2_stu);
        iv3=  findViewById(R.id.iv3_stu);

        /**
         tv1=  findViewById(R.id.tv1);
         tv2 =  findViewById(R.id.tv2);
         tv3=  findViewById(R.id.tv3);
         **/

    }

    //初始化fragment
    private void InitFragment(int index){
        //使用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();

        //启动事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //将所有的Fragment隐藏
        hideAllFragment(transaction);

        switch (index){
            case 1:
                if (fragment1== null){
                    fragment1 = new Fragment_1_2();
                    fragment1.setArguments(bundle);
                    transaction.add(R.id.frame_content,fragment1);
                }
                else{
                    transaction.show(fragment1);
                }
                break;

            case 2:
                if (fragment2== null){
                    fragment2 = new Fragment_2_2();
                    fragment2.setArguments(bundle);
                    transaction.add(R.id.frame_content,fragment2);
                }
                else{
                    transaction.show(fragment2);
                }
                break;

            case 3:
                if (fragment3== null){
                    fragment3 = new Fragment_3_2();
                    fragment3.setArguments(bundle);
                    transaction.add(R.id.frame_content,fragment3);
                }
                else{
                    transaction.show(fragment3);
                }
                break;

        }
        //提交事务
        transaction.commit();
    }

    //初始化事件
    private void InitEvent(){
        //设置LinearLayout监听
        linear1.setOnClickListener(this);
        linear2.setOnClickListener(this);
        linear3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        //每次点击之后，将所有的ImageView和TextView设置为未选中
        restartButton();

        //再根据所选，进行跳转页面，并将ImageView和TextView设置为选中
        switch(view.getId()){
            case R.id.line1_stu:
                //Toast.makeText(StudentLand.this,"first", Toast.LENGTH_SHORT).show();
                iv1.setImageResource(R.drawable.class2);
                //tv_first.setTextColor(getResources().getColor(R.color.colorTextViewPress));
                InitFragment(1);
                break;

            case R.id.line2_stu:
                iv2.setImageResource(R.drawable.create2);
                //tv2.setTextColor(getResources().getColor(R.color.colorTextViewPress));
                InitFragment(2);
                break;

            case R.id.line3_stu:
                iv3.setImageResource(R.drawable.person2);
                //tv3.setTextColor(getResources().getColor(R.color.colorTextViewPress));
                InitFragment(3);
                break;
        }
    }

    //隐藏所有的Fragment
    private void hideAllFragment(FragmentTransaction transaction){
        if (fragment1!= null){
            transaction.hide(fragment1);
        }

        if (fragment2!= null){
            transaction.hide(fragment2);
        }

        if (fragment3!= null){
            transaction.hide(fragment3);
        }

        // transaction.commit();
    }

    //重新设置ImageView和TextView的状态
    private void restartButton(){
        //设置为未点击状态
        iv1.setImageResource(R.drawable.class1);
        iv2.setImageResource(R.drawable.create1);
        iv3.setImageResource(R.drawable.person1);

        //设置为灰色
        /**
         tv1.setTextColor(getResources().getColor(R.color.黄色));
         tv2.setTextColor(getResources().getColor(R.color.黄色));
         tv3.setTextColor(getResources().getColor(R.color.黄色));
         **/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
