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

public class WhileStmt implements IStmt{

    private IExp exp;
    private IStmt stmt;

    public WhileStmt(IExp exp, IStmt stmt){
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws TypeMismatchException, InvalidOperationException, NullKeyException,
            InvalidIDException, DivisionByZeroException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        MyIStack<IStmt> stack = state.getStk();
        IValue expValue = this.exp.eval(symTbl, heap);
        if(expValue.getType().equals(new BoolType())){
            if(((BoolValue)expValue).getValue()) {
                stack.push(this);
                stack.push(this.stmt);
            }
            return null;
        }
        else
            throw new TypeMismatchException("Condition exp is not a boolean");
    }

    @Override
    public String toString(){
        return "while("+this.exp.toString()+"){"+this.stmt.toString()+"}";
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(this.exp.deepCopy(), this.stmt.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = this.exp.typeCheck(typeEnv);
        if(typeExp.equals(new BoolType())){
            this.stmt.typeCheck(MyDictionary.cloneType(typeEnv));
            return typeEnv;
        }
        else
            throw new TypeMismatchException("The condition of IF does not have the type bool");
    }
}
