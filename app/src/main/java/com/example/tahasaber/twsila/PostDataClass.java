package com.example.tahasaber.twsila;

/**
 * Created by mohamed on 06/02/17.
 */

public class PostDataClass {

    private String post_body;
    private int acceptance;
    private String post_id;
    private  int profile_picture;
    private int category_icon;
    private String category;
    private boolean isopend;
    private String user_id;
    private String post_puplisher;
    private String post_date;




    // Defult Constractor needed for firebase.

    public PostDataClass(){}


    public int getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(int category_icon) {
        this.category_icon = category_icon;
    }

    public PostDataClass(String post_body, int acceptance, String category, int profile_picture, String post_date, boolean isopend, int category_icon, String user_id, String post_puplisher) {

        this.acceptance = acceptance;
        this.category = category;
        this.isopend = isopend;
        this.user_id = user_id;
        this.post_puplisher = post_puplisher;
        this.profile_picture = profile_picture;
        this.post_date = post_date;
        this.category_icon=category_icon;
        this.post_body = post_body;

    }

    public int getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(int profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getPost_body() {
        return post_body;
    }

    public void setPost_body(String post_body) {
        this.post_body = post_body;
    }

    public int getAcceptance() {
        return acceptance;
    }

    public void setacceptance(int acceptance) {
        this.acceptance = acceptance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isopend() {
        return isopend;
    }

    public void setIsopend(boolean isopend) {
        this.isopend = isopend;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_puplisher() {
        return post_puplisher;
    }

    public void setPost_puplisher(String post_puplisher) {
        this.post_puplisher = post_puplisher;
    }

    public String getPost_date() {

        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }



}
