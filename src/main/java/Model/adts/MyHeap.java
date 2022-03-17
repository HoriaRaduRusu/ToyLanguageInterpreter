package Model.adts;

import Exceptions.InvalidIDException;
import Exceptions.NullKeyException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MyHeap<T> implements MyIHeap<Integer, T>{

    private int nextFreeAddress;
    private Map<Integer, T> heapTable;

    public MyHeap(){
        this.heapTable = new ConcurrentHashMap<>();
        this.nextFreeAddress = 1;
    }

    @Override
    public synchronized T lookup(Integer id) throws InvalidIDException, NullKeyException {
        if(id == null)
            throw new NullKeyException("Key is null!");
        T result = this.heapTable.get(id);
        if(result == null)
            throw new InvalidIDException(id+" does not appear");
        return result;
    }

    @Override
    public synchronized boolean isDefined(Integer id) throws NullKeyException {
        if(id == null)
            throw new NullKeyException("Key is null!");
        return this.heapTable.containsKey(id);
    }

    @Override
    public synchronized void update(Integer id, T val) throws InvalidIDException, NullKeyException {
        if(id == null)
            throw new NullKeyException("Key is null!");
        if(id == 0)
            throw new InvalidIDException("0 is an invalid address!");
        if(!this.heapTable.containsKey(id))
            throw new InvalidIDException("key is not defined!");
        this.heapTable.put(id, val);
    }

    @Override
    public synchronized void add(Integer id, T val) throws InvalidIDException, NullKeyException {
        throw new InvalidIDException("You cannot specify the key in the heap!");
    }

    @Override
    public synchronized void add(T val){
        this.heapTable.put(this.nextFreeAddress++, val);
    }

    @Override
    public synchronized void remove(Integer id) throws NullKeyException, InvalidIDException {
        if(id == null)
            throw new NullKeyException("Key is null!");
        if(id == 0)
            throw new InvalidIDException("0 is an invalid address!");
        if(!this.heapTable.containsKey(id))
            throw new InvalidIDException("key is not defined!");
        this.heapTable.remove(id);
    }

    @Override
    public synchronized  Set<Integer> getKeys() {
        return this.heapTable.keySet();
    }

    @Override
    public synchronized Integer getNextAddress() {
        return nextFreeAddress;
    }

    @Override
    public synchronized void setContent(Map<Integer, T> content) {
        this.heapTable = content;
        this.nextFreeAddress = this.heapTable.keySet().stream().mapToInt(v->v).max().orElse(0)+1;
    }

    @Override
    public synchronized Map<Integer, T> getContent() {
        return this.heapTable;
    }


    @Override
    public synchronized String toString() {
        StringBuilder retString = new StringBuilder();
        this.heapTable.forEach((key, value)-> retString.append(key).append(" --> ").append(value).append('\n'));
        return retString.toString();
    }
}
