package com.example.messappl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListAdapter extends BaseAdapter {

Activity activity;
List<Order> orders;

    public ListAdapter(Activity activity, List<Order> orders) {
        this.activity = activity;
        this.orders = orders;
    }

    LayoutInflater inflater;

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View itemView, ViewGroup viewGroup) {

        inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView  = inflater.inflate(R.layout.orderitem, null);

        FirebaseApp.initializeApp(activity.getBaseContext());
        final TextView dateaandtime = (TextView) itemView.findViewById(R.id.dateandtime);


        final TextView info = (TextView) itemView.findViewById(R.id.info);

        dateaandtime.setText(orders.get(i).getDate() + " | " + orders.get(i).getTime());
        info.setText(orders.get(i).getInfo());

        return itemView;
    }
}
