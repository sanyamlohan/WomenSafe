package com.example.womensafe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ScreamActivity extends AppCompatActivity {
    MediaPlayer player;
    Button mBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scream);

        mBack = findViewById(R.id.backBtn);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScreamActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    // media player takes up system resources and its good practice to release it as soon as
    // we don't need it anymore

    // so instead of create it in onCreate method
    public void play(View view){
        // we will create it in our play method
        // so we only create it when we actually want to play something

        // here first we check player is equal to null or not
        // because if it's already created we don't want to create a new one

        if(player == null){
            player = MediaPlayer.create(this, R.raw.scream);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    player.start();
                }
            });
        }

        // it's also important to release the media player before creating the new one

        player.start();

    }

    public void pause(View view){
        if(player != null){
            player.pause();
        }
    }

    public void stop(View view){
        player.release();
        player = null;
    }


    @Override
    protected void onStop() {
        super.onStop();
        player.release();
        player = null;
    }


}