package com.example.google_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class NannyOrJobActivity extends AppCompatActivity {

    private VideoView videoView;
    private Button find_nanny_BTN, find_job_BTN;
    private String email, uid;

    private boolean isNanny = false;
    private boolean findNanny = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nanny_or_job);

        findViews();
        startVideo();

        setOnClick();
    }

    private void setOnClick() {
        find_nanny_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NannyOrJobActivity.this,FamilyDetails1Activity.class);
                intent.putExtra("EMAIL",email);
                intent.putExtra("UID",uid);
                Log.d("AABBCC", uid + " putFindNanny");
                startActivity(intent);

            }
        });

        find_job_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNanny = true;
                Intent intent = new Intent(NannyOrJobActivity.this, StartCreateProfileActivity.class);
                intent.putExtra("EMAIL",email);
                intent.putExtra("UID",uid);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getTheIntent() {
        email = getIntent().getStringExtra("EMAIL");
        uid = getIntent().getStringExtra("UID");

    }

    private void startVideo() {
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    private void findViews() {
        videoView = findViewById(R.id.videoview);
        find_nanny_BTN = findViewById(R.id.find_nanny_BTN);
        find_job_BTN = findViewById(R.id.find_job_BTN);

    }

    @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTheIntent();
    }

    @Override
    protected void onPause() {
        videoView.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        super.onDestroy();
    }
}