package com.semas.ccg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;



public class MenuActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button playButton = findViewById(R.id.play_button);
        Button decksButton = findViewById(R.id.decks_button);
        Button settingsButton = findViewById(R.id.settings_button);
        Button exitButton = findViewById(R.id.exit_game_button);

        mediaPlayer = MediaPlayer.create(this, R.raw.backmusic);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        decksButton.setOnClickListener(v -> {
            //управление колодами TODO
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
            startActivity(intent);
        });


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}