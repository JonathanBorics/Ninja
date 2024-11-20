package com.example.math_ninja;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameOptionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);

        String userName = getIntent().getStringExtra("USER_NAME");
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView.setText(userName);
    }
}
