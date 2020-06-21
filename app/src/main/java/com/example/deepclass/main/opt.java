package com.example.deepclass.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepclass.domain.Search_result_bean;

import com.example.deepclass.tools.Base64Util;
import com.example.deepclass.tools.GsonUtils;
import com.example.deepclass.tools.HttpUtil;
import com.example.deepclass.tools.MyHelper;
import com.example.deepclass.tools.toolsUnit;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class opt extends AppCompatActivity implements View.OnClickListener {

    private String ImagePath = null;
    private Bitmap bp = null;
    String id=null;
    String result;
    Button btn_pai;
    ImageView iv_picture;
    TextView tv_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opt);
        btn_pai = (Button) findViewById(R.id.take_a_picture);
        btn_pai.setOnClickListener(this);
        iv_picture = (ImageView) findViewById(R.id.picture);
        id=getIntent().getStringExtra("id");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {          //点击拍照或者从相册选取，返回值为带地址的intent
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();            //7.0拍照必加
        File outputImage = new File(Environment.getExternalStorageDirectory() + File.separator + "face.jpg");     //临时照片存储地
        try {                                                                                   //文件分割符
            if (outputImage.exists()) {   //如果临时地址有照片，先清除
                outputImage.delete();
            }
            outputImage.createNewFile();    ///创建零食地址
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri = Uri.fromFile(outputImage);              //获取Uri

        //   imageUri_display= FileProvider.getUriForFile(opt.this,"com.example.a11630.face_new.fileprovider",outputImage);
        ImagePath = outputImage.getAbsolutePath();
        Log.i("拍照图片路径", ImagePath);         //，是传递你要保存的图片的路径，打开相机后，点击拍照按钮，系统就会根据你提供的地址进行保存图片
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //跳转相机
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);                          //相片输出路径
        startActivityForResult(intent,0);                        //返回照片路径
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相册选择图片
        bp = toolsUnit.getimage(ImagePath);
        runthreaad();      //开启线程，传入图片
        Intent in=new Intent(this, StudentMainActivity.class);
        startActivity(in);
    }

    void runthreaad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
                try {
                    byte[] bytes1 = toolsUnit.getBytesByBitmap(bp);
                    String image1 = Base64Util.encode(bytes1);

                    Map<String, Object> map = new HashMap<>();
                    map.put("image", image1);
                    map.put("liveness_control", "NORMAL");
                    map.put("group_id_list", "face");
                    map.put("image_type", "BASE64");
                    map.put("quality_control", "LOW");
                    String param = GsonUtils.toJson(map);

                    String clientId = "FYXa2vOsI7PNSR0fbGVMpSfQ";
                    String clientSecret = "UHBl3ymQaGS9gsGomCfjdGZxvZbCWOtX";


                    String accessToken = toolsUnit.getAuth(clientId, clientSecret);


                    result = HttpUtil.post(url, accessToken, "application/json", param);


                    Gson gson = new Gson();                      //新建GSON
                    Search_result_bean Result_bean = gson.fromJson(result, Search_result_bean.class);

                    int Error_code = Result_bean.getError_code();
                    if (Error_code == 0) {                     //返回值为零，就是打卡识别成功

                        double score = Result_bean.getResult().getUser_list().get(0).getScore();

                        String user = Result_bean.getResult().getUser_list().get(0).getUser_id();

                     //   System.out.println("分数：" + score);
                        if (score >= 78.0&& id.equals(user)) {
                            SQLiteDatabase db;
                            MyHelper ggg = new MyHelper(opt.this);
                            db = ggg.getWritableDatabase();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                            String select="id=?";
                            String [] columns=new String []{"name"};
                            String [] selectionArgs=new String[]{id};
                            Cursor cursor = db.query("information", columns,
                                    select, selectionArgs, null, null, null);
                            String name;
                            if(cursor.moveToFirst()) {
                                name = cursor.getString(0);
                            }
                            else{
                                name=null;
                            }
                            cursor.close();


                            ggg.Insert_two(db, "attendance", id,name,df.format(new Date()));

                            Looper.prepare();
                            Toast.makeText(opt.this, "打卡成功！", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(opt.this, "打卡失败！", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    } else {
                        String error_message = "打卡失败：" + Result_bean.getError_msg();
                        Looper.prepare();
                        Toast.makeText(opt.this, error_message, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
