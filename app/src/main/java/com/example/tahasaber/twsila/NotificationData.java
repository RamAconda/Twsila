package com.example.tahasaber.twsila;

import java.sql.Time;

/**
 * Created by TahaSaber on 2/6/2017.
 */

public class NotificationData {

    private int image_resource;
    private String publisher_name;
    private String category;
    private Time time;

    NotificationData(int image_resource, String publisher_name, String category){
        this.setImage_resource(image_resource);
        this.setPublisher_name(publisher_name);
        this.setCategory(category);

    }

    public int getImage_resource() {
        return image_resource;
    }

    public void setImage_resource(int image_resource) {
        this.image_resource = image_resource;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
