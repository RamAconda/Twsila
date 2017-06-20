package com.example.tahasaber.twsila;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by TahaSaber on 4/20/2017.
 */

public class ShareRequestHandler {

   private  static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public  static String postbody="postbody";

    public void sendShareRequest(String publisherID, String postID, String requestID) {



        Log.v("pub_id", publisherID);
        Log.v("post_id", postID);
        Log.v("req_id",requestID);



        DatabaseReference myRef = database.getReference().child("share_requests");


        // myRef.child(Integer.toString(publisherID));
        //  myRef.child(Integer.toString(publisherID)).child(postID).child(Integer.toString(requestID)).setValue(Integer.toString(requestID));
        RequestDataClass request=new RequestDataClass(postID,requestID);
        myRef.child(publisherID).child(postID+request.requesterId).setValue(request);

       /* myRef.child(publisherID);
        myRef.child(publisherID).child(postID).child(Integer.toString(requestID)).setValue(Integer.toString(requestID));*/


    }
    public void getPostBody (String postId){

        DatabaseReference myPostRef=database.getReference("posts").child(postId);

         myPostRef.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PostDataClass onepost=dataSnapshot.getValue(PostDataClass.class);
                setPostbody(onepost.getPost_body());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    public  void setPostbody(String s){
       this.postbody=s;
        Log.v("ssssssss",s);
    }

    public void addToPost_Chat(String postid,String requesterId){

        DatabaseReference myChatpPostRef=database.getReference("posts_chat");
        myChatpPostRef.child("users_id").setValue(requesterId);
        myChatpPostRef.child("posts").setValue(postid);


    }


}
