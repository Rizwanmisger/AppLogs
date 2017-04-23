package com.example.rizwan.apphistorydemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.Calendar;

public class SMSLog extends AppCompatActivity {
    SMSAdapter msmsAdapter;
    ListView mlistView ;
    Cursor cursor;
    Activity mActivity;
    String mode;

    private static String[] strFields = {
            Telephony.Sms.DATE,
            Telephony.Sms.TYPE,
            Telephony.Sms.ADDRESS,
            Telephony.Sms.BODY,
    };
    private static int[] viewFields = {
            R.id.sms_date,
            R.id.sms_type,
            R.id.sms_address,
            R.id.sms_body,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smslog);
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        mActivity = this;
        msmsAdapter = new SMSAdapter(this,R.layout.sms_list_item,cursor,strFields,viewFields,0);
       mlistView = (ListView)findViewById(R.id.sms_listView);
        new ShowLog().execute();
    }

    Cursor getSmsLog()
    {
        int year;
        int month;
        int day;
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        // String dateArg2 ;//= String.valueOf(createDate(year, month, day -1));
        String dateArg2 = "";
        switch (mode) {
            case "daily":
                dateArg2 = String.valueOf(createDate(year, month, day - 1));
                break;
            case "weekly":
                dateArg2 = String.valueOf(createDate(year, month, day - 7));
                break;
            case "monthly":
                dateArg2 = String.valueOf(createDate(year, month - 1, day));
                break;}
        String mSelectionClause = "date >?";

        String[] mSelectionArgs = {dateArg2};
        Cursor cursor = managedQuery(Uri.parse("content://sms"),null, mSelectionClause ,mSelectionArgs ,null);
      /*  if(cursor != null)
        {
            Log.v("SMS ","CURSOR IS NOT NULL");
        }
        if(cursor.getCount()<1)
        {
            Log.v("SMS ","CURSOR IS EMPTY");
        }
        cursor.moveToFirst();
        String one = cursor.getString(4);//date
        String two = cursor.getString(9);
        String three = cursor.getString(11);
        String four = cursor.getString(12);
        String five = cursor.getString(2);
        String six = cursor.getString(3);
        String seven = cursor.getColumnName(15);
        Log.v("Column 1","Col DATE "+one);
        Log.v("Column 1","Col TYPE "+two);
        Log.v("Column 1","Col SUBJECT "+three);
        Log.v("Column 1","Col BODY "+four);
        Log.v("Column 1","Col ADD "+five);
        Log.v("Column 1","Col PERSON "+six);
        //Log.v("Column 1","Col 8"+seven);*/
        return cursor;
    }
    public static Long createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day);

        return calendar.getTimeInMillis();

    }

    class ShowLog extends AsyncTask<Void,Void,Cursor>
    {
        @Override
        protected Cursor doInBackground(Void... params) {
            Cursor cursor = getSmsLog();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            super.onPostExecute(cursor);
            msmsAdapter.swapCursor(cursor);
            try{
                mlistView.setAdapter(msmsAdapter);
            }catch(Exception e)
            {
                Log.v("Exception is","EXCEPTION "+e.toString());

            }

        }
    }
}
