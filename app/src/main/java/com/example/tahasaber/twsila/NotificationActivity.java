package com.example.tahasaber.twsila;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NotificationActivity extends Fragment {

    int imgs[] = {R.drawable.me, R.drawable.me, R.drawable.rama, R.drawable.rama, R.drawable.rama, R.drawable.me, R.drawable.me, R.drawable.rama, R.drawable.me, R.drawable.rama};
    ListView listView;
    String publisher[] = {"Taha Saber", "Mohamed Ramadan", "Mohamed Khalifa", "Abd El Madeed", "Adel Ahmed", "Mohamed Taher", "Khalid Osama", "Taha Saber", "Taha Saber", "Taha Saber"};
    String category[] = {"Medical", "Games", "professionals", "Medical", "Games", "professionals", "Medical", "Games", "professionals", "Tawsela"};
    NotificationListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);

        listView = (ListView) rootView.findViewById(R.id.noti_list);
        adapter = new NotificationListAdapter(getActivity(), R.layout.notification_item);
        listView.setAdapter(adapter);
        int i = 0;
        for (String x : publisher) {
            NotificationData notificationData = new NotificationData(imgs[i], publisher[i], category[i]);
            adapter.add(notificationData);
            i++;
        }
        return rootView;
    }
}
