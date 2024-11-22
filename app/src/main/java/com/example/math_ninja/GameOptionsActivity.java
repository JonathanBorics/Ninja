package com.example.math_ninja;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOptionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);

        // Display the user name
        String userName = getIntent().getStringExtra("USER_NAME");
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView.setText(userName);

        // OptionButton1 click event handler
        Button optionButton1 = findViewById(R.id.optionButton1);
        optionButton1.setOnClickListener(v -> {
            // Create an intent to navigate to GamePlusMinusGame Activity
            Intent intent = new Intent(GameOptionsActivity.this, GamePlusMinusGame.class);

            // Passing the user name to the new activity (if needed)
            intent.putExtra("USER_NAME", userName);

            // Start the new activity
            startActivity(intent);
        });

        // OptionButton2 click event handler
        Button optionButton2 = findViewById(R.id.optionButton2);
        optionButton2.setOnClickListener(v -> {
            // Create an intent to navigate to GameMultiplicationDivisionActivity
            Intent intent = new Intent(GameOptionsActivity.this, GameMultiplicationDivisionActivity.class);

            // Passing the user name to the new activity (if needed)
            intent.putExtra("USER_NAME", userName);

            // Start the new activity
            startActivity(intent);
        });
    }
}
