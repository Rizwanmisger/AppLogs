package com.example.rizwan.apphistorydemo;//add package

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;



import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class RecentTweetsService extends IntentService {


	public final static String TWIT_KEY = "jy1qYywYg28Wqi1kQGVsXpXPA";

	public final static String TWIT_SECRET = "7pcZRwU0JhcGVlL9lp1rfbA7lQ4HdvyTDrIz2JXGHJ7qFXIXPA";

	private String LOG_TAG = "RecentTweetsService";

	private SharedPreferences mPrefs;

	private Handler mHandler;

	private DbHelper mdbHelper;

	private SQLiteDatabase mDB;

	private TimelineUpdater mUpdater;
	/**twitter object*/
	private Twitter mTwitter;

	private static int mins = 1;
	private static final long FETCH_DELAY = mins * (60*1000);

	public RecentTweetsService() {
		super("RecentTweetsService");

	}
	@Override
	public void onCreate() {
		super.onCreate();
		mPrefs = getSharedPreferences("TweetPrefs",0);
		mdbHelper = new DbHelper(this);
		mDB = mdbHelper.getWritableDatabase();
		try{
			String userToken = mPrefs.getString("user_token", null);
			String userSecret = mPrefs.getString("user_secret", null);
			Log.v("SERVICE  ", "token here "+userToken);

			Configuration twitConf = new ConfigurationBuilder()
					.setOAuthConsumerKey(TWIT_KEY)
					.setOAuthConsumerSecret(TWIT_SECRET)
					.setOAuthAccessToken(userToken)
					.setOAuthAccessTokenSecret(userSecret)
					.build();

			mTwitter = new TwitterFactory(twitConf).getInstance();
		}catch(Exception e)
		{
			Log.v("ONCREATE SERVICE","ERROR IN THIS METHOD "+e);
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mHandler = new Handler();
		mUpdater = new TimelineUpdater();
		//mHandler.post(mUpdater);
		new Thread(mUpdater).run();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("Service "," DESTROYED");
	//	mHandler.removeCallbacks(mUpdater);
		//mDB.close();
	}


	class TimelineUpdater implements Runnable
	{
		//run method
		public void run() 
		{
			Log.v("Runnable"," CALL TO RUN");
			//check for updates - assume none
			boolean statusChanges = false;
			try 
			{

				List<Status> homeTimeline = mTwitter.getUserTimeline();
				for (Status statusUpdate : homeTimeline) 
				{
					ContentValues timelineValues = mdbHelper.getValues(statusUpdate);
					mDB.insertOrThrow("home", null, timelineValues);
					statusChanges = true;
				}
			} 
			catch (Exception te) { Log.e(LOG_TAG, "Exception: " + te);
			}
			if (statusChanges) 
			{
				sendBroadcast(new Intent("TWITTER_UPDATES"));
			}
			mHandler.postDelayed(this, FETCH_DELAY);
		}
	}
}
