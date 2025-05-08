package com.example.math_ninja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GamePlusMinusGame extends AppCompatActivity {

    private static final String TAG = "GamePlusMinusGame";
    private TextView mathQuestion, scoreTextView;
    private Button optionButton1, optionButton2, optionButton3, optionButton4;
    private ImageView heart1, heart2, heart3; // Szívek (életek) megjelenítéséhez
    private long playerId;
    private int currentScore = 0;
    private int wrongAnswerCount = 0;
    private int livesRemaining = 3; // Életek száma
    private Vibrator vibrator; // Rezgés kezeléséhez
    private CountDownTimer countDownTimer;
    private TextView timerTextView;

    private static final int TIMER_DURATION = 60000;

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
                timerTextView.setText("Time left: " + millisUntilFinished / 1000);
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

        // Válasszunk véletlenszerű műveletet
        String[] operations = {"+", "-"};
        String operation = operations[random.nextInt(operations.length)];

        int num1, num2, correctAnswer;

        if (operation.equals("+")) {
            // Összeadás: két szám 1 és 50 között
            num1 = random.nextInt(50) + 1;
            num2 = random.nextInt(50) + 1;
            correctAnswer = num1 + num2;
        } else {
            // Kivonás: biztosítsuk, hogy pozitív eredményt kapjunk
            num1 = random.nextInt(50) + 10; // Nagyobb szám (10-60)
            num2 = random.nextInt(num1); // Kisebb szám (0-(num1-1))
            correctAnswer = num1 - num2;
        }

        // Opciók generálása
        int[] options = new int[4];
        int correctPosition = random.nextInt(4);
        options[correctPosition] = correctAnswer;

        // Generáljunk három különböző helytelen választ
        for (int i = 0; i < 4; i++) {
            if (i != correctPosition) {
                int incorrectAnswer;
                do {
                    // A helytelen válasz a helyes válasz ±5 tartományában legyen
                    incorrectAnswer = correctAnswer + random.nextInt(11) - 5;
                } while (incorrectAnswer == correctAnswer || // Ne egyezzen a helyes válasszal
                        incorrectAnswer < 0 || // Pozitív legyen
                        contains(options, incorrectAnswer)); // Ne ismétlődjön

                options[i] = incorrectAnswer;
            }
        }

        // UI frissítése a fő szálon
        runOnUiThread(() -> {
            mathQuestion.setText(num1 + " " + operation + " " + num2);
            optionButton1.setText(String.valueOf(options[0]));
            optionButton2.setText(String.valueOf(options[1]));
            optionButton3.setText(String.valueOf(options[2]));
            optionButton4.setText(String.valueOf(options[3]));
        });
    }

    // Segédfüggvény annak ellenőrzésére, hogy egy szám már szerepel-e a tömbben
    private boolean contains(int[] array, int value) {
        for (int item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }

    private void checkAnswer(int selectedAnswer) {
        String currentQuestionText = mathQuestion.getText().toString();
        String[] parts = currentQuestionText.split(" ");

        int num1 = Integer.parseInt(parts[0]);
        int num2 = Integer.parseInt(parts[2]);
        String operation = parts[1];

        int correctAnswer;
        if (operation.equals("+")) {
            correctAnswer = num1 + num2;
        } else { // "-"
            correctAnswer = num1 - num2;
        }

        if (selectedAnswer == correctAnswer) {
            currentScore += 5;
            runOnUiThread(() -> {
                scoreTextView.setText("Score: " + currentScore);
                Toast.makeText(this, "Helyes válasz!", Toast.LENGTH_SHORT).show();
            });
        } else {
            currentScore -= 3;
            wrongAnswerCount++;
            livesRemaining--;
            updateLivesUI();

            runOnUiThread(() -> {
                scoreTextView.setText("Score: " + currentScore);
                Toast.makeText(this, "Helytelen válasz!", Toast.LENGTH_SHORT).show();
            });

            if (livesRemaining <= 0) {
                triggerVibration();
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
        // Mentsük el a játékmenetet
        DatabaseManager dbManager = new DatabaseManager(this);
        dbManager.saveGameSession(playerId, "plus_minus", currentScore);

        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Játék vége")
                    .setMessage("Összpontszám: " + currentScore + "\nPróbáld újra!")
                    .setPositiveButton("Vissza a főmenübe", (dialog, which) -> {
                        Intent intent = new Intent(GamePlusMinusGame.this, GameOptionsActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNeutralButton("Játéktörténet", (dialog, which) -> {
                        Intent intent = new Intent(GamePlusMinusGame.this, GameHistoryActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        });
    }

    private void updateScoreDisplay() {
        runOnUiThread(() -> scoreTextView.setText("Score: " + currentScore));
    }
}
