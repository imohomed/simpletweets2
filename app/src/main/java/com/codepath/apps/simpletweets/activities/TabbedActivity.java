package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.SimpleTweetsApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.adapters.MyFragmentPagerAdapter;
import com.codepath.apps.simpletweets.fragments.TimelineFragment;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;
/**
 * Created by i.mohomed on 12/20/15.
 */
public class TabbedActivity extends AppCompatActivity {
    private TwitterClient client;
    private String TAG = "TabbedActivity";
    private ViewPager viewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_actionbar_menu, menu);
        return true;
    }

    public void onComposeAction(MenuItem item) {
        Toast.makeText(this, "Compose pressed", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,ComposeActivity.class);
        startActivityForResult(i, 1);

    }

    public void onProfileAction(MenuItem item) {
        Toast.makeText(this, "Profile pressed", Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(this,ComposeActivity.class);
        //startActivityForResult(i, 1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        client = SimpleTweetsApplication.getRestClient();

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup tabbed menu
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                TabbedActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_OK)
            {
                final String tweet = data.getStringExtra("tweet");

                client.postTweet(tweet,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // super.onSuccess(statusCode, headers, response);
                        Log.d(TAG, response.toString());
                        //tweets.clear();
                        //tweets.addAll(Tweet.fromJSONArray(response));


//                Log.d(TAG, "" + tweets.get(0).getId());
//                Log.d(TAG, "" + tweets.get(tweets.size()-1).getId());
//                        Collections.sort(tweets);
                        // The following code adds new tweet to existing timeline array
                        Tweet newTweet = Tweet.fromJSON(response);
                        TimelineFragment tf = (TimelineFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":0");

                        tf.integrateNewTweet(newTweet);
//                        tweets.add(0,newTweet);
//
//
//                        Set<Tweet> unique_tweets = new HashSet<Tweet>(tweets);
//                        tweets.clear();
//                        tweets.addAll(unique_tweets);
//                        Collections.sort(tweets);
//                        Log.d(TAG, "" + tweets.size());
//
//                        aTweets.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d(TAG, errorResponse.toString());
                    }
                });

                Toast.makeText(this, "Tweet Sent!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
