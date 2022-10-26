package model;

import controller.Controller;
import objects.Move;
import objects.MoveEfficiency;
import objects.Tile;
import state.State;
import state.StateReturner;
import state.StateSaver;
import view.MainView;

import java.util.*;

public class GameModel implements Model {
    public static final int FIELD_WIDTH = 4;

    private State state;
    private final List<StateSaver> stateSavers;
    private StateReturner stateReturner;

    private final MainView view;
    private final IMoves moves = new Moves();

    public GameModel(MainView view, List<StateSaver> stateSavers, StateReturner stateReturner) {
        this.view = view;
        this.stateSavers = stateSavers;
        this.stateReturner = stateReturner;
        resetGame();
    }

    @Override
    public boolean isGameWon() {
        return state.getMaxTileValue() == Controller.WINNING_TILE;
    }

    @Override
    public boolean isGameLost() {
        if (!isBoardFull()) return false;

        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                Tile t = state.getGameTiles()[x][y];
                if ((x < FIELD_WIDTH - 1 && t.getValue() == state.getGameTiles()[x + 1][y].getValue())
                        || ((y < FIELD_WIDTH - 1) && t.getValue() == state.getGameTiles()[x][y + 1].getValue())) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isBoardFull() {
        return moves.getEmptyTilesCount() == 0;
    }

    @Override
    public void resetGame() {
        state = new State(resetGameTiles(), 0, 2);
        moves.addTile();
        moves.addTile();
    }

    private Tile[][] resetGameTiles() {
        Tile[][] tiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tiles[i][j] = new Tile();
            }
        }

        return tiles;
    }

    @Override
    public void rollback() {
        try {
            state = stateReturner.rollback();
        } catch (Exception ignored) {}
    }

    @Override
    public void saveState() {
        stateSavers.forEach(stateSaver -> stateSaver.saveState(state));
    }

    @Override
    public void randomMove() {
        moves.randomMove();
    }

    @Override
    public void autoMove() {
        moves.autoMove();
    }

    @Override
    public void left() {
        moves.left();
    }

    @Override
    public void right() {
        moves.right();
    }

    @Override
    public void up() {
        moves.up();
    }

    @Override
    public void down() {
        moves.down();
    }

    //getters & setters
    public Tile[][] getGameTiles() {
        return state.getGameTiles();
    }

    public int getScore() {
        return state.getScore();
    }

    public void addStateSaver(StateSaver stateSaver) {
        this.stateSavers.add(stateSaver);
    }

    public void setStateRollback(StateReturner stateReturner) {
        this.stateReturner = stateReturner;
    }

    //moves
    private interface IMoves {
        default void randomMove() {
            int move = (int) (Math.random() * 4);
            switch (move) {
                case 1 -> left();
                case 2 -> right();
                case 3 -> up();
                case 4 -> down();
            }
        }

        void autoMove();

        void left();

        void right();

        void up();

        void down();

        void addTile();

        int getEmptyTilesCount();
    }

    private class Moves implements IMoves {
        @Override
        public void autoMove() {
            PriorityQueue<MoveEfficiency> movesQueue = new PriorityQueue<>(4, Collections.reverseOrder());
            movesQueue.addAll(getMoveEfficiencies());

            Objects.requireNonNull(movesQueue.peek()).getMove().move();
        }

        @Override
        public void left() {
            move();
        }

        @Override
        public void right() {
            state.setGameTiles(rotateClockwise());
            state.setGameTiles(rotateClockwise());
            move();
            state.setGameTiles(rotateClockwise());
            state.setGameTiles(rotateClockwise());
        }

        @Override
        public void up() {
            state.setGameTiles(rotateClockwise());
            state.setGameTiles(rotateClockwise());
            state.setGameTiles(rotateClockwise());
            move();
            state.setGameTiles(rotateClockwise());
        }

        @Override
        public void down() {
            state.setGameTiles(rotateClockwise());
            move();
            state.setGameTiles(rotateClockwise());
            state.setGameTiles(rotateClockwise());
            state.setGameTiles(rotateClockwise());
        }

        @Override
        public void addTile() {
            final List<Tile> emptyTiles = getEmptyTiles();

            if (emptyTiles.isEmpty()) view.setGameLost(true);
            else applyRandomValue(getRandomTile(emptyTiles));
        }

        @Override
        public int getEmptyTilesCount() {
            return getEmptyTiles().size();
        }

        private void move() {
            saveState();
            boolean anyTilesMoved = false;
            for (int i = 0; i < GameModel.FIELD_WIDTH; i++)
                if (compressTiles(state.getGameTiles()[i]) | mergeTiles(state.getGameTiles()[i])) anyTilesMoved = true;
            if (anyTilesMoved) addTile();
        }

        //private
        private List<MoveEfficiency> getMoveEfficiencies() {
            return List.of(getMoveEfficiency(this::left),
                    getMoveEfficiency(this::right),
                    getMoveEfficiency(this::up), getMoveEfficiency(this::down));
        }

        private MoveEfficiency getMoveEfficiency(Move move) {
            MoveEfficiency moveEfficiency = new MoveEfficiency(-1, 0, move);
            move.move();
            if (hasBoardChanged()) {
                moveEfficiency = new MoveEfficiency(getEmptyTilesCount(), state.getScore(), move);
            }
            rollback();
            return moveEfficiency;
        }

        private boolean hasBoardChanged()   {
            for (int i = 0; i < FIELD_WIDTH; i++)
                for (int j = 0; j < FIELD_WIDTH; j++)
                    if (state.getGameTiles()[i][j].getValue() !=
                            stateReturner.fakeRollback().getGameTiles()[i][j].getValue())
                        return true;
            return false;
        }

        private Tile[][] rotateClockwise() {
            final int N = state.getGameTiles().length;
            Tile[][] result = new Tile[N][N];
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    result[c][N - 1 - r] = state.getGameTiles()[r][c];
                }
            }
            return result;
        }

        private boolean compressTiles(Tile[] tiles) {
            boolean didAnyTilesCompress = false;

            int emptyPosition = 0;
            for (int i = 0; i < tiles.length; i++) {
                Tile tile = tiles[i];
                if (!tile.isEmpty()) {
                    if (i != emptyPosition) {
                        tiles[emptyPosition] = tile;
                        tiles[i] = new Tile();
                        didAnyTilesCompress = true;
                    }
                    emptyPosition++;
                }
            }

            return didAnyTilesCompress;
        }

        private boolean mergeTiles(Tile[] tiles) {
            if (!isMergingPossible(tiles)) return false;

            boolean didAnyTilesMerge = false;

            LinkedList<Tile> tilesList = new LinkedList<>();

            for (int i = 0; i < tiles.length-1; i++) {
                Tile tile = tiles[i];
                if (tile.isEmpty()) continue;

                Tile tile2 = tiles[i+1];

                if (tile.equals(tile2)) {
                    tile.updateValue();
                    tiles[i] = tile;
                    tiles[i+1] = new Tile();

                    int tileValue = tile.getValue();
                    state.setScore(tileValue);
                    if (tileValue > state.getMaxTileValue()) state.setMaxTileValue(tileValue);
                    if (isGameWon()) view.setGameWon(true);
                    didAnyTilesMerge = true;
                }

                tilesList.addLast(tile);
            }

            compressTiles(tiles);

            for (int i = 0; i < tilesList.size(); i++) {
                tiles[i] = tilesList.get(i);
            }

            return didAnyTilesMerge;
        }

        private boolean isMergingPossible(Tile[] tiles) {
            return filterEmptyTiles(tiles).size() <= 2;
        }

        private List<Tile> getEmptyTiles() {
            return Arrays.stream(state.getGameTiles()).flatMap(tiles -> filterEmptyTiles(tiles).stream()).toList();
        }

        private List<Tile> filterEmptyTiles(Tile[] tiles) {
            return Arrays.stream(tiles).filter(Tile::isEmpty).toList();
        }

        private void applyRandomValue(Tile tile) {
            int valueForTile = Math.random() < 0.9 ? 2 : 4;
            tile.setValue(valueForTile);
            if (valueForTile > state.getMaxTileValue()) state.setMaxTileValue(valueForTile);
        }

        private Tile getRandomTile(List<Tile> tiles) {
            int randomTileIndex = (int) (Math.random() * tiles.size()) % tiles.size();
            return tiles.get(randomTileIndex);
        }
    }
}
