package Model.expressions;

import Exceptions.*;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.types.IType;
import Model.values.IValue;

public interface IExp{
    IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<Integer, IValue> heap) throws InvalidIDException,
            DivisionByZeroException, TypeMismatchException, InvalidOperationException, NullKeyException;
    IExp deepCopy();
    IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException;
}
