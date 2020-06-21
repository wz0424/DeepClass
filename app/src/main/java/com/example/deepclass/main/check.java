package com.example.deepclass.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class check extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout timeLeaderBoard;
    private LinearLayout gradeLeaderBoard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        timeLeaderBoard=(LinearLayout) findViewById(R.id.timeleaderboard);
        timeLeaderBoard.setOnClickListener(this);

        gradeLeaderBoard=(LinearLayout)findViewById(R.id.gradeleaderboard);
        gradeLeaderBoard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.timeleaderboard){
            Intent intent=new Intent(check.this,TimeLeaderboardActivity.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.gradeleaderboard){
            Intent intent=new Intent(check.this,GradeLeaderBoardActivity.class);
            startActivity(intent);
        }
    }
}
