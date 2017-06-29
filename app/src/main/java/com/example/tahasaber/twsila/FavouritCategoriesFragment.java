package com.example.tahasaber.twsila;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by TahaSaber on 2/14/2017.
 */

public class FavouritCategoriesFragment extends android.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourit_categories, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Favourites");



        return rootView;

    }

    @Override
    public void onStop() {
        super.onStop();

        //Toast.makeText(getActivity(), "share on stop", Toast.LENGTH_LONG).show();
        ((MainActivity) getActivity()).setActionBarTitle("Twsila");

    }
}
