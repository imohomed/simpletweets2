package com.codepath.apps.simpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TimelineActivity extends AppCompatActivity {


    private TwitterClient client;
    private String TAG = "TimelineActivity";
    private SwipeRefreshLayout swipeContainer;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_actionbar_menu, menu);
        return true;
    }

    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private TweetsArrayAdapter aTweets;
//    private long last_downloaded_tweet = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        client = TwitterClientApplication.getRestClient();
//        last_downloaded_tweet = 1;
        populateTimeline(1,-1);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "onLoadMore" + page + " " + totalItemsCount);
                //tweets.addAll((ArrayList<Tweet>) tweets.clone());
                //aTweets.notifyDataSetChanged();
                long max_id = tweets.get(tweets.size() - 1).getId();
                populateTimeline(1, max_id);
                return false;
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(1,-1);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void populateTimeline(long since_id,long max_id) {
        Log.d(TAG,"populateTimeline called");
        client.getTimeline(since_id,max_id,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
               // super.onSuccess(statusCode, headers, response);
                Log.d(TAG, response.toString());
                //tweets.clear();
                tweets.addAll(Tweet.fromJSONArray(response));

//                for(int i=0;i<tweets.size();i++)
//                {
//                    Log.d(TAG, "" + tweets.get(i).getId());
//
//                }
//                Log.d(TAG, "" + tweets.get(0).getId());
//                Log.d(TAG, "" + tweets.get(tweets.size()-1).getId());
                Set<Tweet> unique_tweets = new HashSet<Tweet>(tweets);
                tweets.clear();
                tweets.addAll(unique_tweets);
                Collections.sort(tweets);
                Log.d(TAG, "" + tweets.size());
                aTweets.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(TAG, errorResponse.toString());
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void onComposeAction(MenuItem item) {
//        Toast.makeText(this,"Menu pressed",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,ComposeActivity.class);
        startActivityForResult(i, 1);

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
                        Tweet newTweet = Tweet.fromJSON(response);
                        tweets.add(0,newTweet);


                        Set<Tweet> unique_tweets = new HashSet<Tweet>(tweets);
                        tweets.clear();
                        tweets.addAll(unique_tweets);
                        Collections.sort(tweets);
                        Log.d(TAG, "" + tweets.size());

                        aTweets.notifyDataSetChanged();
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
