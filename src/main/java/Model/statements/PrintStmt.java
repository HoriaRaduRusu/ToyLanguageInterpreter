package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.adts.MyIList;
import Model.expressions.IExp;
import Model.types.IType;
import Model.values.IValue;

public class PrintStmt implements IStmt{
    private IExp exp;

    public PrintStmt(IExp exp) { this.exp = exp;}

    @Override
    public String toString() { return "print("+exp.toString()+")";}

    @Override
    public PrgState execute(PrgState state) throws TypeMismatchException, InvalidOperationException, InvalidIDException,
            DivisionByZeroException, NullKeyException {
        MyIList<IValue> out = state.getOut();
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();
        out.add(this.exp.eval(symTable, heap));
        return null;
    }

    @Override
    public PrintStmt deepCopy() {
        return new PrintStmt(this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        this.exp.typeCheck(typeEnv);
        return typeEnv;
    }
}
