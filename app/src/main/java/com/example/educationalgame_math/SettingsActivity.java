package com.example.educationalgame_math;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private Spinner mDropdown;
    private Button mButtonSave;
    private Button mButtonGoogleLogin;
    private CheckBox mCheckboxSounds;
    private EditText mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDropdown = findViewById(R.id.spinner);
        mButtonSave = findViewById(R.id.buttonSave);
        mCheckboxSounds = findViewById(R.id.checkBox);
        mUsername = findViewById(R.id.editTextUsername);
        mButtonGoogleLogin = findViewById(R.id.buttonGoogleLogin);

        SharedPreferences settings = getSharedPreferences("GameSettings", MODE_PRIVATE);

        int gameDifficulty = settings.getInt("GameDifficulty", 0);
        boolean gameSounds = settings.getBoolean("GameSounds", true);
        String gameUsername = settings.getString("GameUsername", "noname");

        Intent intentg = getIntent();
        String googleUsername = intentg.getStringExtra("GoogleUserName");

        mCheckboxSounds.setChecked(gameSounds);
        mUsername.setText(googleUsername == null || googleUsername.isEmpty() ? gameUsername : googleUsername);

        String[] items = new String[] { "Easy", "Medium", "Difficult" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        mDropdown.setAdapter(adapter);
        mDropdown.setSelection(gameDifficulty);

        mButtonSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = settings.edit();

            editor.putInt("GameDifficulty", Integer.parseInt(String.valueOf(mDropdown.getSelectedItemId())));
            editor.putBoolean("GameSounds", mCheckboxSounds.isChecked());

            if (!gameUsername.trim().isEmpty())
                editor.putString("GameUsername", mUsername.getText().toString());

            editor.commit();

            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);

            finish();
        });

        mButtonGoogleLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), GoogleAuth_Activity.class);
            startActivity(intent);

            finish();
        });
    }
}