package com.example.tahasaber.twsila;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by anaconda on 6/16/17.
 */

public class RetreivePost extends AsyncTask<DatabaseReference,void,PostDataClass> {
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected PostDataClass doInBackground(DatabaseReference... postsRef) {
        for(DatabaseReference postRef : postsRef){
            postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(PostDataClass postDataClass) {
        super.onPostExecute(postDataClass);
    }
}
