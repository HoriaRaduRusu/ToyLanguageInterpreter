package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.expressions.IExp;
import Model.types.IType;
import Model.types.StringType;
import Model.values.IValue;
import Model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStmt implements IStmt{

    private IExp exp;

    public CloseRFileStmt(IExp exp) { this.exp = exp; }

    @Override
    public String toString() { return "closeRFile("+this.exp.toString()+")";}


    @Override
    public PrgState realize(PrgState state) throws TypeMismatchException, InvalidOperationException, NullKeyException,
            InvalidIDException, DivisionByZeroException, FileNotOpenException, InvalidFileException {
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        IValue value = this.exp.eval(symTbl, heap);
        if(value.getType().equals(new StringType())){
            StringValue strValue = (StringValue) value;
            if(fileTable.isDefined(strValue)) {
                BufferedReader reader = fileTable.lookup(strValue);
                try {
                    reader.close();
                    fileTable.remove(strValue);
                    return null;
                } catch (IOException e) {
                    throw new InvalidFileException("Invalid file!");
                }
            }
            else
                throw new FileNotOpenException("File is not open!");
        }
        else
            throw new TypeMismatchException("Expression is not a string!");
    }

    @Override
    public IStmt deepCopy() {
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = this.exp.typeCheck(typeEnv);
        if(typeExp.equals(new StringType()))
            return typeEnv;
        throw new TypeMismatchException("Close file: argument is not of type string!");
    }
}
