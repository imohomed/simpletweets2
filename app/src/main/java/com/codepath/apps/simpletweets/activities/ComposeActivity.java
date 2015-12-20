package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;

public class ComposeActivity extends AppCompatActivity {

    private EditText etTweet;
    private TextView tvNumChars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        //getSupportActionBar().setTitle("Post Tweet");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Post a Tweet");
        //
        etTweet = (EditText) findViewById(R.id.etTweet);
        tvNumChars = (TextView) findViewById(R.id.tvNumChars);

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here is your code
                int numCharsTyped = s.length();
                int remaining = (140 - numCharsTyped);
                tvNumChars.setText(remaining + "");
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                int remaining = (140 - s.length());
                if (remaining < 0){

                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void onTweet(View view) {
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("tweet", etTweet.getText().toString());
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        // set result code and bundle data for response
        finish();
    }

    public void cancelledPressed(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
