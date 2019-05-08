package com.example.messappl;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialOverlayLayout;
import com.leinardi.android.speeddial.SpeedDialView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;

import static com.example.messappl.R.drawable.sundaybreakfast;

public class Home extends AppCompatActivity {

    android.support.v7.widget.Toolbar mToolbar;
    FloatingActionButton contactpopup;

    SpeedDialOverlayLayout speedDialOverlayLayout;
    SpeedDialView speedDialView;
    android.support.v7.app.AlertDialog contactpopupdialog;
    CountDownTimer time;
    ImageView breakfastImage , LunchImage , DinnerImage;
    TextView BT , LT , DT;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.contactpopup, null);
        builder.setView(view);
        contactpopupdialog = builder.create();
        contactpopupdialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;


        speedDialOverlayLayout = (SpeedDialOverlayLayout) findViewById(R.id.overlay);
        speedDialView = findViewById(R.id.speedDial);

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_FaceDetection, R.drawable.face)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Face Detection")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_MyOrders, R.drawable.orders)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("My Orders")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_Schedule, R.drawable.readlines)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Schedule")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .create()
        );

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.nav_Schedule:
                        Intent intent2 = new Intent(Home.this, Schedule.class);
                        startActivity(intent2);
                        return false;
                    case R.id.nav_MyOrders:
                        Intent intent = new Intent(Home.this, MyOrders.class);
                        startActivity(intent);
                        return false;
                    case R.id.nav_FaceDetection:
                        Intent intent1 = new Intent(Home.this, FacesDetection.class);
                        startActivity(intent1);
                        return false;
                    default:
                        return false;
                }
            }
        });


        breakfastImage = (ImageView)findViewById(R.id.breakfastImage);
        BT = (TextView)findViewById(R.id.BT);
        LunchImage = (ImageView)findViewById(R.id.LunchImage);
        LT = (TextView)findViewById(R.id.LT);
        DT = (TextView)findViewById(R.id.DT);
        DinnerImage = (ImageView)findViewById(R.id.DinnerImage);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        TextView date = (TextView)findViewById(R.id.date);
        date.setText(formattedDate + " , " + dayOfTheWeek);
        switch (day) {
            case Calendar.SUNDAY:
                BT.setText("Dosa , Sambhar and Narial Chutney.");
                LT.setText("Soya Chaap , Arhar Daal and Dahi.");
                DT.setText("Butter Paneer Masala , Non Veg. , Masoor Daal , Tandoori Roti , IceCream/Pastry");
               breakfastImage.setImageDrawable(getDrawable(R.drawable.sundaybreakfast));
                LunchImage.setImageDrawable(getDrawable(R.drawable.sundaylunch));
                DinnerImage.setImageDrawable(getDrawable(R.drawable.sundaydinner));

                break;
            case Calendar.MONDAY:
                BT.setText("Aloo Paratha , Daliya.");
                LT.setText("Veg Biryani , Rayta , Tamatar Chutney and Soyabean Daal.");
                DT.setText("Chole puri  ,Kheer , Masala Rice. ");
                breakfastImage.setImageDrawable(getDrawable(R.drawable.mondaybreakfast));
                LunchImage.setImageDrawable(getDrawable(R.drawable.mondaylunch));
                DinnerImage.setImageDrawable(getDrawable(R.drawable.mondaydinner));

                break;
            case Calendar.TUESDAY:
                BT.setText("Poha  ,Jalebi , Sandwiches and Cornflakes.");
                LT.setText("Baigan Bharta , Kadhi Pakoda.");
                DT.setText("Matar Paneer , Daal Makhini and Gulab Jamun (2)");
                breakfastImage.setImageDrawable(getDrawable(R.drawable.tuesdaybreakfast));
                LunchImage.setImageDrawable(getDrawable(R.drawable.tuesdaylunch));
                DinnerImage.setImageDrawable(getDrawable(R.drawable.tuesdaydinner));

                break;
            case Calendar.WEDNESDAY:
                BT.setText("Macroni , Break Pakoda , Fruit/Boiled Egg , Daliya.");
                LT.setText("Arbi, Arhar Daal and Dahi.");
                DT.setText("Chana Daal , Palak Khofta , Jeera Rice.");
                breakfastImage.setImageDrawable(getDrawable(R.drawable.wednesdaybreakfast));
                LunchImage.setImageDrawable(getDrawable(R.drawable.wednesdaylunch));
                DinnerImage.setImageDrawable(getDrawable(R.drawable.wednesdaydinner));
                break;
            case Calendar.THURSDAY:
                BT.setText("Vada Sambhar , Corn flakes + Chutney.");
                LT.setText("Razma , Jeera rice , Mix Veg. and Dahi.");
                DT.setText("Pav Bhaji + Chilli potato , Manchurian , Paneer Bhurjia / Egg Curry");
                breakfastImage.setImageDrawable(getDrawable(R.drawable.thursdaybreakfast));
                LunchImage.setImageDrawable(getDrawable(R.drawable.thursdaylunch));
                DinnerImage.setImageDrawable(getDrawable(R.drawable.thursdaydinner));
                break;
            case Calendar.FRIDAY:
                BT.setText("Kachori , Aloo Sabji , Fruit + Daliya.");
                LT.setText("Kadaai Paneer , Moong Daal , Fruit Cream.");
                DT.setText("Chana Daal , Patta Gobi , Dahi vada");
                breakfastImage.setImageDrawable(getDrawable(R.drawable.fridaybreakfast));
                LunchImage.setImageDrawable(getDrawable(R.drawable.fridaylunch));
                DinnerImage.setImageDrawable(getDrawable(R.drawable.fridaydinner));

                break;
            case Calendar.SATURDAY:
                BT.setText("idli , Uttapan , Chutney , Sambhar.");
                LT.setText("Chole Bature , Sabji and Dahi.");
                DT.setText("Aloo Gobi , Arhar Daal , Sujhi Halvaa.");
                breakfastImage.setImageDrawable(getDrawable(R.drawable.saturdaybreakfast));
                LunchImage.setImageDrawable(getDrawable(R.drawable.saturdaylunch));
                DinnerImage.setImageDrawable(getDrawable(R.drawable.saturdaydinner));

                break;
        }

        final TextView count = (TextView) findViewById(R.id.count);
        final ProportionalImageView imageView = (ProportionalImageView) findViewById(R.id.messimage);
        FirebaseDatabase.getInstance().getReference().child("Messcounting")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {

                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                count.setText("Faces Detected : " + map.get("Count"));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        time = new CountDownTimer(8000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                FirebaseDatabase.getInstance().getReference().child("Messcounting").child("ImageUrl")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {

                                    Picasso.with(getApplicationContext()).load(String.valueOf(dataSnapshot.getValue(String.class))).into(imageView);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                start();
            }
        }.start();

        final Button ok = (Button) view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactpopupdialog.dismiss();
            }
        });
        contactpopupdialog.setCancelable(true);
        AccelerateDecelerateInterpolator a = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(5000, a);
        KenBurnsView image = (KenBurnsView) findViewById(R.id.image);
        image.setTransitionGenerator(generator);
        contactpopup = (FloatingActionButton) findViewById(R.id.contactpopup);
        contactpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactpopupdialog.show();


            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (time != null){
            time.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null){
            time.cancel();
        }
    }
}
