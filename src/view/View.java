package view;

public interface View {
    boolean isGameWon();

    void setGameWon(boolean gameWon);

    boolean isGameLost();

    void setGameLost(boolean gameLost);

    void resetBooleans();
}
