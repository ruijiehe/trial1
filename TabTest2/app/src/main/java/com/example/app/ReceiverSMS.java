package com.example.app;

import android.annotation.SuppressLint;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
                    sb.append("content:");
                    sb.append(s.getDisplayMessageBody());
                    saveMsg(context,s.getDisplayOriginatingAddress(),getLocal(),s.getDisplayMessageBody(),getGMT(),getGMT());
                }
                Toast.makeText(
                        context,
                        "received: " + sb.toString(),
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
        Toast.makeText(contexts,"msg saved:"+msgUri,Toast.LENGTH_LONG).show();

    }
    public static String getGMT(){
        Date date = new Date();
        Timestamp currentTimestamp = new Timestamp(date.getTime());
        return currentTimestamp.toString();
    }
    private String getLocal() {
        // TODO get local number from provider
        return "+8615527518807";
    }
}
