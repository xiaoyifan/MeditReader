package com.uchicago.yifan.meditreader.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Yifan on 7/16/16.
 */
@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String text;

    public Comment(){

    }

    public Comment(String uid, String text){
        this.uid = uid;
        this.text = text;
    }
}
