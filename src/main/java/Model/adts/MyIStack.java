package Model.adts;


import Exceptions.EmptyStackException;

public interface MyIStack<T> {
    T pop() throws EmptyStackException;
    void push(T v);
    T peek() throws EmptyStackException;
    boolean isEmpty();
}
