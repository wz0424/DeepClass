package com.example.deepclass.main;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deepclass.main.R;
import com.example.deepclass.tools.MyHelper;
import java.util.Calendar;

public class searcher extends AppCompatActivity implements View.OnClickListener {

    Button btn_search, btn_search_TI, btn_choosetime, btn_confirm;
    TextView search_sum;
    int flag;
    EditText IDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        flag = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change);
        btn_search = (Button) findViewById(R.id.search_NI);
        btn_search.setOnClickListener(this);
        btn_search_TI = (Button) findViewById(R.id.search_ka);
        btn_search_TI.setOnClickListener(this);
        search_sum = (TextView) findViewById(R.id.tv_sum);

        btn_choosetime = (Button) findViewById(R.id.time);
        btn_choosetime.setOnClickListener(this);

        btn_confirm = (Button) findViewById(R.id.FINALLY);
        btn_confirm.setOnClickListener(this);

        IDS = (EditText) findViewById(R.id.ids);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_ka) {
            StringBuilder sum = new StringBuilder();
            SQLiteDatabase db;
            MyHelper ggg = new MyHelper(searcher.this);
            db = ggg.getWritableDatabase();
            Cursor cursor = db.query("attendance", null,
                    null, null, null, null, null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String time = cursor.getString(2);
                sum.append("查询结果：\n ID          name              time\n");
                sum.append(id).append("\t").append(name).append("\t").append(time).append("\n");
                while (cursor.moveToNext()) {
                    String id1 = cursor.getString(0);
                    String name1 = cursor.getString(1);
                    String time1 = cursor.getString(2);
                    sum.append(id1).append("\t").append(name1).append("\t").append(time1).append("\n");
                }
            }
            cursor.close();
            db.close();
            search_sum.setText(sum.toString());

        } else if (v.getId() == R.id.search_NI) {

            StringBuilder sum = new StringBuilder();
            SQLiteDatabase db;
            MyHelper ggg = new MyHelper(searcher.this);
            db = ggg.getWritableDatabase();
            String selection="identity=?";
            String [] selectionArgs=new String[]{"我是学生"};
            Cursor cursor = db.query("information", null,
                    selection, selectionArgs, null, null, null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String tel = cursor.getString(3);
                sum.append("查询结果：\nID          name             phone\n");
                sum.append(id).append("\t").append(name).append("\t").append(tel).append("\n");

                while (cursor.moveToNext()) {
                    String id1 = cursor.getString(0);
                    String name1 = cursor.getString(1);
                    String tel1=cursor.getString(2);
                    sum.append(id1).append("\t").append(name1).append("\t").append(tel1).append("\n");
                }
            }
            cursor.close();
            db.close();
            search_sum.setText(sum.toString());
        } else if (v.getId() == R.id.time) {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(searcher.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    c.set(year, monthOfYear, dayOfMonth);
                    btn_choosetime.setText(DateFormat.format("yyy-MM-dd", c));
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.show();
            flag = 1;

        } else if (v.getId() == R.id.FINALLY) {
            if (flag == 1) {    //选了日期
                if (IDS.getText().toString().trim().equals("")) {      //只选了日期
                    String TIMES = btn_choosetime.getText().toString().trim();   //日期在此.
                    StringBuilder sum = new StringBuilder();
                    SQLiteDatabase db;
                    MyHelper ggg = new MyHelper(searcher.this);
                    db = ggg.getWritableDatabase();
                    Cursor cursor = db.query("attendance", null,
                            null, null, null, null, null);

                    sum.append("结果如下：");
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        String id = cursor.getString(0);
                        String name = cursor.getString(1);
                        String time = cursor.getString(2);
                        if (TIMES.equals
                                (time.substring(0, TIMES.length()))) {    //截取前面的部分比较。如2018-12-31
                            sum.append("\nID       time       name\n");
                            sum.append(id).append("\t").append(name).append("\t").append(time).append("\n");
                        }

                        while (cursor.moveToNext()) {
                            String id1 = cursor.getString(0);
                            String name1 = cursor.getString(1);
                            String time1 = cursor.getString(2);
                            if (TIMES.equals
                                    (time1.substring(0, TIMES.length()))) {
                                    sum.append(id1).append("\t").append(name1).append("\t").append(time1).append("\n");
                            }
                        }
                    }
                    cursor.close();
                    db.close();
                    search_sum.setText(sum.toString());
                } else {    //选了日期，选了ID

                    String id = IDS.getText().toString().trim();
                    String TIMES = btn_choosetime.getText().toString().trim();   //日期在此.
                    SQLiteDatabase db;
                    MyHelper ggg = new MyHelper(searcher.this);
                    db = ggg.getWritableDatabase();

                    StringBuilder sum = new StringBuilder();

                    String sql = "select * from time_id where id=\"" + id + "\"";

                    Cursor cursor = db.rawQuery(sql, null);
                    while (cursor.moveToNext()) {
                        String ID = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
                        String NAME = cursor.getString(1);//获取第二列的值
                        String TIME = cursor.getString(2);//获取第二列的值

                        if(TIMES.equals(TIME.substring(0,TIMES.length()))){
                            sum.append("ID          name              time:\n");
                            sum.append(ID).append("\t").append(NAME).append("\t").append(TIME).append("\n");
                        }
                    }
                    cursor.close();
                    db.close();
                    search_sum.setText(sum.toString());



                }
            } else {     //flag=0；  //没选日期
                if (IDS.getText().toString().trim().equals("")) {    //啥都没选
                    Toast.makeText(this, "选一个呗", Toast.LENGTH_SHORT).show();
                } else {       //只选了ID
                    String id = IDS.getText().toString().trim();    //获取id

                    SQLiteDatabase db;
                    MyHelper ggg = new MyHelper(searcher.this);
                    db = ggg.getWritableDatabase();

                    StringBuilder sum = new StringBuilder();

                    String sql = "select * from attendance where id=\"" + id + "\"";
                   // System.out.println("差部分：" + sql);
                    Cursor cursor = db.rawQuery(sql, null);
                    while (cursor.moveToNext()) {
                        String ID = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
                        String NAME = cursor.getString(1);//获取第二列的值
                        String TIME = cursor.getString(2);//获取第二列的值
                        sum.append("ID          name                   time\n");
                        sum.append(ID).append("\t").append(NAME).append("\t").append(TIME).append("\n");
                    }
                    cursor.close();
                    db.close();
                    search_sum.setText(sum.toString());
                }
            }
        }
    }
}
