package com.example.math_ninja;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "number_ninjas.db";
    private static final int DATABASE_VERSION = 4; // Updated version number

    // Table and column names for Players table
    public static final String TABLE_PLAYERS = "Players";
    public static final String COLUMN_PLAYER_ID = "player_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Table and column names for Game Sessions table
    public static final String TABLE_GAME_SESSIONS = "GameSessions";
    public static final String COLUMN_SESSION_ID = "session_id";
    public static final String COLUMN_PLAYER_ID_FK = "player_id_fk";
    public static final String COLUMN_GAME_TYPE = "game_type";
    public static final String COLUMN_SESSION_SCORE = "session_score";
    public static final String COLUMN_SESSION_DATE = "session_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Játékosok tábla létrehozása
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PLAYERS + " (" +
                    COLUMN_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_SCORE + " INTEGER DEFAULT 0, " +
                    COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

            // Játékmenetek tábla létrehozása
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GAME_SESSIONS + " (" +
                    COLUMN_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PLAYER_ID_FK + " INTEGER, " +
                    COLUMN_GAME_TYPE + " TEXT NOT NULL, " +
                    COLUMN_SESSION_SCORE + " INTEGER DEFAULT 0, " +
                    COLUMN_SESSION_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COLUMN_PLAYER_ID_FK + ") REFERENCES " +
                    TABLE_PLAYERS + "(" + COLUMN_PLAYER_ID + "));");

            Log.d(TAG, "onCreate: Táblák sikeresen létrehozva.");
        } catch (Exception e) {
            Log.e(TAG, "Hiba a táblák létrehozásakor: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion < 3) {
                db.execSQL("ALTER TABLE " + TABLE_PLAYERS + " ADD COLUMN " +
                        COLUMN_SCORE + " INTEGER DEFAULT 0;");
            }

            if (oldVersion < 4) {
                // Játékmenetek tábla létrehozása az új verzióhoz
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GAME_SESSIONS + " (" +
                        COLUMN_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_PLAYER_ID_FK + " INTEGER, " +
                        COLUMN_GAME_TYPE + " TEXT NOT NULL, " +
                        COLUMN_SESSION_SCORE + " INTEGER DEFAULT 0, " +
                        COLUMN_SESSION_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY(" + COLUMN_PLAYER_ID_FK + ") REFERENCES " +
                        TABLE_PLAYERS + "(" + COLUMN_PLAYER_ID + "));");
            }

            Log.d(TAG, "onUpgrade: Adatbázis frissítve a " + newVersion + " verzióra.");
        } catch (Exception e) {
            Log.e(TAG, "Hiba az adatbázis frissítésekor: " + e.getMessage());
        }
    }
}