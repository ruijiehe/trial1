package edu.whitworth.app;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConversationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // get listview object
        final ListView listView = (ListView) findViewById(R.id.conversation_list);

        // get the array of values to put into the list
        final String[] values = new String[]{
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Donec ut sem tortor",
                "Nam molestie erat sit amet mauris iaculis commodo ut in neque",
                "Curabitur id sollicitudin purus, nec pellentesque justo",
                "Fusce vulputate metus nisl, sollicitudin eleifend nisl placerat et",
                "Nunc tempus arcu turpis, vitae eleifend purus dictum vel",
                "Duis sodales eu leo auctor egestas",
                "Duis placerat id nibh et bibendum",
                "Etiam feugiat lorem nisi, id tempus felis auctor quis",
                "Vestibulum tincidunt ac nisl gravida iaculis",
                "Nulla ultrices est sit amet nisl mattis",
                "Quisque nec ipsum vitae risus feugiat accumsan"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.activity_list_item, android.R.id.list, values);


        // Something's wrong here
        /*listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemPosition = i;

                String itemValue = (String) listView.getItemAtPosition(i);

                Toast.makeText(getApplicationContext(),
                        "Position: "+itemPosition+" ListItem: " + itemValue, Toast.LENGTH_LONG).show();
            }
        });
        */
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //TextView textView = new TextView(this);
        //textView.setTextSize(40);
        //textView.setText(message);

        //setContentView(textView);
    }


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
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
            return rootView;
        }
    }

}
