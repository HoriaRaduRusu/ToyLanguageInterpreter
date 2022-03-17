package Model.adts;

public class MyPair<T1, T2> implements MyIPair<T1, T2>{

    private T1 firstItem;
    private T2 secondItem;

    public MyPair(T1 firstElement, T2 secondElement){
        this.firstItem = firstElement;
        this.secondItem = secondElement;
    }

    @Override
    public T1 getFirstItem() {
        return firstItem;
    }

    @Override
    public T2 getSecondItem() {
        return secondItem;
    }

    @Override
    public String toString(){
        return this.firstItem.toString();
    }
}
