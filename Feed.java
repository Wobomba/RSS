package com.rss.myapplication;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Feed extends ListActivity {
    //    variables
    private ProgressBar pDialog;
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<>();
    RSS_FeedParser rssFeedParser = new RSS_FeedParser();
    List<FeedObject> feedObjects = new ArrayList<>();
    private static final String TAG_TITLE = "title";
    private static final String TAG_LINK = "link";
    private static final String TAG_PUB_DATE = "pubDate";
    private static final String TAG_DESC = "description";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_s_s_feed);
        pDialog = findViewById(R.id.progressBar);
//        receive intent string for url
        String rss_link = getIntent().getStringExtra("rssLink");
        new LoadRSSFeedItems().execute(rss_link);
        ListView lv = getListView();
//        parse link and desc to intent
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent in = new Intent(getApplicationContext(), ReadOutItem.class);
//            get  views
            String page_url = ((TextView) view.findViewById(R.id.page_url)).getText().toString().trim();
            String page_desc = ((TextView) view.findViewById(R.id.pub_desc)).getText().toString().trim();
            String page_title = ((TextView) view.findViewById(R.id.title)).getText().toString().trim();
            String page_date = ((TextView) view.findViewById(R.id.pub_date)).getText().toString().trim();
            in.putExtra("url", page_url);
            in.putExtra("title", page_title);
            in.putExtra("desc", page_desc);
            in.putExtra("date", page_date);
            startActivity(in);
        });
    }
    public class LoadRSSFeedItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];
            // list of rss items
            feedObjects = rssFeedParser.getRSSFeedItems(rss_url);

            // looping through each item
            for (FeedObject item : feedObjects) {
                // creating new HashMap
                if (item.link.equals(""))
                    break;
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value

//                format date
                String givenDateString = item.pubdate.trim();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
                try {
                    Date mDate = sdf.parse(givenDateString);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);
                    item.pubdate = sdf2.format(mDate);

                } catch (ParseException e) {
                    e.printStackTrace();

                }

                map.put(TAG_TITLE, item.title);
                map.put(TAG_LINK, item.link);
                map.put(TAG_DESC, item.description);
                map.put(TAG_PUB_DATE, item.pubdate);

                // adding HashList to ArrayList
                rssItemList.add(map);
            }


            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            Feed.this,
                            rssItemList, R.layout.rss_item_list_row,
                            new String[]{TAG_LINK, TAG_TITLE, TAG_PUB_DATE, TAG_DESC},
                            new int[]{R.id.page_url, R.id.title, R.id.pub_date, R.id.pub_desc});

                    // updating listview
                    setListAdapter(adapter);
                }
            });
            return null;
        }

        protected void onPostExecute(String args) {
            pDialog.setVisibility(View.GONE);
        }
    }
}