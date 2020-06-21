package com.example.deepclass.databasectrl;

//import org.litepal.crud.DataSupport;

import org.litepal.crud.LitePalSupport;

public class UserInfo extends LitePalSupport {
    private int id;
    private String password;
    private String tel;
    private boolean is_teacher;
    private String name;
    private String num;
    private long totalTime;
    private int grade;

    public int getGrade() { return grade; }

    public void setGrade(int grade){
        this.grade=grade;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setIs_teacher(boolean is_student) {
        this.is_teacher = is_student;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getTel() {
        return tel;
    }

    public boolean isIs_teacher() {
        return is_teacher;
    }

    public String getName() {
        return name;
    }
}

