package edu.whitworth.app;

import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ConversationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        /*setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(), R.array.tut_titles, R.layout.conversation_list));

        final String[] links = getResources().getStringArray(R.array.tut_links);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String content = links[i];
                Intent showContent = new Intent(getApplicationContext(),
                        Chat.class); // this is where we go to the CHAT activity
                showContent.setData(Uri.parse(content));
                startActivity(showContent);
            }
        });*/

    }

    // The functions below I am not using yet!

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.conversation, menu);
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
    public class PlaceholderFragment extends ListFragment {

        public PlaceholderFragment() {
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);
            setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(), R.array.tut_titles, R.layout.conversation_list));

            final String[] links = getResources().getStringArray(R.array.tut_links);

            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String content = links[i];
                    Intent showContent = new Intent(getApplicationContext(),
                            Chat.class); // this is where we go to the CHAT activity
                    showContent.setData(Uri.parse(content));
                    startActivity(showContent);
                }
            });
        }
        //@Override
        /*
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);

            setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(), R.array.tut_titles, R.layout.conversation_list));

            final String[] links = getResources().getStringArray(R.array.tut_links);

            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String content = links[i];
                    Intent showContent = new Intent(getApplicationContext(),
                            Chat.class); // this is where we go to the CHAT activity
                    showContent.setData(Uri.parse(content));
                    startActivity(showContent);
                }
            });
            return rootView;
        }
        */
    }

}
