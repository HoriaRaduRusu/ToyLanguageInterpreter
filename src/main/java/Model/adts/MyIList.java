package Model.adts;

import Exceptions.InvalidPositionException;

public interface MyIList<T> {
    void add(T value);

    int size();

    T getAt(int position) throws InvalidPositionException;

    T removeAt(int position) throws InvalidPositionException;
}
