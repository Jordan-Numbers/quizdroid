package edu.washington.j75smith.quizdroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private String source;
    private PendingIntent alarmIntent = null;
    private BroadcastReceiver alarmReceiver;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the QuizApp and domain topics
        QuizApp app = (QuizApp) getApplication();
        List<topic> topics = app.getAllTopics();

        //get the buttons
        Button btn_one = (Button) this.findViewById(R.id.quiz_one);
        Button btn_two = (Button) this.findViewById(R.id.quiz_two);
        Button btn_three = (Button) this.findViewById(R.id.quiz_three);

        //set button text
        btn_one.setText(topics.get(0).getTitle());
        btn_two.setText(topics.get(1).getTitle());
        btn_three.setText(topics.get(2).getTitle());

        // listen to the buttons
        OnClickListener listener = new mainOnClickListener(topics);
        btn_one.setOnClickListener(listener);
        btn_two.setOnClickListener(listener);
        btn_three.setOnClickListener(listener);
    }

    @Override
    protected void onStart(){
        super.onStart();
        int interval;

        //get prefs
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        //define defaults
        String defSource = (String) getText(R.string.source_one);
        int defTime = 600;

        if(sharedPref.getAll() == null){
            //user has no prefs
            //set to defaults

            //locally
            this.source = defSource;
            interval = defTime;

            //and in prefs
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("when", interval);
            editor.putString("URl", this.source);
            editor.apply();

        } else {
            //get the users preferred update
            this.source = sharedPref.getString("URL", defSource);
            interval = sharedPref.getInt("when", defTime);
        }

        //Get the manager
       this.manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //init our receiver
        this.alarmReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(MainActivity.this, source, Toast.LENGTH_SHORT).show();
            }
        };

        //register our receiver
        registerReceiver(this.alarmReceiver, new IntentFilter("edu.washington.j75smith.quizdroid"));

        //make the pending intent and its intent
        Intent stuffing = new Intent();
        stuffing.setAction("edu.washington.j75smith.awty");
        alarmIntent = PendingIntent.getBroadcast(this, 0, stuffing, 0);

        int millis = Math.round(interval * 60000);
        manager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + millis, millis, alarmIntent);

        Log.i("QUIZDROID_onStart", "interval " + interval + " URL " + this.source);
    }

    class mainOnClickListener implements OnClickListener {

        List<topic> topics;

        mainOnClickListener(List<topic> topics){
            this.topics = topics;
        }

        @Override
        public void onClick(View v) {
            topic selectedTopic;
            //which one was selected?
            switch (v.getId()) {
                case R.id.quiz_one:
                    selectedTopic = topics.get(0);
                    break;

                case R.id.quiz_two:
                    selectedTopic = topics.get(1);
                    break;

                case R.id.quiz_three:
                    selectedTopic = topics.get(2);
                    break;

                default:
                    selectedTopic = topics.get(0);
                    break;
            }

            Intent next = new Intent(MainActivity.this, TopicActivity.class);
            next.putExtra("topic", selectedTopic);
            startActivity(next);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_prefs) {
            Intent pref = new Intent(MainActivity.this, PreferencesActivity.class);
            startActivity(pref);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //stop sending
        manager.cancel(alarmIntent);
        alarmIntent.cancel();
        unregisterReceiver(this.alarmReceiver);
    }
}
