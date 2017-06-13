package com.example.tahasaber.twsila;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by TahaSaber on 4/20/2017.
 */

public class ShareRequestHandler {

    public void sendShareRequest(String publisherID, String postID, int requestID) {


        Log.v("pub_id", publisherID);
        Log.v("post_id", postID);
        Log.v("req_id", Integer.toString(requestID));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("share_requests");

        myRef.child(publisherID);
        myRef.child(publisherID).child(postID).child(Integer.toString(requestID)).setValue(Integer.toString(requestID));


    }


}
