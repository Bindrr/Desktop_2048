package com.javarush.task.task35.task3513_2048.state;

public interface StateReturner {
    State rollback() throws Exception;

    State fakeRollback();
}
