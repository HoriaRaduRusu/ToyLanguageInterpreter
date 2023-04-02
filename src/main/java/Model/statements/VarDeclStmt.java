package Model.statements;

import Exceptions.InvalidIDException;
import Exceptions.MyException;
import Exceptions.NullKeyException;
import Exceptions.RedeclaredVariableException;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.types.IType;
import Model.values.IValue;

public class VarDeclStmt implements IStmt{

    private String name;
    private IType typ;

    public VarDeclStmt(String name, IType typ) { this.name = name; this.typ = typ;}

    @Override
    public String toString(){
        return this.typ.toString() + " " + this.name;
    }

    @Override
    public PrgState realize(PrgState state) throws RedeclaredVariableException, InvalidIDException, NullKeyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        if(symTable.isDefined(this.name))
            throw new RedeclaredVariableException("variable is already declared");
        symTable.add(this.name, this.typ.defaultValue());
        return null;
    }

    @Override
    public VarDeclStmt deepCopy() {
        return new VarDeclStmt(this.name, this.typ.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        typeEnv.add(this.name, this.typ);
        return typeEnv;
    }
}
