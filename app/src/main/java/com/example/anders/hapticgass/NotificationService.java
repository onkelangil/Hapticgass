package com.example.anders.hapticgass;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.User;

/**
 * Created by Peter on 20-May-17.
 */

public class NotificationService extends Service {

    private NotificationManager notificationManager;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    //The service is being created
    @Override
    public void onCreate() {
        //Gets an instance of the NotificationManager service
        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //Get authentication and database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        reference = database.getReference("farts");
    }

    // The service is starting, due to a call to startService()
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Adds listener on farts-table
        reference.addChildEventListener(new ChildEventListener() {
            //Listens for a new row added to the table
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //ID of the receiver
                String receiverID = dataSnapshot.child("receiver").getValue() + "";

                //Whether fart seen or not
                boolean seen = Boolean.parseBoolean(dataSnapshot.child("seen").getValue() + "");

                //Checks if this user is the recipient of the sent fart and if it hasn't been seen
                if (receiverID.equals(auth.getCurrentUser().getUid()) && !seen) {
                    //Get ID from sender
                    String senderID = dataSnapshot.child("sender").getValue() + "";

                    database.getReference("userlist").child(senderID).child("username").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            notificationSend(snapshot.getValue() + "");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return START_STICKY;
    }

    public void onDestroy(){
        notificationManager.cancelAll();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notificationSend(String msg) {
        //Set which activity to go to when clicked
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        //Builds notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.gasnotification)
                        .setContentTitle(msg + " just farted in your general direction")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Click to open app!"));
        builder.setAutoCancel(true);

        //Set sound
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/raw/fart");
        builder.setSound(uri);

        //Set vibration
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        //Set LED
        builder.setLights(Color.argb(100,34,0,0), 300, 100);

        //Set link
        builder.setContentIntent(resultPendingIntent);

        //Sets an ID for the notification
        int notificationId = 001;

        //Builds the notification and issues it
        notificationManager.notify(notificationId, builder.build());
    }
}
