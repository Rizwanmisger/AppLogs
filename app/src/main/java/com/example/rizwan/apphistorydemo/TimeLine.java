package com.example.rizwan.apphistorydemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TimeLine extends AppCompatActivity  {

    Activity activity;

    private final static String TWITTER_CONSUMER_KEY = "jy1qYywYg28Wqi1kQGVsXpXPA";

    private final static String TWITTER_CONSUMER_SECRET = "7pcZRwU0JhcGVlL9lp1rfbA7lQ4HdvyTDrIz2JXGHJ7qFXIXPA";
    private String LOG_TAG = "TimeLine";
    private Twitter mTwitter;
    private ListView homeTimeline;
    private DbHelper mHelper;
    private SQLiteDatabase mDB;
    private Cursor mCursor;
    private UpdateAdapter mAdapter;
    private BroadcastReceiver mStatusReceiver;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_time_line);

        context = this.getApplicationContext();

        new loginToTwitter().execute();
        setupTimeline();
    }



    private void setupTimeline() {

        try {

            homeTimeline = (ListView) findViewById(R.id.listView);
            mHelper = new DbHelper(this);
            mDB = mHelper.getReadableDatabase();
            mCursor = mDB.query("home", null, null, null, null, null, "update_time DESC");
            startManagingCursor(mCursor);
            mAdapter = new UpdateAdapter(this, mCursor);
            homeTimeline.setAdapter(mAdapter);
            mStatusReceiver = new TwitterUpdateReceiver();
            registerReceiver(mStatusReceiver, new IntentFilter("TWITTER_UPDATES"));
            try{
            Intent intent = new Intent(this, RecentTweetsService.class);
            startService(intent);}
            catch(Exception e)
            {
                Log.v("ERRRRRRROR ","UNABLE TO OPEN YOUR SERVICE " );
            }
        } catch (Exception te) {
            Log.e(LOG_TAG, "Failed to fetch timeline: " + te.getMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();


    }

    public class TwitterUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int rowLimit = 100;
            if (DatabaseUtils.queryNumEntries(mDB, "home") > rowLimit) {
                String deleteQuery = "DELETE FROM home WHERE " + BaseColumns._ID + " NOT IN " +
                        "(SELECT " + BaseColumns._ID + " FROM home ORDER BY " + "update_time DESC " +
                        "limit " + rowLimit + ")";
                mDB.execSQL(deleteQuery);
            }

            mCursor = mDB.query("home", null, null, null, null, null, "update_time DESC");
            startManagingCursor(mCursor);
            mAdapter = new UpdateAdapter(context, mCursor);
            homeTimeline.setAdapter(mAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mStatusReceiver);
            mDB.close();
        } catch (Exception se) {
            Log.e(LOG_TAG, "unable to stop service or receiver");
        }
    }

    private class loginToTwitter extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

            SharedPreferences mSharedPreferences = getSharedPreferences("TweetPrefs", 0);
            String access_token = mSharedPreferences.getString("user_token", "");
            // Access Token Secret
            String access_token_secret = mSharedPreferences.getString("user_secret", "");
            Log.v("SP TOKENS","Access "+access_token) ;
            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            mTwitter = new TwitterFactory(builder.build()).getInstance(accessToken);

            return null;
        }
    }

}



