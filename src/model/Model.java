package model;

public interface Model {
    //general
    boolean isGameWon();

    boolean isGameLost();

    void resetGame();

    //state
    void rollback();

    void saveState();

    //moves
    void randomMove();

    void autoMove();

    void left();

    void right();

    void up();

    void down();
}
