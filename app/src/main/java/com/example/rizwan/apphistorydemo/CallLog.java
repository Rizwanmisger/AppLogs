package com.example.rizwan.apphistorydemo;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;

public class CallLog extends AppCompatActivity {

    CAllLogAdapter mcAllLogAdapter;
    String mode;
    private static String[] strFields = {
            android.provider.CallLog.Calls.DATE,
            android.provider.CallLog.Calls.TYPE,
            android.provider.CallLog.Calls.NUMBER,
            android.provider.CallLog.Calls.DURATION,
    };
    private static int[] viewFields = {
            R.id.date,
            R.id.type,
            R.id.number,
            R.id.duration
    };
   private static String strOrder = android.provider.CallLog.Calls.DURATION + " DESC";
    Cursor mcursor;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);
        Intent intent = getIntent();
         mode = intent.getStringExtra("mode");
       mcAllLogAdapter = new CAllLogAdapter(this,R.layout.list_item,mcursor,strFields,viewFields,0);
       // listView = (ListView)findViewById(R.id.list);
        listView = (ListView)findViewById(R.id.list);
        if(listView == null)
        {
            Log.v("CallLog","NULL LISTVIEW");
        }
        ShowLog showLog = new ShowLog();
        showLog.execute();

    }

    public Cursor getLog()
    {
        int year;
        int  month;
        int day ;
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        String dateArg2="";
        switch(mode)
        {
            case "daily":
                dateArg2 = String.valueOf(createDate(year, month, day -1));
                break;
            case "weekly":
                dateArg2 = String.valueOf(createDate(year, month, day-7));
                break;
            case "monthly":
                dateArg2 = String.valueOf(createDate(year, month-1, day));
                break;


        }
       // String dateArg2 = String.valueOf(createDate(year, month, day -1));
        String mSelectionClause =  "date >?";

        String[] mSelectionArgs = { dateArg2 };
        Cursor cursor = managedQuery(android.provider.CallLog.Calls.CONTENT_URI, null, mSelectionClause, mSelectionArgs, strOrder);
        return cursor;
    }

    public static Long createDate(int year, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day);

        return calendar.getTimeInMillis();

    }

    class ShowLog extends AsyncTask<Void,Void,Cursor>
    {
        @Override
        protected Cursor doInBackground(Void... params) {
            Cursor cursor = getLog();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

                super.onPostExecute(cursor);
                    mcAllLogAdapter.swapCursor(cursor);
            try{
                    listView.setAdapter(mcAllLogAdapter);
                }catch(Exception e)
            {
                Log.v("Exception is","EXCEPTION "+e.toString());

            }

        }
    }
}
