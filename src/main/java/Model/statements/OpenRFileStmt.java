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
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStmt implements IStmt{

    private IExp exp;

    public OpenRFileStmt(IExp exp){
        this.exp = exp;
    }

    @Override
    public String toString() { return "openRFile("+this.exp.toString()+")";}

    @Override
    public PrgState realize(PrgState state) throws TypeMismatchException, InvalidOperationException, NullKeyException,
            InvalidIDException, DivisionByZeroException, FileAlreadyOpenException, InvalidFileException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        IValue value = this.exp.eval(symTbl, heap);
        if(value.getType().equals(new StringType())) {
            StringValue strValue = (StringValue) value;
            if(!fileTable.isDefined(strValue)){
                try {
                    BufferedReader file = new BufferedReader(new FileReader(strValue.getValue()));
                    fileTable.add(strValue, file);
                    return null;
                }
                catch (IOException ioe){
                    throw new InvalidFileException("Invalid file!");
                }
            }
            else
                throw new FileAlreadyOpenException("File is already declared!");
        }
        else
            throw new TypeMismatchException("File name is not of String Type!");
    }

    @Override
    public IStmt deepCopy() {
        return new OpenRFileStmt(this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = this.exp.typeCheck(typeEnv);
        if(typeExp.equals(new StringType()))
            return typeEnv;
        throw new TypeMismatchException("Close file: argument is not of type string!");
    }
}
