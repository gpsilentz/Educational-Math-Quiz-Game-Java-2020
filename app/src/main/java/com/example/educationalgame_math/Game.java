package com.example.educationalgame_math;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int mScore = 0;
    private int mRounds = 0;
    private int mLives = 3;

    final public int MILI_SECONDS_PER_GAME = 20000;
    final public int MAXIMUM_ROUNDS_PER_GAME = 20;

    public enum Equation {
        PLUS,
        MINUS,
        MULTIPLY;
    }

    public int[] getRandomNumbers(int difficulty, Equation action) {
        Random rand = new Random();

        int num1 = 0, num2 = 0;

        switch (difficulty) {
            case 0:
                if (action == Equation.MULTIPLY) {
                    num1 = rand.nextInt(10) + 2;
                    num2 = rand.nextInt(10) + 2;
                }
                else {
                    num1 = rand.nextInt(100) + 1;
                    num2 = rand.nextInt(100) + 1;
                }
                break;
            case 1:
                if (action == Equation.MULTIPLY) {
                    num1 = rand.nextInt(20) + 2;
                    num2 = rand.nextInt(20) + 2;
                }
                else {
                    num1 = rand.nextInt(500) + 1;
                    num2 = rand.nextInt(500) + 1;
                }
                break;
            case 2:
                if (action == Equation.MULTIPLY) {
                    num1 = rand.nextInt(50) + 2;
                    num2 = rand.nextInt(50) + 2;
                }
                else {
                    num1 = rand.nextInt(2000) + 1;
                    num2 = rand.nextInt(2000) + 1;
                }
                break;
        }

        return new int[] { num1, num2 };
    }

    public int[] generateFakeEquation(int result) {
        Random rand = new Random();

        float fakePercentage = rand.nextInt(25) + 1;

        float val = (fakePercentage / 100) * (float)result;

        int fakeAns1 = (int)((float)result + val);
        int fakeAns2 = (int)((float)result - val);

        return new int[] { fakeAns1, fakeAns2 };
    }

    public ArrayList<Integer> generateEquation(int difficulty, Equation action) {
        int[] rndNumbers = getRandomNumbers(difficulty, action);
        int result = 0;

        switch (action) {
            case PLUS:
                result = rndNumbers[0] + rndNumbers[1];
                break;
            case MINUS:
                result = rndNumbers[0] - rndNumbers[1];
                break;
            case MULTIPLY:
                result = rndNumbers[0] * rndNumbers[1];
                break;
        }

        ArrayList<Integer> mEquation = new ArrayList<>();

        mEquation.add(rndNumbers[0]);
        mEquation.add(rndNumbers[1]);
        mEquation.add(result);

        return mEquation;
    }

    public boolean validateGame() { return !(this.mRounds >= MAXIMUM_ROUNDS_PER_GAME || this.mLives < 0); }

    public void addRound() { this.mRounds++; }

    public void addScore() { this.mScore++; }

    public void minusLives() { this.mLives--; }

    public int getLives() { return this.mLives; }

    public int getRounds() { return this.mRounds; }

    public int getScore() { return this.mScore; }
}
