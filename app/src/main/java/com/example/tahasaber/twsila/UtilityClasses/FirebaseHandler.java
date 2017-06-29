package com.example.tahasaber.twsila.UtilityClasses;

import android.app.Activity;
import android.util.Log;

import com.example.tahasaber.twsila.DataClasses.NotificationDataClass;
import com.example.tahasaber.twsila.DataClasses.PostDataClass;
import com.example.tahasaber.twsila.DataClasses.RequestDataClass;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
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

public class FirebaseHandler extends Activity{


    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static String postbody = "postbody";
    private static DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofire");
    private static GeoFire geoFire = new GeoFire(ref);
    private static FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();


    public static void sendShareRequest(String publisherID, String postID, String requestID, String postbody, String requeerName) {

        DatabaseReference myRef = database.getReference().child("share_requests");


        RequestDataClass request = new RequestDataClass(postID, requestID, requeerName, postbody);
        myRef.child(publisherID).child(postID + request.requesterId).setValue(request);


    }

    public static void addUserToPostChat(String postid, String requesterId) {

        DatabaseReference myChatpPostRef = database.getReference("posts_chat");
        myChatpPostRef.child("posts").child(postid).child("users").child(requesterId).setValue("1");

        // myChatpPostRef.child("posts").child(postid).child("chat").setValue("hello");
        //  myChatpPostRef.child("posts").child("users_id").setValue(requesterId);


    }
   /* public static void write_messege(String msgBody,String publisherId,String postId){
        DatabaseReference myChatpPostRef=database.getReference("posts_chat");
        myChatpPostRef.child("posts").child(postId).child("chat").push().setValue(new MessageDataClass(msgBody,publisherId));
    }*/

    public void deleteRequest(String postId, String requesterId) {

        ////ACCEPT OR REJECT TO DELETE FROM FIREBASE

        DatabaseReference myRefToDelt = database.getReference().child("share_requests")
                .child(mUser.getUid())
                .child(postId + requesterId);
        myRefToDelt.removeValue();

    }

    public void getNumOfAcc(final String postId){

        DatabaseReference myRefToupdate=database.getReference().child("posts")
                .child(postId);//.child("acceptance")
        myRefToupdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   PostDataClass postToUpdate=dataSnapshot.getValue(PostDataClass.class);

                    updateNOfAcc(postId,postToUpdate.getacceptance());

                    Log.v("pppppppppoooooooooo",postToUpdate.getacceptance()+"");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("TagSrh","error_oncanclled");

            }
        });

    }
    public void updateNOfAcc(String postId,int oldValue){


            DatabaseReference myRefToupdate = database.getReference().child("posts")
                    .child(postId).child("acceptance");
            myRefToupdate.setValue(oldValue - 1);


    }

    public static void writePostToFirebase(PostDataClass post,GeoLocation geoLocation){

        // write the new post to fire base

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("posts");

        String myPostKey = myRef.push().getKey();

        //write user id to post node in chat_posts node

        FirebaseHandler.addUserToPostChat(myPostKey,mUser.getUid());

        //set post id with the firebase key
        post.setPost_id(myPostKey);

        myRef.child(myPostKey).setValue(post);


        // write loction of this post in geofire node


        geoFire.setLocation(myPostKey,geoLocation);


    }
    public static void Write_share_request_notification(String userId, NotificationDataClass notification){
        DatabaseReference myNotifRef=database.getReference().child("share_request_notification").child(userId).push();
        myNotifRef.setValue(notification);

    }


}




