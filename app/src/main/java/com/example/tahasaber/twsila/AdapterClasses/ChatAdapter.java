package com.example.tahasaber.twsila.AdapterClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tahasaber.twsila.DataClasses.MessageDataClass;
import com.example.tahasaber.twsila.R;

import java.util.ArrayList;

//import com.bumptech.glide.Glide;
//import com.example.tahasaber.twsila.MessageDataClass;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    ArrayList<MessageDataClass> messages;
    Context mContext;

    public ChatAdapter() {
    }


    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        ChatAdapter.ChatViewHolder chatAdapter = new ChatAdapter.ChatViewHolder(v);

        return chatAdapter;
    }

    public ChatAdapter(ArrayList<MessageDataClass> messages, Context mContext) {
        this.messages = messages;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ChatViewHolder holder, final int position) {

        final String messageBodyText = messages.get(position).getMsgBody();
        final String messageSenderName = messages.get(position).getMsgPublisher();
       /* if (messages.get(position).getMsgPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
            int colorValue = Color.parseColor("#34AEE8");
            int textColor = Color.parseColor("#ffffff");
            holder.messageBody.setBackgroundColor(colorValue);
            holder.messageBody.setTextColor(textColor);
            holder.messageBody.setBackgroundColor(colorValue);
            holder.messageBody.setTextColor(textColor);

        } else {
            int colorValue = Color.parseColor("#E0E0E0");
            int textColor = Color.parseColor("#000000");
            holder.messageBody.setBackgroundColor(colorValue);
            holder.messageBody.setBackgroundColor(colorValue);
            holder.messageBody.setTextColor(textColor);
            holder.messageBody.setTextColor(textColor);


        }*/
        holder.messageBody.setText(messageBodyText);
        holder.senderName.setText(messageSenderName);


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView messageBody, senderName;

        public ChatViewHolder(View itemView) {
            super(itemView);

            messageBody = (TextView) itemView.findViewById(R.id.messageTextView);
            senderName = (TextView) itemView.findViewById(R.id.nameTextView);

        }
    }
}
