package com.javarush.task.task35.task3513_2048.state;

import com.javarush.task.task35.task3513_2048.model.GameModel;
import com.javarush.task.task35.task3513_2048.objects.Tile;

import java.util.Stack;

public class StackStateSaver implements StateSaver {
    private static final StackStateSaver instance = new StackStateSaver();

    public static StateSaver getInstance() {
        return instance;
    }

    private StackStateSaver() {
    }

    private final Stack<State> states = new Stack<>();

    public Stack<State> getStates() {
        return states;
    }

    @Override
    public void saveState(State state) {
        Tile[][] tempTiles = new Tile[GameModel.FIELD_WIDTH][GameModel.FIELD_WIDTH];
        for (int i = 0; i < GameModel.FIELD_WIDTH; i++)
            for (int j = 0; j < GameModel.FIELD_WIDTH; j++)
                tempTiles[i][j] = new Tile(state.getGameTiles()[i][j].getValue());

        states.push(new State(tempTiles, state.getScore(), state.getMaxTileValue()));
    }
}
