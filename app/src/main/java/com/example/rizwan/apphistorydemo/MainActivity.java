package com.example.rizwan.apphistorydemo;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    UsageStatsManager musageStatsManager ;
    Activity mActivity;
    HashMap<String,Long> mhashMap;
    TextView textViewCall ;
    TextView textViewSMS ;
    TextView textViewBrowser ;
    TextView textViewSN ;
    TextView textViewRide ;

    String mode ;
    Integer interval ;//= UsageStatsManager.INTERVAL_DAILY;
    Calendar cal;
    Long begin;
    Long end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mhashMap = new HashMap<String,Long>();
         interval = new Integer(UsageStatsManager.INTERVAL_DAILY);
        musageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Intent intent = new Intent(this, com.example.rizwan.apphistorydemo.SMSLog.class);
        //startActivity(intent);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Daily");
        categories.add("Weekly");
        categories.add("Monthly");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

        ImageView imageViewCall = (ImageView)findViewById(R.id.imageViewCall);
        imageViewCall.setImageResource(R.drawable.call2);
        ImageView imageViewSMS = (ImageView)findViewById(R.id.imageViewSMS);
        imageViewSMS.setImageResource(R.drawable.asms);
        ImageView imageViewBrowser = (ImageView)findViewById(R.id.imageViewBrowser);
        imageViewBrowser.setImageResource(R.drawable.browser);
        ImageView imageViewSN = (ImageView)findViewById(R.id.imageViewSN);
        imageViewSN.setImageResource(R.drawable.facebook);
        ImageView imageViewRide = (ImageView)findViewById(R.id.imageViewRide);
        imageViewRide.setImageResource(R.drawable.car);

        LinearLayout layout_call=(LinearLayout)findViewById(R.id.layout_call);
        layout_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, com.example.rizwan.apphistorydemo.CallLog.class);
                intent.putExtra("mode",mode);
                startActivity(intent);
            }
        });

       LinearLayout layout_browser=(LinearLayout)findViewById(R.id.layout_browser);
        layout_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,BrowserLog.class);
                intent.putExtra("mode",mode);
                startActivity(intent);
            }
        });

        LinearLayout layout_sms=(LinearLayout)findViewById(R.id.layout_sms);
        layout_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,SMSLog.class);
                intent.putExtra("mode",mode);
                startActivity(intent);
            }
        });
        LinearLayout layout_sn=(LinearLayout)findViewById(R.id.layout_sn);
        layout_sn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,LaunchTwitter.class);
                intent.putExtra("mode",mode);
                startActivity(intent);
            }
        });

        LinearLayout layout_ride=(LinearLayout)findViewById(R.id.layout_ride);
        layout_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,WIP.class);
                intent.putExtra("mode",mode);
                startActivity(intent);
            }
        });

         textViewCall = (TextView) findViewById(R.id.UpTimeCall);
         textViewSMS = (TextView) findViewById(R.id.upTimeSMS);
         textViewBrowser = (TextView) findViewById(R.id.UpTimeBrowser);
         textViewSN = (TextView) findViewById(R.id.UpTimeSN);
         textViewRide = (TextView) findViewById(R.id.UpTimeRide);

        show();

}//End of onCreate
    public static Long createDate(int year, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day);

        return calendar.getTimeInMillis();

    }
    public List<UsageStats> getUsageStatistics() {
/*
//Start code
/*
        int year;
        int  month;
        int day ;
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        //String dateArg2 = String.valueOf(createDate(year, month, day -1));
        Long begin = createDate(year, month, day -6);
        Long end = createDate(year, month, day-4 );
        String mSelectionClause =  "date >?";
      //  String[] mSelectionArgs = { dateArg2 };
        List<UsageStats> queryUsageStats = musageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 18, 19);
        //END OF CODE
        */

        Calendar calEND = Calendar.getInstance();
Log.v("INTERVAL ", "Interval is " + interval+" begin "+ begin);
        List<UsageStats> queryUsageStats = musageStatsManager.queryUsageStats(interval, begin, end);
        if(queryUsageStats == null)
        {
            Log.v("Error in b","egin and end");
        }
        return queryUsageStats;
    }//End of getUsageStatistics

    void show()
    {
       // Calendar cal = Calendar.getInstance();
        List<UsageStats> queryUsageStats = getUsageStatistics();
        if (queryUsageStats.size() == 0) {
            queryUsageStats=null;
        }
        for (int i = 0; i < queryUsageStats.size(); i++) {
            String key = queryUsageStats.get(i).getPackageName();
            Log.v("KEY","PCK "+key);
            if(key.toUpperCase().indexOf("CHROME")!=-1)
            {
                Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                mhashMap.put("Chrome",upTime);
                Log.v("Chrome ","UP "+upTime);
            }
            if(key.toUpperCase().indexOf("FACEBOOK")!=-1)
            {
                Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                mhashMap.put("Facebook",upTime);
                Log.v("Facebook ", "UP " + upTime);
            }
            if(key.toUpperCase().indexOf("OLA")!=-1)
            {
                Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                mhashMap.put("Ola",upTime);
                Log.v("Ola ", "UP " + upTime);
            }
            if(key.toUpperCase().indexOf("SMS")!=-1 || key.toUpperCase().indexOf("MMS")!=-1)
            {
                Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                mhashMap.put("Sms",upTime);
                Log.v("SMS ", "UP " + upTime);
            }
            if(key.toUpperCase().indexOf("CALL")!=-1 || key.toUpperCase().indexOf("TEL")!=-1)
            {
                Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                mhashMap.put("Call",upTime);
                Log.v("CALL ", "UP " + upTime);
            }
        }

        if (queryUsageStats == null) {
            Toast.makeText(mActivity, "Access to APPUSAGE is not enabled", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        try {
            textViewCall.setText("Use_Time "+mhashMap.get("Call").toString());
        }catch(Exception e)
        {}
        try{
            textViewSMS.setText("Use_Time "+mhashMap.get("Sms").toString());}
        catch(Exception e)
        {}
        try{
            textViewBrowser.setText("Use_Time "+mhashMap.get("Chrome").toString());}
        catch (Exception e){}
        try{
            textViewSN.setText("Use_Time "+mhashMap.get("Facebook").toString());}
        catch (Exception e){}
        try{
            textViewRide.setText("Use_Time "+mhashMap.get("Ola").toString());}
        catch (Exception e){}
    }
/*
    class Seed extends AsyncTask<Void, Void, List<UsageStats>> {
        @Override
        protected List<UsageStats> doInBackground(Void... params) {
            Calendar cal = Calendar.getInstance();
            List<UsageStats> queryUsageStats = getUsageStatistics();
            if (queryUsageStats.size() == 0) {
                return null;
            }
            for (int i = 0; i < queryUsageStats.size(); i++) {
                String key = queryUsageStats.get(i).getPackageName();
                Log.v("KEY","PCK "+key);
               if(key.toUpperCase().indexOf("CHROME")!=-1)
               {
                   Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                   mhashMap.put("Chrome",upTime);
                   Log.v("Chrome ","UP "+upTime);
               }
                if(key.toUpperCase().indexOf("FACEBOOK")!=-1)
                {
                    Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                    mhashMap.put("Facebook",upTime);
                    Log.v("Facebook ", "UP " + upTime);
                }
                if(key.toUpperCase().indexOf("OLA")!=-1)
                {
                    Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                    mhashMap.put("Ola",upTime);
                    Log.v("Ola ", "UP " + upTime);
                }
                if(key.toUpperCase().indexOf("SMS")!=-1 || key.toUpperCase().indexOf("MMS")!=-1)
                {
                    Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                    mhashMap.put("Sms",upTime);
                    Log.v("SMS ", "UP " + upTime);
                }
                if(key.toUpperCase().indexOf("CALL")!=-1 || key.toUpperCase().indexOf("TEL")!=-1)
                {
                    Long upTime = queryUsageStats.get(i).getTotalTimeInForeground();
                    mhashMap.put("Call",upTime);
                    Log.v("CALL ", "UP " + upTime);
                }
            }
            return queryUsageStats;
        }
        @Override
        protected void onPostExecute (List < UsageStats > usageStatses) {
            super.onPostExecute(usageStatses);
            if (usageStatses == null) {
                Toast.makeText(mActivity, "Access to APPUSAGE is not enabled", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
            try {
                textViewCall.setText("Use_Time "+mhashMap.get("Call").toString());
            }catch(Exception e)
            {}
            try{
            textViewSMS.setText("Use_Time "+mhashMap.get("Sms").toString());}
            catch(Exception e)
            {}
            try{
            textViewBrowser.setText("Use_Time "+mhashMap.get("Chrome").toString());}
            catch (Exception e){}
            try{
            textViewSN.setText("Use_Time "+mhashMap.get("Facebook").toString());}
            catch (Exception e){}
            try{
            textViewRide.setText("Use_Time "+mhashMap.get("Ola").toString());}
            catch (Exception e){}
        }


    }//End of Async Seed
*/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        int year;
        int month;
        int day;
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        end= createDate(year, month, day );
        String item = parent.getItemAtPosition(position).toString();
        switch(item) {
            case "Daily":
                mode = "daily";
                interval = UsageStatsManager.INTERVAL_DAILY;
                // cal = Calendar.getInstance();
                //cal.add(Calendar.YEAR, -1);
               begin= createDate(year, month, day - 1);


                break;
            case "Weekly":
                mode = "weekly";
                interval = UsageStatsManager.INTERVAL_WEEKLY;
                begin= createDate(year, month, day - 7);

                break;
            case "Monthly":
                mode = "monthly";
                interval = UsageStatsManager.INTERVAL_MONTHLY;
                begin= createDate(year, month-1, day );

                break;
        }
       show();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
       show();
        // TODO Auto-generated method stub
    }

}//End of Activity
