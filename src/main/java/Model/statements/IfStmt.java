package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyDictionary;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.adts.MyIStack;
import Model.expressions.IExp;
import Model.types.BoolType;
import Model.types.IType;
import Model.values.BoolValue;
import Model.values.IValue;

public class IfStmt implements IStmt{

    private IExp exp;
    private IStmt thenS;
    private IStmt elseS;

    public IfStmt(IExp e, IStmt t, IStmt el) {
        this.exp = e;
        this.thenS = t;
        this.elseS = el;
    }

    @Override
    public String toString() {
        return "(IF(" + this.exp.toString() +") THEN(" + this.thenS.toString() +") ELSE(" + this.elseS.toString() +"))";
    }

    @Override
    public PrgState execute(PrgState state) throws TypeMismatchException, InvalidOperationException,
            InvalidIDException, DivisionByZeroException, NullKeyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIStack<IStmt> stk = state.getStk();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        IValue cond = this.exp.eval(symTable, heap);
        if(!cond.getType().equals(new BoolType()))
            throw new TypeMismatchException("conditional expr is not a boolean");
        BoolValue boolCond = (BoolValue) cond;
        if(boolCond.getValue())
            stk.push(thenS);
        else
            stk.push(elseS);
        return null;
    }

    @Override
    public IfStmt deepCopy() {
        return new IfStmt(this.exp.deepCopy(), this.thenS.deepCopy(), this.elseS.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = this.exp.typeCheck(typeEnv);
        if(typeExp.equals(new BoolType())){
            this.thenS.typeCheck(MyDictionary.cloneType(typeEnv));
            this.elseS.typeCheck(MyDictionary.cloneType(typeEnv));
            return typeEnv;
        }
        else
            throw new TypeMismatchException("The condition of IF does not have the type bool");
    }


}
