package Model.adts;

import Exceptions.InvalidPositionException;

import java.util.List;
import java.util.Vector;

public class MyList<T> implements MyIList<T>{
    private List<T> list;

    public MyList() {
        this.list = new Vector<>();
    }


    @Override
    public synchronized void add(T value) {
        this.list.add(value);
    }

    @Override
    public synchronized int size() {
        return this.list.size();
    }

    @Override
    public synchronized T getAt(int position) throws InvalidPositionException {
        if(position < 0 || position >= this.list.size()){
            throw new InvalidPositionException("invalid position!");
        }
        return this.list.get(position);
    }

    @Override
    public synchronized T removeAt(int position) throws InvalidPositionException {
        if(position < 0 || position >= this.list.size()){
            throw new InvalidPositionException("invalid position!");
        }
        return this.list.remove(position);
    }

    @Override
    public synchronized String toString(){
        StringBuilder retString = new StringBuilder();
        this.list.forEach(i->retString.append(i).append('\n'));
        return retString.toString();
    }
}
