package state;

public interface StateReturner {
    State rollback() throws Exception;

    State fakeRollback();
}
