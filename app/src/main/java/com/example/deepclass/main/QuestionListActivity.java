package com.example.deepclass.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepclass.adapter.QuestionListAdapter;
import com.example.deepclass.databasectrl.DataQuery;
import com.example.deepclass.myutil.QuestionBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestionListActivity extends AppCompatActivity {
    TextView time_text;
    long mTimeLeftInMillis;


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        final DataQuery dataQuery = new DataQuery(this);

        if (dataQuery.getQstNum() == 0)
            setContentView(R.layout.activity_question_list_null);
        else {
            final int count_que=dataQuery.getQstNum();
            setContentView(R.layout.activity_question_list);
            time_text=(TextView)findViewById(R.id.tim);

            List<QuestionBean> questionBeans = new ArrayList<>();


            for (int i = 0; i < dataQuery.getQstNum(); i++) {
                String[] text = dataQuery.queryData(i);

                QuestionBean questionBean = new QuestionBean();
                questionBean.setId(Integer.valueOf(text[0]));
                questionBean.setQstTitle(text[1]);
                questionBean.setQstDesp(text[2]);
                questionBeans.add(questionBean);
            }

            ListView mLvQuestion = findViewById(R.id.lv_question);
            mLvQuestion.setAdapter(new QuestionListAdapter(QuestionListActivity.this, questionBeans));

            mLvQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(QuestionListActivity.this, QuestionDetailActivity.class);
                    intent.putExtra("pos", position);
                    startActivity(intent);
                }
            });



            final Button b = (Button) findViewById(R.id.button);
            b.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(QuestionListActivity.this);
                    dialog.setTitle("提交提醒");
                    dialog.setMessage("只可提交一次，确定提交吗？");
                    dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i =new Intent(QuestionListActivity.this,Grade.class);
                            i.putExtra("count_que",count_que);
                            startActivity(i);


                        }
                    });
                    dialog.show();
                }
            });
            String TABLE_NAME="choice_question";
            String temp_t="5";
            QuestionCreateActivity.MyDBHelper  helper =new QuestionCreateActivity.MyDBHelper(this);
            SQLiteDatabase db= helper.getWritableDatabase();
            Cursor cursor =db.query(TABLE_NAME,null,null,null,null,null,null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            int i;
            for(i=0;i<count;i++){
                cursor.moveToPosition(i);
                temp_t=cursor.getString(3);
            }

            int t=Integer.parseInt(temp_t);

            time_text.setText(t+":00");
            mTimeLeftInMillis=t*1000*60;

            CountDownTimer timer=new CountDownTimer(mTimeLeftInMillis,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    updateTime();
                }

                @Override
                public void onFinish() {
                    b.setEnabled(false);
                    AlertDialog.Builder dialog=new AlertDialog.Builder(QuestionListActivity.this);
                    dialog.setTitle("警告");
                    dialog.setMessage("时间已到，自动提交！");
                    dialog.show();
                    Intent o =new Intent(QuestionListActivity.this,Grade.class);
                    startActivity(o);
                }
            }.start();

        }

    }


    private void updateTime() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        time_text.setText(timeLeftFormatted);

    }
}
