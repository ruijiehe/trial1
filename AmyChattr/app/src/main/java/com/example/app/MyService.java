package com.example.app;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.InputFilter.LengthFilter;
import android.widget.Toast;

/**
 * Created by Administrator on 14-4-19.
 */
public class MyService extends Service {
    public MyService() {
    }
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onCreate() {
        //Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
    }
    public void ShowToastInIntentService(final String sText)
    {  final Context MyContext = this;
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {  @Override public void run()
            {  Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_LONG);
                toast1.show();
            }
        });
    };
    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...

        new Thread (new Runnable() {
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(LoginActivity.recv_Url);
                String src=getLocal();
                try {
                    // Add your data
                    List<NameValuePair> msgValuePairs = new ArrayList<NameValuePair>(2);
                    msgValuePairs.add(new BasicNameValuePair("number", src));
                    httppost.setEntity(new UrlEncodedFormEntity(msgValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    String strResult = EntityUtils.toString(response.getEntity());
                    //ShowToastInIntentService("received: ".concat(strResult));
                    boolean if_exists_unread ;
                    Integer sequence = 0;
                    try {
                        JSONObject jsonMsg = new JSONObject(strResult);
                        if_exists_unread = jsonMsg.has("0");
                        //existing unread message
                        while(jsonMsg.has(sequence.toString())){
                            //save the msg to content provider, and increase sequence number
                            JSONObject msgString = jsonMsg.getJSONObject(sequence.toString());
                            sequence++;
                            //ShowToastInIntentService("received: ".concat(msgString));
                            String recv_src,recv_dest,recv_text,recv_submit_time,recv_forward_time;
                            try {
                                JSONObject jsonMsgString = msgString;
                                recv_src = jsonMsgString.getString("src");
                                recv_dest = jsonMsgString.getString("dest");
                                recv_text = jsonMsgString.getString("text");
                                recv_submit_time = jsonMsgString.getString("submit_time");
                                recv_forward_time = jsonMsgString.getString("forward_time");
                                saveMsg(recv_src, recv_dest, recv_text, recv_submit_time, recv_forward_time);
                                ShowToastInIntentService("from: ".concat(recv_src).concat("\n").concat(recv_text));
                            } catch (JSONException e) {
                                //Toast.makeText(CallAndSms.this, "failed "+e.toString(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }
                        //			Toast.makeText(CallAndSms.this, "PATH: "+PATH, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        //Toast.makeText(CallAndSms.this, "failed "+e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } catch (ClientProtocolException e) {
                    //Toast.makeText(this, "receive failed "+e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    //Toast.makeText(this, "receive failed "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }


        }).start();

        //Toast.makeText(this, " Service Started: "+strResult, Toast.LENGTH_LONG).show();

    }

    public void saveMsg(String src, String dest, String text, String submit_time, String forward_time){
        //insert the msg to ContentProvider
        ContentResolver contentResolver = null;
        contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MSGS.MSG._FROM,src);
        values.put(MSGS.MSG._TO,dest);
        values.put(MSGS.MSG._TEXT,text);
        values.put(MSGS.MSG._SENT,submit_time);
        values.put(MSGS.MSG._RECEIVED,forward_time);
        //here goes the insert method;
        Uri msgUri = contentResolver.insert(MSGS.MSG.MSGS_CONTENT_URI, values);
        //print a notification
        ShowToastInIntentService("msg saved:"+msgUri);

    }

    public String getLocal(){
        final Context context = this;
        final String[] number = {""};
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {  @Override public void run(){
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
                        //Toast.makeText(context, "null query", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (mCursor.moveToFirst()) { // if Cursor is not empty
                            String num = mCursor.getString(mCursor.getColumnIndex(USERS.USER._NUM));
                            //Toast.makeText(context, "Num: " + num, Toast.LENGTH_LONG).show();
                            number[0] = num;
                        }
                        else {
                            // Cursor is empty
                           // Toast.makeText(context, "no num found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(context, "query error"+e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                //Toast.makeText(context,"checking error"+ e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            }
        });
        return number[0];
    }
    public static String getGMT(){
        Date date = new Date();
        Timestamp currentTimestamp = new Timestamp(date.getTime());
        return currentTimestamp.toString();
    }
    public void onDestroy() {
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }
}