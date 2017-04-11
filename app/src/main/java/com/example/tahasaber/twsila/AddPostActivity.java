package com.example.tahasaber.twsila;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * Created by mohamed on 07/02/17.
 */
public class AddPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button postButton;
    private EditText writePostEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_post_activity);

        toolbar = (Toolbar) findViewById(R.id.post_action_id);
        setSupportActionBar(toolbar);

        postButton = (Button) findViewById(R.id.post_button);
        writePostEditText = (EditText) findViewById(R.id.write_post_edit_text);


        // spinner addapter
        Spinner spinner = (Spinner) findViewById(R.id.choose_category_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_toPostIn, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        // Enable PostDataClass button when if anyone write anything in edit text
        writePostEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    postButton.setEnabled(true);
                } else {
                    postButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String postContent = writePostEditText.toString();
                PostDataClass newPost = new PostDataClass(R.drawable.rama, "Mohamed Ramadan", "2/5/2017", postContent, R.drawable.ic_local_play_black_24dp, "10");


            }
        });


    }
}
