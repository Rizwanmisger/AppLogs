package com.example.rizwan.apphistorydemo;

//import twitter4j.Status;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import twitter4j.Status;

/**
 * Home Timeline database - the main db for the app
 * 
 * - stores updates for the user's home timeline
 * - each record has ID, user screen name, tweet text, time sent and profile image URL
 */
public class DbHelper extends SQLiteOpenHelper {


	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "home1.db";

	private static final String HOME_COL = BaseColumns._ID;

	private static final String UPDATE_COL = "update_text";

	private static final String USER_COL = "user_screen";

	private static final String TIME_COL = "update_time";




	private static final String DATABASE_CREATE = "CREATE TABLE home (" + HOME_COL + " INTEGER NOT NULL " +
			"PRIMARY KEY, " + UPDATE_COL + " TEXT, " + USER_COL + " TEXT, " +
					TIME_COL + " INTEGER);";


	public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
    }
    

    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS home");
		db.execSQL("VACUUM");
		onCreate(db);
	}


    public static ContentValues getValues(Status status) {
    	Log.v("NiceDataHelper", "converting values");
    	

        ContentValues homeValues = new ContentValues();
        try {

	        homeValues.put(HOME_COL, status.getId());
	        homeValues.put(UPDATE_COL, status.getText());
	        homeValues.put(USER_COL, status.getUser().getScreenName());
	        homeValues.put(TIME_COL, status.getCreatedAt().getTime());
		}
        catch(Exception te) { Log.e("DbHelper", te.getMessage()); }

        return homeValues;
      }

}
