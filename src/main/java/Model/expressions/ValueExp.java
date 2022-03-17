package Model.expressions;

import Exceptions.MyException;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.types.IType;
import Model.values.IValue;

public class ValueExp implements IExp{

    private IValue value;

    public ValueExp(IValue value) { this.value = value; }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<Integer, IValue> heap) {
        return this.value;
    }

    @Override
    public String toString() { return this.value.toString(); }

    @Override
    public IExp deepCopy() {
        return new ValueExp(this.value.deepCopy());
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return this.value.getType();
    }
}
