package com.example.math_ninja;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameMultiplicationDivisionActivity extends AppCompatActivity {

    private TextView mathQuestion, scoreTextView;
    private Button multiplyButton1, multiplyButton2, multiplyButton3, multiplyButton4;
    private Vibrator vibrator; // Vibrátor hozzáadása

    private CountDownTimer countDownTimer;
    private TextView timerTextView;

    private static final int TIMER_DURATION = 60000; // 1 minute in milliseconds



    private static final String TAG = "GameMultiplicationDivisionActivity";

    private Button optionButton1, optionButton2, optionButton3, optionButton4;
    private ImageView heart1, heart2, heart3; // Szívek (életek) megjelenítéséhez
    private long playerId;
    private int currentScore = 0;
    private int wrongAnswerCount = 0;
    private int livesRemaining = 3; // Életek száma




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

        // Szívek inicializálása
        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);

        // Vibrátor inicializálása
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

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

        timerTextView = findViewById(R.id.timerTextView);

        startTimer();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the timer display
                timerTextView.setText("Time left: " + millisUntilFinished / 1000 + " másodperc");
            }

            @Override
            public void onFinish() {
                // When time is up, trigger the same game over logic as 3 wrong answers
                endGame();
            }
        }.start();
    }

    private void handleTimeOut() {
        triggerVibration();
        mathQuestion.setText(R.string.app_name); // Update question text
        generateMathQuestion(); // Optionally generate a new question
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void generateMathQuestion() {
        Random random = new Random();

        // Generate two random numbers
        int num1 = random.nextInt(20) + 1; // random number between 1 and 20
        int num2 = random.nextInt(20) + 1; // random number between 1 and 20

        // Randomly choose an operation
        String[] operations = {"*", "/"}; // Only multiplication and division
        String operation = operations[random.nextInt(operations.length)];
        int correctAnswer = operation.equals("+") ? num1 * num2 : num1 / num2;

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




 /*       // Calculate the correct answer based on the operation
        if (operation.equals("*")) {
            correctAnswer = num1 * num2;
        } else {
            // Ensure division results in a whole number
            while (num1 % num2 != 0) { // Ensure num1 is divisible by num2 with no remainder
                num1 = random.nextInt(20) + 1; // Regenerate num1
            }
            correctAnswer = num1 / num2;
        }

        // Create the question text
        String questionText = num1 + " " + operation + " " + num2;
        mathQuestion.setText(questionText);

        // Generate random incorrect answers
        int[] options = new int[4];
        int correctPosition = random.nextInt(4); // Randomly choose a position for the correct answer
        options[correctPosition] = correctAnswer;

        // Fill in the rest of the options with incorrect answers
        for (int i = 0; i < 4; i++) {
            if (options[i] == 0) {
                options[i] = random.nextInt(40) + 1; // Random incorrect answers
            }
        }

        // Assign the answers to the buttons
        multiplyButton1.setText(String.valueOf(options[0]));
        multiplyButton2.setText(String.valueOf(options[1]));
        multiplyButton3.setText(String.valueOf(options[2]));
        multiplyButton4.setText(String.valueOf(options[3]));



        // Set click listeners for each option using lambdas
       // multiplyButton1.setOnClickListener(view -> checkAnswer(options[0], correctAnswer));
       // multiplyButton2.setOnClickListener(view -> checkAnswer(options[1], correctAnswer));
      //  multiplyButton3.setOnClickListener(view -> checkAnswer(options[2], correctAnswer));
      //  multiplyButton4.setOnClickListener(view -> checkAnswer(options[3], correctAnswer));
    } */

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
            livesRemaining--; // Csökkentjük az életek számát
            updateLivesUI(); // Frissítjük a szívek megjelenítését

            runOnUiThread(() -> {
                scoreTextView.setText("Pontszám: " + currentScore);
                Toast.makeText(this, "Helytelen válasz!", Toast.LENGTH_SHORT).show();
            });

            if (livesRemaining <= 0) { // Ha nincs több élet, vége a játéknak
                triggerVibration(); // Rezgés hozzáadása
                endGame();
                return;
            }
        }

        new Thread(this::generateMathQuestion).start();
    }

    private void updateLivesUI() {
        // Frissíti a szívek megjelenítését a hátralévő életek alapján
        if (livesRemaining < 3) heart3.setImageResource(R.drawable.heart_empty);
        if (livesRemaining < 2) heart2.setImageResource(R.drawable.heart_empty);
        if (livesRemaining < 1) heart1.setImageResource(R.drawable.heart_empty);
    }

    private void triggerVibration() {
        if (vibrator != null) {
            vibrator.vibrate(500); // 500 ms-os rezgés
        }
    }


    private void endGame() {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Játék vége")
                    .setMessage("Összesített pontszám: " + currentScore + "\nPróbáld újra!")
                    .setPositiveButton("Vissza a főmenübe", (dialog, which) -> {
                        Intent intent = new Intent(GameMultiplicationDivisionActivity.this, GameOptionsActivity.class);
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
