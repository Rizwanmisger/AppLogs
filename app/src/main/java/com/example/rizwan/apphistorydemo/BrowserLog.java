package com.example.rizwan.apphistorydemo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Browser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;

public class BrowserLog extends AppCompatActivity {

    BrowserLogAdapter mbrowserLogAdapter;
    Cursor mcursor;
    ListView listView;
    String mode;
    private static String[] strFields = {
            "date",
            "title",
    };
    private static int[] viewFields = {
            R.id.browser_date,
            R.id.browser_url,
    };
    private static String strOrder = "date DESC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_log);
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        mbrowserLogAdapter = new BrowserLogAdapter(this, R.layout.browser_list_item, mcursor, strFields, viewFields, 0);
        listView = (ListView) findViewById(R.id.browser_list);
        // getBrowserHist();
        new ShowBrowserLog().execute();
    }

    public Cursor getBrowserHist() {
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

            Cursor Cur = managedQuery(Uri.parse("content://browser/bookmarks"),
                    null, mSelectionClause, mSelectionArgs, strOrder);
            Cur.moveToFirst();
            if (Cur == null) {
                Log.v("Cursor", "Cursor NULL");
            }
            if (Cur.getCount() < 1) {
                Log.v("Cursor", "Cursor Empty");
            }
            if (Cur.moveToFirst() && Cur.getCount() > 0) {
                while (Cur.isAfterLast() == false) {
                    Log.v("titleIdx", "at 1 " + Cur
                            .getString(1)); // 1=url
                    Log.v("urlIdx", "at 3 " + Cur
                            .getString(3)); //3=date
                    Cur.moveToNext();
                    Cur.getColumnName(3);
                    Log.v("Column 1", "NAME " + Cur.getColumnName(5));//5=title
                    Log.v("Column 3", "NAME " + Cur.getColumnName(6));
                }
            }
            return Cur;
        }

    public static Long createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day);

        return calendar.getTimeInMillis();

    }

    class ShowBrowserLog extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            Cursor cursor = getBrowserHist();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            mbrowserLogAdapter.swapCursor(cursor);
            try {
                listView.setAdapter(mbrowserLogAdapter);
            } catch (Exception e) {
                Log.v("Exception is", "EXCEPTION " + e.toString());

            }
        }
    }
}
