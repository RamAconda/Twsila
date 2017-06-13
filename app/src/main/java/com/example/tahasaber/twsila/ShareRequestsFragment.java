package com.example.tahasaber.twsila;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by TahaSaber on 6/12/2017.
 */


public class ShareRequestsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    RequestItemData obj1 = new RequestItemData("Taha Saber", "السلام عليكم");
    RequestItemData obj2 = new RequestItemData("Mohamed Ali","عرض اربع تيشرتات وواحد هدية");
    RequestItemData obj3 = new RequestItemData("Ahmed Samir", "playing football");
    RequestItemData obj4 = new RequestItemData("Merna Adel", "where is FCI");
    RequestItemData obj5 = new RequestItemData("Ali Ahmed", "انا رايح الجيزة حد جاي معايا");
    RequestItemData obj6 = new RequestItemData("Taha Saber", "where is FCI");

    ArrayList<RequestItemData> requests;
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_LONG).show();

        requests = new ArrayList<RequestItemData>();
        requests.add(obj1);
        requests.add(obj2);
        requests.add(obj3);
        requests.add(obj4);
        requests.add(obj5);
        requests.add(obj6);

        //initialization work of the recycler view
        View rootView = inflater.inflate(R.layout.fragment_share_requests, container, false);



        return recyclerViewInitializationWork(rootView);
    }

    private View recyclerViewInitializationWork(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.share_requests_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShareRequestAdapter(requests, getActivity());
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }






}
