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

    private  static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public   static String postbody="postbody";
    public   FirebaseUser mUser=FirebaseAuth.getInstance().getCurrentUser();

    public  static  void sendShareRequest(String publisherID,String postID, String requestID,String postbody,String requeerName) {

        DatabaseReference myRef = database.getReference().child("share_requests");
        RequestDataClass request=new RequestDataClass(postID,requestID,requeerName,postbody);
        myRef.child(publisherID).child(postID+request.requesterId).setValue(request);




    }

    public static void addUserToPostChat(String postid,String requesterId){

        DatabaseReference myChatpPostRef=database.getReference("posts_chat");
        myChatpPostRef.child("posts").child(postid).child("users").child(requesterId).setValue("1");

       // myChatpPostRef.child("posts").child(postid).child("chat").setValue("hello");
      //  myChatpPostRef.child("posts").child("users_id").setValue(requesterId);


    }
    public static void write_messege(String msgBody,String publisherId,String postId){
        DatabaseReference myChatpPostRef=database.getReference("posts_chat");
        myChatpPostRef.child("posts").child(postId).child("chat").push().setValue(new MessageDataClass(msgBody,publisherId));
    }

    public void deleteRequest(String postId,String requesterId){

        ////ACCEPT OR REJECT TO DELETE FROM FIREBASE

        DatabaseReference myRefToDelt=database.getReference().child("share_requests")
                .child(mUser.getUid())
                .child(postId+requesterId);
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
        DatabaseReference myRefToupdate=database.getReference().child("posts")
                .child(postId).child("acceptance");
        myRefToupdate.setValue(oldValue-1);
    }




    }




