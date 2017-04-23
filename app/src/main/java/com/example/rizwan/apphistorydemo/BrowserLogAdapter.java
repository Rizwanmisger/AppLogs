package com.example.rizwan.apphistorydemo;

import android.content.Context;
import android.database.Cursor;
import android.provider.*;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Date;

public class BrowserLogAdapter extends SimpleCursorAdapter {
    public BrowserLogAdapter(Context context, int layout, Cursor c, String[] from, int[] to,int flag) {
        super(context, layout, c, from, to,flag);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        long DateOfCall=Long.parseLong(cursor.getString(3));
        Date date = new Date(DateOfCall);
        int hr = date.getHours();
        int min = date.getMinutes();
        int month = date.getMonth();
        String time = (new Integer(month)+1)+"//"+new Integer(hr).toString() +":"+new Integer(min).toString();
        TextView textViewDate = (TextView) view.findViewById(R.id.browser_date);
        textViewDate.setText(time);
    }
}
