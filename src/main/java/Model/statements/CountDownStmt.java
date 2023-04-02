package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.types.IType;
import Model.types.IntType;
import Model.values.IValue;
import Model.values.IntValue;

public class CountDownStmt implements IStmt{

    private String var;

    public CountDownStmt(String var){
        this.var = var;
    }

    @Override
    public PrgState realize(PrgState state) throws InvalidIDException, TypeMismatchException, InvalidOperationException, DivisionByZeroException, UndeclaredVariableException, RedeclaredVariableException, NullKeyException, FileAlreadyOpenException, InvalidFileException, FileNotOpenException, FileReadException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIHeap<Integer, Integer> latchTable = state.getLatchTable();
        IValue foundValue = symTbl.lookup(this.var);
        if(foundValue.getType().equals(new IntType())){
            IntValue foundValueInt = (IntValue) foundValue;
            Integer latchTableValue;
            synchronized (state.getLatchTable()){
                latchTableValue = latchTable.lookup(foundValueInt.getValue());
                if(latchTableValue > 0)
                    latchTable.update(foundValueInt.getValue(), latchTableValue - 1);
            }
            state.getOut().add(new IntValue(state.getId()));
        }
        else
            throw new TypeMismatchException("CountDown: "+ this.var + " is not an int!");
        return null;
    }

    @Override
    public CountDownStmt deepCopy() {
        return new CountDownStmt(this.var);
    }

    @Override
    public String toString(){
        return "countDown("+this.var+")";
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(this.var);
        if(typeVar.equals(new IntType()))
            return typeEnv;
        throw  new TypeMismatchException("CountDown: " + this.var + " is not an int!");
    }
}
