package com.codepath.apps.simpletweets.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.TimelineFragment;
import com.codepath.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by i.mohomed on 12/20/15.
 */
//  picture, tagline, # of followers, # of following, and tweets on their profile.
public class ProfileActivity extends AppCompatActivity implements TimelineFragment.OnItemSelectedListener {

    private User mUser;

    // Function is blank on purpose. Not being used at the moment.
    public void onUserSelected(String userHandle)
    {
        //Toast.makeText(this, "User selected:" + userHandle, Toast.LENGTH_SHORT).show();
    }

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

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        TimelineFragment tf = new TimelineFragment();
        Bundle args = new Bundle();
        args.putParcelable("user",mUser);
        args.putString("type", "user");
        tf.setArguments(args);
// Replace the contents of the container with the new fragment
        ft.replace(R.id.idFrame, tf);
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
        ft.commit();
    }
}
