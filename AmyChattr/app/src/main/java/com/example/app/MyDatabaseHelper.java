package com.example.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 14-4-19.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "chattrdb";
    public static final int DATABASE_VERSION = 1;
    public final String CREATE_TABLE_MSGS = "create table msgs(_id integer primary key autoincrement,src,dest,text,submit_time,forward_time)";
    public final String CREATE_TABLE_USERS = "create table users(_id integer primary key autoincrement,num,zone,subnum,name,flag)";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_MSGS);
        db.execSQL(CREATE_TABLE_USERS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){

        Log.w(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS msgs");
        db.execSQL("DROP TABLE IF EXISTS users");

        onCreate(db);
    }
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }
}
