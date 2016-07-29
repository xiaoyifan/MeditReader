package com.uchicago.yifan.meditreader.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yifan on 7/16/16.
 */
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String body;
    public String date;
    public int commentCount = 0;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public Map<String, Boolean> bookmarks = new HashMap<>();


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String body){
        this.uid = uid;
        this.author = author;
        this.body = body;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("body", body);
        result.put("date", date);
        result.put("starCount", starCount);
        result.put("commentCount", commentCount);
        result.put("stars", stars);
        result.put("bookmarks", bookmarks);

        return result;
    }

}
