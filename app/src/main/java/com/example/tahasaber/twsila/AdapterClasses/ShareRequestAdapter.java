package com.example.tahasaber.twsila.AdapterClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tahasaber.twsila.DataClasses.NotificationDataClass;
import com.example.tahasaber.twsila.DataClasses.RequestDataClass;
import com.example.tahasaber.twsila.R;
import com.example.tahasaber.twsila.UtilityClasses.FirebaseHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by TahaSaber on 6/12/2017.
 */

public class ShareRequestAdapter extends RecyclerView.Adapter<ShareRequestAdapter.ShareRequestViewHolder> {


    ArrayList<RequestDataClass> requests;
    static ArrayList<RequestDataClass> rdc = new ArrayList<RequestDataClass>();
    Context mContext;
    private FirebaseUser mUser;

    public ShareRequestAdapter() {
    }

    public ShareRequestAdapter(ArrayList<RequestDataClass> requests, Context mContext) {
        this.requests = requests;
        this.mContext = mContext;
    }


    @Override
    public ShareRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_request, parent, false);
        ShareRequestViewHolder shareRequestAdapter = new ShareRequestViewHolder(v);

        return shareRequestAdapter;
    }

    @Override
    public void onBindViewHolder(ShareRequestViewHolder holder, final int position) {

        final String requesterName = requests.get(position).requesterName;
        final String postBody = requests.get(position).postBody;
        final String postId = requests.get(position).postId;
        final String requesterId = requests.get(position).requesterId;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.requesterName.setText(requesterName);
        holder.postBody.setText(postBody);


        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseHandler srh=new FirebaseHandler();
                srh.addUserToPostChat(postId,requesterId);
                srh.deleteRequest(postId,requesterId);
                srh.getNumOfAcc(postId);
                NotificationDataClass notification=new NotificationDataClass(postBody,mUser.getDisplayName(),"Accepts");
                srh.Write_share_request_notification(requesterId,notification);


            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseHandler srh = new FirebaseHandler();
                srh.deleteRequest(postId, requesterId);
                NotificationDataClass notification=new NotificationDataClass(postBody,mUser.getDisplayName(),"Rejects");
                srh.Write_share_request_notification(requesterId,notification);

            }
        });


    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ShareRequestViewHolder extends RecyclerView.ViewHolder {

        public TextView requesterName, postBody;
        Button rejectButton, acceptButton;

        public ShareRequestViewHolder(View itemView) {
            super(itemView);

            requesterName = (TextView) itemView.findViewById(R.id.requester_id);
            postBody = (TextView) itemView.findViewById(R.id.share_post_body);
            rejectButton = (Button) itemView.findViewById(R.id.reject_button_id);
            acceptButton = (Button) itemView.findViewById(R.id.accept_button_id);


        }
    }

   /* public void setrequesterid(ArrayList<RequestDataClass> reqs){
        Log.v("ddddddddddddddddd",reqs.get(0).requesterId);
        this.rdc=reqs;

    }*/


}
