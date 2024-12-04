package com.example.math_ninja;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "number_ninjas.db";
    private static final int DATABASE_VERSION = 1;

    // Players table
    public static final String TABLE_PLAYERS = "Players";
    public static final String COLUMN_PLAYER_ID = "player_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Games table
    public static final String TABLE_GAMES = "Games";
    public static final String COLUMN_GAME_ID = "game_id";
    public static final String COLUMN_PLAYER_ID_FK = "player_id";
    public static final String COLUMN_MODE = "mode";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_PLAYED_AT = "played_at";

    // Questions table
    public static final String TABLE_QUESTIONS = "Questions";
    public static final String COLUMN_QUESTION_ID = "question_id";
    public static final String COLUMN_GAME_ID_FK = "game_id";
    public static final String COLUMN_QUESTION_TEXT = "question_text";
    public static final String COLUMN_CORRECT_ANSWER = "correct_answer";
    public static final String COLUMN_PLAYER_ANSWER = "player_answer";
    public static final String COLUMN_IS_CORRECT = "is_correct";

    // SQL Statements
    private static final String CREATE_PLAYERS_TABLE =
            "CREATE TABLE " + TABLE_PLAYERS + " (" +
                    COLUMN_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    private static final String CREATE_GAMES_TABLE =
            "CREATE TABLE " + TABLE_GAMES + " (" +
                    COLUMN_GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PLAYER_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_MODE + " TEXT NOT NULL, " +
                    COLUMN_SCORE + " INTEGER NOT NULL, " +
                    COLUMN_DURATION + " INTEGER NOT NULL, " +
                    COLUMN_PLAYED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (" + COLUMN_PLAYER_ID_FK + ") REFERENCES " + TABLE_PLAYERS + "(" + COLUMN_PLAYER_ID + "));";

    private static final String CREATE_QUESTIONS_TABLE =
            "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                    COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GAME_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_QUESTION_TEXT + " TEXT NOT NULL, " +
                    COLUMN_CORRECT_ANSWER + " REAL NOT NULL, " +
                    COLUMN_PLAYER_ANSWER + " REAL, " +
                    COLUMN_IS_CORRECT + " INTEGER, " +
                    "FOREIGN KEY (" + COLUMN_GAME_ID_FK + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_GAME_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLAYERS_TABLE);
        db.execSQL(CREATE_GAMES_TABLE);
        db.execSQL(CREATE_QUESTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }
}
