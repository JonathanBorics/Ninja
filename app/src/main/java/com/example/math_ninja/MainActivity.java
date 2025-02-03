package com.example.math_ninja;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = true; // Hang állapota
    private boolean isVibrationOn = true; // Rezgés állapota
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MediaPlayer inicializálása
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Vibrátor inicializálása
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Hangkapcsoló ikon kezelése
        ImageView soundToggle = findViewById(R.id.soundToggle);
        soundToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    soundToggle.setImageResource(R.drawable.ic_sound_off); // OFF ikon
                } else {
                    mediaPlayer.start();
                    soundToggle.setImageResource(R.drawable.ic_sound_on); // ON ikon
                }
                isPlaying = !isPlaying; // Állapot váltása
            }
        });

        // Rezgés kapcsoló ikon kezelése
        ImageView vibrationToggle = findViewById(R.id.vibrationToggle);
        vibrationToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVibrationOn) {
                    vibrationToggle.setImageResource(R.drawable.ic_vibration_off); // OFF ikon
                } else {
                    vibrator.vibrate(100); // 100ms teszt rezgés
                    vibrationToggle.setImageResource(R.drawable.ic_vibration_on); // ON ikon
                }
                isVibrationOn = !isVibrationOn; // Állapot váltása
            }
        });

        // START gomb hivatkozása és kattintás figyelése
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameNameInputActivity.class);
                startActivity(intent);
            }
        });

        // TUTORIAL gomb hivatkozása és kattintás figyelése
        Button tutorialButton = findViewById(R.id.tutorialButton);
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameTutorialActivity.class);
                startActivity(intent);
            }
        });

        Button highScoreButton = findViewById(R.id.HScoreButton);
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameHighScore.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MediaPlayer leállítása és felszabadítása
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}