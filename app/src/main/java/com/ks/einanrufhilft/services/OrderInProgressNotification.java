package com.ks.einanrufhilft.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.ks.einanrufhilft.Database.OrderDTO;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.home.Home;

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

        SharedPreferences sharedPreferences = getSharedPreferences("oderDetails", 0);
        OrderDTO orderDTO = new Gson().fromJson(sharedPreferences.getString("orderDetails", null), OrderDTO.class);
        createNotificationChannel();
        Intent notifIntent = new Intent(this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,  0 , notifIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Machbarschaft")
                .setContentText("Du erledigst gerade Auftrag: Für Frau Schneider")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
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
