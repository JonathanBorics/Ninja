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
            ContentValues values = new ContentValues();
            values.put(COLUMN_SCORE, score);
            int rowsAffected = db.update(TABLE_PLAYERS, values, COLUMN_PLAYER_ID + "=?", new String[]{String.valueOf(playerId)});
            if (rowsAffected > 0) {
                Log.d(TAG, "saveScore: Score updated for playerId=" + playerId);
            } else {
                Log.e(TAG, "saveScore: Failed to update score.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error saving score: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
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
    public int getTotalScore(long playerId) {
        int totalScore = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            String query = "SELECT " + COLUMN_SCORE + " FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_PLAYER_ID + "=?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(playerId)});

            if (cursor.moveToFirst()) {
                totalScore = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
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
