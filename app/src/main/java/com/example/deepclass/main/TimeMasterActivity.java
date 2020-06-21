package com.example.deepclass.main;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepclass.databasectrl.MyApplication;
import com.example.deepclass.databasectrl.UserInfo;
import com.example.deepclass.myutil.AlarmMusicService;
import com.example.deepclass.myutil.MusicService;
import com.example.deepclass.myutil.MyService;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import java.util.List;
import java.util.Locale;

public class TimeMasterActivity extends AppCompatActivity {
    private TextView mTextViewCountDown;      //显示剩余时间
    private Button mButtonStartPause;           //开始/结束按钮
    private SeekBar mBar;                      //进度条
    private TextView mTxt;                      //进度条下方文本
    private ImageView MusicImage;   //音乐播放器
    private TextView returnText;  //程序出口，返回主页

    private CountDownTimer mCountDownTimer;  //倒数计时器

    private boolean mTimerRunning;              //是否在计时

    //    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mTimeLeftInMillis=30*60000;     //默认一开始的时间是30分钟
    private long tempTime;      //记录当时选择的学习时长
    List<UserInfo> userInfos;
    String name;
    String tel;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Connector.getDatabase();
        MyApplication myapp=(MyApplication) getApplication();
        name=myapp.getName();
        tel = myapp.getPhone_number();

//        if(DataSupport.findFirst(StudyTime.class)==null) {//如果该数据库没有元素就新建一个，初始的学习时间为0分钟
//            StudyTime a = new StudyTime();
//            a.setTotalTime(0);
//            a.save();
//        }
        //MyApplication myapp=(MyApplication)getApplication();
//        MyApplication myapp=(MyApplication) getApplication();
//        name=myapp.getName();
//        tel = myapp.getPhone_number();
        userInfos = LitePal.where("tel=?",tel).find(UserInfo.class);//通过电话号码来找数据库中的用户信息

        if(userInfos.size()==0){
            UserInfo a = new UserInfo();
            a.setTotalTime(0);
            userInfos.add(a);
        }
        //xml绑定
        setContentView(R.layout.activity_time_master);
        mTextViewCountDown = findViewById(R.id.textView3);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mBar= findViewById(R.id.seekBar_time);
        mTxt = (TextView) findViewById(R.id.textView4);
        MusicImage=(ImageView)findViewById(R.id.listen);
        returnText=(TextView)findViewById(R.id.returntt);
        returnText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        returnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TimeMasterActivity.this,StudentMainActivity.class);
                startActivity(intent);
            }
        });
        bindViews();    //动态显示进度条函数
        upDataStudyTime();  //更新专注时长
        if (!isAccessGranted()) {  //查看用户权限是否设置，若未设置则引导用户去设置用户权限
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        MusicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //音乐播放器，点击开启音乐
                if(isMyServiceRunning(MusicService.class)){
                    stopMusicService();
                }
                else startMusicService();
            }
        });
//        Button testButt=findViewById(R.id.testButton);
////        Button testButt=findViewById(R.id.testButt);
//        testButt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(TimeMasterActivity.this, testActivity.class);
//                startActivity(intent);
//            }
//        });
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //开始/放弃按钮
                if (mTimerRunning) {  //在计时而放弃了
//                        pauseTimer();
                    showQuitEvent();
                    startAlarmMusicService();
                } else {  //未计时则开始计时，打开各个服务
                    startTimer();
                    mBar.setEnabled(false);
                    startMyService();  //加上检测有无打开其他app
                    startMusicService();  //开始播放背景音乐

                }
            }
        });
        updateCountDownText();  //更新闹钟信息
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//防止因为按back而停止计时
        if(keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {//判断一个service是否在运行
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isAccessGranted() {//判断用户是否开启了权限，此权限让后台可以获取当前执行的activity
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void startAlarmMusicService(){//警告音
        Intent intent=new Intent(TimeMasterActivity.this, AlarmMusicService.class);
        startService(intent);
    }
    private void stopAlarmMusicService(){
        Intent intent=new Intent(TimeMasterActivity.this,AlarmMusicService.class);
        stopService(intent);
    }
    private  void startMusicService(){//背景音乐
        Intent intent=new Intent(TimeMasterActivity.this, MusicService.class);
        startService(intent);
    }
    private void stopMusicService(){
        Intent intent=new Intent(TimeMasterActivity.this,MusicService.class);
        stopService(intent);
    }
    private void startMyService(){ //监测用户是否打开了其他app
        Intent intent=new Intent(TimeMasterActivity.this, MyService.class);
        startService(intent);
    }
    private void stopMyService(){
        Intent intent=new Intent(TimeMasterActivity.this,MyService.class);
        stopService(intent);
    }

    private void bindViews() {//动态更新进度条函数

        mBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){   //最新改变
                    mButtonStartPause.setEnabled(false);
                }else{
                    mButtonStartPause.setEnabled(true);
                }
                mTxt.setText("选取时间:" + progress+"分钟");
//                START_TIME_IN_MILLIS=progress;
                mTimeLeftInMillis=progress*60000;
                tempTime=mTimeLeftInMillis;
                mTextViewCountDown.setText(progress+":00");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void startTimer() {//开始计时
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            private AlertDialog.Builder dialog;

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {   //计时完成
                mTimerRunning = false;
                mButtonStartPause.setText("开始" );
//                mButtonStartPause.setVisibility(View.VISIBLE);
//                mButtonReset.setVisibility(View.VISIBLE)
                //将总学习时长存入数据库并更新页面
//                StudyTime myClock=new StudyTime();
//                StudyTime preClock=new StudyTime();
                //preClock=DataSupport.find(StudyTime.class,1);
//                preClock = studyTimes.get(0);
                long preTime=userInfos.get(0).getTotalTime();
                long total=preTime+tempTime/60000;
                UserInfo myClock = new UserInfo();
                myClock.setTotalTime(total);
                myClock.updateAll("tel=?",tel);
                upDataStudyTime();

                //弹窗提醒


                AlertDialog.Builder dialog = new AlertDialog.Builder(TimeMasterActivity.this);
                dialog.setTitle("消息提示");
                dialog.setMessage("你已经完成了"+tempTime/60000+"分钟的学习");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopMyService();   //最新改变
                        mBar.setEnabled(true);
                        mTextViewCountDown.setText(tempTime/60000+":00");
                        mTimeLeftInMillis=tempTime;

                    }
                });
                dialog.show();



                //实验代码。可用
//                ClockData a=new ClockData();
//                a.setTotalTime(500);
//                a.save();


//                onCreate(null);


            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("放弃");
//        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {//停止计时
        mCountDownTimer.cancel();
        mTimerRunning = false;
        int num=mBar.getProgress();
        mTextViewCountDown.setText(num+":00");
        mTimeLeftInMillis=num*60000;
        mButtonStartPause.setText("开始");
    }



    private void updateCountDownText() {//更新闹钟信息
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }
    private void upDataStudyTime() {//更新学习时间
        TextView myStudyTime = findViewById(R.id.myStudyTime);
//        StudyTime studyTime=DataSupport.find(StudyTime.class,1);
//        StudyTime studyTime=studyTimes.get(0);
        userInfos = LitePal.where("tel=?",tel).find(UserInfo.class);//原本没这行
        if (userInfos.size() != 0) {
            myStudyTime.setText("你学习的时间为:" + userInfos.get(0).getTotalTime() + "分钟");
        }
        else{
            myStudyTime.setText("你学习的时间为:" + 0 + "分钟");
       }
    }

    private void showQuitEvent(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(TimeMasterActivity.this);
        dialog.setTitle("放弃警告");
        dialog.setMessage("放弃将导致本次时间不计入时长");
        dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopAlarmMusicService();  //警告音乐关闭
            }
        });
        dialog.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这代表你放弃了，将时间重新设置
                pauseTimer();
                mBar.setEnabled(true);
                updateCountDownText();  //新增
                stopMyService();
                stopAlarmMusicService();
                stopMusicService();


            }
        });
        dialog.show();
    }

}
