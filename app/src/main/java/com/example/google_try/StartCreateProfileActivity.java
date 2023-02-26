package com.example.google_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class StartCreateProfileActivity extends AppCompatActivity {

    private Button start_BTN;
    private VideoView videoView2;
    private String email, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_create_profile);

        findViews();
        startVideo();
        setOnClick();
        getTheIntent();

    }

    private void getTheIntent() {
        email = getIntent().getStringExtra("EMAIL");
        uid = getIntent().getStringExtra("UID");
    }

    private void setOnClick() {
        start_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartCreateProfileActivity.this, NannyDetails1Activity.class);
                intent.putExtra("EMAIL",email);
                intent.putExtra("UID",uid);
                startActivity(intent);
            }
        });

    }

    private void startVideo() {
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.babysitter);
        videoView2.setVideoURI(uri);
        videoView2.start();

        videoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    private void findViews() {
        start_BTN = findViewById(R.id.start_BTN);
        videoView2 = findViewById(R.id.videoview2);

    }

    @Override
    protected void onRestart() {
        videoView2.start();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        videoView2.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        videoView2.stopPlayback();
        super.onDestroy();
    }
}