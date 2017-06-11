package com.example.yash.intellih_firebase;

import android.content.Intent;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    FloatingActionButton unlockDoor, systemShutDown;

    DatabaseReference databaseReference;
    TextView temperature_textview, humidity_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        temperature_textview = (TextView) findViewById(R.id.temperature_textview);
        humidity_textview = (TextView) findViewById(R.id.humidity_textview);

        databaseReference.child("sensors").child("temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temperature_textview.setText(dataSnapshot.getValue().toString() + "°C");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        databaseReference.child("sensors").child("humidity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                humidity_textview.setText("HUMIDITY : " + dataSnapshot.getValue().toString() + "%");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        systemShutDown = (FloatingActionButton) findViewById(R.id.systemShutDown);
        unlockDoor = (FloatingActionButton) findViewById(R.id.unlock);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();
        addData();
        adapter = new CustomAdapter(getApplicationContext(), data);
        recyclerView.setAdapter(adapter);

        unlockDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FingerprintActivity.class));
            }
        });

        systemShutDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("bedroom").child("light").setValue("0");
                databaseReference.child("bedroom").child("fan").setValue("0");
                databaseReference.child("drawingroom").child("light").setValue("0");
                databaseReference.child("drawingroom").child("fan").setValue("0");
            }
        });

    }

    void addData() {
        data.add(new DataModel("Bed Room", R.drawable.bed__2_));
        data.add(new DataModel("Drawing Room", R.drawable.sofa__2_));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(MainActivity.this, SensorService.class));
    }
}
