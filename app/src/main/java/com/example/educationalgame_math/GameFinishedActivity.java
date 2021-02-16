package com.example.educationalgame_math;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GameFinishedActivity extends AppCompatActivity {

    private TextView mTextViewHighScore;
    private TextView mTextViewCurScore;

    private Button mButtonRestart;
    private Button mButtonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finished);

        mTextViewHighScore = findViewById(R.id.textView_highestScore);
        mTextViewCurScore = findViewById(R.id.textView_currentScore);
        mButtonRestart = findViewById(R.id.buttonRestart);
        mButtonMenu = findViewById(R.id.buttonMenu);

        Intent intent = getIntent();
        int currentScore = intent.getIntExtra("Score", 0);

        mTextViewCurScore.setText(getString(R.string.curscore) + String.valueOf(currentScore));

        SharedPreferences settings = getSharedPreferences("GameSettings", MODE_PRIVATE);
        String gameUsername = settings.getString("GameUsername", "noname");

        HighScores hs = new HighScores(this);

        int highestScore = hs.getScoreForUser(gameUsername, currentScore);

        if (currentScore > highestScore) {
            mTextViewHighScore.setText(getString(R.string.highscore) + String.valueOf(currentScore));
            hs.update(gameUsername, currentScore);
        }
        else
            mTextViewHighScore.setText(getString(R.string.highscore) + String.valueOf(highestScore));

        mButtonRestart.setOnClickListener(v -> {
            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentMain);

            finish();
        });

        mButtonMenu.setOnClickListener(v -> {
            Intent intentMenu = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intentMenu);

            finish();
        });
    }
}