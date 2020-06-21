package com.example.deepclass.databasectrl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataQuery {

    private static final String dbName = "question.db";
    private static final String tableName="choice_question";
    private Context context;

    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;

    public DataQuery(Context context){
        myDBHelper = new MyDBHelper(context, "question.db", null, 3);
    }

    public int getQstNum(){
        db=myDBHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName,null,null,null,null,null,null);
        int questionNum = cursor.getCount();
        cursor.close();
        db.close();
        return questionNum;
    }


    public String[] queryData(int i) {
        db=myDBHelper.getReadableDatabase();
        String[] results = new String[9];
        Cursor cursor=db.query(tableName,null,null,null,null,null,null);
        if(cursor.getCount()==0){
            showMsg(context,"当前没有试题");
            return null;
        }
        else{
            cursor.moveToFirst();
            int count = cursor.getCount();
            if(i < count) {
                cursor.moveToPosition(i);
                results[0] = String.valueOf(i + 1);
                results[1] = cursor.getString(1);
                results[2] = cursor.getString(2);
                results[3] = cursor.getString(3);
                results[4] = cursor.getString(4);
                results[5] = cursor.getString(5);
                results[6] = cursor.getString(6);
                results[7] = cursor.getString(7);
                results[8]=  cursor.getString(8);
            }
        }
        cursor.close();
        db.close();

        return results;

    }

    private void showMsg(Context context , String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, dbName, null, 3);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists "+tableName+"("
                    +"id integer ,"
                    +"title varchar,"
                    +"description varchar,"
                    +"time varchar,"
                    +"a varchar,"
                    +"b varchar,"
                    +"c varchar,"
                    +"d varchar,"
                    +"answer varchar)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists "+tableName);
            this.onCreate(db);
        }
    }
}
