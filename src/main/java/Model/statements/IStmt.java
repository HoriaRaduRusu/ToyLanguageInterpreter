package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.types.IType;

public interface IStmt{
    PrgState execute(PrgState state) throws InvalidIDException, TypeMismatchException, InvalidOperationException,
            DivisionByZeroException, UndeclaredVariableException, RedeclaredVariableException, NullKeyException,
            FileAlreadyOpenException, InvalidFileException, FileNotOpenException, FileReadException;
    IStmt deepCopy();
    MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException;
}
