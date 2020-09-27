package com.example.radiostreaming;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.radiostreaming.App.CHANNEL_ID;

public class Notif {
    private RadioService service;

    public Notif(RadioService service) {
        this.service = service;
    }

    void create(boolean isPlay){
        Intent intent1 = new Intent("pauseResume");
        int icon = isPlay ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;
        PendingIntent playResume = PendingIntent.getBroadcast(service,0, intent1,0);
        Intent intent = new Intent("stop");
        PendingIntent stop = PendingIntent.getBroadcast(service,0, intent,0);
        Intent main = new Intent(service, MainActivity.class);
        PendingIntent mainn = PendingIntent.getActivity(service,0, main,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                .setContentTitle("TOP FM BUMIAYU")
                .setContentText("95.1 FM")
                .setContentIntent(mainn)
                .addAction(icon,"Close", playResume)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel,"Close", stop)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1)
                        )
                ;

        Notification notification = builder.build();

        if (isPlay){
            service.startForeground(1, notification);
        }else {
            service.stopForeground(true);
            NotificationManager mNotificationManager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, notification);
        }
    }
}
