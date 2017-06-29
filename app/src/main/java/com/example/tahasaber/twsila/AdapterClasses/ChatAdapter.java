package com.example.tahasaber.twsila.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tahasaber.twsila.DataClasses.MessageDataClass;
import com.example.tahasaber.twsila.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

//import com.bumptech.glide.Glide;

public class ChatAdapter extends ArrayAdapter<MessageDataClass> {
    public ChatAdapter(Context context, int resource, List<MessageDataClass> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        MessageDataClass message = getItem(position);


        messageTextView.setVisibility(View.VISIBLE);

        if(message.getMsgPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
            int colorValue = Color.parseColor("#34AEE8");
            int textColor = Color.parseColor("#ffffff");
            messageTextView.setBackgroundColor(colorValue);
            messageTextView.setTextColor(textColor);
            authorTextView.setBackgroundColor(colorValue);
            authorTextView.setTextColor(textColor);

        }

        else {
            int colorValue = Color.parseColor("#E0E0E0");
            int textColor = Color.parseColor("#000000");
            messageTextView.setBackgroundColor(colorValue);
            authorTextView.setBackgroundColor(colorValue);
            messageTextView.setTextColor(textColor);
            authorTextView.setTextColor(textColor);


        }
        messageTextView.setText(message.getMsgBody());
        authorTextView.setText(message.getMsgPublisher());

        return convertView;
    }
}
