package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyDictionary;
import Model.adts.MyIDictionary;
import Model.adts.MyStack;
import Model.types.BoolType;
import Model.types.IType;

public class ForkStmt implements IStmt{

    private IStmt stmt;

    public ForkStmt(IStmt stmt) { this.stmt = stmt; }

    @Override
    public PrgState realize(PrgState state) throws InvalidIDException, TypeMismatchException, InvalidOperationException,
            DivisionByZeroException, UndeclaredVariableException, RedeclaredVariableException, NullKeyException,
            FileAlreadyOpenException, InvalidFileException, FileNotOpenException, FileReadException {
        return new PrgState(new MyStack<>(), state.getSymTableCopy(), state.getOut(), this.stmt,
                state.getFileTable(), state.getHeap(), state.getLatchTable());
    }

    @Override
    public String toString(){
        return "fork("+this.stmt+")";
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(this.stmt.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        this.stmt.typeCheck(MyDictionary.cloneType(typeEnv));
        return typeEnv;
    }
}
