package com.example.rizwan.apphistorydemo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class WIP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wip);
        ImageView imageView = (ImageView)findViewById(R.id.imageViewWIP);
        imageView.setImageResource(R.drawable.wip);
        for (PackageInfo pack : getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
            ProviderInfo[] providers = pack.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    Log.v("Example", "provider: " + provider.authority);
                }
            }
        }
        Cursor cursor = this.getContentResolver().query(Uri.parse("com.twitter.android.provider.TwitterProvider"),null,null,null,null);
        if(cursor == null)
        {
            Log.v("Cursor ","Twitter Cursor is null");
        }
      /* if(cursor.getCount()<1)
        {
            Log.v("Cursor ","Twitter Cursor is EMPTY");
        }*/
    }
}
