package com.example.tahasaber.twsila;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TahaSaber on 2/6/2017.
 */

public class NotificationListAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public NotificationListAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler {

        ImageView img;
        TextView name;
        TextView cat;

    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);


    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        DataHandler handler;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.notification_item, parent, false);
            handler = new DataHandler();
            handler.img = (ImageView) row.findViewById(R.id.noti_img);
            handler.name = (TextView) row.findViewById(R.id.noti_name);
            handler.cat = (TextView) row.findViewById(R.id.cat_id);
            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }

        NotificationData notificationData;
        notificationData = (NotificationData) this.getItem(position);
        handler.img.setImageResource(notificationData.getImage_resource());
        handler.name.setText(notificationData.getPublisher_name());
        handler.cat.setText(notificationData.getCategory());

        return row;

    }
}
