package Model.types;

import Model.values.BoolValue;
import Model.values.IValue;

public class BoolType implements IType {
    @Override
    public boolean equals(Object o){
        return o instanceof BoolType;
    }

    @Override
    public String toString() {return "bool";}

    @Override
    public IValue defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public BoolType deepCopy() {
        return new BoolType();
    }
}
