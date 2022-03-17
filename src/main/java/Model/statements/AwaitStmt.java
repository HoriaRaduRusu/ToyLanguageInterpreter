package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.types.IType;
import Model.types.IntType;
import Model.values.IValue;
import Model.values.IntValue;

public class AwaitStmt implements IStmt{

    private String var;

    public AwaitStmt(String var){
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws InvalidIDException, TypeMismatchException, InvalidOperationException, DivisionByZeroException, UndeclaredVariableException, RedeclaredVariableException, NullKeyException, FileAlreadyOpenException, InvalidFileException, FileNotOpenException, FileReadException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIHeap<Integer, Integer> latchTable = state.getLatchTable();
        IValue foundValue = symTbl.lookup(this.var);
        if(foundValue.getType().equals(new IntType())){
            IntValue foundValueInt = (IntValue) foundValue;
            if(!latchTable.isDefined(foundValueInt.getValue())){
                throw new InvalidIDException("Await: var is not defined in the latch table");
            }
            else{
                Integer latchValue;
                synchronized (state.getLatchTable()) {
                    latchValue = latchTable.lookup(foundValueInt.getValue());
                }
                if(latchValue!= 0)
                    state.getStk().push(this);
            }
        }
        else
            throw new TypeMismatchException("Await: var is not an integer");
        return null;
    }

    @Override
    public AwaitStmt deepCopy() {
        return new AwaitStmt(this.var);
    }

    @Override
    public String toString(){
        return "await("+this.var+")";
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(this.var);
        if(typeVar.equals(new IntType()))
            return typeEnv;
        throw new TypeMismatchException("Await: var is not an integer!");
    }
}
