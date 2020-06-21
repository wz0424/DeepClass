package com.example.deepclass.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepclass.adapter.GradeboardAdapter;
import com.example.deepclass.databasectrl.UserInfo;

import org.litepal.LitePal;

import java.util.List;

public class GradeLeaderBoardActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_leaderboard);



        RecyclerView recyclerView;
        GradeboardAdapter gradeboardAdapter;
        List<UserInfo> userInfos;
        int flag= LitePal.deleteAll(UserInfo.class,"Is_teacher=?","1");
        System.out.print(""+flag);
        userInfos= LitePal.order("grade desc").find(UserInfo.class);
        recyclerView = findViewById(R.id.RecyclerView);
        gradeboardAdapter = new GradeboardAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(gradeboardAdapter);
        gradeboardAdapter.setUserInfos(userInfos);
        gradeboardAdapter.notifyDataSetChanged();
    }
}
