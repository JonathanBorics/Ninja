package com.example.math_ninja;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {


    private DatabaseHelper dbHelper;


    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addPlayer(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        long playerId = db.insert(DatabaseHelper.TABLE_PLAYERS, null, values);
        db.close();
        return playerId;
    }

    public void updateScore(long playerId, int scoreChange) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(
                "UPDATE " + DatabaseHelper.TABLE_PLAYERS + " SET " +
                        DatabaseHelper.COLUMN_SCORE + " = " + DatabaseHelper.COLUMN_SCORE + " + ? WHERE " +
                        DatabaseHelper.COLUMN_PLAYER_ID + " = ?",
                new Object[]{scoreChange, playerId}
        );
        db.close();
    }



    public long addGame(long playerId, String mode, int score, int duration) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PLAYER_ID_FK, playerId); // Már long típusú
        values.put(DatabaseHelper.COLUMN_MODE, mode);
        values.put(DatabaseHelper.COLUMN_SCORE, score);
        values.put(DatabaseHelper.COLUMN_DURATION, duration);
        long id = db.insert(DatabaseHelper.TABLE_GAMES, null, values);
        db.close();
        return id;
    }


    public long addQuestion(int gameId, String questionText, float correctAnswer, float playerAnswer, boolean isCorrect) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_GAME_ID_FK, gameId);
        values.put(DatabaseHelper.COLUMN_QUESTION_TEXT, questionText);
        values.put(DatabaseHelper.COLUMN_CORRECT_ANSWER, correctAnswer);
        values.put(DatabaseHelper.COLUMN_PLAYER_ANSWER, playerAnswer);
        values.put(DatabaseHelper.COLUMN_IS_CORRECT, isCorrect ? 1 : 0);
        long id = db.insert(DatabaseHelper.TABLE_QUESTIONS, null, values);
        db.close();
        return id;
    }



    public int getTotalScore(long playerId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + DatabaseHelper.COLUMN_SCORE + ") FROM " + DatabaseHelper.TABLE_GAMES +
                        " WHERE " + DatabaseHelper.COLUMN_PLAYER_ID_FK + " = ?",
                new String[]{String.valueOf(playerId)});
        int totalScore = 0;
        if (cursor.moveToFirst()) {
            totalScore = cursor.getInt(0); // Összesített pontszám
        }
        cursor.close();
        db.close();
        return totalScore;
    }

}