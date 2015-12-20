package com.codepath.apps.simpletweets.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.MentionFragment;
import com.codepath.apps.simpletweets.fragments.TimelineFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String TAG = "MyFragmentPagerAdapter";
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "HOME", "MENTIONS" };
    private Context context;

    private int[] imageResId = {
            R.drawable.home24,
            R.drawable.users24
    };

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "Fragment " + position + " was created");
        if (position == 0) {
            TimelineFragment tf = new TimelineFragment();
            return tf;
        }
        MentionFragment mf = new MentionFragment();
        return mf;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        //return tabTitles[position];
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " +  tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    private static String makeFragmentName(int viewId, int index)
    {
        return "android:switcher:" + viewId + ":" + index;
    }
}