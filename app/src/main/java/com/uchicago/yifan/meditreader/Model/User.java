package com.uchicago.yifan.meditreader.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Yifan on 7/22/16.
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
