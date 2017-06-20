package com.example.tahasaber.twsila;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


public class ShareRequestsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<RequestItemData> requests;
    public ArrayList<RequestDataClass> reData=new ArrayList<RequestDataClass>();
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private FirebaseUser mUser;


    RequestItemData obj1 = new RequestItemData("Taha Saber", "السلام عليكم");
    RequestItemData obj2 = new RequestItemData("Mohamed Ali","عرض اربع تيشرتات وواحد هدية");
    RequestItemData obj3 = new RequestItemData("Ahmed Samir", "playing football");
    RequestItemData obj4 = new RequestItemData("Merna Adel", "where is FCI");
    RequestItemData obj5 = new RequestItemData("Ali Ahmed", "انا رايح الجيزة حد جاي معايا");
    RequestItemData obj6 = new RequestItemData("Taha Saber", "where is FCI");


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_LONG).show();
        View rootView = inflater.inflate(R.layout.fragment_share_requests, container, false);
        mUser = FirebaseAuth.getInstance().getCurrentUser();




        requests = new ArrayList<>();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.share_requests_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShareRequestAdapter(requests, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference().child("share_requests").child(mUser.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requests.clear();
                for(DataSnapshot request:dataSnapshot.getChildren()) {
                    RequestDataClass onerequest = request.getValue(RequestDataClass.class);
                    reData.add(onerequest);
                    ShareRequestHandler srh=new ShareRequestHandler();
                    srh.getPostBody(onerequest.postId);
                    RequestItemData reqItem=new RequestItemData(onerequest.requesterId,srh.postbody);
                    requests.add(reqItem);
                    Log.v("megooooooooooooooo", onerequest.requesterId);
                }
                ShareRequestAdapter srad=new ShareRequestAdapter();
                srad.rdc=reData;
                mAdapter = new ShareRequestAdapter(requests, getActivity());
                mRecyclerView.setAdapter(mAdapter);


                for(int i=0;i<requests.size();i++){
                    Log.v("sharerequests",requests.get(i).getPostBody()
                            +"want to share you in this post"+
                            requests.get(i).getRequesterName());}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("Failed to read value.","error");
            }
        });


        return rootView;
    }

}
