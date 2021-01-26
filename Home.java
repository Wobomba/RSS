package com.rss.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Home extends AppCompatActivity {
    EditText link;
    Button feed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        link = findViewById(R.id.link);
        feed = findViewById(R.id.getFeed);
        link.setText("http://www.rediff.com/rss/moviesreviewsrss.xml");

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getLink = link.getText().toString();
                if (TextUtils.isEmpty(getLink)){ }
                else{
//                validate
                    try {
                        if (!getLink.startsWith("http://") && !getLink.startsWith("https://"))
                            getLink = "http://"+getLink;
                        startActivity(new Intent(getApplicationContext(), Feed.class).putExtra("rssLink", getLink));

                    } catch (Exception e) {
                    }
                }
            }
        });
    }
}