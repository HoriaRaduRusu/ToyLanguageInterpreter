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

public class HeapAllocStmt implements IStmt{

    private String var_name;
    private IExp exp;

    public HeapAllocStmt(String var_name, IExp exp){
        this.var_name = var_name;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws NullKeyException, InvalidIDException, TypeMismatchException,
            InvalidOperationException, DivisionByZeroException, UndeclaredVariableException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        if(symTbl.isDefined(var_name)) {
            IValue value = symTbl.lookup(var_name);
            if(value.getType() instanceof RefType){
                IValue expValue = exp.eval(symTbl, heap);
                RefType valueType = ((RefValue)value).getType();
                if(expValue.getType().equals(valueType.getInner())){
                    int address = heap.getNextAddress();
                    heap.add(expValue);
                    symTbl.update(var_name, new RefValue(address, valueType.getInner()));
                    return null;
                }
                else
                    throw new TypeMismatchException("The types of " + this.var_name + " and the given expression don't " +
                            "match");
            }
            else
                throw new TypeMismatchException(this.var_name + " is not a reference type");
        }
        else
            throw new UndeclaredVariableException(this.var_name + " is not defined");
    }

    @Override
    public String toString(){
        return "new("+this.var_name+","+this.exp.toString()+")";
    }

    @Override
    public IStmt deepCopy() {
        return new HeapAllocStmt(this.var_name, this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(this.var_name);
        IType typeExp = this.exp.typeCheck(typeEnv);
        if(typeVar.equals(new RefType(typeExp)))
            return typeEnv;
        throw new MyException("NEW stmt: right hand side and left hand side have different types");
    }
}
