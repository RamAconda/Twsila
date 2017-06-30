package com.example.tahasaber.twsila.FragmentClasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tahasaber.twsila.ActivityClasses.MainActivity;
import com.example.tahasaber.twsila.AdapterClasses.ShareRequestAdapter;
import com.example.tahasaber.twsila.DataClasses.RequestDataClass;
import com.example.tahasaber.twsila.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by TahaSaber on 6/12/2017.
 */


public class ShareRequestsFragment extends android.app.Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<RequestDataClass> requests;
    public ArrayList<RequestDataClass> reData = new ArrayList<RequestDataClass>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser mUser;


    @Override
    public void onStop() {
        super.onStop();

        //Toast.makeText(getActivity(), "share on stop", Toast.LENGTH_LONG).show();
        ((MainActivity) getActivity()).setActionBarTitle("Twsila");

    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_LONG).show();
        View rootView = inflater.inflate(R.layout.fragment_share_requests, container, false);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ((MainActivity) getActivity()).setActionBarTitle("Requests");


        requests = new ArrayList<>();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.share_requests_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShareRequestAdapter(requests, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("share_requests").child(mUser.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot request : dataSnapshot.getChildren()) {
                    RequestDataClass onerequest = request.getValue(RequestDataClass.class);
                    requests.add(onerequest);
                }

                mAdapter = new ShareRequestAdapter(requests, getActivity());
                mRecyclerView.setAdapter(mAdapter);


                for (int i = 0; i < requests.size(); i++) {
                    Log.v("sharerequests", requests.get(i).requesterName
                            + "want to share you in this post" +
                            requests.get(i).postBody);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("Failed to read value.", "error");
            }


        });




        return rootView;
    }

}
