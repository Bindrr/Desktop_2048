package objects;

public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private final int amountOfEmptyTiles;
    private final int score;
    private final Move move;

    public MoveEfficiency(Move move) {
        this(-1, 0, move);
    }

    public MoveEfficiency(int amountOfEmptyTiles, int score, Move move) {
        this.amountOfEmptyTiles = amountOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public int compareTo(MoveEfficiency o) {
        return Integer.compare(Integer.compare(amountOfEmptyTiles, o.amountOfEmptyTiles), Integer.compare(score, o.score));
    }
}
