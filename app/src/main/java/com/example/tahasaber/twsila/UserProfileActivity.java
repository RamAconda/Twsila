package com.example.tahasaber.twsila;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by TahaSaber on 2/18/2017.
 */

public class UserProfileActivity extends AppCompatActivity {

    CircleImageView profilePic;
    ImageButton editButton;
    public static TextView userName;
    private static final int CAMERA_PIC_REQUEST = 100;
    Uri imageUri;
    EditText userNameEdit;
    ImageButton editNameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        profilePic = (CircleImageView) findViewById(R.id.profile_image_editable);
        editButton = (ImageButton) findViewById(R.id.myButton);
        editButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        //intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, CAMERA_PIC_REQUEST);
                    }
                });

        editNameButton = (ImageButton) findViewById(R.id.edit_user_name_button);
        userNameEdit = (EditText) findViewById(R.id.name_edit_text);
        editNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditUserNameActivity.class);
                startActivity(intent);


            }
        });

        userName = (TextView) findViewById(R.id.edit_user_name);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_PIC_REQUEST) {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);

        }
    }


}