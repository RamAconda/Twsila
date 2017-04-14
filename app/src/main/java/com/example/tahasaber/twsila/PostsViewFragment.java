package com.example.tahasaber.twsila;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostsViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton writePostButton;
    PostDataClass mpost = new PostDataClass(R.drawable.profile, "Mohamed Abd Almageed", "20/2/2017", "أنا في المريوطية هرم ورايح جامعة القاهرة. هاطلب تاكسي حد يحب يركب معي ؟؟؟ ", R.drawable.ic_drive_eta_black_24dp, "3");
    String s2 = " أنا هاحجز ساعة كورة ؟؟";
    String s = "Hello everyone";
    String s3 = "ياجدعان انا قدامي عرض جامد في محل ملابس عند محطة مترو السيدة زينب 3 تشيرت وعليهم 3 هدية حد يشاركني في العرض دة ؟؟؟";
    PostDataClass mpost4 = new PostDataClass(R.drawable.profile, "Taha Saber", "2/5/2017", s2, R.drawable.kora, "6");
    PostDataClass mpost3 = new PostDataClass(R.drawable.me, "Mohamed Gamal", "10/5/2017", s, R.drawable.ic_drive_eta_black_24dp, "2");
    PostDataClass mpost2 = new PostDataClass(R.drawable.rama, "Mohamed Ramadan", "2/5/2017", s3, R.drawable.ic_local_play_black_24dp, "1");
    //changed to arraylist
    //PostDataClass[] arr = {mpost, mpost3, mpost4, mpost2, mpost3, mpost4, mpost2};

    //posts array list
    ArrayList<PostDataClass> posts = null;

    //database reference to the posts node in the firebase database
    DatabaseReference postsReference = null;//= FirebaseDatabase.getInstance().getReference().child("/posts");
    //defining the geofire object to query posts
    GeoFire geofireToSearchPosts = null;
    //geoquery object
    GeoQuery geoQueryToSearchPosts = null;
    //the geolocation variable to send as a parameter to the geoquery object to leasen on.
    GeoLocation geoLocation = null;
    GeoQueryEventListener geoQueryEventListener = null;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        posts = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.posts_fragment, container, false);
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

    private void firebaseWork(){
        //setting up the reference and the geoquery objects
        postsReference = FirebaseDatabase.getInstance().getReference().child("/posts");
        geofireToSearchPosts = new GeoFire(postsReference);

        //set the query on the current location and around the user with 1 kilo meter.
        geoQueryToSearchPosts = geofireToSearchPosts.queryAtLocation(updateLocation() , 1);

        attachTheGeoQueryListener();

    }

    private void attachTheGeoQueryListener() {
        if(geoQueryEventListener == null){
            geoQueryEventListener = new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    //retrieving the post by listening on the post node.
                    postsReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //adding the post to the array.
                            posts.add(0 , (PostDataClass) dataSnapshot.getValue());
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
                            posts.remove(post);
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

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            };
        }
    }

    private GeoLocation updateLocation(){

    }

}