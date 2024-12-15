package com.example.math_ninja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameNameInputActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_input);

        EditText nameInput = findViewById(R.id.nameInput);
        Button continueButton = findViewById(R.id.continueButton);

        DatabaseManager dbManager = new DatabaseManager(this);

        // Check for saved name in SharedPreferences
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        String savedName = prefs.getString("playerName", null);
        long savedPlayerId = prefs.getLong("playerId", -1);

        if (savedName != null && savedPlayerId != -1) {
            // Redirect to GameOptionsActivity if name and ID exist
            Intent intent = new Intent(GameNameInputActivity.this, GameOptionsActivity.class);
            startActivity(intent);
            finish();
        }

        continueButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Kérlek, add meg a nevedet!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                long playerId = dbManager.addPlayer(name); // Add to database
                if (playerId == -1) {
                    throw new Exception("Az adatbázis nem adott vissza érvényes ID-t!");
                }

                // Save name and player ID in SharedPreferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("playerName", name);
                editor.putLong("playerId", playerId);
                editor.apply();

                // Redirect to GameOptionsActivity
                Intent intent = new Intent(GameNameInputActivity.this, GameOptionsActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.e("GameNameInputActivity", "Hiba történt: ", e);
                Toast.makeText(this, "Adatbázis hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
