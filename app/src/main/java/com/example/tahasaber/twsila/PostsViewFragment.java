package com.example.tahasaber.twsila;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PostsViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton writePostButton;
    String s2 = " أنا هاحجز ساعة كورة ؟؟";


    PostDataClass mpost = new PostDataClass(s2,10,"sa3etKora",R.drawable.rama,"12/12/2013",true,R.drawable.ic_local_play_black_24dp,20130208,"Mohamed Abd Almageed");

   /* String s = "Hello everyone";
    String s3 = "ياجدعان انا قدامي عرض جامد في محل ملابس عند محطة مترو السيدة زينب 3 تشيرت وعليهم 3 هدية حد يشاركني في العرض دة ؟؟؟";
    PostDataClass mpost4 = new PostDataClass(R.drawable.profile, "Taha Saber", "2/5/2017", s2, R.drawable.kora, "6");
    PostDataClass mpost3 = new PostDataClass(R.drawable.me, "Mohamed Gamal", "10/5/2017", s, R.drawable.ic_drive_eta_black_24dp, "2");
    PostDataClass mpost2 = new PostDataClass(R.drawable.rama, "Mohamed Ramadan", "2/5/2017", s3, R.drawable.ic_local_play_black_24dp, "1");*/
    PostDataClass[] arr = {mpost, mpost, mpost, mpost, mpost, mpost, mpost};


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.posts_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostsCardViewAdapter(arr, getActivity());
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


}