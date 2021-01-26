package com.rss.myapplication;


public class FeedObject {

    public String title;
    public String link;
    public String description;
    public String pubdate;
    public String guid;

    public FeedObject(String title, String link, String description, String pubdate, String guid) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubdate = pubdate;
        this.guid = guid;
    }
}
