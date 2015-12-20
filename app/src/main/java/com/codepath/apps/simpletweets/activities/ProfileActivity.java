package com.codepath.apps.simpletweets.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by i.mohomed on 12/20/15.
 */
//  picture, tagline, # of followers, # of following, and tweets on their profile.
public class ProfileActivity extends AppCompatActivity{

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);


        User mUser = (User)getIntent().getExtras().getParcelable("user");
        mTitle.setText("@" + mUser.getName());
        TextView tvName = (TextView)findViewById(R.id.tvName);
        TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
        TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
        ImageView ivUserPhoto = (ImageView)findViewById(R.id.ivUserPhoto);


        tvName.setText(mUser.getScreenname());
        tvFollowers.setText("Followers: " + mUser.getFollowers_count());
        tvFollowing.setText("Following: " + mUser.getFriends_count());
        tvStatus.setText(mUser.getDescription());

        Picasso.with(this).load(Uri.parse(mUser.getProfileImageUrl())).into(ivUserPhoto);
    }
}
