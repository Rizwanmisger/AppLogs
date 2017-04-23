package com.example.rizwan.apphistorydemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterLoginWebViewActivity extends Activity {
    private WebView webView;

    private final static String TWIT_KEY = "jy1qYywYg28Wqi1kQGVsXpXPA";
    private final static String TWIT_SECRET = "7pcZRwU0JhcGVlL9lp1rfbA7lQ4HdvyTDrIz2JXGHJ7qFXIXPA";

    private final static String TWIT_URL = "x-oauthflow-twitter://callback";

    private Twitter mTwitter;
    private RequestToken mRequestToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_twitter);

        webView = (WebView) findViewById(R.id.twitter_login_web_view);
        webView.setWebViewClient(new twitterLoginWebView());

        new FetchTwitterToken().execute();
    }

    private class twitterLoginWebView extends WebViewClient {
        String TAG = this.getClass().toString();

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equalsIgnoreCase("x-oauthflow-twitter") &&
                    uri.getAuthority().equalsIgnoreCase("callback")) {
                if (uri.getQueryParameter("oauth_token") != null) {
                    String authCode = uri.getQueryParameter("oauth_token");
                    String authVerifier = uri.getQueryParameter("oauth_verifier");

                    new SeedAccessToken().execute(authVerifier);

                    Toast.makeText(TwitterLoginWebViewActivity.this, "Authenticated.", Toast.LENGTH_SHORT).show();
                //    Intent timeLineIntet = new Intent(TwitterLoginWebViewActivity.this, TimeLine.class);

//                    finish();
                //    TwitterLoginWebViewActivity.this.startActivity(timeLineIntet);
                } else {
                    //TODO: Inform the user as per the errorCode
                    String errorCode = uri.getQueryParameter("error");
                    Toast.makeText(TwitterLoginWebViewActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
           // Toast.makeText(TwitterLoginWebViewActivity.this, "Sorry error occured. Try again  " + description, Toast.LENGTH_SHORT).show();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private class FetchTwitterToken extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String authenticationUrl = null;
            try {
                mTwitter = new TwitterFactory().getInstance();
                mTwitter.setOAuthConsumer(TWIT_KEY, TWIT_SECRET);
                mRequestToken = mTwitter.getOAuthRequestToken(TWIT_URL);
                authenticationUrl = mRequestToken.getAuthenticationURL();
            } catch (TwitterException e) {
            }
            return authenticationUrl;
        }

        @Override
        protected void onPostExecute(String authURL) {
            super.onPostExecute(authURL);

            if (mRequestToken != null) {
                webView.loadUrl(authURL);
            }
        }
    }

    private class SeedAccessToken extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                AccessToken accToken = mTwitter.getOAuthAccessToken(mRequestToken, params[0]);

                SharedPreferences.Editor editor = getSharedPreferences("TweetPrefs", 0).edit();
                editor.putString("user_token", accToken.getToken());
                editor.putString("user_secret", accToken.getTokenSecret());

                editor.apply();
                SharedPreferences sp = getSharedPreferences("TweetPrefs", 0);
                Log.v("Login ", "token here  " + sp.getString("user_token", null));
                Log.v("LOGIN ", "TOKEN IS " + accToken.getToken() + " and sec " + accToken.getTokenSecret());
            } catch (Exception e) {
                Log.e("error", "error_occured : " + e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent timeLineIntet = new Intent(TwitterLoginWebViewActivity.this, TimeLine.class);

//                    finish();
            TwitterLoginWebViewActivity.this.startActivity(timeLineIntet);

        }


    }

}
