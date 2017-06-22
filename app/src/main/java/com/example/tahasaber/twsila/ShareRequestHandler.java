package com.example.tahasaber.twsila;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by TahaSaber on 4/20/2017.
 */

public class ShareRequestHandler {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static String postbody = "postbody";
    public FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    public void sendShareRequest(String publisherID, String postID, String requestID) {


        Log.v("pub_id", publisherID);
        Log.v("post_id", postID);
        Log.v("req_id", requestID);


        DatabaseReference myRef = database.getReference().child("share_requests");


        // myRef.child(Integer.toString(publisherID));
        //  myRef.child(Integer.toString(publisherID)).child(postID).child(Integer.toString(requestID)).setValue(Integer.toString(requestID));
        RequestDataClass request = new RequestDataClass(postID, requestID);
        myRef.child(publisherID).child(postID + request.requesterId).setValue(request);

       /* myRef.child(publisherID);
        myRef.child(publisherID).child(postID).child(Integer.toString(requestID)).setValue(Integer.toString(requestID));*/


    }

    public void getPostBody(String postId) {

        DatabaseReference myPostRef = database.getReference("posts").child(postId);

        myPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PostDataClass onepost = dataSnapshot.getValue(PostDataClass.class);
                setPostbody(onepost.getPost_body());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void setPostbody(String s) {
        this.postbody = s;
        Log.v("ssssssss", s);
    }

    public void addToPost_Chat(String postid, String requesterId) {

        DatabaseReference myChatpPostRef = database.getReference("posts_chat");
        myChatpPostRef.child("posts").child(postid).child(requesterId).setValue("1");
        // myChatpPostRef.child("posts").child(postid).child("chat").setValue("hello");
        //  myChatpPostRef.child("posts").child("users_id").setValue(requesterId);


    }

    public void write_messege(String msgBody, String publisherId, String postId) {
        DatabaseReference myChatpPostRef = database.getReference("posts_chat");
        myChatpPostRef.child("posts").child(postId).child("chat").push().setValue(new MessageDataClass(msgBody, publisherId));
    }

    public void deleteRequest(String postId, String requesterId) {
        ////ACCEPT OR REJECT TO DELETE FROM FIREBASE
        DatabaseReference myRefToDelt = database.getReference().child("share_requests")
                .child(mUser.getUid())
                .child(postId + requesterId);
        myRefToDelt.removeValue();

    }


}




