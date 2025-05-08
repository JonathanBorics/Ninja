package com.example.math_ninja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
        userNameTextView.setText(userName);

        // Pontszám megjelenítése
        TextView totalScoreTextView = findViewById(R.id.totalScoreTextView);
        DatabaseManager dbManager = new DatabaseManager(this);
        int totalScore = dbManager.getTotalScore(userId);
        totalScoreTextView.setText(String.valueOf(totalScore));

        // Játéktörténet gomb eseménykezelője
        Button historyLinkButton = findViewById(R.id.historyLinkButton);
        historyLinkButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOptionsActivity.this, GameHistoryActivity.class);
            startActivity(intent);
        });

        // Kártyák eseménykezelői
        CardView plusMinusCard = findViewById(R.id.plusMinusCard);
        plusMinusCard.setOnClickListener(v -> {
            Intent intent = new Intent(GameOptionsActivity.this, GamePlusMinusGame.class);
            startActivity(intent);
        });

        CardView multiplyDivideCard = findViewById(R.id.multiplyDivideCard);
        multiplyDivideCard.setOnClickListener(v -> {
            Intent intent = new Intent(GameOptionsActivity.this, GameMultiplicationDivisionActivity.class);
            startActivity(intent);
        });

        CardView historyCard = findViewById(R.id.historyCard);
        historyCard.setOnClickListener(v -> {
            Intent intent = new Intent(GameOptionsActivity.this, GameHistoryActivity.class);
            startActivity(intent);
        });
    }
}