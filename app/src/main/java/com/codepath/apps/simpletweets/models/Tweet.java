package com.codepath.apps.simpletweets.models;

//{
//        "coordinates": null,
//        "truncated": false,
//        "created_at": "Tue Aug 28 21:16:23 +0000 2012",
//        "favorited": false,
//        "id_str": "240558470661799936",
//        "in_reply_to_user_id_str": null,
//        "entities": {
//        "urls": [
//
//        ],
//        "hashtags": [
//
//        ],
//        "user_mentions": [
//
//        ]
//        },
//        "text": "just another test",
//        "contributors": null,
//        "id": 240558470661799936,
//        "retweet_count": 0,
//        "in_reply_to_status_id_str": null,
//        "geo": null,
//        "retweeted": false,
//        "in_reply_to_user_id": null,
//        "place": null,
//        "source": "<a href="//realitytechnicians.com%5C%22" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
//        "user": {
//        "name": "OAuth Dancer",
//        "profile_sidebar_fill_color": "DDEEF6",
//        "profile_background_tile": true,
//        "profile_sidebar_border_color": "C0DEED",
//        "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
//        "created_at": "Wed Mar 03 19:37:35 +0000 2010",
//        "location": "San Francisco, CA",
//        "follow_request_sent": false,
//        "id_str": "119476949",
//        "is_translator": false,
//        "profile_link_color": "0084B4",
//        "entities": {
//        "url": {
//        "urls": [
//        {
//        "expanded_url": null,
//        "url": "http://bit.ly/oauth-dancer",
//        "indices": [
//        0,
//        26
//        ],
//        "display_url": null
//        }
//        ]
//        },
//        "description": null
//        },
//        "default_profile": false,
//        "url": "http://bit.ly/oauth-dancer",
//        "contributors_enabled": false,
//        "favourites_count": 7,
//        "utc_offset": null,
//        "profile_image_url_https": "https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
//        "id": 119476949,
//        "listed_count": 1,
//        "profile_use_background_image": true,
//        "profile_text_color": "333333",
//        "followers_count": 28,
//        "lang": "en",
//        "protected": false,
//        "geo_enabled": true,
//        "notifications": false,
//        "description": "",
//        "profile_background_color": "C0DEED",
//        "verified": false,
//        "time_zone": null,
//        "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
//        "statuses_count": 166,
//        "profile_background_image_url": "http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
//        "default_profile_image": false,
//        "friends_count": 14,
//        "following": false,
//        "show_all_inline_media": false,
//        "screen_name": "oauth_dancer"
//        },
//        "in_reply_to_screen_name": null,
//        "in_reply_to_status_id": null
//        }

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Tweet implements Comparable {

    private String TAG = "Tweet";
    public String getCreated_at() {
        return created_at;
    }

    @Override
    public int compareTo(Object otherTweet) {
        long comp = ((Tweet)otherTweet).get_created_at_ts();
        return (int)(comp - get_created_at_ts());
        /* For Ascending order*/
//        return this.studentage-compareage;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }


    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public long get_created_at_ts() {
        Log.i(TAG, created_at);
        // Sun Dec 13 21:32:36 +0000 2015
        final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
        try {
            return sf.parse(created_at).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    String text;
    String created_at;
    long id;
    User user;
    long created_at_ts;
    boolean isRetweet;
    String retweeter;
    int retweetCount;

    public boolean isRetweet()
    {
        return isRetweet;
    }

    public int getRetweetCount()
    {
        return retweetCount;
    }

    public String getRetweeter()
    {
        return retweeter;
    }

    public static Tweet fromJSON(JSONObject json){
        Tweet t = new Tweet();
        try {
            t.isRetweet = json.has("retweeted_status");
            t.retweetCount = json.getInt("retweet_count");
            if (t.isRetweet) {
                t.retweeter = json.getJSONObject("user").getString("name");
                json = json.getJSONObject("retweeted_status");
            }
                t.text = json.getString("text");
            t.created_at = json.getString("created_at");
            t.id = json.getLong("id");
            t.user = User.fromJSON(json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray)
    {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++)
        {
            try {
                tweets.add(Tweet.fromJSON(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return new Long(id).hashCode();
        //return super.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if(o == null)
            return false;
        if(!(o instanceof Tweet))
            return false;

        Tweet other = (Tweet) o;
        return this.id == other.getId();
    }

}
