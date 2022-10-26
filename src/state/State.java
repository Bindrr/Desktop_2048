package com.javarush.task.task35.task3513_2048.state;

import com.javarush.task.task35.task3513_2048.objects.Tile;

public class State {
    private Tile[][] gameTiles;
    private int score;
    private int maxTileValue;

    public State(Tile[][] gameTiles, int score, int maxTileValue) {
        this.gameTiles = gameTiles;
        this.score = score;
        this.maxTileValue = maxTileValue;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public void setGameTiles(Tile[][] gameTiles) {
        this.gameTiles = gameTiles;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMaxTileValue() {
        return maxTileValue;
    }

    public void setMaxTileValue(int maxTileValue) {
        this.maxTileValue = maxTileValue;
    }
}
