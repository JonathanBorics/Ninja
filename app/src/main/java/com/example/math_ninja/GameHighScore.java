package com.example.math_ninja;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHighScore extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // Inicializáljuk a ListView-t
        ListView highScoreList = findViewById(R.id.highScoreList);

        // Lekérjük a pontszámokat az adatbázisból
        DatabaseManager dbManager = new DatabaseManager(this);
        Cursor cursor = dbManager.getTopScores(10); // Top 10 pontszám

        if (cursor != null && cursor.getCount() > 0) {
            List<Map<String, String>> data = new ArrayList<>();

            // Pozíció számláló
            int position = 1;

            try {
                // Előre meghatározzuk az oszlopindexeket
                int nameColumnIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME);
                int scoreColumnIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE);

                // Végigmegyünk a kurzor sorain
                while (cursor.moveToNext()) {
                    Map<String, String> item = new HashMap<>();
                    String name = cursor.getString(nameColumnIndex);
                    int score = cursor.getInt(scoreColumnIndex);

                    item.put("position", String.valueOf(position));
                    item.put("name", name);
                    item.put("score", String.valueOf(score));

                    data.add(item);
                    position++;
                }

                // Egyszerű adapter egy egyedi listához
                SimpleAdapter adapter = new SimpleAdapter(
                        this,
                        data,
                        R.layout.high_score_item, // Egyedi high score item layout
                        new String[]{"position", "name", "score"},
                        new int[]{R.id.positionTextView, R.id.nameTextView, R.id.scoreTextView}
                );

                highScoreList.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(this, "Hiba a pontszámok betöltésekor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                // Kurzor bezárása
                cursor.close();
            }
        } else {
            Toast.makeText(this, "Még nincsenek pontszámok!", Toast.LENGTH_SHORT).show();
        }
    }
}