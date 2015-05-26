package edu.washington.j75smith.quizdroid;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.List;

/*
*
*
*
* */

public class MainActivity extends ActionBarActivity {

    private String source;
    private BroadcastReceiver downloadReceiver;
    private PendingIntent alarmIntent = null;
    private BroadcastReceiver alarmReceiver;
    private AlarmManager alarmManager;
    private DownloadManager downloadManager;
    private List<topic> topics;
    private Long downloadRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the QuizApp and domain topics
        final QuizApp app = (QuizApp) getApplication();
        this.topics = app.getAllTopics();

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

        //Get the manager
        this.alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //init our receiver
        this.alarmReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Get Connection info
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                int isAirplane;

                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                    isAirplane = Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON,0);
                } else{
                    isAirplane = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,0);
                }

                //Check connection info
                if (isAirplane == 1) {

                    //If Airplane send alert(turn off airplane)
                    planeDialog();

                } else if (activeNetwork != null && activeNetwork.isConnected()) {
                    //if connected get to downloading

                    //get our downloadManager
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                    //create our request
                    Uri fileSource = Uri.parse(source);

                    DownloadManager.Request request = new DownloadManager.Request(fileSource);

                    //into this directory ***failed here***
//                    request.setDestinationInExternalFilesDir(context, app.getApplicationContext().getFilesDir().getPath(), "questions.json");

                    //get the id for this request
                    downloadRequest = downloadManager.enqueue(request);
                    Log.i("QuizDroid", "Update Download Successful!");

                } else {

                    //If 0 bars send alert(No internet access)
                    noServiceDialog();
                }
            }
        };

        //init downloadreciever
        this.downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //what do we have
                long which = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);

                if (which == downloadRequest) {

                    //grab the downloadRequest
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(which);
                    Cursor cursor = downloadManager.query(query);

                    //make sure it isnt empty (it shouldnt)
                    if (cursor.moveToFirst()) {
                        int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusIndex)) {

                            //If fails send alert(Retry now or later)
                            failDialog();

                        } else if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
                            //It worked!!
                            //update domains?
                            app.refreshRepository();
                            topics = app.getAllTopics();
                        } else {
                            Log.i("Quizdroid", "Update is in progress");
                        }
                    }
                }
            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();
        int interval;

        //get prefs
        SharedPreferences sharedPref = this.getSharedPreferences("quizdroid", Context.MODE_PRIVATE);

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

        //make the pending intent and its intent
        Intent stuffing = new Intent();
        stuffing.setAction("edu.washington.j75smith.quizdroid");
        alarmIntent = PendingIntent.getBroadcast(this, 0, stuffing, 0);

        int millis = Math.round(interval * 60000);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + millis, millis, alarmIntent);

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

    private void failDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("QuizDroid Update Download Failed");
        alertDialogBuilder.setPositiveButton("Retry Now",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //send a request to download now

                        //make the pending intent and its intent
                        Intent stuffing = new Intent();
                        stuffing.setAction("edu.washington.j75smith.quizdroid");
                        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, stuffing, 0);

                        //send the pending intent now
                        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), alarmIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Retry Later",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //do nothing
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void planeDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(
                "Cannot Update QuizDroid in Airplane Mode. Would you like to turn off Airplane Mode?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS), 0);
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //do nothing
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void noServiceDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("No network Connection found. Unable to download QuizDroid update.");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //register our receivers
        registerReceiver(this.alarmReceiver, new IntentFilter("edu.washington.j75smith.quizdroid"));
        registerReceiver(this.downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onPause(){
        super.onPause();
        //unregister recievers
        unregisterReceiver(this.downloadReceiver);
        unregisterReceiver(this.alarmReceiver);
    }

    @Override
    protected void onDestroy(){
        //stop sending
        this.alarmManager.cancel(this.alarmIntent);
        this.alarmIntent.cancel();
    }
}
