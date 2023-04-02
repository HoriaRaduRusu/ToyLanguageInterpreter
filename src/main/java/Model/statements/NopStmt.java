package Model.statements;

import Exceptions.MyException;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.types.IType;

public class NopStmt implements IStmt{

    @Override
    public String toString() {
        return "nop";
    }

    @Override
    public PrgState realize(PrgState state){
        return null;
    }

    @Override
    public NopStmt deepCopy() {
        return new NopStmt();
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }
}
