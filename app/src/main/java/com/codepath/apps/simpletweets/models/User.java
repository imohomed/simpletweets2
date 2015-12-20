package com.codepath.apps.simpletweets.models;

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

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {
    String name;
    String screenname;
    int friends_count;
    int followers_count;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenname() {
        return screenname;
    }

    public int getFriends_count() {return friends_count;}

    public int getFollowers_count() {return  followers_count;}

    long id;
    String profileImageUrl;
    String description;

    public String getDescription() {return description;}
    private static String TAG = "USER";

    public static User fromJSON(JSONObject json){
        User u = new User();
        try {
            u.screenname = json.getString("name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.name = json.getString("screen_name");
            u.id = json.getLong("id");
            u.friends_count = json.getInt("friends_count");
            u.followers_count = json.getInt("followers_count");
            u.description = json.getString("description");
            Log.e(TAG,u.screenname + " " + u.profileImageUrl + " "  + u.name + " "  + u.id + " "  + u.friends_count + " "  + u.followers_count + " "
                    + u.description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.screenname);
        dest.writeInt(this.friends_count);
        dest.writeInt(this.followers_count);
        dest.writeLong(this.id);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.description);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.screenname = in.readString();
        this.friends_count = in.readInt();
        this.followers_count = in.readInt();
        this.id = in.readLong();
        this.profileImageUrl = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
