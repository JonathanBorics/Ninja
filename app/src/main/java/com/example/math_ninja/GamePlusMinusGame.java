package com.example.math_ninja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GamePlusMinusGame extends AppCompatActivity {

    private static final String TAG = "GamePlusMinusGame";
    private TextView mathQuestion, scoreTextView;
    private Button optionButton1, optionButton2, optionButton3, optionButton4;
    private long playerId;
    private int currentScore = 0;
    private int wrongAnswerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable StrictMode for debugging
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .build());

        setContentView(R.layout.activity_game_plus_minus);

        mathQuestion = findViewById(R.id.mathQuestion);
        scoreTextView = findViewById(R.id.totalScoreTextView);
        optionButton1 = findViewById(R.id.optionButton1);
        optionButton2 = findViewById(R.id.optionButton2);
        optionButton3 = findViewById(R.id.optionButton3);
        optionButton4 = findViewById(R.id.optionButton4);

        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        playerId = prefs.getLong("playerId", -1);

        if (playerId == -1) {
            Toast.makeText(this, "Hiba: Felhasználói azonosító nem található!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        updateScoreDisplay();
        generateMathQuestion();

        optionButton1.setOnClickListener(view -> checkAnswer(Integer.parseInt(optionButton1.getText().toString())));
        optionButton2.setOnClickListener(view -> checkAnswer(Integer.parseInt(optionButton2.getText().toString())));
        optionButton3.setOnClickListener(view -> checkAnswer(Integer.parseInt(optionButton3.getText().toString())));
        optionButton4.setOnClickListener(view -> checkAnswer(Integer.parseInt(optionButton4.getText().toString())));
    }

    private void generateMathQuestion() {
        Random random = new Random();
        int num1 = random.nextInt(20) + 1;
        int num2 = random.nextInt(20) + 1;

        String[] operations = {"+", "-"};
        String operation = operations[random.nextInt(operations.length)];
        int correctAnswer = operation.equals("+") ? num1 + num2 : num1 - num2;

        int[] options = new int[4];
        int correctPosition = random.nextInt(4);
        options[correctPosition] = correctAnswer;

        for (int i = 0; i < 4; i++) {
            if (i != correctPosition) {
                do {
                    options[i] = correctAnswer + random.nextInt(10) - 5;
                } while (options[i] == correctAnswer || options[i] <= 0);
            }
        }

        runOnUiThread(() -> {
            mathQuestion.setText(num1 + " " + operation + " " + num2);
            optionButton1.setText(String.valueOf(options[0]));
            optionButton2.setText(String.valueOf(options[1]));
            optionButton3.setText(String.valueOf(options[2]));
            optionButton4.setText(String.valueOf(options[3]));
        });
    }

    private void checkAnswer(int selectedAnswer) {
        String currentQuestionText = mathQuestion.getText().toString();
        String[] parts = currentQuestionText.split(" ");

        int num1 = Integer.parseInt(parts[0]);
        int num2 = Integer.parseInt(parts[2]);
        String operation = parts[1];
        int correctAnswer = operation.equals("+") ? num1 + num2 : num1 - num2;

        if (selectedAnswer == correctAnswer) {
            currentScore += 5;
            runOnUiThread(() -> {
                scoreTextView.setText("Pontszám: " + currentScore);
                Toast.makeText(this, "Helyes válasz!", Toast.LENGTH_SHORT).show();
            });
        } else {
            currentScore -= 3;
            wrongAnswerCount++;
            runOnUiThread(() -> {
                scoreTextView.setText("Pontszám: " + currentScore);
                Toast.makeText(this, "Helytelen válasz!", Toast.LENGTH_SHORT).show();
            });

            if (wrongAnswerCount >= 3) {
                endGame();
                return;
            }
        }

        new Thread(this::generateMathQuestion).start();
    }

    private void endGame() {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Játék vége")
                    .setMessage("Összesített pontszám: " + currentScore + "\nPróbáld újra!")
                    .setPositiveButton("Vissza a főmenübe", (dialog, which) -> {
                        Intent intent = new Intent(GamePlusMinusGame.this, GameOptionsActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        });
    }

    private void updateScoreDisplay() {
        runOnUiThread(() -> scoreTextView.setText("Pontszám: " + currentScore));
    }
}
