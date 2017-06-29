package com.example.tahasaber.twsila.FragmentClasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tahasaber.twsila.R;

/**
 * Created by TahaSaber on 2/14/2017.
 */

public class FavouritCategoriesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourit_categories, container, false);


        return rootView;

    }
}
