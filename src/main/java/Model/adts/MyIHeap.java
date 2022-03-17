package Model.adts;

import Exceptions.InvalidIDException;
import Exceptions.NullKeyException;

import java.util.Map;
import java.util.Set;

public interface MyIHeap<T1, T2> extends MyIDictionary<T1, T2>{
    void add(T2 val);
    T1 getNextAddress();
}
