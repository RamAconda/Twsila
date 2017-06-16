package com.example.tahasaber.twsila;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationServices.*;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostsViewFragment extends Fragment implements PostsLocationConnector{
    private static final int PERMISSION_RESOLVER_CODE = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;

    /*********************************************************************************************/

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton writePostButton;
    /*********************************************************************************************/
    //posts array list
    ArrayList<PostDataClass> posts = null;
    //database reference to the posts node in the firebase database
    DatabaseReference postsReference = null;//= FirebaseDatabase.getInstance().getReference().child("/posts");
    DatabaseReference geofireReference = null;
    //defining the geofire object to query posts
    GeoFire geofireToSearchPosts = null;
    //geoquery object
    GeoQuery geoQueryToSearchPosts = null;
    GeoQueryEventListener geoQueryEventListener = null;
    //location variable got from the main activity

    /*********************************************************************************************/

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("logging", "in fragment create function");

        posts = new ArrayList<>();

        //initialization work of the recycler view
        View rootView = inflater.inflate(R.layout.posts_fragment, container, false);

        return recyclerViewInitializationWork(rootView);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Anaconda", "in fragment start function");
        //avoiding redundancy in recycler view
        //if the posts arraylist and the adapter are not null
        //then this fragment has been called before so we need to
        //clear the adapter.
        if (posts != null && mAdapter != null) {
            int size = posts.size();
            posts.clear();
            mAdapter.notifyItemRangeRemoved(0, size);
        }
        if(geoQueryToSearchPosts != null && geoQueryEventListener != null)
            geoQueryToSearchPosts.addGeoQueryEventListener(geoQueryEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Anaconda", "in fragment resume function");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Anaconda", "in fragment stop function");
        Toast.makeText(getActivity(), "onStop", Toast.LENGTH_LONG).show();
        if(geoQueryToSearchPosts != null)
            geoQueryToSearchPosts.removeAllListeners();
    }

    private void firebaseInitializationWork(GeoLocation geoLocation) {
        //if (posts == null)
        //posts = new ArrayList<>();

        //setting up the reference and the geoquery objects
        Log.d("Anaconda", "in fragment firebaseInitializationWork function");
        postsReference = FirebaseDatabase.getInstance().getReference().child("posts");
        geofireReference = FirebaseDatabase.getInstance().getReference().child("geofire");
        geofireToSearchPosts = new GeoFire(geofireReference);

        //set the query on the current location and around the user with 1 kilo meter.

        //InitializeLocation();

        //we update the userLocation when the listener calls the onChangeLocation
        //so when we here update the location a single time then the userLocation will
        //be updated so I can use it in the next line of code.
        //locationManager.requestSingleUpdate("gps", locationListener, null);

        //GeoLocation geoLocation = getLastKnownLocation();

        geoQueryToSearchPosts = geofireToSearchPosts.queryAtLocation(geoLocation, 1);

        //creating the listener and adding it to the geoQueryToSearchPosts.
        attachTheGeoQueryListener();

    }

    private View recyclerViewInitializationWork(View rootView) {
        Log.d("Anaconda", "in fragment recyclerViewInitializationWork function");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostsCardViewAdapter(posts, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        writePostButton = (FloatingActionButton) rootView.findViewById(R.id.write_post_button);
        writePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Snackbar.make(v, "FAB Clicked", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);

            }
        });
        return rootView;
    }

    //function to initialize the geofire query listener
    //and attach it to the geofire query object (geoQueryToSearchPosts)
    private void attachTheGeoQueryListener() {
        Log.d("Anaconda", "in fragment attachTheGeoQueryListener function");
        if (geoQueryEventListener == null) {
            geoQueryEventListener = new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    Log.d("Anaconda", "in fragment onKeyEntered function");
                    //retrieving the post by listening on the post node.
                    RetreivePost retreivePost = new RetreivePost();
                    retreivePost.execute(postsReference.child(key));
                }

                @Override
                public void onKeyExited(String key) {
                    Log.d("Anaconda", "in fragment onKeyExited function");
                    postsReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Anaconda", "in fragment onKeyExited onDataChange function");
                            //deleting the post from the posts array and notify the adapter that
                            //the post the postion has been deleted.
                            PostDataClass post = (PostDataClass) dataSnapshot.getValue();
                            int postPosition = posts.indexOf(post);
                            posts.remove(postPosition);
                            mAdapter.notifyItemRemoved(postPosition);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("Anaconda", "in fragment onCancelled function");
                        }
                    });
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {
                    Log.d("Anaconda", "in fragment onGeoQueryReady function");
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    Log.d("Anaconda", "in fragment onGeoQueryError function");
                }
            };
            //geoQueryToSearchPosts.addGeoQueryEventListener(geoQueryEventListener);
        }
    }

//    /*@Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_RESOLVER_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    locationManager.requestLocationUpdates("gps", *//*10 minutes*//*10 * 60 * 1000, 0, locationListener);
//                    break;
//                }
//            case REQUEST_PERMISSION_CODE:
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    getLastKnownLocation();
//                    break;
//                }
//
//        }
//    }*/

    private boolean firstTimeCallChangeLocation = true;
    @Override
    public void changeLocation(Location location) {
        Log.d("Anaconda", "in fragment changeLocation function");
        GeoLocation geoLocation = new GeoLocation(location.getLatitude() , location.getLongitude());
        if(firstTimeCallChangeLocation) {
            firebaseInitializationWork(geoLocation);
            geoQueryToSearchPosts.addGeoQueryEventListener(geoQueryEventListener);
            firstTimeCallChangeLocation = false;
        }
        else {
            geoQueryToSearchPosts.setCenter(geoLocation);
        }

    }

    public class RetreivePost extends AsyncTask<DatabaseReference,Void,PostDataClass> {
        PostDataClass newPost = null;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected PostDataClass doInBackground(DatabaseReference... postsRef) {
            for(DatabaseReference postRef : postsRef){
                postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Anaconda", "in fragment onKeyEntered onDataChange function");
                        //adding the post to the array.
                        newPost = dataSnapshot.getValue(PostDataClass.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Anaconda", "in fragment onCancelled function");
                    }
                });

            }
            while(newPost == null){
                //waiting for retreiving the post
            }
            return newPost;
        }

        @Override
        protected void onPostExecute(PostDataClass postDataClass) {
            posts.add(0, postDataClass);

            // notifying the adapter that there is
            //an element inserted.
            mAdapter.notifyItemInserted(0);
            //scroll to the beginning of the list
            mRecyclerView.smoothScrollToPosition(0);
        }
    }
}