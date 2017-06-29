package com.example.tahasaber.twsila.AdapterClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tahasaber.twsila.DataClasses.NotificationDataClass;
import com.example.tahasaber.twsila.R;

import java.util.ArrayList;

/**
 * Created by TahaSaber on 2/6/2017.
 */

public class NotificationListAdapter extends ArrayAdapter<NotificationDataClass> {

    private final Context context;
    private ArrayList<NotificationDataClass> notifications;

public NotificationListAdapter(Context context,ArrayList<NotificationDataClass> notifications){
    super(context,-1,notifications);
    this.context=context;
    this.notifications=notifications;
}

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_notification, parent, false);

        TextView textView=(TextView) rowView.findViewById(R.id.post_publisher_name);
        TextView textViewPostBody=(TextView) rowView.findViewById(R.id.post_body_notif);
        TextView type=(TextView) rowView.findViewById(R.id.type);
        textView.setText(notifications.get(position).getPostPublisherName());
        textViewPostBody.setText(notifications.get(position).getPostBody());
        type.setText(notifications.get(position).getType());


        return rowView;
    }


    //    public NotificationListAdapter(Context context, int resource) {
//        super(context, resource);
//    }
//
//    static class DataHandler {
//
//        ImageView img;
//        TextView name;
//        TextView cat;
//
//    }
//
//    @Override
//    public void add(Object object) {
//        super.add(object);
//        list.add(object);
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return this.list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return this.list.get(position);
//
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View row = convertView;
//        DataHandler handler;
//        if (convertView == null) {
//
//            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row = inflater.inflate(R.layout.item_notification, parent, false);
//            handler = new DataHandler();
//            handler.img = (ImageView) row.findViewById(R.id.noti_img);
//            handler.name = (TextView) row.findViewById(R.id.noti_name);
//            handler.cat = (TextView) row.findViewById(R.id.cat_id);
//            row.setTag(handler);
//        } else {
//            handler = (DataHandler) row.getTag();
//        }
//
//        NotificationDataClass notificationDataClass;
//        notificationDataClass = (NotificationDataClass) this.getItem(position);
//        handler.img.setImageResource(notificationDataClass.getImage_resource());
//        handler.name.setText(notificationDataClass.getPublisher_name());
//        handler.cat.setText(notificationDataClass.getCategory());
//
//        return row;
//
//    }
}
