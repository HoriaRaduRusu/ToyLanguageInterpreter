package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.expressions.IExp;
import Model.types.IType;
import Model.types.IntType;
import Model.values.IValue;
import Model.values.IntValue;

public class NewLatchStmt implements IStmt{

    private String var;
    private IExp exp;

    public NewLatchStmt(String var, IExp exp){
        this.exp = exp;
        this.var = var;
    }

    @Override
    public PrgState realize(PrgState state) throws InvalidIDException, TypeMismatchException, InvalidOperationException, DivisionByZeroException, UndeclaredVariableException, RedeclaredVariableException, NullKeyException, FileAlreadyOpenException, InvalidFileException, FileNotOpenException, FileReadException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        MyIHeap<Integer, Integer> latchTable = state.getLatchTable();
        IValue value = this.exp.eval(symTbl, heap);
        if(value.getType().equals(new IntType())){
            IntValue intValue = (IntValue) value;
            IValue currentValue = symTbl.lookup(this.var);
            Integer newFreeLocation = latchTable.getNextAddress();
            synchronized (state.getLatchTable()){
                latchTable.add(intValue.getValue());
            }
            if(currentValue.getType().equals(new IntType())){
                symTbl.update(this.var, new IntValue(newFreeLocation));
            }
            else
                throw new TypeMismatchException("New Latch: "+ this.var +" is not an int type!");
        }
        else
            throw new TypeMismatchException("New Latch: " + this.exp + " is not an integer!");
        return null;
    }

    @Override
    public NewLatchStmt deepCopy() {
        return new NewLatchStmt(this.var, this.exp.deepCopy());
    }

    @Override
    public String toString(){
        return "newLatch("+this.var+","+this.exp.toString()+")";
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(this.var);
        IType typeExp = this.exp.typeCheck(typeEnv);
        if(typeVar.equals(new IntType())){
            if(typeExp.equals(new IntType()))
                return typeEnv;
            throw new TypeMismatchException("New Latch: " + this.exp + " is not an integer!");
        }
        throw new TypeMismatchException("New Latch: " + this.var + " is not an integer!");
    }
}
