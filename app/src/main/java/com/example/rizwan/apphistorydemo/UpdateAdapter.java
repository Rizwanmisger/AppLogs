package com.example.rizwan.apphistorydemo;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class UpdateAdapter extends SimpleCursorAdapter {


	public final static String TWIT_KEY = "your key";//alter

	public final static String TWIT_SECRET = "your secret";//alter
	

	static final String[] from = { "update_text", "user_screen", "update_time"};

	static final int[] to = { R.id.updateText, R.id.userScreen, R.id.updateTime};
	
	private String LOG_TAG = "UpdateAdapter";


	public UpdateAdapter(Context context, Cursor c) {
		super(context, R.layout.tweet, c, from, to);
	}


	@Override
	public void bindView(View row, Context context, Cursor cursor) {
		super.bindView(row, context, cursor);
		//get the update time
		long createdAt = cursor.getLong(cursor.getColumnIndex("update_time"));
		//get the update time view
		TextView textCreatedAt = (TextView) row.findViewById(R.id.updateTime);
		//adjust the way the time is displayed to make it human-readable
		textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(createdAt)+" ");

	}


	
}
