package com.example.tahasaber.twsila;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


//import com.google.android.gms.appindexing.AppIndex;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private PostsViewFragment postsFragment;
    private PostsLocationConnector postsLocationConnector;

    private Context context = this;

    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    private static final int RC_SIGN_IN = 1;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    //firebase auth variable
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Anaconda", "in activity create function");
        setContentView(R.layout.activity_main);

        //initializing firebase auth and user.
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        initializeLocationWork();
        enableLocation();

        postsFragment = new PostsViewFragment();
        postsLocationConnector = postsFragment;

        //begin the posts fragment logic(code)
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.main_frahgment_container, postsFragment).commit();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //moved to the function defineGoogleApiClient
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
 //                   addUserToDatabaseIfNotThere();
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    private void addUserToDatabaseIfNotThere() {
        Log.d("Anaconda", "in activity addUserToDatabaseIfNotThere function");
        mUser = mAuth.getCurrentUser();
        usersRef.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData user = dataSnapshot.getValue(UserData.class);
                if (user == null) {
                    user = new UserData(mUser.getUid() , mUser.getDisplayName() , mUser.getEmail());
                    usersRef.child(mUser.getUid()).setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Anaconda", "in activity start function");
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Anaconda", "in activity resume function");
        mAuth.addAuthStateListener(authStateListener);
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Anaconda", "in activity pause function");
        mAuth.removeAuthStateListener(authStateListener);
        if(googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Anaconda", "in activity stop function");
        googleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Anaconda", "in activity onActivityResult function");
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                addUserToDatabaseIfNotThere();
            } else if (resultCode == RESULT_CANCELED) {
                //code comes here
                //but we don't need anything right now.
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Anaconda", "in activity onOptionsItemSelected function");
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    public void notifications(View view) {
        Log.d("Anaconda", "in activity notifications function");
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frahgment_container, new NotificationActivity());
        ft.addToBackStack(null);
        ft.commit();

    }

    public void getProfileLayout(View view) {
        Log.d("Anaconda", "in activity getProfileLayout function");
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(intent);


    }

    public void setFavourites(View view) {
        Log.d("Anaconda", "in activity setFavourites function");
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frahgment_container, new FavouritCategoriesFragment());
        ft.addToBackStack(null);
        ft.commit();

    }

    public void setShareRequest(View view) {
        Log.d("Anaconda", "in activity setShareRequest function");
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frahgment_container, new ShareRequestsFragment());
        ft.addToBackStack(null);
        ft.commit();

    }


    private void initializeLocationWork() {
        Log.d("Anaconda", "in activity initializeLocationWork function");
        defineGoogleApiClient();
        defineLocationRequest(10); // get location update every 5 ms, will be changed after the first call
    }

    private void defineGoogleApiClient() {
        Log.d("Anaconda", "in activity defineGoogleApiClient function");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void defineLocationRequest(int interval) {
        Log.d("Anaconda", "in activity defineLocationRequest function");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(interval / 2);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void requestLocationUpdates() {
        Log.d("Anaconda", "in activity requestLocationUpdates function");
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Anaconda", "in activity onConnected function");
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Anaconda", "in activity onConnectionSuspended function");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Anaconda", "in activity onConnectionFailed function");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Anaconda", "in activity onLocationChanged function");

        postsLocationConnector.changeLocation(location);
        int interval = 10*60*1000;
        if(locationRequest.getInterval() < interval){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            defineLocationRequest(interval);
            requestLocationUpdates();
        }
    }

    private void enableLocation(){
        Log.d("Anaconda", "in activity enableLocation function");
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
                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
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