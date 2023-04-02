package Model.statements;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.expressions.IExp;
import Model.types.BoolType;
import Model.types.IType;

public class CondAssignmentStmt implements IStmt{

    private IExp checkExp;
    private IExp trueExp;
    private IExp falseExp;
    private String var;

    public CondAssignmentStmt(String var, IExp checkExp, IExp trueExp, IExp falseExp){
        this.var = var;
        this.checkExp = checkExp;
        this.trueExp = trueExp;
        this.falseExp = falseExp;
    }

    @Override
    public PrgState realize(PrgState state) throws InvalidIDException, TypeMismatchException, InvalidOperationException, DivisionByZeroException, UndeclaredVariableException, RedeclaredVariableException, NullKeyException, FileAlreadyOpenException, InvalidFileException, FileNotOpenException, FileReadException {
        IStmt ifStmt = new IfStmt(this.checkExp, new AssignStmt(this.var, this.trueExp), new AssignStmt(this.var, this.falseExp));
        state.getStk().push(ifStmt);
        return null;
    }

    @Override
    public CondAssignmentStmt deepCopy() {
        return new CondAssignmentStmt(this.var, this.checkExp.deepCopy(), this.trueExp.deepCopy(), this.falseExp.deepCopy());
    }

    @Override
    public String toString(){
        return this.var+"=("+this.checkExp.toString()+")?"+this.trueExp.toString()+":"+this.falseExp.toString();
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookup(this.var);
        IType typeCheck = this.checkExp.typeCheck(typeEnv);
        IType typeTrue = this.trueExp.typeCheck(typeEnv);
        IType typeFalse = this.falseExp.typeCheck(typeEnv);
        if(typeCheck.equals(new BoolType())) {
            if (typeVar.equals(typeTrue)) {
                if (typeVar.equals(typeFalse))
                    return typeEnv;
                throw new TypeMismatchException("Conditional assignment: " + this.var + " and " + this.falseExp.toString() + " do not have the same type");
            }
            throw new TypeMismatchException("Conditional assignment: " + this.var + " and " + this.trueExp.toString() + " do not have the same type");
        }
        throw new TypeMismatchException("Conditional assignment: " + this.checkExp.toString() + " does not have bool type");
    }
}
