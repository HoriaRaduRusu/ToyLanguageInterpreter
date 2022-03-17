package Model.values;

import Model.types.IType;
import Model.types.RefType;

public class RefValue implements IValue{
    private int heapAddress;
    private IType locationType;

    public RefValue(int address, IType locationType){
        this.heapAddress = address;
        this.locationType = locationType;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof RefValue)
            return this.heapAddress == ((RefValue)o).heapAddress && this.locationType.equals(((RefValue)o).locationType);
        return false;
    }

    @Override
    public RefType getType() {
        return new RefType(locationType);
    }

    @Override
    public RefValue deepCopy() {
        return new RefValue(this.heapAddress, this.locationType.deepCopy());
    }

    @Override
    public String toString(){
        return "(" + this.heapAddress + ", " + this.locationType.toString() + ")";
    }

    public int getAddr(){
        return this.heapAddress;
    }
}
