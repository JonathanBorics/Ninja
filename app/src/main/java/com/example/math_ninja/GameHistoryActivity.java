package com.example.math_ninja;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        // Inicializáljuk a ListView-t
        ListView gameHistoryList = findViewById(R.id.gameHistoryList);
        Button backButton = findViewById(R.id.backButton);

        // Vissza gomb eseménykezelője
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Visszalép az előző képernyőre
            }
        });

        // Felhasználói ID lekérése
        long playerId = getSharedPreferences("GamePrefs", MODE_PRIVATE)
                .getLong("playerId", -1);

        if (playerId == -1) {
            Toast.makeText(this, "Hiba: Felhasználói azonosító nem található!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lekérjük a játéktörténetet az adatbázisból
        DatabaseManager dbManager = new DatabaseManager(this);
        Cursor cursor = dbManager.getPlayerGameHistory(playerId);

        if (cursor != null && cursor.getCount() > 0) {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                // Előre meghatározzuk az oszlopindexeket
                int gameTypeColumnIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GAME_TYPE);
                int scoreColumnIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SESSION_SCORE);
                int dateColumnIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SESSION_DATE);

                // Végigmegyünk a kurzor sorain
                while (cursor.moveToNext()) {
                    Map<String, String> item = new HashMap<>();
                    String gameType = cursor.getString(gameTypeColumnIndex);
                    int score = cursor.getInt(scoreColumnIndex);
                    String date = cursor.getString(dateColumnIndex);

                    // Játék típus formázása
                    String formattedGameType;
                    if (gameType.equals("plus_minus")) {
                        formattedGameType = "Összeadás/Kivonás";
                    } else if (gameType.equals("multiply_divide")) {
                        formattedGameType = "Szorzás/Osztás";
                    } else {
                        formattedGameType = gameType;
                    }

                    // Dátum formázása (ha SQLite timestamp formátumban van)
                    String formattedDate = formatDate(date);

                    item.put("date", formattedDate);
                    item.put("gameType", formattedGameType);
                    item.put("score", String.valueOf(score) + " pont");

                    data.add(item);
                }

                // SimpleAdapter a játéktörténet listához
                SimpleAdapter adapter = new SimpleAdapter(
                        this,
                        data,
                        R.layout.game_history_item,
                        new String[]{"date", "gameType", "score"},
                        new int[]{R.id.dateTextView, R.id.gameTypeTextView, R.id.scoreTextView}
                );

                gameHistoryList.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(this, "Hiba a játéktörténet betöltésekor: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            } finally {
                // Kurzor bezárása
                cursor.close();
            }
        } else {
            Toast.makeText(this, "Még nincs játéktörténet!", Toast.LENGTH_SHORT).show();
        }
    }

    // SQLite timestamp formázása olvasható dátummá
    private String formatDate(String sqliteDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(sqliteDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            return sqliteDate;
        }
    }
}