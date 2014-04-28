package edu.whitworth.sendandrecv.app;

import android.annotation.SuppressLint;
import java.io.IOException;
import java.net.URLEncoder;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CallAndSms extends Activity {

   @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_and_sms);
        setComponent();
        startService(new Intent(this, MyService.class));
        start_recv_service();
    }
//    public String getLocal(){
//        TelephonyManager phoneMgr=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//
//        String lineNumber = phoneMgr.getLine1Number().toString();
//        String countryCode = phoneMgr.getNetworkCountryIso().toString();
//        if (countryCode.equals("cn"))
//            countryCode =  "+86";
//        else
//            countryCode =  "+1";
//        String num_with_code = countryCode.concat(lineNumber);
//
//        TextView localNumberView = (TextView)findViewById(R.id.editText3);
//        localNumberView.setText(num_with_code);
//        //String num_with_code = "+8615527518807";
//        return num_with_code;
//    }
    public void start_recv_service()
    {
        AlarmManager aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(CallAndSms.this,MyService.class);
        final PendingIntent pi = PendingIntent.getService(CallAndSms.this, 0, intent, 0);
        aManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 15000, pi);
        //Toast.makeText(CallAndSms.this, "start service to recv unread", Toast.LENGTH_LONG).show();
    }

    // Start the  service
    public void startNewService() {

        startService(new Intent(this, MyService.class));
    }

    // Stop the  service
    public void stopNewService(View view) {

        stopService(new Intent(this, MyService.class));
    }
    public static  String getLocal(){
        //TODO get local number from content provider
    return "+8615527518807";
    }
    public static String getGMT(){
        Date date = new Date();
        Timestamp currentTimestamp = new Timestamp(date.getTime());
        return currentTimestamp.toString();
    }
    public String addZoneCode(String number){//zone number will be set
        TelephonyManager phoneMgr=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

        String countryCode = phoneMgr.getNetworkCountryIso();
        if (number.startsWith("+"))
            return number;
        else
        if (countryCode.equals("cn"))
            return "+86".concat(number);
        else
            return "+1".concat(number);
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
        contentResolver.insert(MSGS.MSG.MSGS_CONTENT_URI, values);
        //print a notification
        Toast.makeText(CallAndSms.this,"msg saved",Toast.LENGTH_LONG).show();

    }

    public void sendMsg(final String dest_num, final String text_content){//the dest number would be sent to DB for check

                //initialize the message Structure
                String msg_id = "2";
            /*
             * 1 for test on PC
             * 2 for test on phone
             * 0 for common user*/
                String src = getLocal();
                String dest = addZoneCode(dest_num);
                String text = text_content;
                String submit_time = getGMT();
                String forward_time = "2014 04 03 08:49:34";

                //save to local ContentProvider
                saveMsg(src,dest,text,submit_time,forward_time);
    	/*following is sending using HttpClient*/
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://1.rtest2.sinaapp.com/chat/msg/");
            String strResult = "";
            try {
                // Add your data
                List<NameValuePair> msgValuePairs = new ArrayList<NameValuePair>(2);
                msgValuePairs.add(new BasicNameValuePair("msgid", msg_id));
                msgValuePairs.add(new BasicNameValuePair("src", src));
                msgValuePairs.add(new BasicNameValuePair("dest", dest));
                msgValuePairs.add(new BasicNameValuePair("text", URLEncoder.encode(text, "UTF-8")));
                msgValuePairs.add(new BasicNameValuePair("submit_time", submit_time));
                msgValuePairs.add(new BasicNameValuePair("forward_time", forward_time));
                httppost.setEntity(new UrlEncodedFormEntity(msgValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                strResult = EntityUtils.toString(response.getEntity());
    //	        Toast.makeText(CallAndSms.this, "received: "+strResult, Toast.LENGTH_SHORT).show();
            } catch (ClientProtocolException e) {
                Toast.makeText(CallAndSms.this, "failed "+e.toString(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(CallAndSms.this, "failed "+e.toString(), Toast.LENGTH_SHORT).show();
            }
            ///////////////////////
	    /*parsing responding json and decide whether SMS or NET*/
                String PATH ="";
                try {
                    JSONObject jsonPath = new JSONObject(strResult);
                    PATH = jsonPath.getString("PATH");
//			Toast.makeText(CallAndSms.this, "PATH: "+PATH, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(CallAndSms.this, "failed "+e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
	    /*if SMS*/
                try {
                    if (PATH.equals("NET")){
                        Toast.makeText(	CallAndSms.this,"Sent Via Network",Toast.LENGTH_SHORT ).show();

                    }
                    else {
                        SmsManager sms = SmsManager.getDefault();
                        List<String> texts = sms.divideMessage(text);
                        for (String subtext : texts) {
                            //the destination is got from text1
                            sms.sendTextMessage(dest, null, subtext, null, null);
                        }
                        // note: not checked success or failure yet
                        Toast.makeText(	CallAndSms.this,"Sent Via SMS",Toast.LENGTH_SHORT ).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(CallAndSms.this, "failed " + e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
    }

    private void setComponent() {
        Button bt2 = (Button) findViewById(R.id.Button02);

        bt2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                //get the message content from text2
                TextView messagContentView = (TextView)findViewById(R.id.editText2);
                String smsContent = messagContentView.getText().toString();
                //get the destination number from text1
                TextView destContentView = (TextView)findViewById(R.id.editText1);
                String destNumber = destContentView.getText().toString();

                //send to server and save to local ContentProvider
                sendMsg(destNumber,smsContent);
            }
        });
    }
}
