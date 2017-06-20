package com.example.tahasaber.twsila;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by mohamed on 06/02/17.
 */

public class PostsCardViewAdapter extends RecyclerView.Adapter<PostsCardViewAdapter.PostViewHolder> {

    //PostDataClass[] PostDataClasses;
    ArrayList<PostDataClass> PostDataClasses;
    Context mcContext;
    ShareRequestHandler shareRequestHandler;
    private FirebaseUser mUser;




    //changed to be dynamic adding and removing posts to the ArrayList
    //so I changed the regular array to ArrayList.
    PostsCardViewAdapter(/*PostDataClass[] PostDataClasses*/
                         ArrayList<PostDataClass> PostDataClasses, Context mcContext) {
        this.PostDataClasses = PostDataClasses;
        this.mcContext = mcContext;
    }

    @Override
    public int getItemCount() {
        return PostDataClasses.size();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        PostViewHolder pvh = new PostViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PostViewHolder postViewHolder, int i) {
        final String postId = PostDataClasses.get(i).getPost_id();
        final String publisherId = PostDataClasses.get(i).getUser_id();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        postViewHolder.post_body.setText(PostDataClasses.get(i).getPost_body());
        postViewHolder.post_publisher.setText(PostDataClasses.get(i).getPost_puplisher());
        postViewHolder.post_date.setText(PostDataClasses.get(i).getPost_date());
        postViewHolder.publisher_image.setImageResource(PostDataClasses.get(i).getProfile_picture());
        postViewHolder.category_icon.setImageResource(PostDataClasses.get(i).getCategory_icon());
        postViewHolder.team_counter.setText(String.valueOf(PostDataClasses.get(i).getacceptance()));

        postViewHolder.join_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareRequestHandler = new ShareRequestHandler();
                shareRequestHandler.sendShareRequest(publisherId, postId,mUser.getUid());
                Toast.makeText(mcContext,"Request has sent successfully", Toast.LENGTH_LONG).show();


            }
        });

        postViewHolder.msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shareRequestHandler =new ShareRequestHandler();
                shareRequestHandler.write_messege("hello",mUser.getUid(),postId);
               /* ContentValues values = new ContentValues();
                values.put(ContactsContract.Data.RAW_CONTACT_ID, 001);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "1-800-GOOG-411");
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
                values.put(ContactsContract.CommonDataKinds.Phone.LABEL, "Nirav");
                Uri dataUri = mcContext.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);*/
               /* addContact("twsella_ta", "01121818822");
                Uri uri = Uri.parse("smsto:" + "01121818822");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                mcContext.startActivity(Intent.createChooser(i, ""));*/


            }
        });


    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView post_body;
        public TextView post_publisher;
        public TextView post_date;
        public ImageView publisher_image;
        public Button msg_btn;
        public Button join_button;
        public ImageView category_icon;
        public TextView team_counter;


        public PostViewHolder(View itemView) {
            super(itemView);
            post_body = (TextView) itemView.findViewById(R.id.post_body);
            post_publisher = (TextView) itemView.findViewById(R.id.publisher_name);
            post_date = (TextView) itemView.findViewById(R.id.post_date);
            publisher_image = (ImageView) itemView.findViewById(R.id.profile_image);
            msg_btn = (Button) itemView.findViewById(R.id.msg_btn);
            join_button = (Button) itemView.findViewById(R.id.join_btn);
            category_icon = (ImageView) itemView.findViewById(R.id.icon_image);
            team_counter = (TextView) itemView.findViewById(R.id.counter_id);

        }
    }

    private void addContact(String name, String phone) {

        ContentValues values = new ContentValues();
        values.put(Contacts.People.NUMBER, phone);
        values.put(Contacts.People.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        values.put(Contacts.People.LABEL, name);
        values.put(Contacts.People.NAME, name);
        Uri dataUri = mcContext.getContentResolver().insert(Contacts.People.CONTENT_URI, values);
        Uri updateUri = Uri.withAppendedPath(dataUri, Contacts.People.Phones.CONTENT_DIRECTORY);
        values.clear();
        values.put(Contacts.People.Phones.TYPE, Contacts.People.TYPE_MOBILE);
        values.put(Contacts.People.NUMBER, phone);
        updateUri = mcContext.getContentResolver().insert(updateUri, values);
        Log.d("CONTACT", "" + updateUri);
    }


}
