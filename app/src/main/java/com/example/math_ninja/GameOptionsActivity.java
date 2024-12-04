package com.example.math_ninja;

import android.content.Intent;
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

        // Lekérjük a továbbított adatokat
        String userName = getIntent().getStringExtra("USER_NAME");
        long userId = getIntent().getLongExtra("USER_ID", -1);

        // Ellenőrizzük az adatokat
        if (userId == -1 || userName == null) {
            Toast.makeText(this, "Hiba: Érvénytelen azonosító vagy név!", Toast.LENGTH_SHORT).show();
            Log.e("GameOptionsActivity", "Érvénytelen USER_ID vagy USER_NAME: " + userId + ", " + userName);
            finish(); // Zárjuk be az Activity-t, ha hibás adatok vannak
            return;
        }

        // Felhasználói név megjelenítése
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView.setText("Üdv, " + userName + "!");

        // Pontszám megjelenítése
        TextView totalScoreTextView = findViewById(R.id.totalScoreTextView);
        DatabaseManager dbManager = new DatabaseManager(this);
        try {
            int totalScore = dbManager.getTotalScore(userId);
            totalScoreTextView.setText("Összesített pontszámod: " + totalScore);
        } catch (Exception e) {
            Toast.makeText(this, "Hiba a pontszám lekérése során!", Toast.LENGTH_SHORT).show();
            Log.e("GameOptionsActivity", "Hiba a pontszám lekérésekor: ", e);
        }

        // OptionButton1 click event handler
        Button optionButton1 = findViewById(R.id.optionButton1);
        optionButton1.setOnClickListener(v -> {
            Intent intent = new Intent(GameOptionsActivity.this, GamePlusMinusGame.class);
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // OptionButton2 click event handler
        Button optionButton2 = findViewById(R.id.optionButton2);
        optionButton2.setOnClickListener(v -> {
            Intent intent = new Intent(GameOptionsActivity.this, GameMultiplicationDivisionActivity.class);
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }
}
