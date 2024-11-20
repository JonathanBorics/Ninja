package com.example.math_ninja;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GameNameInputActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_input);

        EditText nameInput = findViewById(R.id.nameInput);
        Button continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            Intent intent = new Intent(GameNameInputActivity.this, GameOptionsActivity.class);
            intent.putExtra("USER_NAME", name);
            startActivity(intent);
        });
    }
}
