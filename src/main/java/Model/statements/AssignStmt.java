package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.expressions.IExp;
import Model.types.IType;
import Model.values.IValue;

public class AssignStmt implements IStmt{

    private String id;
    private IExp exp;

    public AssignStmt(String id, IExp exp) { this.id = id; this.exp = exp;}

    @Override
    public String toString() {return this.id+"="+this.exp.toString();}

    @Override
    public PrgState realize(PrgState state) throws InvalidIDException, TypeMismatchException,
            InvalidOperationException, DivisionByZeroException, UndeclaredVariableException, NullKeyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        if(symTbl.isDefined(this.id)){
            IValue val = this.exp.eval(symTbl, heap);
            IType typId = symTbl.lookup(this.id).getType();
            if(val.getType().equals(typId))
                symTbl.update(this.id, val);
            else throw new TypeMismatchException("declared type of variable "+this.id+" and type of the assigned " +
                    "expression do not match");
        }
        else throw new UndeclaredVariableException("the used variable "+ this.id +" was not declared before");
        return null;
    }

    @Override
    public AssignStmt deepCopy(){
        return new AssignStmt(this.id, this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(this.id);
        IType typeExp = this.exp.typeCheck(typeEnv);
        if(typeVar.equals(typeExp))
            return typeEnv;
        else
            throw new TypeMismatchException("Assignment: right hand side and left hand side have different types");
    }
}
