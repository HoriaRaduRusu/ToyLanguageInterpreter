package Model.adts;

import Exceptions.InvalidIDException;
import Exceptions.NullKeyException;
import Model.types.IType;
import Model.values.IValue;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MyDictionary<T1, T2> implements MyIDictionary<T1, T2>{
    private Map<T1, T2> dictionary;

    public static <K> MyIDictionary<K, IValue> cloneValue(MyIDictionary<K, IValue> toCopy){
        MyIDictionary<K, IValue> retDict = new MyDictionary<>();
        retDict.setContent(toCopy.getContent().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().deepCopy())));
        return retDict;
    }

    public static <K> MyIDictionary<K, IType> cloneType(MyIDictionary<K, IType> toCopy){
        MyIDictionary<K, IType> retDict = new MyDictionary<>();
        retDict.setContent(toCopy.getContent().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().deepCopy())));
        return retDict;
    }

    public MyDictionary() {
        this.dictionary = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized T2 lookup(T1 id) throws InvalidIDException, NullKeyException {
        if(id == null)
            throw new NullKeyException("Key is null!");
        T2 result = this.dictionary.get(id);
        if(result == null)
            throw new InvalidIDException("variable "+id+" is not defined");
        return result;
    }

    @Override
    public synchronized boolean isDefined(T1 id) throws NullKeyException {
        if(id == null)
            throw new NullKeyException("Key is null!");
        return this.dictionary.containsKey(id);
    }

    @Override
    public synchronized void update(T1 id, T2 val) throws InvalidIDException, NullKeyException {
        if(id == null)
            throw new NullKeyException("Key is null!");
        if(!this.dictionary.containsKey(id))
            throw new InvalidIDException("key is not defined!");
        this.dictionary.put(id, val);
    }

    @Override
    public synchronized void add(T1 id, T2 val) throws InvalidIDException, NullKeyException {
        if(id == null)
            throw new NullKeyException("Key is null!");
        if(this.dictionary.containsKey(id))
            throw new InvalidIDException("key is defined!");
        this.dictionary.put(id, val);
    }

    @Override
    public synchronized void remove(T1 id) throws NullKeyException, InvalidIDException {
        if(id == null)
            throw new NullKeyException("Key is null!");
        if(!this.dictionary.containsKey(id))
            throw new InvalidIDException("key is not defined!");
        this.dictionary.remove(id);
    }

    @Override
    public synchronized Set<T1> getKeys() {
        return this.dictionary.keySet();
    }

    @Override
    public synchronized Map<T1, T2> getContent() {
        return this.dictionary;
    }

    @Override
    public synchronized void setContent(Map<T1, T2> content) {
        this.dictionary = content;
    }



    @Override
    public synchronized String toString() {
        StringBuilder retString = new StringBuilder();
        this.dictionary.forEach((key, value)-> retString.append(key).append(" --> ").append(value).append('\n'));
        return retString.toString();
    }

}
