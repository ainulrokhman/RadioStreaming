package com.example.radiostreaming;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

public class RadioService extends Service {
    private MediaPlayer mediaPlayer;
    private Notif notif;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        notif = new Notif(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("play");
        filter.addAction("pauseResume");
        filter.addAction("stop");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                assert action != null;
                switch (action){
                    case "play":
                        try {
                            mediaPlayer.setDataSource("http://streaming.topfm951.net:9510/stream");
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mediaPlayer.start();
                                    notif.create(mediaPlayer.isPlaying());
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "pauseResume":
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        }else {
                            mediaPlayer.start();
                        }
                        notif.create(mediaPlayer.isPlaying());
                        sendBroadcast(new Intent("pause"));
                        break;
                    case "stop":
                        stopSelf();
                        break;
                    default:
                        break;
                }
            }
        };
        registerReceiver(receiver,filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        unregisterReceiver(receiver);
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }
}
