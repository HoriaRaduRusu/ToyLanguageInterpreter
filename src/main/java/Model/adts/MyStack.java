package Model.adts;

import Exceptions.EmptyStackException;

import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MyStack<T> implements MyIStack<T>{

    private Stack<T> stack;

    public MyStack() {this.stack = new Stack<T>();}

    @Override
    public synchronized T pop() throws EmptyStackException {
        if(this.stack.isEmpty())
            throw new EmptyStackException("The stack is empty!");
        return this.stack.pop();
    }

    @Override
    public synchronized void push(T v) {
        this.stack.push(v);
    }

    @Override
    public synchronized T peek() throws EmptyStackException{
        if(this.stack.isEmpty())
            throw new EmptyStackException("The stack is empty!");
        return this.stack.peek();
    }

    @Override
    public synchronized boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public synchronized String toString() {
        StringBuilder retString= new StringBuilder();
        IntStream.range(0, this.stack.size())
                .mapToObj(i->this.stack.get(this.stack.size()-1-i))
                .forEach(i->retString.append(i).append('\n'));
        return retString.toString();
    }
}
