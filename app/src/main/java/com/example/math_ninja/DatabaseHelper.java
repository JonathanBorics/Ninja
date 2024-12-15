package com.example.math_ninja;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "number_ninjas.db";
    private static final int DATABASE_VERSION = 3; // Updated version number

    // Table and column names
    public static final String TABLE_PLAYERS = "Players";
    public static final String COLUMN_PLAYER_ID = "player_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SCORE = "score"; // New column
    public static final String COLUMN_CREATED_AT = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PLAYERS + " (" +
                    COLUMN_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_SCORE + " INTEGER DEFAULT 0, " +
                    COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");
            Log.d(TAG, "onCreate: Tables created successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion < 3) {
                db.execSQL("ALTER TABLE " + TABLE_PLAYERS + " ADD COLUMN " + COLUMN_SCORE + " INTEGER DEFAULT 0;");
                Log.d(TAG, "onUpgrade: Database upgraded to version " + newVersion);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
        }
    }
}
