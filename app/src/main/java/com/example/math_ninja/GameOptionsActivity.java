package com.example.math_ninja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameOptionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);

        // Lekérjük a SharedPreferences adatokat
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        String userName = prefs.getString("playerName", null);
        long userId = prefs.getLong("playerId", -1);

        // Ellenőrizzük az adatokat
        if (userId == -1 || userName == null || userName.isEmpty()) {
            Toast.makeText(this, "Hiba: Nincsenek érvényes felhasználói adatok!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, GameNameInputActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Felhasználói név megjelenítése
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView.setText("Hi, " + userName + "!");

        // Pontszám megjelenítése
        TextView totalScoreTextView = findViewById(R.id.totalScoreTextView);
        DatabaseManager dbManager = new DatabaseManager(this);
        try {
            int totalScore = dbManager.getTotalScore(userId);
            totalScoreTextView.setText("Your total score: " + totalScore);
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving score!", Toast.LENGTH_SHORT).show();
            Log.e("GameOptionsActivity", "Error retrieving score: ", e);
        }

        // Plusz/mínusz játék gomb eseménykezelője
        Button optionButton1 = findViewById(R.id.optionButton1);
        optionButton1.setOnClickListener(v -> {
            Intent intent = new Intent(GameOptionsActivity.this, GamePlusMinusGame.class);
            startActivity(intent);
        });

        // Szorzás/osztás játék gomb eseménykezelője
        Button optionButton2 = findViewById(R.id.optionButton2);
        optionButton2.setOnClickListener(v -> {
            Intent intent = new Intent(GameOptionsActivity.this, GameMultiplicationDivisionActivity.class);
            startActivity(intent);
        });
    }
}
