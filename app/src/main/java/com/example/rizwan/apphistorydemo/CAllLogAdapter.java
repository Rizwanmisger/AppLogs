package com.example.rizwan.apphistorydemo;


import android.content.Context;
import android.database.Cursor;
import android.provider.*;
import android.provider.CallLog;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Date;

public class CAllLogAdapter extends SimpleCursorAdapter {
    public CAllLogAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        String callType = "";
        int type = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
        switch(type)
        {
            case CallLog.Calls.INCOMING_TYPE:
                callType = "Incoming";break;
            case CallLog.Calls.OUTGOING_TYPE:
                callType = "Outgoing";break;
             case CallLog.Calls.MISSED_TYPE:
                callType = "Missed";break;
             default:
                 callType = "Unknown";
        }
        TextView textView = (TextView) view.findViewById(R.id.type);
        textView.setText(callType);

       long DateOfCall=Long.parseLong(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)));
        Date date = new Date(DateOfCall);
        int hr = date.getHours();
        int min = date.getMinutes();
        String time = new Integer(hr).toString() +":"+new Integer(min).toString();
        TextView textViewDate = (TextView) view.findViewById(R.id.date);
        textViewDate.setText(time);

    }
}
