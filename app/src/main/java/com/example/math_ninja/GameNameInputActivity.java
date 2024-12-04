package com.example.math_ninja;

import android.content.Intent;
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

        continueButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Kérlek, add meg a nevedet!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                long playerId = dbManager.addPlayer(name); // Hozzáadjuk az adatbázishoz
                if (playerId == -1) {
                    throw new Exception("Az adatbázis nem adott vissza érvényes ID-t!");
                }

                Intent intent = new Intent(GameNameInputActivity.this, GameOptionsActivity.class);
                intent.putExtra("USER_ID", playerId);
                intent.putExtra("USER_NAME", name);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("GameNameInputActivity", "Hiba történt: ", e);
                Toast.makeText(this, "Adatbázis hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}