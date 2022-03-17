package Model.values;

import Model.types.BoolType;
import Model.types.IType;

public class BoolValue implements IValue{

    private boolean value;

    public BoolValue(boolean v) {this.value = v;}

    public boolean getValue() {return this.value;}

    @Override
    public boolean equals(Object o) {
        if(o instanceof BoolValue)
            return this.value == ((BoolValue)o).value;
        return false;
    }

    @Override
    public String toString() {return Boolean.toString(this.value);}

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public BoolValue deepCopy() {
        return new BoolValue(this.value);
    }
}
