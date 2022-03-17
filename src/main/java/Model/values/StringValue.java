package Model.values;

import Model.types.IType;
import Model.types.StringType;

public class StringValue implements IValue{

    private String val;

    public StringValue(String val) {this.val = val;}


    public String getValue() { return this.val; }

    @Override
    public boolean equals(Object o) {
        if(o instanceof StringValue)
            return this.val.equals(((StringValue)o).val);
        return false;
    }

    @Override
    public String toString() {return this.val;}


    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(this.val);
    }
}
