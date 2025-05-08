package com.example.math_ninja;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager extends DatabaseHelper {

    private static final String TAG = "DatabaseManager";

    public DatabaseManager(Context context) {
        super(context);
    }

    public long addPlayer(String name) {
        long playerId = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_SCORE, 0); // Initialize with default score
            playerId = db.insert(TABLE_PLAYERS, null, values);
            Log.d(TAG, "addPlayer: Player added with ID=" + playerId);
        } catch (Exception e) {
            Log.e(TAG, "Error adding player: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return playerId;
    }

    public void saveScore(long playerId, int score) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            // Először lekérdezzük a jelenlegi pontszámot
            String query = "SELECT " + COLUMN_SCORE + " FROM " + TABLE_PLAYERS +
                    " WHERE " + COLUMN_PLAYER_ID + "=?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(playerId)});

            int currentScore = 0;
            if (cursor.moveToFirst()) {
                int scoreColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_SCORE);
                currentScore = cursor.getInt(scoreColumnIndex);
            }
            cursor.close();

            // Hozzáadjuk az új pontszámot a régihez
            int newTotalScore = currentScore + score;

            // Frissítjük az adatbázist
            ContentValues values = new ContentValues();
            values.put(COLUMN_SCORE, newTotalScore);
            int rowsAffected = db.update(TABLE_PLAYERS, values, COLUMN_PLAYER_ID + "=?",
                    new String[]{String.valueOf(playerId)});

            if (rowsAffected > 0) {
                Log.d(TAG, "saveScore: Pontszám frissítve, játékos ID=" + playerId +
                        ", új összpontszám=" + newTotalScore);
            } else {
                Log.e(TAG, "saveScore: Nem sikerült frissíteni a pontszámot.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Hiba a pontszám mentésekor: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public long saveGameSession(long playerId, String gameType, int sessionScore) {
        long sessionId = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            // Hozzáadjuk az új játékmenetet
            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAYER_ID_FK, playerId);
            values.put(COLUMN_GAME_TYPE, gameType);
            values.put(COLUMN_SESSION_SCORE, sessionScore);

            sessionId = db.insert(TABLE_GAME_SESSIONS, null, values);

            // Frissítjük a játékos összpontszámát is
            saveScore(playerId, sessionScore);

            Log.d(TAG, "saveGameSession: Játékmenet mentve, ID=" + sessionId);
        } catch (Exception e) {
            Log.e(TAG, "Hiba a játékmenet mentésekor: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return sessionId;
    }

    public Cursor getTopScores(int limit) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_PLAYERS + " ORDER BY " + COLUMN_SCORE + " DESC LIMIT " + limit;
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching top scores: " + e.getMessage());
        }
        return cursor;
    }

    public Cursor getPlayerGameHistory(long playerId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_GAME_SESSIONS +
                    " WHERE " + COLUMN_PLAYER_ID_FK + "=?" +
                    " ORDER BY " + COLUMN_SESSION_DATE + " DESC";
            cursor = db.rawQuery(query, new String[]{String.valueOf(playerId)});
        } catch (Exception e) {
            Log.e(TAG, "Hiba a játéktörténet lekérdezésekor: " + e.getMessage());
        }
        return cursor;
    }

    public int getTotalScore(long playerId) {
        int totalScore = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            String query = "SELECT " + COLUMN_SCORE + " FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_PLAYER_ID + "=?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(playerId)});

            if (cursor.moveToFirst()) {
                int scoreColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_SCORE);
                totalScore = cursor.getInt(scoreColumnIndex);
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error fetching total score: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return totalScore;
    }
}