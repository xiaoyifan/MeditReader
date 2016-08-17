package com.uchicago.yifan.meditreader.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yifan on 7/16/16.
 */
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String title;
    public String url;
    public String date;
    public String description;
    public String post_type;
    public int commentCount = 0;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public Map<String, Boolean> bookmarks = new HashMap<>();


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String type, String title, String description){
        this.uid = uid;
        this.post_type = type;
        this.title = title;
        this.description = description;

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.date = df.format(c.getTime());
    }

    public Post(String uid, String type, String title, String url, String description){
        this.uid = uid;
        this.post_type = type;
        this.title = title;
        this.url = url;
        this.description = description;

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.date = df.format(c.getTime());
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("post_type", post_type.toString());
        result.put("title", title);
        result.put("description", description);
        result.put("date", date);
        result.put("url", url);
        result.put("starCount", starCount);
        result.put("commentCount", commentCount);
        result.put("stars", stars);
        result.put("bookmarks", bookmarks);

        return result;
    }

}
