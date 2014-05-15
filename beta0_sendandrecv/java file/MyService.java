package edu.whitworth.sendandrecv.app;

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

import android.app.Service;
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
                    //Toast.makeText(this, "received: "+strResult, Toast.LENGTH_SHORT).show();
                } catch (ClientProtocolException e) {
                    //Toast.makeText(this, "receive failed "+e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    //Toast.makeText(this, "receive failed "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

        //Toast.makeText(this, " Service Started: "+strResult, Toast.LENGTH_LONG).show();

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
