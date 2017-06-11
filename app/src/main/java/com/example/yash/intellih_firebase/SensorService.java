package com.example.yash.intellih_firebase;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by YASH on 28-Apr-17.
 */

public class SensorService extends IntentService {

    public SensorService() {
        super("SensorService");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        final Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        databaseReference.child("sensors").child("gas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int val = Integer.parseInt(dataSnapshot.getValue(String.class));

                if (val>600){
                    NotificationCompat.Builder mBuilder =
                            (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Intelli-H Smart Home")
                                    .setContentText("There might be a gas leak in your house!")
                                    .setSound(uri);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        databaseReference.child("sensors").child("light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int val = Integer.parseInt(dataSnapshot.getValue(String.class));
                if (val>600){
                    databaseReference.child("drawingroom").child("light").setValue(1);
                    databaseReference.child("bedroom").child("light").setValue(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        databaseReference.child("pir").child("pir_out").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int val = (dataSnapshot.getValue(Integer.class));
                if (val==1){
                    NotificationCompat.Builder mBuilder =
                            (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Intelli-H Smart Home")
                                    .setContentText("Intruder Detected in your house!")
                                    .setSound(uri);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                    mNotificationManager.notify(002, mBuilder.build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        databaseReference.child("pir").child("pir_in").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int val = (dataSnapshot.getValue(Integer.class));
                if (val==1){
                    Toast.makeText(getApplicationContext(), "User is present in room", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
