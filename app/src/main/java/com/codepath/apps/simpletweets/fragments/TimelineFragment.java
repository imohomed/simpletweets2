package com.codepath.apps.simpletweets.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.simpletweets.EndlessScrollListener;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.SimpleTweetsApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TimelineFragment extends Fragment {


    private TwitterClient client;
    private String TAG = "TimelineActivity";
    private SwipeRefreshLayout swipeContainer;
    private Context mContext;

    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private TweetsArrayAdapter aTweets;
    private String type;
    private User mUser;
//    private long last_downloaded_tweet = 1;

    // The listener and associated interface are not actually used at this point
    private OnItemSelectedListener listener;

    // Interface not used
    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onUserSelected(String userHandle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type", "");
        mUser = getArguments().getParcelable("user");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getContext(); //container.getContext();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        lvTweets.setAdapter(aTweets);
        client = SimpleTweetsApplication.getRestClient();

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



        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(1, -1);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        populateTimeline(1, -1);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_timeline, container, false);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        lvTweets = (ListView)view.findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

//        last_downloaded_tweet = 1;



        return view;
    }

    private void populateTimeline(long since_id,long max_id) {
        Log.d(TAG,"populateTimeline called");
        JsonHttpResponseHandler rspHandler = new JsonHttpResponseHandler() {
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
        };

        if (type.equals("home")) {
            client.getTimeline(since_id, max_id, rspHandler);
        } else if (type.equals("mentions"))
        {
            client.getMentions(since_id, max_id, rspHandler);
        } else if (type.equals("user"))
        {
            client.getUserTimeline(mUser,since_id, max_id, rspHandler);
        }
    }

    public void integrateNewTweet(Tweet newTweet)
    {
        tweets.add(0,newTweet);

        Set<Tweet> unique_tweets = new HashSet<Tweet>(tweets);
        tweets.clear();
        tweets.addAll(unique_tweets);
        Collections.sort(tweets);
        Log.d(TAG, "" + tweets.size());

        aTweets.notifyDataSetChanged();
    }



    /*@Override
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
    }*/
}
