package com.example.educationalgame_math;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HighScores extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MAHTQUIZEXTRA";
    private static final int DATABASE_VERSION = 13;
    private Context context;

    HighScores(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE HIGHSCOREST (NAME TEXT, SCORE INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS HIGHSCOREST");
            onCreate(db);
        }
    }

    public void add(String name, int score) {
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("SCORE", score);

        SQLiteDatabase db = getWritableDatabase();

        db.insert("HIGHSCOREST", null, values);
        db.close();
    }

    public void update(String name, int score) {
        ContentValues values = new ContentValues();
        values.put("SCORE", score);

        SQLiteDatabase db = getWritableDatabase();

        db.update("HIGHSCOREST", values, "NAME = ?", new String[]{name});
        db.close();
    }

    public ArrayList<String> getScores() {
        ArrayList<String> listItemsScores = new ArrayList<String>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM HIGHSCOREST ORDER BY SCORE DESC", null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                listItemsScores.add("Name: " + cursor.getString(cursor.getColumnIndex("NAME")) +
                        "\nScore: " + cursor.getString(cursor.getColumnIndex("SCORE")) + "\n");
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return listItemsScores;
    }

    public int getScoreForUser(String username, int curScore) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SCORE FROM HIGHSCOREST WHERE NAME = '"+username+"'", null);

        int highestScore = 0;

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                highestScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex("SCORE")));
            } while (cursor.moveToNext());

            cursor.close();
        }
        else {
            add(username, curScore);
            highestScore = curScore;
        }

        db.close();

        return highestScore;
    }
}
