package Model.types;

import Model.values.IValue;
import Model.values.RefValue;


public class RefType implements IType{
    IType inner;

    public RefType(IType inner) {this.inner=inner;}

    public IType getInner() {return inner;}

    @Override
    public boolean equals(Object another){
        if (another instanceof RefType)
            return isEquals((RefType) another);
        else
            return false;
    }

    private boolean isEquals(RefType another) {
        return inner.equals(another.getInner());
    }

    @Override
    public String toString() { return "Ref " +inner.toString();}

    public IValue defaultValue() { return new RefValue(0,inner);}

    @Override
    public IType deepCopy() {
        return new RefType(this.inner.deepCopy());
    }
}
