package com.example.deepclass.myutil;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class MyService extends Service {

    boolean flag = true;
    private String lastTaskName;
    private ActivityManager activityManager;
    private Timer timer;
    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);

    private TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String topPackageName;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                @SuppressLint("WrongConstant") UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService("usagestats");
                long time = System.currentTimeMillis();
                // We get usage stats for the last 10 seconds
                List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
                // Sort the stats by the last time used
                if (stats != null) {
                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                    for (UsageStats usageStats : stats) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (mySortedMap != null && !mySortedMap.isEmpty()) {
                        topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                        if (!topPackageName.equals("com.example.deepclass.main")
                            //一定要改
                            //一定要改
                            //一定要改
                            //一定要改
                            //一定要改
                        ) {
                            Log.d("MonitorService", "Yes--recentTaskName=" + topPackageName);
                            Intent intentNewActivity = new Intent(MyService.this,
                                    testActivity.class);
                            intentNewActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentNewActivity);
////                            Toast.makeText(getApplicationContext(), "默认Toast样式",
////                                    Toast.LENGTH_SHORT).show();



                        } else {
                            Log.d("MonitorService", "No--recentTaskName=" + topPackageName);
                        }
                    }
                }

//            if(lastTaskName != null){
//                if(!lastTaskName.equals(topPackageName) && !recentTaskName.equals("com.example.myapplication") && !recentTaskName.equals("com.android.launcher")){
//                    Log.d("recentPackageName", recentTaskName);
//                    Intent intentNewActivity = new Intent(MyService.this, StudentLand.class);
//                    intentNewActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intentNewActivity);
////Toast.makeText(XuebaService.this, lastTaskName, Toast.LENGTH_SHORT).show();
//                }
//                else Log.d("recentPackageName",recentTaskName);
//            }
//            lastTaskName = recentTaskName;


//            if (recentTaskName.equals("com.example.myapplication")    //换种思路
//            ) {
//                Log.d("MonitorService", "Yes--recentTaskName=" + recentTaskName);
//                Intent intentNewActivity = new Intent(MyService.this,
//                        StudentLand.class);
//                intentNewActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intentNewActivity);
//
//            }
//            else{
//                Log.d("MonitorService", "No--recentTaskName="+recentTaskName);
//
//            }


            }
        }


    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (flag == true) {
            timer = new Timer();
            timer.schedule(task, 0, 1000);
            flag = false;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
