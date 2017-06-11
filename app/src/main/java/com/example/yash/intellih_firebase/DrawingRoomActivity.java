package com.example.yash.intellih_firebase;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class DrawingRoomActivity extends AppCompatActivity {

    FloatingActionButton fab_light, fab_fan, switch_light, switch_fan, switch_settings, fab_refresh, fab_power;
    View parentView;
    BottomSheetDialog bottomSheetDialog;
    DatabaseReference databaseReference;
    int curr_fan_value, curr_light_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        parentView = getLayoutInflater().inflate(R.layout.dialog, null);

        fab_power = (FloatingActionButton) findViewById(R.id.powerButton);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        fab_light = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab_fan = (FloatingActionButton) findViewById(R.id.floatingActionButton4);
        switch_light = (FloatingActionButton) parentView.findViewById(R.id.bulb_fab);
        switch_fan = (FloatingActionButton) parentView.findViewById(R.id.fan_fab);
        switch_settings = (FloatingActionButton) parentView.findViewById(R.id.setting_fab);
        fab_refresh = (FloatingActionButton) parentView.findViewById(R.id.refresh_fab);

        databaseReference.child("drawingroom").child("light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curr_light_value = Integer.parseInt(dataSnapshot.getValue().toString());
                if (curr_light_value == 1) {
                    switch_light.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)));
                } else
                    switch_light.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("drawingroom").child("fan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curr_fan_value = Integer.parseInt(dataSnapshot.getValue().toString());
                if (curr_fan_value == 1) {
                    switch_fan.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)));
                } else
                    switch_fan.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        fab_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        switch_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr_light_value == 1) {
                    databaseReference.child("drawingroom").child("light").setValue("0");
                } else
                    databaseReference.child("drawingroom").child("light").setValue("1");
            }
        });

        switch_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr_fan_value == 1) {
                    databaseReference.child("drawingroom").child("fan").setValue("0");
                } else
                    databaseReference.child("drawingroom").child("fan").setValue("1");
            }
        });

        fab_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("drawingroom").child("light").setValue("0");
                databaseReference.child("drawingroom").child("fan").setValue("0");
            }
        });

        switch_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrawingRoomActivity.this, SettingsActivity.class));
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_UP):
                showBottomSheet();
                Toast.makeText(this, "Action was UP", Toast.LENGTH_SHORT).show();
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public void showBottomSheet() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(DrawingRoomActivity.this);
            bottomSheetDialog.setContentView(parentView);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
            bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
            bottomSheetDialog.show();
        } else
            bottomSheetDialog.show();
    }
}
