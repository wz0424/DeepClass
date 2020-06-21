package com.example.deepclass.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.deepclass.adapter.LeaderboardAdapter;
import com.example.deepclass.databasectrl.UserInfo;

import org.litepal.LitePal;

import java.util.List;

public class TimeLeaderboardActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LeaderboardAdapter leaderboardAdapter;
    List<UserInfo> userInfos;
    Button addInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_leaderboard);
        userInfos= LitePal.order("totalTime desc").find(UserInfo.class);
        recyclerView = findViewById(R.id.RecyclerView);
        leaderboardAdapter = new LeaderboardAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(leaderboardAdapter);
        leaderboardAdapter.setUserInfos(userInfos);
        leaderboardAdapter.notifyDataSetChanged();
    }
}
