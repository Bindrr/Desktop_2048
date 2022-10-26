package state;

public class StackStateReturner implements StateReturner {
    private static StackStateReturner instance = null;

    public static StackStateReturner getInstance(StackStateSaver stateSaver) {
        if (instance == null) instance = new StackStateReturner(stateSaver);
        return instance;
    }

    private final StackStateSaver stackStateSaver;

    private StackStateReturner(StackStateSaver stackStateSaver) {
        this.stackStateSaver = stackStateSaver;
    }

    @Override
    public State rollback() {
        return stackStateSaver.getStates().pop();
    }

    @Override
    public State fakeRollback() {
        return stackStateSaver.getStates().peek();
    }
}
