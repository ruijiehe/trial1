package edu.whitworth.chattr1.app;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        startService(new Intent(this, MyService.class));
        start_recv_service();
    }
    public void start_recv_service()
    {
        AlarmManager aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this,MyService.class);
        final PendingIntent pi = PendingIntent.getService(MainActivity.this, 0, intent, 0);
        aManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 15000, pi);
        //Toast.makeText(CallAndSms.this, "start service to recv unread", Toast.LENGTH_LONG).show();
    }

    // Start the  service
    public void startNewService() {

        startService(new Intent(this, MyService.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    //methods for tab selecting
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            PlaceholderFragment PHF = PlaceholderFragment.newInstance(position + 1);
            return PHF;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
             PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView;
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){//make it a composing page
                rootView = inflater.inflate(R.layout.activity_call_and_sms, container, false);
                setComponent(rootView);


            }
            else{
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            }
            return rootView;
        }
        //methods for composing fragment
        public void setComponent(final View rootView) {
            Button bt2 = (Button) rootView.findViewById(R.id.Button02);
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get the message content from text2
                    final TextView messagContentView = (TextView)rootView.findViewById(R.id.editText2);
                    String smsContent = messagContentView.getText().toString();
                    //get the destination number from text1
                    final TextView destContentView = (TextView)rootView.findViewById(R.id.editText1);
                    String destNumber = destContentView.getText().toString();
                    //send to server and save to local ContentProvider
                    sendMsg(destNumber,smsContent);
                }
            });
        }
        public String getLocal(){
            //TODO get local number from content provider
            return "+8615527518807";
        }
        public String getGMT(){
            Date date = new Date();
            Timestamp currentTimestamp = new Timestamp(date.getTime());
            return currentTimestamp.toString();
        }
        public String addZoneCode(String number){//zone number will be set
            //TelephonyManager phoneMgr=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            //String countryCode = phoneMgr.getNetworkCountryIso();
            String localNum = getLocal();
            if (number.startsWith("+"))
                return number;
            else
            if (localNum.startsWith("+86"))
                return "+86".concat(number);
            else
                return "+1".concat(number);
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
            contentResolver.insert(MSGS.MSG.MSGS_CONTENT_URI, values);
            //print a notification
            Toast.makeText(this.getActivity(),"msg saved",Toast.LENGTH_LONG).show();

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
            saveMsg(this.getActivity(),src,dest,text,submit_time,forward_time);
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
                Toast.makeText(this.getActivity(), "failed " + e.toString(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this.getActivity(), "failed "+e.toString(), Toast.LENGTH_SHORT).show();
            }
            ///////////////////////
	    /*parsing responding json and decide whether SMS or NET*/
            String PATH ="";
            try {
                JSONObject jsonPath = new JSONObject(strResult);
                PATH = jsonPath.getString("PATH");
//			Toast.makeText(CallAndSms.this, "PATH: "+PATH, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(this.getActivity(), "failed "+e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
	    /*if SMS*/
            try {
                if (PATH.equals("NET")){
                    Toast.makeText(	this.getActivity(),"Sent Via Network",Toast.LENGTH_SHORT ).show();

                }
                else {
                    SmsManager sms = SmsManager.getDefault();
                    List<String> texts = sms.divideMessage(text);
                    for (String subtext : texts) {
                        //the destination is got from text1
                        sms.sendTextMessage(dest, null, subtext, null, null);
                    }
                    // note: not checked success or failure yet
                    Toast.makeText(	this.getActivity(),"Sent Via SMS",Toast.LENGTH_SHORT ).show();
                }
            } catch (Exception e) {
                Toast.makeText(this.getActivity(), "failed " + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

}
