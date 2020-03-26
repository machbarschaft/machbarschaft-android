package com.ks.einanrufhilft.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.database.Storage;
import com.ks.einanrufhilft.view.home.Home;

/**
 * Creates a notification in the status bar of android to display a order is in progress.
 */
public class OrderInProgressNotification extends Service {
    private static final String CHANNEL_ID = "NotificationChannel";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        createNotificationChannel();
        Intent notifIntent = new Intent(this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Du erledigst gerade Auftrag für:  " + Storage.getInstance().getOrderInProgress(getApplicationContext()).getClientName() + " Die Adresse lautet: " + Storage.getInstance().getOrderInProgress(getApplicationContext()).getCompleteAddress())
                .setSmallIcon(R.drawable.ic_machbarschaft_clear_white)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);


        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);

        }
    }
}
