package com.codepath.apps.simpletweets.adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    private final String TAG = "TweetsArrayAdapter";
    private TextView tvText;
    private  TextView tvScreenName;
    private  TextView tvTime;
    private  TextView tvHandle;
    private  TextView tvRetweet;
    private ImageView ivPhoto;
    private Transformation transformation;

    public TweetsArrayAdapter(Context c, List<Tweet> tweets){
        super(c, R.layout.item_tweet,tweets);
        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }
        tvScreenName = (TextView)convertView.findViewById(R.id.tvScreenName);
        tvText = (TextView)convertView.findViewById(R.id.tvText);
        tvTime = (TextView)convertView.findViewById(R.id.tvTime);
        tvHandle = (TextView)convertView.findViewById(R.id.tvHandle);
        tvRetweet = (TextView)convertView.findViewById(R.id.tvRetweet);
        ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);

        Tweet tweet = getItem(position);
        tvText.setText(tweet.getText());
        tvScreenName.setText(tweet.getUser().getScreenname());
        tvScreenName.setTypeface(null, Typeface.BOLD);
        tvHandle.setText(" @" + tweet.getUser().getName());
        if (tweet.isRetweet())
        {
            tvRetweet.setText(tweet.getRetweeter() + " retweeted (" + tweet.getRetweetCount() + " retweets)");
        }
        else {
            tvRetweet.setVisibility(View.GONE);
        }

//        Log.e("NOW",now + "");
//        Log.e("CREATED",mediaItem.createdTime);
        long ts = tweet.get_created_at_ts();
        Log.e(TAG, ts + "TIMESTAMP");
        long now = System.currentTimeMillis();
        String timeStr = "just now";
        if (ts < now)
            timeStr = DateUtils.getRelativeTimeSpanString(ts, now, 0, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        tvTime.setText(timeStr);

        Picasso.with(getContext()).load(Uri.parse(tweet.getUser().getProfileImageUrl())).transform(transformation).into(ivPhoto);
        return convertView;
    }
}
