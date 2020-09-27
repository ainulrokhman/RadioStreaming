package com.example.radiostreaming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView playPause;
    private boolean state, play;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null)
        getSupportActionBar().hide();

        playPause = findViewById(R.id.play);
        playPause.setOnClickListener(this);
        state = true;
        play = false;

        IntentFilter filter = new IntentFilter();
        filter.addAction("play");
        filter.addAction("pause");
        filter.addAction("stop");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                assert action != null;
                if (action.equalsIgnoreCase("play")){
                    play = true;
                }
                else if (action.equalsIgnoreCase("pause")){
                    play = !play;
                }else if (action.equalsIgnoreCase("stop")){
                    state = true;
                    play = false;
                }
                int image = play ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;
                playPause.setImageResource(image);
            }
        };
        registerReceiver(receiver, filter);
    }

    @Override
    public void onClick(View view) {
        if (state){
            startService(new Intent(this, RadioService.class));
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast(new Intent("play"));
                }
            },1000);
            state = false;
        }else {
            sendBroadcast(new Intent("pauseResume"));
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, RadioService.class));
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}