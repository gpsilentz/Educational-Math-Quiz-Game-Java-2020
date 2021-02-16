package com.example.educationalgame_math;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SoundPool player_ans;
    MediaPlayer player_background;
    int player_correct_ans;
    int player_wrong_ans;
    private Button mButtonCheck1;
    private Button mButtonCheck2;
    private Button mButtonCheck3;
    private TextView mTextViewEquation;
    private TextView mTextViewRound;
    private TextView mTextViewResult;
    private TextView mTextViewTimer;
    private TextView mTextViewScore;
    private TextView mTextViewLives;
    private TextView mTextViewTip;
    private ProgressBar mProgressBar;
    private Game gameAct;
    private CountDownTimer mTimer;
    private ShakeActivity mShaker;
    private int gameForRes;
    private Random rand;
    private int progressTimer;
    private long timeLeft;
    private int mGameDifficulty;
    private boolean mGameSounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewScore = findViewById(R.id.textView_Score);
        mTextViewResult = findViewById(R.id.textView_Result);
        mTextViewTimer = findViewById(R.id.textView_TimeLeft);
        mTextViewRound = findViewById(R.id.textView_Round);
        mTextViewLives = findViewById(R.id.textView_Lives);
        mTextViewTip = findViewById(R.id.textView_Tip);
        mButtonCheck1 = findViewById(R.id.buttonCheck1);
        mButtonCheck2 = findViewById(R.id.buttonCheck2);
        mButtonCheck3 = findViewById(R.id.buttonCheck3);
        mProgressBar = findViewById(R.id.progressBar);
        mTextViewEquation = findViewById(R.id.textView_MathEquation);

        SharedPreferences settings = getSharedPreferences("GameSettings", MODE_PRIVATE);

        mGameDifficulty = settings.getInt("GameDifficulty", 0);
        mGameSounds = settings.getBoolean("GameSounds", true);

        if (mGameSounds) {
            player_ans = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            player_correct_ans = player_ans.load(this, R.raw.correct, 1);
            player_wrong_ans = player_ans.load(this, R.raw.wrong, 1);
            player_background = MediaPlayer.create(this, R.raw.background);
        }

        rand = new Random();
        gameAct = new Game();
        gameForRes = runGame();

        if (mGameSounds) {
            player_background.setLooping(true);
            player_background.start();
        }

        setButtonListeners();
        setShakeListener();
    }

    public void setButtonListeners() {
        mButtonCheck1.setOnClickListener(v -> {
            checkResult(gameForRes == Integer.parseInt(mButtonCheck1.getText().toString()));
            gameForRes = runGame();
        });

        mButtonCheck2.setOnClickListener(v -> {
            checkResult(gameForRes == Integer.parseInt(mButtonCheck2.getText().toString()));
            gameForRes = runGame();
        });

        mButtonCheck3.setOnClickListener(v -> {
            checkResult(gameForRes == Integer.parseInt(mButtonCheck3.getText().toString()));
            gameForRes = runGame();
        });
    }

    public void setShakeListener() {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeActivity(this);
        mShaker.setOnShakeListener(() -> {
            vibe.vibrate(500);

            // After shaking the phone, stop the timer and start a new one with time left plus 5 seconds
            stopTimer();
            startTimer((int)timeLeft + 5000);
        });
    }

    public void startTimer(int msSecs) {
        mTimer = new CountDownTimer(msSecs, 1000) {
            public void onTick(long millisUntilFinished) {
                progressTimer++;
                mProgressBar.setProgress(progressTimer);
                mProgressBar.setMax(20);

                mTextViewTimer.setText(getString(R.string.timeLeft) + millisUntilFinished / 1000 + " sec");

                timeLeft = millisUntilFinished;

                if ((millisUntilFinished / 1000) < 5)
                    mTextViewTip.setVisibility(View.VISIBLE);
            }

            public void onFinish() {
                gameAct.minusLives();

                mTextViewTimer.setText("");
                mTextViewLives.setText(getString(R.string.lives) + gameAct.getLives());
                mTextViewResult.setText(R.string.timeout);
                mTextViewResult.setTextColor(Color.RED);

                mProgressBar.setProgress(100);

                gameForRes = runGame();
            }
        };

        if (mTimer != null)
            mTimer.start();
    }

    public void stopTimer() {
        if (mTimer != null)
            mTimer.cancel();

        progressTimer = 0;
        mProgressBar.setProgress(progressTimer);
    }

    public void checkResult(boolean result) {
        if (result) {
            gameAct.addScore();

            mTextViewScore.setText(getString(R.string.score) + gameAct.getScore());
            mTextViewResult.setText(R.string.correctans);
            mTextViewResult.setTextColor(Color.GREEN);

            if (mGameSounds)
                player_ans.play(player_correct_ans, 1, 1, 0, 0, 1);
        }
        else {
            gameAct.minusLives();

            mTextViewLives.setText(getString(R.string.lives) + gameAct.getLives());
            mTextViewResult.setText(R.string.wrongans);
            mTextViewResult.setTextColor(Color.RED);

            if (mGameSounds)
                player_ans.play(player_wrong_ans, 1, 1, 0, 0, 1);
        }
    }

    public int runGame() {
        if (!gameAct.validateGame()) {
            finishGame();
            return 0;
        }

        gameAct.addRound();

        stopTimer();
        startTimer(gameAct.MILI_SECONDS_PER_GAME);

        mTextViewRound.setText(getString(R.string.round) + gameAct.getRounds());

        char mathSign;
        ArrayList<Integer> mEquation = new ArrayList<>();

        int randFunc = rand.nextInt(3);
        switch (randFunc) {
            case 0:
                mEquation = gameAct.generateEquation(mGameDifficulty, Game.Equation.PLUS);
                mathSign = '+';
                break;
            case 1:
                mEquation = gameAct.generateEquation(mGameDifficulty, Game.Equation.MINUS);
                mathSign = '-';
                break;
            default:
                mEquation = gameAct.generateEquation(mGameDifficulty, Game.Equation.MULTIPLY);
                mathSign = '*';
                break;
        }

        int[] fakeAnswers = gameAct.generateFakeEquation(mEquation.get(2));
        int randButton = rand.nextInt(3);

        switch (randButton) {
            case 0:
                mButtonCheck1.setText(mEquation.get(2).toString());
                mButtonCheck2.setText(String.valueOf(fakeAnswers[0]));
                mButtonCheck3.setText(String.valueOf(fakeAnswers[1]));
                break;
            case 1:
                mButtonCheck1.setText(String.valueOf(fakeAnswers[0]));
                mButtonCheck2.setText(mEquation.get(2).toString());
                mButtonCheck3.setText(String.valueOf(fakeAnswers[1]));
                break;
            default:
                mButtonCheck1.setText(String.valueOf(fakeAnswers[0]));
                mButtonCheck2.setText(String.valueOf(fakeAnswers[1]));
                mButtonCheck3.setText(mEquation.get(2).toString());
                break;
        }

        StringBuilder eqStr = new StringBuilder();
        eqStr.append(mEquation.get(0).toString()).append(" " + mathSign + " ").append(mEquation.get(1).toString());

        mTextViewEquation.setText(eqStr);

        return mEquation.get(2);
    }

    public void finishGame() {
        stopTimer();

        if (mGameSounds) {
            player_background.stop();
            player_ans.release();
        }

        Intent intent = new Intent(getApplicationContext(), GameFinishedActivity.class);
        intent.putExtra("Score", gameAct.getScore());

        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        mShaker.resume();
        super.onResume();

        if (mGameSounds) {
            player_background.start();
            player_background.setLooping(true);
        }
    }

    @Override
    public void onPause() {
        mShaker.pause();
        super.onPause();

        if (mGameSounds) {
            player_background.stop();
            player_ans.release();
        }
    }

    @Override
    public void onStop() {
        mShaker.pause();
        super.onStop();

        if (mGameSounds) {
            player_background.stop();
            player_ans.release();
        }
    }

    @Override
    public void onDestroy() {
        mShaker.pause();
        super.onDestroy();

        if (mGameSounds) {
            player_background.stop();
            player_ans.release();
        }
    }
}