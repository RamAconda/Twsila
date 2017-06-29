package com.example.tahasaber.twsila.ActivityClasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tahasaber.twsila.R;

/**
 * Created by TahaSaber on 2/18/2017.
 */

public class EditUserNameActivity extends AppCompatActivity {

    Button okButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        okButton = (Button) findViewById(R.id.ok_btn);
        editText = (EditText) findViewById(R.id.name_edit_text);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editText.getText().toString();
                if (name != null) {
                    UserProfileActivity.userName.setText(name);
                    finish();

                } else {
                    finish();
                }


            }
        });

    }
}
