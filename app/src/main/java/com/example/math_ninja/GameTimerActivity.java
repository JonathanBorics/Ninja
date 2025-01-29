package com.example.math_ninja;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameTimerActivity extends AppCompatActivity {
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 1 minute (60,000 milliseconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Determine which layout to use based on an intent extra
        Intent intent = getIntent();
        String gameType = intent.getStringExtra("gameType");

        boolean isMultiplicationGame = getIntent().getBooleanExtra("IS_MULTIPLICATION", true);
        if (isMultiplicationGame) {
            setContentView(R.layout.activity_game_multiplication_division);
        } else {
            setContentView(R.layout.activity_game_plus_minus);
        }

        timerTextView = findViewById(R.id.timerTextView);
        startTimer();

        // Restore state if needed
        if (savedInstanceState != null) {
            timeLeftInMillis = savedInstanceState.getLong("timeLeft", 60000);
            updateTimer();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                Log.d("TIMER_DEBUG", "Time left: " + timeLeftInMillis);
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Time's up!");
                endGame();
            }


        }.start();
    }


    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    private void endGame() {
        // Example: Show a dialog or finish the activity
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("Game Over")
                    .setMessage("Time is up!")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeLeft", timeLeftInMillis);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeLeftInMillis = savedInstanceState.getLong("timeLeft");
        startTimer();
    }
}

