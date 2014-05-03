package com.example.app;

import java.io.IOException;
import java.util.ArrayList;
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
import android.os.Handler;
import android.os.IBinder;
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
    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...

        new Thread (new Runnable() {
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://1.rtest2.sinaapp.com/chat/receive/");
                String src=getLocal();
                try {
                    // Add your data
                    List<NameValuePair> msgValuePairs = new ArrayList<NameValuePair>(2);
                    msgValuePairs.add(new BasicNameValuePair("number", src));
                    httppost.setEntity(new UrlEncodedFormEntity(msgValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    String strResult = EntityUtils.toString(response.getEntity());

                    boolean if_exists_unread ;
                    Integer sequence = 0;
                    try {
                        JSONObject jsonMsg = new JSONObject(strResult);
                        if_exists_unread = jsonMsg.has("0");
                        //existing unread message
                        if(if_exists_unread){
                            while(jsonMsg.has(sequence.toString())){
                                //save the msg to content provider, and increase sequence number, sequence starts from 0
                                String msgString = jsonMsg.getString(sequence.toString());
                                String recv_src,recv_dest,recv_text,recv_submit_time,recv_forward_time;
                                sequence++;
                                try {
                                    Toast.makeText(getApplicationContext(), "MSG: "+msgString, Toast.LENGTH_SHORT).show();
                                    JSONObject jsonMsgString = new JSONObject(msgString);
                                    recv_src = jsonMsgString.getString("src");
                                    recv_dest = jsonMsgString.getString("dest");
                                    recv_text = jsonMsgString.getString("text");
                                    recv_submit_time = jsonMsgString.getString("submit_time");
                                    recv_forward_time = jsonMsgString.getString("forward_time");
                                    saveMsg(recv_src, recv_dest, recv_text, recv_submit_time, recv_forward_time);
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "failed "+e.toString(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getBaseContext(), "json failed "+e.toString(), Toast.LENGTH_SHORT).show();
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

    public void saveMsg(String recv_src, String recv_dest, String recv_text, String recv_submit_time, String recv_forward_time) {
        //insert the msg to ContentProvider
        try{
        ContentResolver contentResolver = null;
        contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MSGS.MSG._FROM,recv_src);
        values.put(MSGS.MSG._TO,recv_dest);
        values.put(MSGS.MSG._TEXT,recv_text);
        values.put(MSGS.MSG._SENT,recv_submit_time);
        values.put(MSGS.MSG._RECEIVED,recv_forward_time);
        //here goes the insert method;
        contentResolver.insert(MSGS.MSG.MSGS_CONTENT_URI, values);
        //print a notification
        Toast.makeText(getApplicationContext(),"msg saved",Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "save failed "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getLocal() {
        // TODO get local number from provider
//        TelephonyManager phoneMgr=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//        String lineNumber = phoneMgr.getLine1Number().toString();
//        String countryCode = phoneMgr.getNetworkCountryIso().toString();
//        if (countryCode.equals("cn"))
//            countryCode =  "+86";
//        else
//            countryCode =  "+1";
//        String num_with_code = countryCode.concat(lineNumber);
        return "+8615527518807";
    }

    public void onDestroy() {
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }
}
