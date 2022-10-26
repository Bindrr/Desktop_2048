package com.javarush.task.task35.task3513_2048.controller;

import com.javarush.task.task35.task3513_2048.objects.Tile;
import com.javarush.task.task35.task3513_2048.view.MainView;
import com.javarush.task.task35.task3513_2048.model.GameModel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    public static final int WINNING_TILE = 2048;

    private final GameModel gameModel;
    private final MainView view;

    public Controller(GameModel gameModel, MainView view) {
        this.gameModel = gameModel;
        this.view = view;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int eventKeyCode = event.getKeyCode();

        if (isSpecialKey(eventKeyCode)) handleSpecialKey(eventKeyCode);
        else {
            if (!view.isGameWon() || !view.isGameLost())
                switch (eventKeyCode) {
                    case KeyEvent.VK_R -> gameModel.randomMove();
                    case KeyEvent.VK_A -> gameModel.autoMove();
                    case KeyEvent.VK_LEFT -> gameModel.left();
                    case KeyEvent.VK_RIGHT -> gameModel.right();
                    case KeyEvent.VK_UP -> gameModel.up();
                    case KeyEvent.VK_DOWN -> gameModel.down();
                }
        }

        if (gameModel.isGameLost()) view.setGameLost(true);

        view.repaint();
    }

    private boolean isSpecialKey(int eventKeyCode) {
        return eventKeyCode == KeyEvent.VK_ESCAPE || eventKeyCode == KeyEvent.VK_Z;
    }

    private void handleSpecialKey(int eventKeyCode) {
        if (eventKeyCode == KeyEvent.VK_ESCAPE) {
            gameModel.resetGame();
            view.resetBooleans();
        } else if (eventKeyCode == KeyEvent.VK_Z) {
            gameModel.rollback();
            view.resetBooleans();
        }
    }

    public Tile[][] getGameTiles() {
        return gameModel.getGameTiles();
    }

    public int getScore() {
        return gameModel.getScore();
    }
}
