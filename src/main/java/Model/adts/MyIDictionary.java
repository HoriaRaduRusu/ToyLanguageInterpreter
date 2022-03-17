package Model.adts;

import Exceptions.InvalidIDException;
import Exceptions.NullKeyException;

import java.util.Map;
import java.util.Set;

public interface MyIDictionary<T1, T2>{

    T2 lookup(T1 id) throws InvalidIDException, NullKeyException;

    boolean isDefined(T1 id) throws NullKeyException;

    void update(T1 id, T2 val) throws InvalidIDException, NullKeyException;

    void add(T1 id, T2 val) throws InvalidIDException, NullKeyException;

    void remove(T1 id) throws NullKeyException, InvalidIDException;

    Set<T1> getKeys();

    Map<T1, T2> getContent();

    void setContent(Map<T1, T2> content);
}
