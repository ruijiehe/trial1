package com.example.app;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    private static int SPLASH_TIME_OUT = 500;
    public static String msg_Url = "http://1.rtest2.sinaapp.com/chat/msg/";
    public static String reg_Url = "http://1.rtest2.sinaapp.com/chat/register/";
    public static String recv_Url = "http://1.rtest2.sinaapp.com/chat/receive/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if(true){
        //if(false){
        if(if_user_exist()){
           // Toast.makeText(this, "user exists", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
//            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.container, new PlaceholderFragment())
//                        .commit();
//            }
        }
        else{//when no user exists, login and register
            Toast.makeText(this, "first Launch", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_login);
            setLogin(savedInstanceState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }

    public boolean if_user_exist(){
        try{
            Toast.makeText(this, "checking if exist ", Toast.LENGTH_SHORT).show();
            ContentResolver contentResolver = null;
            contentResolver = getContentResolver();
            String [] mProjection = {
                    USERS.USER._FLAG,
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
                    Toast.makeText(this, "null query", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    Toast.makeText(this, "user exists"+mCursor.getCount(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            catch (Exception e){
                Toast.makeText(this, "query error"+e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this,"checking error"+ e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return false;
    }

    public void setLogin(final Bundle savedInstanceState){
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                //get the zone content from zone
                TextView zoneContentView = (TextView)findViewById(R.id.zone);
                String zone = zoneContentView.getText().toString();
                //get the subnum content from subnum
                TextView subnumContentView = (TextView)findViewById(R.id.subnum);
                String subnum = subnumContentView.getText().toString();
                //get the name content from name
                TextView nameContentView = (TextView)findViewById(R.id.name);
                String name = nameContentView.getText().toString();
                String number = zone.concat(subnum);//generate a full number like +8615527518807
                if(!(zone.startsWith("+"))||number.equals("")||name.equals("")){//input error
                    Toast.makeText(v.getContext(), "Invalidate Input String", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendUser(name,number);
                    saveUser(name,zone,subnum,number);
                    //Switch Activity after user registered and saved
                    new Handler().postDelayed(new Runnable() {
                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */
                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            // Start your app main activity
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);

                            // close this activity
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }
            }
        });
    }
    public void sendUser(final String name,final String number){//send the name and number to server
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(LoginActivity.reg_Url);
        String strResult = "";
        try {
            // Add your data
            List<NameValuePair> msgValuePairs = new ArrayList<NameValuePair>(2);
            msgValuePairs.add(new BasicNameValuePair("name", name));
            msgValuePairs.add(new BasicNameValuePair("number", number));
            httppost.setEntity(new UrlEncodedFormEntity(msgValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            strResult = EntityUtils.toString(response.getEntity());
            Toast.makeText(LoginActivity.this, "received: "+strResult, Toast.LENGTH_SHORT).show();
        } catch (ClientProtocolException e) {
            Toast.makeText(LoginActivity.this, "failed "+e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(LoginActivity.this, "failed "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void saveUser(final String name, final String zone, final String subnum,final String num){
        ContentResolver contentResolver = null;
        contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(USERS.USER._NUM,num);
        values.put(USERS.USER._ZONE,zone);
        values.put(USERS.USER._SUBNUM,subnum);
        values.put(USERS.USER._NAME,name);
        values.put(USERS.USER._FLAG,"1");
        //here goes the insert method;
        try{
            Uri userUri = contentResolver.insert(USERS.USER.USERS_CONTENT_URI, values);
            Toast.makeText(LoginActivity.this,"user saved:"+ userUri,Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(LoginActivity.this,"user save faild:"+e.toString(),Toast.LENGTH_LONG).show();
        }
        //print a notification

    }

}
