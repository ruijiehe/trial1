package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends ActionBarActivity {

    public static List<String> lines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .add(R.id.container, new ComposeFragment())
                            .commit();
        }
        lines = Arrays.asList(getResources().getStringArray(R.array.dummy_convo));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
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
     * This function will send the message to the database
     * (or the string-array, for now)
     */
    public void send_message(View view) {
        Toast.makeText(getApplicationContext(), "Hello world!", Toast.LENGTH_SHORT).show();
        EditText et = (EditText)findViewById(R.id.compose_reply);
        String temp = et.getText().toString();

        /* The following line crashed.  I don't know how else to fix this problem. */
        //lines.add(temp);

        // This line should tell the listview to update with the new lines array
        // I haven't tested it yet.
        adapter.notifyDataSetChanged();

    }

    /**
     * This fragment holds the composition box and the 'send' button
     */
    public static class ComposeFragment extends Fragment{
        public ComposeFragment(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.compose_reply, container, false);
            return rootView;
        }
    }

    public static ArrayAdapter<String> adapter;
    /**
     * A placeholder fragment containing the chat bubbles list
     */
    public static class PlaceholderFragment extends ListFragment {

        public PlaceholderFragment() {
        }

        /**
         * This function sets the margins for the listview fragment
         * I call it in onViewCreated because the listview was being a little brat
         * @param v
         * @param l
         * @param t
         * @param r
         * @param b
         */
        public static void setMargins (View v, int l, int t, int r, int b) {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            // Set up the listview with the dummy information and the chat layout
            //List<String> lines = new ArrayList<String>();
            //lines = Arrays.asList(getResources().getStringArray(R.array.dummy_convo));

            adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.chat_layout, lines);
            setListAdapter(adapter);
            ListView lv = getListView();

            // top margin = 150 is a large enough distance to prevent the two fragments from overlapping
            // it looks fine on the tablet, but it may not on a phone or other device
            /* HACK-Y FIX */
            setMargins(view, 0, 150, 0, 0);

            // This gets rid of the listview dividers between each item
            lv.setDivider(null);

            // TODO:
            // Make the listview items unclickable
            // Include MSG class
            // Change chat bubble image according to sender
            // Change text-justification according to sender
            // Fix send_message function
            // Fix contacts tab to show contacts_layout


        }
    }

}
