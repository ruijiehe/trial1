package edu.whitworth.sendandrecv.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 14-4-19.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    final String CREATE_TABLE_SQL = "create table msgs(_id integer primary key autoincrement,src,dest,text,submit_time,forward_time)";
    public MyDatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_SQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
        System.out.println("on update called"+oldVersion+"--->"+newVersion);
    }
}
