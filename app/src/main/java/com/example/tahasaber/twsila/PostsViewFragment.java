package com.example.tahasaber.twsila;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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


    /*String s2 = " أنا هاحجز ساعة كورة ؟؟";


    PostDataClass mpost = new PostDataClass(s2,10,"sa3etKora",R.drawable.rama,"12/12/2013",true,R.drawable.ic_local_play_black_24dp,20130208,"Mohamed Abd Almageed");

    String s = "Hello everyone";
    String s3 = "ياجدعان انا قدامي عرض جامد في محل ملابس عند محطة مترو السيدة زينب 3 تشيرت وعليهم 3 هدية حد يشاركني في العرض دة ؟؟؟";
    PostDataClass mpost4 = new PostDataClass(R.drawable.profile, "Taha Saber", "2/5/2017", s2, R.drawable.kora, "6");
    PostDataClass mpost3 = new PostDataClass(R.drawable.me, "Mohamed Gamal", "10/5/2017", s, R.drawable.ic_drive_eta_black_24dp, "2");
<<<<<<< HEAD
    PostDataClass mpost2 = new PostDataClass(R.drawable.rama, "Mohamed Ramadan", "2/5/2017", s3, R.drawable.ic_local_play_black_24dp, "1");
    //changed to arraylist
    //PostDataClass[] arr = {mpost, mpost3, mpost4, mpost2, mpost3, mpost4, mpost2};


=======
    PostDataClass mpost2 = new PostDataClass(R.drawable.rama, "Mohamed Ramadan", "2/5/2017", s3, R.drawable.ic_local_play_black_24dp, "1");*//*
    *//*PostDataClass[] arr = {mpost, mpost, mpost, mpost, mpost, mpost, mpost};
>>>>>>> c5353a62e04f31d7b954d4cf4384d2f129b5c994*/

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
        Toast.makeText(getActivity(), "onStart", Toast.LENGTH_LONG).show();
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
        updateLocation();
        //we update the userLocation when the listener calls the onChangeLocation
        //so when we here update the location a single time then the userLocation will
        //be updated so I can use it in the next line of code.
        //locationManager.requestSingleUpdate("gps", locationListener, null);
        geoQueryToSearchPosts = geofireToSearchPosts.queryAtLocation(
                /*userLocation*/getLastKnownLocation()/*new GeoLocation(29.9061584,31.2710861)*/, 1);

        //creating the listener and adding it to the geoQueryToSearchPosts.
        attachTheGeoQueryListener();

    }

    private GeoLocation getLastKnownLocation() {
        GeoLocation geoLocation = null;
        List<String> providers = locationManager.getProviders(true);
        Location location=null;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        geoLocation = new GeoLocation(location.getLatitude() , location.getLongitude());
        return geoLocation;
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

    private GeoLocation updateLocation() {
        //first time to get location, which means there is no listener on the location
        if (locationManager == null) {
            //define the location manager using the parent activity.
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            // the location listener logic
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (geoQueryToSearchPosts != null)
                        geoQueryToSearchPosts.setCenter(new GeoLocation(location.getLatitude(),
                                location.getLongitude()));
                    userLocation = new GeoLocation(location.getLatitude(),
                            location.getLongitude());
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

            };


            if (ActivityCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET}, PERMISSION_RESOLVER_CODE);

            }
            locationManager.requestLocationUpdates("gps", /*10 minutes*/10 * 60 * 1000, 0, locationListener);
        }
        return userLocation;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RESOLVER_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates("gps", /*10 minutes*/10 * 60 * 1000, 0, locationListener);
                    break;
                }
            case REQUEST_PERMISSION_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLastKnownLocation();
                    break;
                }

        }
    }
}