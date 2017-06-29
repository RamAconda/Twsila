package com.example.tahasaber.twsila.ActivityClasses;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahasaber.twsila.UtilityClasses.FirebaseHandler;
import com.example.tahasaber.twsila.DataClasses.PostDataClass;
import com.example.tahasaber.twsila.R;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by mohamed on 07/02/17.
 */
public class AddPostActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Toolbar toolbar;
    private TextView publisher;
    private Button postButton;
    private EditText writePostEditText;
    private EditText NumberOfAcceptance;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String category;
    private FirebaseUser mUser;
    private FirebaseHandler firebaseHandler;
    private Map<String,Integer> categoryIconMap=new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_post);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = (Toolbar) findViewById(R.id.post_action_id);
        setSupportActionBar(toolbar);

        publisher = (TextView) findViewById(R.id.publisher_name);
        publisher.setText(mUser.getDisplayName());

        postButton = (Button) findViewById(R.id.post_button);

        writePostEditText = (EditText) findViewById(R.id.write_post_edit_text);

        NumberOfAcceptance = (EditText) findViewById(R.id.Number_of_acceptance);

        // GoogleAPIClient instantiation.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // spinner addapter
        Spinner spinner = (Spinner) findViewById(R.id.choose_category_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_toPostIn, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(adapterView.getContext(),
                        "Your selected category is : " + adapterView.getItemAtPosition(i).toString(),
                        Toast.LENGTH_SHORT).show();
                category = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Enable PostDataClass button when if anyone write anything in edit text
        writePostEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    postButton.setEnabled(true);
                } else {
                    postButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // initialisation category map

        categoryIconMap.put("Erkab M3ana",R.drawable.ic_drive_eta_black_24dp);
        categoryIconMap.put("Sa3et Kora",R.drawable.kora);
        categoryIconMap.put("Specialist",R.drawable.ic_specialist_24dp);
        categoryIconMap.put("Egarat",R.drawable.ic_egarat_24dp);
        categoryIconMap.put("A3lanat",R.drawable.ic_a3lanat_black_24dp);
        categoryIconMap.put("Others",R.drawable.ic_other_black_24dp);


        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String postContent = writePostEditText.getText().toString();
                String str_nofacceptance = NumberOfAcceptance.getText().toString();
                int nofAcceptance;

                //get current Date and time
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm EEE dd/MM");
                String mycurrentDate = dateFormat.format(date);

                //check number of acceptanc != empty
                if (!str_nofacceptance.isEmpty()) {
                    if(isNumeric(str_nofacceptance)) {
                        nofAcceptance = Integer.parseInt(str_nofacceptance);
                    }
                    else{

                        nofAcceptance=-1;
                    }
                } else {
                    nofAcceptance = 0;
                }

                if (isLocationEnabled(getBaseContext())) {
                    // if nunber of acceptance =0 and the category is not A3lanat or other so the user must enter
                    // valid number.
                    if(nofAcceptance==-1){
                        Toast.makeText(getBaseContext(),"You have to enter a vlid number",Toast.LENGTH_SHORT).show();
                    }
                    else if (nofAcceptance == 0 && !category.equals("A3lanat")&&!category.equals("Others")) {
                        Toast.makeText(getBaseContext(), "You Have To set Number of acceptance Greater Than 0", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        PostDataClass newPost = new PostDataClass(postContent, nofAcceptance, category,
                                R.drawable.anonymous, mycurrentDate, true,categoryIconMap.get(category), mUser.getUid(), mUser.getDisplayName());
                        FirebaseHandler.writePostToFirebase(newPost, geGeoLocationObject());
                      //  firebaseHandler.addUserToPostChat(newPost.getPost_id(), newPost.getUser_id());

                        Toast.makeText(getBaseContext(), "DONE -_-", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);

                    }

                } else {
                    Toast.makeText(getBaseContext(), "You Have To Enable GPS", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public GeoLocation geGeoLocationObject() {

        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();
        Log.v("POST", String.valueOf(mLastLocation.getLatitude()));

        return new GeoLocation(latitude, longitude);
    }

    @Override
    public void onConnected(Bundle bundle) {

        // permission check.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.v("POST", String.valueOf(mLastLocation.getLatitude()));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public static boolean isNumeric(String str)
    {
        try
        {
            int n = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }


}
