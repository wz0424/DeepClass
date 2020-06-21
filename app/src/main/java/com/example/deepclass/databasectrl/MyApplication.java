package com.example.deepclass.databasectrl;

import android.app.Application;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

public class MyApplication extends Application {
    private String phone_number,name;

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        setPhone_number("12345678900");
        setName("张三");
    }
}
