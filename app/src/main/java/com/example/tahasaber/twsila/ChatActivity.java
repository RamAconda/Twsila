package com.example.tahasaber.twsila;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by TahaSaber on 6/23/2017.
 */

public class ChatActivity extends Activity {

    private static final String TAG = "MainActivity";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER = 2;

    public static final int RC_SIGN_IN = 1;

    private ListView mMessageListView;
    private ChatAdapter chatAdapter;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private Button mSendButton;
    private TextView postOwnerTextView;
    private TextView postBodyTextView;


    private FirebaseUser mUser;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseUser mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        postOwnerTextView = (TextView) findViewById(R.id.post_owner_name_in_chat);
        postBodyTextView = (TextView) findViewById(R.id.text_inchat);


        Bundle bundle = getIntent().getExtras();
        final String postOwner = bundle.getString("publisherName");
        String postBody = bundle.getString("postBody");
        String postId = bundle.getString("postId");


        if (postBody.length() > 15) {
            postBody = postBody.substring(0, 15);
        }

        postOwnerTextView.setText(postOwner);
        postBodyTextView.setText(postBody);


        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //Toast.makeText(getApplicationContext(), postId, Toast.LENGTH_LONG).show();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("posts_chat").child("posts").child(postId).child("chat");
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        List<MessageDataClass> friendlyMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(chatAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);


        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // to limit message limit (message length)
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageDataClass friendlyMessage = new MessageDataClass(mMessageEditText.getText().toString(), mUser.getDisplayName());
                mMessagesDatabaseReference.push().setValue(friendlyMessage);

                // Clear input box
                mMessageEditText.setText("");
            }
        });

        attachDatabaseReaderListener();

    }

    private void attachDatabaseReaderListener() {

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MessageDataClass friendlyMessage = dataSnapshot.getValue(MessageDataClass.class);
                    chatAdapter.add(friendlyMessage);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }

    private void detachDatabaseReaderListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeValue((DatabaseReference.CompletionListener) mChildEventListener);
            mChildEventListener = null;
        }

    }


}
