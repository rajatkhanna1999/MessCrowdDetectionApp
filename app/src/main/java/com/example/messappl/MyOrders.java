package com.example.messappl;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MyOrders extends AppCompatActivity {

    android.support.v7.widget.Toolbar mToolbar;
    ImageView addorder;

    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    AlertDialog alertDialog;
    String time;
    String id;
    SpotsDialog dialog;

    ListView listdata;
    List<Order> bookingsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id = FirebaseAuth.getInstance().getUid();

        listdata = (ListView)findViewById(R.id.listofbookings);
        addEventFirebaselistener();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.order, null);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        final TextView txtDate, txtTime;
        Button submit = (Button) view.findViewById(R.id.submit);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtTime = (TextView) view.findViewById(R.id.txtTime);
        dialog = new SpotsDialog(this);

        addorder = (ImageView) findViewById(R.id.addorder);
        alertDialog.setCancelable(false);
        addorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog dialog = new DatePickerDialog(
                        MyOrders.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListener
                        , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();

            }
        });

        final EditText info = (EditText)view.findViewById(R.id.info);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                dialog.setMessage("Please wait..");
                Map<String , Object> map = new HashMap<String, Object>();
                map.put("date" , txtDate.getText());
                map.put("time" , txtTime.getText());
                map.put("info" ,info.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("MyOrders")
                        .child(id)
                        .push()
                        .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        alertDialog.dismiss();
                        Toast.makeText(MyOrders.this, "Submitted ! ", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(MyOrders.this, "Failed ! "+ e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();

                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);

                Calendar calendar = Calendar.getInstance();
                TimePickerDialog dialog = new TimePickerDialog(
                        MyOrders.this, android.R.style.Theme_Holo_Dialog_MinWidth, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
                );
                dialog.show();


            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                Calendar calendar = Calendar.getInstance();
                if (dayOfMonth == calendar.get(Calendar.DAY_OF_MONTH)) {
                    time = "today";
                } else {

                    time = "nottoday";
                }
                String monthh = null;

                switch (month) {
                    case 1:
                        monthh = "January";
                        break;
                    case 2:
                        monthh = "February";
                        break;
                    case 3:
                        monthh = "March";
                        break;
                    case 4:
                        monthh = "April";
                        break;
                    case 5:
                        monthh = "May";
                        break;
                    case 6:
                        monthh = "June";
                        break;
                    case 7:
                        monthh = "July";
                        break;
                    case 8:
                        monthh = "August";
                        break;
                    case 9:
                        monthh = "September";
                        break;
                    case 10:
                        monthh = "October";
                        break;
                    case 11:
                        monthh = "November";
                        break;
                    case 12:
                        monthh = "December";

                        break;


                }
                String date = monthh + " " + dayOfMonth + " , " + year;

                txtDate.setText(date);

            }
        };

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }
                if (hourOfDay > 12)
                    hourOfDay = hourOfDay - 12;


                String time = hourOfDay + " : " + minute + " " + AM_PM;
                txtTime.setText(time);


            }
        };

    }

    private void addEventFirebaselistener() {

        FirebaseDatabase.getInstance().getReference().child("MyOrders").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (bookingsList.size() > 0)
                    bookingsList.clear();
                if (dataSnapshot != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Order order = dataSnapshot1.getValue(Order.class);
                        bookingsList.add(order);
                    }
                    ListAdapter adapter = new ListAdapter(MyOrders.this, bookingsList);

                    TextView t = (TextView) findViewById(R.id.t);

                    listdata.setEmptyView(t);

                    listdata.setAdapter(adapter);

                    // showt();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
