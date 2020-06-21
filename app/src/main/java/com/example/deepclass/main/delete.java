package com.example.deepclass.main;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deepclass.domain.add_result_bean;
import com.example.deepclass.main.R;
import com.example.deepclass.tools.GsonUtils;
import com.example.deepclass.tools.HttpUtil;
import com.example.deepclass.tools.MyHelper;
import com.example.deepclass.tools.toolsUnit;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class delete extends AppCompatActivity implements View.OnClickListener {

    Button btn_confirm;
    EditText et_user;
    String S_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);
        btn_confirm=(Button)findViewById(R.id.confirm);
        btn_confirm.setOnClickListener(this);
        et_user=(EditText)findViewById(R.id.user);
    }

    @Override
    public void onClick(View v) {
       S_user=et_user.getText().toString().trim();
       runthreaad();
    }

    void runthreaad()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url ="https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/delete";
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("group_id", "face");
                    map.put("user_id", S_user);
                    String param = GsonUtils.toJson(map);
                    String clientId = "FYXa2vOsI7PNSR0fbGVMpSfQ";
                    String clientSecret = "UHBl3ymQaGS9gsGomCfjdGZxvZbCWOtX";
                    String accessToken = toolsUnit.getAuth(clientId, clientSecret);
                    String result = HttpUtil.post(url, accessToken, "application/json", param);
                    System.out.println(result);
                    Gson gson=new Gson();
                    add_result_bean Result_bean=gson.fromJson(result,add_result_bean.class);
                    int Error_code=Result_bean.getError_code();
                    if(Error_code==0){

                        SQLiteDatabase db;
                        MyHelper ggg= new MyHelper(delete.this);
                        db=ggg.getWritableDatabase();
                        ggg.Delete(db,"name_id",S_user);
                        ggg.Delete(db,"time_id",S_user);

                        Looper.prepare();
                        Toast.makeText(delete.this,"删除成功" , Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }else{
                        SQLiteDatabase db;
                        MyHelper ggg= new MyHelper(delete.this);
                        db=ggg.getWritableDatabase();
                        ggg.Delete(db,"name_id",S_user);
                        ggg.Delete(db,"time_id",S_user);
                        String error_message="删除失败："+Result_bean.getError_msg();
                        Looper.prepare();
                        Toast.makeText(delete.this,error_message , Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}