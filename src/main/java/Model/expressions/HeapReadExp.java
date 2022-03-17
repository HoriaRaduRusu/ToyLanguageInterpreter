package Model.expressions;

import Exceptions.*;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.types.IType;
import Model.types.RefType;
import Model.values.IValue;
import Model.values.RefValue;

public class HeapReadExp implements IExp{

    private IExp exp;

    public HeapReadExp(IExp exp){
        this.exp = exp;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<Integer, IValue> heap) throws TypeMismatchException,
            InvalidOperationException, NullKeyException, InvalidIDException, DivisionByZeroException {
        IValue expValue = this.exp.eval(tbl, heap);
        if(expValue.getType() instanceof RefType){
            RefValue expRefValue = (RefValue) expValue;
            int address = expRefValue.getAddr();
            if(heap.isDefined(address)){
                return heap.lookup(address);
            }
            else
                throw new InvalidIDException("Address has no value in heap!");
        }
        else
            throw new TypeMismatchException("Expression does not result in a reference type!");
    }

    @Override
    public String toString(){
        return "rH("+this.exp.toString()+")";
    }

    @Override
    public IExp deepCopy() {
        return new HeapReadExp(this.exp.deepCopy());
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typ = this.exp.typeCheck(typeEnv);
        if(typ instanceof RefType reft){
            return reft.getInner();
        }
        else
            throw new MyException("the rH argument is not a RefType");
    }
}
