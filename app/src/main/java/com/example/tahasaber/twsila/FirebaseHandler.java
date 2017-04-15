package com.example.tahasaber.twsila;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mohamed on 13/04/17.
 */

public class FirebaseHandler {


    private static DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofire");
    private static GeoFire geoFire = new GeoFire(ref);


    public static void writePostToFirebase(PostDataClass post,GeoLocation geoLocation){

        // write the new post to fire base

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("posts");

        String myPostKey = myRef.push().getKey();

        //set post id with the firebase key
        post.setPost_id(myPostKey);

        myRef.child(myPostKey).setValue(post);


        // write loction of this post in geofire node


        geoFire.setLocation(myPostKey,geoLocation);


    }
}
