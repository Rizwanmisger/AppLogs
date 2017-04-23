package com.example.rizwan.apphistorydemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LaunchTwitter extends AppCompatActivity implements View.OnClickListener{
    Button signIn;

    private String TAG = this.getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPrefs;
        mPrefs = getSharedPreferences("TweetPrefs", 0);

        if (mPrefs.getString("user_token", null) != null){
            Log.v("Main ","token here "+mPrefs.getString("user_token",null));
            // The user is already authenticated; move him to the twitter feeds
            Intent timeLineIntent = new Intent(this, TimeLine.class);
            timeLineIntent.putExtra("user_token", mPrefs.getString("user_token", null));
            startActivity(timeLineIntent);
        }
        // The user needs to authenticate hence landing on the signin page.
        setContentView(R.layout.main);

        signIn = (Button) findViewById(R.id.signin);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signin:
                Intent twitterWebViewLoginIntent = new Intent(LaunchTwitter.this, TwitterLoginWebViewActivity.class);
                LaunchTwitter.this.startActivity(twitterWebViewLoginIntent);
                break;
        }
    }
}
