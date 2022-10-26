package com.javarush.task.task35.task3513_2048.view;

public interface View {
    boolean isGameWon();

    void setGameWon(boolean gameWon);

    boolean isGameLost();

    void setGameLost(boolean gameLost);

    void resetBooleans();
}
