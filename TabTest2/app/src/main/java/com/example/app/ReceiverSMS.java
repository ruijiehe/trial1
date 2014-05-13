package com.example.app;

import android.annotation.SuppressLint;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 14-4-18.
 */
@SuppressLint("NewApi")
public class ReceiverSMS extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(
                "android.provider.Telephony.SMS_RECEIVED")) {
            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    msgs[i] = SmsMessage
                            .createFromPdu((byte[]) pdus[i]);
                }
                for (SmsMessage s : msgs) {
                    sb.append("received");
                    sb.append(s.getDisplayOriginatingAddress());
                    sb.append("\ncontent:");
                    sb.append(s.getDisplayMessageBody());
                    saveMsg(context,s.getDisplayOriginatingAddress(),getLocal(context),s.getDisplayMessageBody(),getGMT(),getGMT());
                }
                Toast.makeText(
                        context,
                        sb.toString(),
                        Toast.LENGTH_LONG).show();
            }

        }
    }
    public void saveMsg(Context contexts,String src, String dest, String text, String submit_time, String forward_time){
        //insert the msg to ContentProvider
        ContentResolver contentResolver = null;
        contentResolver = contexts.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MSGS.MSG._FROM,src);
        values.put(MSGS.MSG._TO,dest);
        values.put(MSGS.MSG._TEXT,text);
        values.put(MSGS.MSG._SENT,submit_time);
        values.put(MSGS.MSG._RECEIVED,forward_time);
        //here goes the insert method;
        Uri msgUri = contentResolver.insert(MSGS.MSG.MSGS_CONTENT_URI, values);
        //print a notification
        //Toast.makeText(contexts,"msg saved:"+msgUri,Toast.LENGTH_LONG).show();

    }
    public static String getGMT(){
        Date date = new Date();
        Timestamp currentTimestamp = new Timestamp(date.getTime());
        return currentTimestamp.toString();
    }
    public String getLocal(Context context){
        try{
            //Toast.makeText(context, "accessing user number", Toast.LENGTH_SHORT).show();
            ContentResolver contentResolver = null;
            contentResolver = context.getContentResolver();
            String [] mProjection = {
                    USERS.USER._NUM,
            };
            String mSelection = "flag like ?";
            String[] mSelectionArgs = {"1"};
            final Cursor mCursor;
            //here goes the insert method;
            try {
                mCursor = contentResolver.query(
                        USERS.USER.USERS_CONTENT_URI,
                        mProjection,
                        mSelection,
                        mSelectionArgs,
                        null);
                if(0 == mCursor.getCount()){//return 0,and ready for an insert from app_user
                   // Toast.makeText(context, "null query", Toast.LENGTH_SHORT).show();
                    return "";
                }
                else {
                    if (mCursor.moveToFirst()) { // if Cursor is not empty
                        String num = mCursor.getString(mCursor.getColumnIndex(USERS.USER._NUM));
                        //Toast.makeText(context, "Num: " + num, Toast.LENGTH_LONG).show();
                        return num;
                    }
                    else {
                        // Cursor is empty
                        //Toast.makeText(context, "no num found", Toast.LENGTH_SHORT).show();
                        return "";
                    }
                }
            }
            catch (Exception e){
               // Toast.makeText(context, "query error"+e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            //Toast.makeText(context,"checking error"+ e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return "";
    }
}
