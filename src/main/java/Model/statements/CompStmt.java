package Model.statements;

import Exceptions.MyException;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIStack;
import Model.types.IType;

public class CompStmt implements IStmt{
    private IStmt first;
    private IStmt second;

    public CompStmt(IStmt first, IStmt second){
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString(){
        return this.first.toString()+";"+this.second.toString();
    }

    @Override
    public PrgState realize(PrgState state){
        MyIStack<IStmt> stk = state.getStk();
        stk.push(this.second);
        stk.push(this.first);
        return null;
    }

    @Override
    public CompStmt deepCopy() {
        return new CompStmt(this.first.deepCopy(), this.second.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return this.second.typeCheck(this.first.typeCheck(typeEnv));
    }
}
