package com.example.math_ninja;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameMultiplicationDivisionActivity extends AppCompatActivity {

    private TextView mathQuestion;
    private Button multiplyButton1, multiplyButton2, multiplyButton3, multiplyButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_multiplication_division);

        mathQuestion = findViewById(R.id.mathQuestion);
        multiplyButton1 = findViewById(R.id.optionButton1);
        multiplyButton2 = findViewById(R.id.optionButton2);
        multiplyButton3 = findViewById(R.id.optionButton3);
        multiplyButton4 = findViewById(R.id.optionButton4);

        generateMathQuestion(); // Load a question when the activity starts
    }

    private void generateMathQuestion() {
        Random random = new Random();

        // Generate two random numbers
        int num1 = random.nextInt(20) + 1; // random number between 1 and 20
        int num2 = random.nextInt(20) + 1; // random number between 1 and 20

        // Randomly choose an operation
        String[] operations = {"*", "/"}; // Only multiplication and division
        String operation = operations[random.nextInt(operations.length)];

        // Declare the correct answer variable
        final int correctAnswer;

        // Calculate the correct answer based on the operation
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
        multiplyButton1.setOnClickListener(view -> checkAnswer(options[0], correctAnswer));
        multiplyButton2.setOnClickListener(view -> checkAnswer(options[1], correctAnswer));
        multiplyButton3.setOnClickListener(view -> checkAnswer(options[2], correctAnswer));
        multiplyButton4.setOnClickListener(view -> checkAnswer(options[3], correctAnswer));
    }

    private void checkAnswer(int selectedAnswer, int correctAnswer) {
        if (selectedAnswer == correctAnswer) {
            mathQuestion.setText(R.string.correct_answer); // Using string resource
            generateMathQuestion(); // Generate a new question if the answer is correct
        } else {
            mathQuestion.setText(R.string.wrong_answer); // Using string resource
        }
    }
}