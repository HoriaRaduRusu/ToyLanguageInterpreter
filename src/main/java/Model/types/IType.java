package Model.types;

import Model.values.IValue;

public interface IType{
    IValue defaultValue();
    IType deepCopy();
}
