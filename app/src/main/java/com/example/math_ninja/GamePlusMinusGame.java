package com.example.math_ninja;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GamePlusMinusGame extends AppCompatActivity {

    private TextView mathQuestion;
    private Button optionButton1, optionButton2, optionButton3, optionButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_plus_minus);

        mathQuestion = findViewById(R.id.mathQuestion);
        optionButton1 = findViewById(R.id.optionButton1);
        optionButton2 = findViewById(R.id.optionButton2);
        optionButton3 = findViewById(R.id.optionButton3);
        optionButton4 = findViewById(R.id.optionButton4);

        // Kérdés generálása azonnal, amikor az Activity elindul
        generateMathQuestion();

        // Kattintás események a válaszokhoz
        optionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Integer.parseInt(optionButton1.getText().toString()));
            }
        });

        optionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Integer.parseInt(optionButton2.getText().toString()));
            }
        });

        optionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Integer.parseInt(optionButton3.getText().toString()));
            }
        });

        optionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Integer.parseInt(optionButton4.getText().toString()));
            }
        });
    }

    private void generateMathQuestion() {
        Random random = new Random();

        // Generate two random numbers
        int num1 = random.nextInt(20) + 1; // random number between 1 and 20
        int num2 = random.nextInt(20) + 1; // random number between 1 and 20

        // Randomly choose an operation (+ or -)
        String[] operations = {"+", "-"};
        String operation = operations[random.nextInt(operations.length)];

        // Create the question based on the operation
        String questionText = num1 + " " + operation + " " + num2;
        final int correctAnswer; // Declare the variable without assigning it yet

        // Calculate the correct answer based on the operation
        switch (operation) {
            case "+":
                correctAnswer = num1 + num2;
                break;
            case "-":
                correctAnswer = num1 - num2;
                break;
            default:
                correctAnswer = 0; // Default value (should never happen)
        }

        // Set the question text
        mathQuestion.setText(questionText);

        // Generate random incorrect answers
        final int[] options = new int[4]; // Declare final array
        int correctPosition = random.nextInt(4); // Randomly choose a position for the correct answer
        options[correctPosition] = correctAnswer;

        // Fill in the rest of the options with incorrect answers
        for (int i = 0; i < 4; i++) {
            if (options[i] == 0) {
                options[i] = random.nextInt(40) + 1; // Random incorrect answers
            }
        }

        // Assign the answers to the buttons
        optionButton1.setText(String.valueOf(options[0]));
        optionButton2.setText(String.valueOf(options[1]));
        optionButton3.setText(String.valueOf(options[2]));
        optionButton4.setText(String.valueOf(options[3]));
    }

    private void checkAnswer(int selectedAnswer) {
        String currentQuestionText = mathQuestion.getText().toString();
        String[] questionParts = currentQuestionText.split(" ");

        // Get the correct answer by calculating it based on the question
        int num1 = Integer.parseInt(questionParts[0]);
        int num2 = Integer.parseInt(questionParts[2]);
        String operation = questionParts[1];
        int correctAnswer = 0;

        // Calculate the correct answer based on the operation
        switch (operation) {
            case "+":
                correctAnswer = num1 + num2;
                break;
            case "-":
                correctAnswer = num1 - num2;
                break;
        }

        // If the selected answer is correct
        if (selectedAnswer == correctAnswer) {
            mathQuestion.setText("Correct! Let's try another one.");
            generateMathQuestion(); // Generate new question
        } else {
            mathQuestion.setText("Wrong! Try again.");
        }
    }
    private void checkAnswer(int selectedAnswer, int correctAnswer, long playerId) {
        DatabaseManager dbManager = new DatabaseManager(this);
        if (selectedAnswer == correctAnswer) {
            dbManager.updateScore(playerId, 5); // 5 pont hozzáadása helyes válaszért
            mathQuestion.setText("Helyes! Új kérdés következik.");
        } else {
            dbManager.updateScore(playerId, -3); // 3 pont levonása helytelen válaszért
            mathQuestion.setText("Helytelen! Próbáld újra.");
        }
        generateMathQuestion(); // Új kérdés generálása
    }

}