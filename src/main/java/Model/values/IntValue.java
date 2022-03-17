package Model.values;

import Model.types.IType;
import Model.types.IntType;

public class IntValue implements IValue{

    private int val;

    public IntValue(int v) { this.val = v; }

    public int getValue() { return this.val; }

    @Override
    public boolean equals(Object o) {
        if(o instanceof IntValue)
            return this.val == ((IntValue)o).val;
        return false;
    }

    @Override
    public String toString() {return Integer.toString(this.val);}

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public IntValue deepCopy() {
        return new IntValue(this.val);
    }
}
