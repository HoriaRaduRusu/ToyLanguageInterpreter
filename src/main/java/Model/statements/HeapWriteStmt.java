package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.expressions.IExp;
import Model.types.IType;
import Model.types.RefType;
import Model.values.IValue;
import Model.values.RefValue;

public class HeapWriteStmt implements IStmt{

    private String varName;
    private IExp exp;

    public HeapWriteStmt(String varName, IExp exp){
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws NullKeyException, InvalidIDException, TypeMismatchException,
            InvalidOperationException, DivisionByZeroException, UndeclaredVariableException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        if(symTbl.isDefined(this.varName)){
            IValue varValue = symTbl.lookup(this.varName);
            if(varValue.getType() instanceof RefType){
                RefValue varRefValue = (RefValue) varValue;
                int address = varRefValue.getAddr();
                if(heap.isDefined(address)){
                    IValue expValue = this.exp.eval(symTbl, heap);
                    if(expValue.getType().equals(varRefValue.getType().getInner())){
                        heap.update(address, expValue);
                        return null;
                    }
                    else
                        throw new TypeMismatchException("The reference type of " + this.varName + " and the resulting " +
                                "type of the expression do not match");
                }
                else
                    throw new InvalidIDException("There is no value at the address given by "+this.varName);
            }
            else
                throw new TypeMismatchException(this.varName + " is not a reference");
        }
        else
            throw new UndeclaredVariableException(this.varName + " is not declared");
    }

    @Override
    public String toString(){
        return "wH("+this.varName+","+this.exp.toString()+")";
    }

    @Override
    public IStmt deepCopy() {
        return new HeapWriteStmt(this.varName, this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(this.varName);
        IType typeExp = this.exp.typeCheck(typeEnv);
        if(typeVar.equals(new RefType(typeExp)))
            return typeEnv;
        throw new MyException("Write stmt: right hand side and left hand side have different types");
    }
}
