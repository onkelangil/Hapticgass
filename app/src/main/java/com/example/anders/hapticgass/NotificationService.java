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

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Peter on 20-May-17.
 */

public class NotificationService extends Service {

    private NotificationManager notificationManager;
    private FirebaseAuth auth;

    //The service is being created
    @Override
    public void onCreate() {
        //Gets an instance of the NotificationManager service
        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        auth = FirebaseAuth.getInstance();
    }

    // The service is starting, due to a call to startService()
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationSend();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notificationSend() {
        Intent dismissIntent = new Intent(this, MainActivity.class);
        dismissIntent.setAction("123");
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(this, MainActivity.class);
        snoozeIntent.setAction("123");
        PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);

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
                        .setSmallIcon(R.drawable.ic_twitter_bird_white_24dp)
                        .setContentTitle(auth.getCurrentUser().getDisplayName() + " just farted in your general direction")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Go to app"));
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
