package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.expressions.IExp;
import Model.types.IType;
import Model.types.IntType;
import Model.types.StringType;
import Model.values.IValue;
import Model.values.IntValue;
import Model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStmt implements IStmt{

    private IExp exp;
    private String var_name;

    public ReadFileStmt(IExp exp, String var_name){
        this.exp = exp;
        this.var_name = var_name;
    }

    @Override
    public String toString(){
        return "readFile(" + this.exp.toString() +", "+this.var_name+")";
    }


    @Override
    public PrgState execute(PrgState state) throws UndeclaredVariableException, NullKeyException, InvalidIDException,
            TypeMismatchException, InvalidOperationException, DivisionByZeroException, FileNotOpenException,
            FileReadException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        if(symTbl.isDefined(var_name)){
            IValue value = symTbl.lookup(var_name);
            if(value.getType().equals(new IntType())){
                value = exp.eval(symTbl, heap);
                if(value.getType().equals(new StringType())){
                    StringValue fileDesc = (StringValue) value;
                    if(fileTable.isDefined(fileDesc)){
                        BufferedReader fileReader = fileTable.lookup(fileDesc);
                        IntValue readValue;
                        try {
                            String readStr = fileReader.readLine();
                            if(readStr == null)
                                readValue = new IntValue(0);
                            else
                                readValue = new IntValue(Integer.parseInt(readStr));
                            symTbl.update(var_name, readValue);
                            return null;
                        } catch (IOException e) {
                            throw new FileReadException("File could not be read!");
                        }
                    }
                    else
                        throw new FileNotOpenException("File given is not open!");
                }
                else
                    throw new TypeMismatchException("Expression does not result in a String!");
            }
            else
                throw new TypeMismatchException("Variable is not an integer!");
        }
        else
            throw new UndeclaredVariableException("Variable not declared!");
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFileStmt(this.exp.deepCopy(), this.var_name);
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = this.exp.typeCheck(typeEnv);
        IType typeVar = typeEnv.lookup(this.var_name);
        if(typeExp.equals(new StringType()))
            if(typeVar.equals(new IntType()))
                return typeEnv;
            else
                throw new TypeMismatchException("Read file: right argument is not of type int!");
        else
            throw new TypeMismatchException("Read file: left argument is not of type string!");
    }
}
