package Model.expressions;

import Exceptions.InvalidIDException;
import Exceptions.MyException;
import Exceptions.NullKeyException;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.types.IType;
import Model.values.IValue;

public class VarExp implements IExp{

    private String id;

    public VarExp(String id) { this.id = id; }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<Integer, IValue> heap) throws InvalidIDException,
            NullKeyException {
        return tbl.lookup(this.id);
    }

    @Override
    public String toString() {return this.id;}

    @Override
    public VarExp deepCopy() {
        return new VarExp(this.id);
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv.lookup(this.id);
    }
}
