package com.example.deepclass.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context) {
        super(context, "facedata.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String information = "create table information(id text primary key ,name text,password text,phone text,identity text)";
        String attendance = "create table attendance(id text ,name text,time text)";
        String attendanceT = "create table attendanceT(id text,time text,la text,lo text)";
        String attendanceP = "create table attendanceP(id text,time text)";
        db.execSQL(information);
        db.execSQL(attendance);
        db.execSQL(attendanceT);
        db.execSQL(attendanceP);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public  void Register(SQLiteDatabase db, String table, String id, String name,String password,String phone,String identity) {
        ContentValues cValue = new ContentValues();
        cValue.put("id", id);
        cValue.put("name", name);
        cValue.put("password", password);
        cValue.put("phone", phone);
        cValue.put("identity", identity);
        db.insert(table, null, cValue);
    }

    public  void Insert_two(SQLiteDatabase db, String table,String name, String id, String time) {
        ContentValues cValue = new ContentValues();
        cValue.put("id", name);
        cValue.put("name",id);
        cValue.put("time", time);
        db.insert(table, null, cValue);
    }
    public void AT(SQLiteDatabase db,String table,String id,String time,String la,String lo){
        ContentValues cValue = new ContentValues();
        cValue.put("id",id);
        cValue.put("time", time);
        cValue.put("la",la);
        cValue.put("lo", lo);
        db.insert(table, null, cValue);
    }
    public void AP(SQLiteDatabase db,String table,String id,String time){
        ContentValues cValue = new ContentValues();
        cValue.put("id",id);
        cValue.put("time", time);
        db.insert(table, null, cValue);
    }
    public void Delete(SQLiteDatabase db, String table, String message) {
        String sql = "delete from " + table + " where id=" + message;
        db.execSQL(sql);
    }
}
