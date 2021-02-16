package com.example.educationalgame_math;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button mButtonPlay;
    private Button mButtonLearderboard;
    private Button mButtonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mButtonPlay = findViewById(R.id.button_Play);
        mButtonLearderboard = findViewById(R.id.button_Leaderboard);
        mButtonSettings = findViewById(R.id.button_Settings);

        mButtonPlay.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        mButtonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });

        mButtonLearderboard.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HighestScores_Activity.class);
            startActivity(intent);
        });
    }
}