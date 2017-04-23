package com.example.rizwan.apphistorydemo;

import android.content.Context;
import android.database.Cursor;
import android.provider.*;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Date;

public class SMSAdapter extends SimpleCursorAdapter {
    public SMSAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        long DateOfCall = Long.parseLong(cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE)));
        Date date = new Date(DateOfCall);
        int hr = date.getHours();
        int min = date.getMinutes();
        String time = new Integer(hr).toString() + ":" + new Integer(min).toString();
        TextView textViewDate = (TextView) view.findViewById(R.id.sms_date);
        textViewDate.setText(time);

        String callType = "";
        int type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));
        switch(type)
        {
            case Telephony.Sms.MESSAGE_TYPE_INBOX:
                callType = "Inbox";break;
            case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                callType = "Outbox";break;
            default:
                callType = "Unknown";
        }
        TextView textView = (TextView) view.findViewById(R.id.sms_type);
        textView.setText(callType);

    }
}
