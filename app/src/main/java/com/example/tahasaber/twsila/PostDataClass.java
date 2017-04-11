package com.example.tahasaber.twsila;

/**
 * Created by mohamed on 06/02/17.
 */
public class PostDataClass {
    int Profile_picture;
    String post_puplisher;
    int categoryItem;
    String post_date;
    String post_body;
    String counter;


    public PostDataClass(int profile_picture, String post_puplisher, String post_date, String post_body, int item, String counter) {
        this.Profile_picture = profile_picture;
        this.post_puplisher = post_puplisher;
        this.post_date = post_date;
        this.post_body = post_body;
        this.categoryItem = item;
        this.counter = counter;

    }
}
