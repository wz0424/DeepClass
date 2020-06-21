package com.example.deepclass.main;


import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deepclass.domain.add_result_bean;
import com.example.deepclass.tools.Base64Util;
import com.example.deepclass.tools.GsonUtils;
import com.example.deepclass.tools.HttpUtil;
import com.example.deepclass.tools.toolsUnit;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class in extends AppCompatActivity implements View.OnClickListener {
    //定义登录的姓名和ID号
    String id=null;
    //定义登录按钮
    Button btn_Login;
    //定义输入的文本域
    EditText et_ID, et_name ,et_tel;
    //定义图片的路径
    private String imagePath = null;

    private Bitmap bp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in);
        btn_Login = (Button) findViewById(R.id.take_a_picture);
        btn_Login.setOnClickListener(this);
        id=getIntent().getStringExtra("id");
        }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();

            //临时照片存储地
            File outputImage = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "face.jpg");
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
                                    //获取Uri
            //定义图片的uri
            Uri imageUri = Uri.fromFile(outputImage);   //将file转成uri对象
            imagePath = outputImage.getAbsolutePath();

            //，是传递你要保存的图片的路径，系统会根据你提供的地址进行保存图片
            Log.i("拍照图片路径", imagePath);

            //跳转相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //相片输出路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //返回照片路径
            startActivityForResult(intent,0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bp = toolsUnit.getimage(imagePath);
        runthreaad();
        Intent in=new Intent(this, StudentMainActivity.class);
        startActivity(in);
    }



    void runthreaad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
                try {
                    byte[] bytes1 = toolsUnit.getBytesByBitmap(bp);
                    String image1 = Base64Util.encode(bytes1);


                    Map<String, Object> map = new HashMap<>();
                    map.put("image", image1);
                    map.put("group_id", "face");
                    map.put("user_id", id);
                    map.put("user_info", "abc");
                    map.put("liveness_control", "NORMAL");
                    map.put("image_type", "BASE64");
                    map.put("quality_control", "LOW");
                    String param = GsonUtils.toJson(map);


                    String clientId = "FYXa2vOsI7PNSR0fbGVMpSfQ";
                    String clientSecret = "UHBl3ymQaGS9gsGomCfjdGZxvZbCWOtX";


                    String accessToken = toolsUnit.getAuth(clientId, clientSecret);

                    String result = HttpUtil.post(url, accessToken, "application/json", param);
                    Gson gson = new Gson();

                    add_result_bean Result_bean = gson.fromJson(result, add_result_bean.class);


                    int Error_code = Result_bean.getError_code();
                    if (Error_code == 0) {
                        Looper.prepare();
                        Toast.makeText(in.this, "上传成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        String error_message = "上传失败：" + Result_bean.getError_msg();
                        Looper.prepare();
                        Toast.makeText(in.this, error_message, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
