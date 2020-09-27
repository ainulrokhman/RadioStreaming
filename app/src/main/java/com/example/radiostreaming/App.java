package com.example.radiostreaming;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static String CHANNEL_ID = "TOP_FM";
    static String CHANNEL_NAME = "TOPFM";

    @Override
    public void onCreate() {
        super.onCreate();
        start();
    }

    void start(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(notificationChannel);
            }
    }
}
