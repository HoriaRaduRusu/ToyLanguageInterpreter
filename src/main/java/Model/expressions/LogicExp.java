package Model.expressions;

import Exceptions.*;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.types.BoolType;
import Model.types.IType;
import Model.types.IntType;
import Model.values.BoolValue;
import Model.values.IValue;

public class LogicExp implements IExp{

    private IExp e1;
    private IExp e2;
    private char op;

    public LogicExp(char operation, IExp e1, IExp e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = operation;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<Integer, IValue> heap) throws TypeMismatchException,
            InvalidOperationException, InvalidIDException, DivisionByZeroException, NullKeyException {
        IValue n1 = this.e1.eval(tbl, heap);
        if(n1.getType().equals(new BoolType())) {
            IValue n2 = this.e2.eval(tbl, heap);
            if(n2.getType().equals(new BoolType())) {
                BoolValue b1 = (BoolValue) n1, b2 = (BoolValue) n2;
                boolean o1 = b1.getValue(), o2 = b2.getValue();
                return switch (this.op) {
                    case '&' -> new BoolValue(o1 && o2);
                    case '|' -> new BoolValue(o1 || o2);
                    default -> throw new InvalidOperationException("Invalid operation!");
                };
            }
            else throw new TypeMismatchException("Operand2 is not a boolean!");
        }
        else throw new TypeMismatchException("Operand1 is not a boolean!");
    }

    @Override
    public String toString() {return this.e1.toString() + this.op + this.op + this.e2.toString(); }

    @Override
    public IExp deepCopy() {
        return new LogicExp(this.op, this.e1.deepCopy(), this.e2.deepCopy());
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typ1, typ2;
        typ1 = this.e1.typeCheck(typeEnv);
        typ2 = this.e2.typeCheck(typeEnv);
        if(typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())){
                return new BoolType();
            } else
                throw new TypeMismatchException("second operand is not a boolean");
        } else
            throw new TypeMismatchException("first operand is not a boolean");
    }

}
