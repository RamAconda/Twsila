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

public class PostsViewFragment extends Fragment {
    private static final int PERMISSION_RESOLVER_CODE = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;

    /*********************************************************************************************/

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton writePostButton;
    /*********************************************************************************************/

    //location variables
    LocationManager locationManager = null;
    LocationListener locationListener = null;
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
    Location location = null;
    GeoLocation userLocation = null;

    /*********************************************************************************************/

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_LONG).show();

        posts = new ArrayList<>();

        //initialization work of the recycler view
        View rootView = inflater.inflate(R.layout.posts_fragment, container, false);

        //initialization work of the firebase database
        firebaseInitializationWork();

        return recyclerViewInitializationWork(rootView);
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "onStart in fragment", Toast.LENGTH_LONG).show();
        //avoiding redundancy in recycler view
        //if the posts arraylist and the adapter are not null
        //then this fragment has been called before so we need to
        //clear the adapter.
        if (posts != null && mAdapter != null) {
            int size = posts.size();
            posts.clear();
            mAdapter.notifyItemRangeRemoved(0, size);
        }
        geoQueryToSearchPosts.addGeoQueryEventListener(geoQueryEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getActivity(), "onStop", Toast.LENGTH_LONG).show();
        geoQueryToSearchPosts.removeAllListeners();
    }

    private void firebaseInitializationWork() {
        //if (posts == null)
        posts = new ArrayList<>();

        //setting up the reference and the geoquery objects
        postsReference = FirebaseDatabase.getInstance().getReference().child("posts");
        geofireReference = FirebaseDatabase.getInstance().getReference().child("geofire");
        geofireToSearchPosts = new GeoFire(geofireReference);

        //set the query on the current location and around the user with 1 kilo meter.
        InitializeLocation();
        //we update the userLocation when the listener calls the onChangeLocation
        //so when we here update the location a single time then the userLocation will
        //be updated so I can use it in the next line of code.
        //locationManager.requestSingleUpdate("gps", locationListener, null);
        GeoLocation geoLocation = getLastKnownLocation();
        geoQueryToSearchPosts = geofireToSearchPosts.queryAtLocation(geoLocation, 1);

        //creating the listener and adding it to the geoQueryToSearchPosts.
        attachTheGeoQueryListener();

    }

/**
    //this function is all about making alert to enable the gps if
    //it's disabled,
    /*private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void enableGPS(){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled" , true);
        getActivity().sendBroadcast(intent);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    private GeoLocation getLastKnownLocation() {
        /*if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }*/

        List<String> providers = locationManager.getProviders(true);
        Location location=null;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity()
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET
            },REQUEST_PERMISSION_CODE);
        }
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                // Found best last known location: %s", l);
                location = l;
            }
        }
        return new GeoLocation(location.getLatitude() , location.getLongitude());

        /*LocationUtils utils = new LocationUtils();
        return utils.getLastLocation(getActivity());*/
    }

    //function to initialize the geofire query listener
    //and attach it to the geofire query object (geoQueryToSearchPosts)
    private void attachTheGeoQueryListener() {

        if (geoQueryEventListener == null) {
            geoQueryEventListener = new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    Toast.makeText(getActivity() , "onKeyEntered" , Toast.LENGTH_LONG).show();
                    //retrieving the post by listening on the post node.
                    postsReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //adding the post to the array.
                            posts.add(0, dataSnapshot.getValue(PostDataClass.class));

                            // notifying the adapter that there is
                            //an element inserted.
                            mAdapter.notifyItemInserted(0);
                            //scroll to the beginning of the list
                            mRecyclerView.smoothScrollToPosition(0);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onKeyExited(String key) {
                    postsReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //deleting the post from the posts array and notify the adapter that
                            //the post the postion has been deleted.
                            PostDataClass post = (PostDataClass) dataSnapshot.getValue();
                            int postPosition = posts.indexOf(post);
                            posts.remove(postPosition);
                            mAdapter.notifyItemRemoved(postPosition);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {
                    Toast.makeText(getActivity() , "onGeoQueryReady" , Toast.LENGTH_LONG).show();
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    Toast.makeText(getActivity() , "onGeoQueryError" , Toast.LENGTH_LONG).show();
                }
            };
            //geoQueryToSearchPosts.addGeoQueryEventListener(geoQueryEventListener);
        }
    }

    private void /*GeoLocation*/ InitializeLocation() {
        //first time to get location, which means there is no listener on the location
        //define the location manager using the parent activity.
        //locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // the location listener logic

        enableLocation();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GeoLocation geoLocation;
                if(location == null){
                    geoLocation = getLastKnownLocation();
                }
                else{
                    geoLocation = new GeoLocation(location.getLatitude(),location.getLongitude());
                }
                if (geoQueryToSearchPosts != null)
                    geoQueryToSearchPosts.setCenter(geoLocation);
                //userLocation = geoLocation;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);*/
                enableLocation();
            }

        };


        /*if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, PERMISSION_RESOLVER_CODE);

        }*/
        locationManager.requestLocationUpdates("gps", /*10 minutes*/10 * 60 * 1000, 0, locationListener);
        //return userLocation;
    }


    private View recyclerViewInitializationWork(View rootView) {
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

    private void enableLocation(){
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            public static final String TAG = "hello";
            public static final int REQUEST_CHECK_SETTINGS = 0;

            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

}