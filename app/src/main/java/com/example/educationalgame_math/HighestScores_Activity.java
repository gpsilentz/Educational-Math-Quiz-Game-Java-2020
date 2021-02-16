package com.example.educationalgame_math;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HighestScores_Activity extends AppCompatActivity {

    private ListView mList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_scores);

        HighScores hs = new HighScores(this);

        ArrayList<String> listItems = new ArrayList<String>();
        listItems = hs.getScores();

        mList = findViewById(R.id.ListView_Scores);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        mList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}