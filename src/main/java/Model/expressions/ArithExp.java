package Model.expressions;

import Exceptions.*;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.types.IType;
import Model.types.IntType;
import Model.values.IValue;
import Model.values.IntValue;

public class ArithExp implements IExp{
    private IExp e1;
    private IExp e2;
    private char op;

    public ArithExp(char operation, IExp e1, IExp e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = operation;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<Integer, IValue> heap) throws InvalidIDException,
            DivisionByZeroException, TypeMismatchException, InvalidOperationException, NullKeyException {
        IValue v1, v2;
        v1 = this.e1.eval(tbl, heap);
        if(v1.getType().equals(new IntType())){
            v2 = e2.eval(tbl, heap);
            if(v2.getType().equals(new IntType())){
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                int n1 = i1.getValue(), n2 = i2.getValue();
                switch (this.op){
                    case '+': return new IntValue(n1 + n2);
                    case '-': return new IntValue(n1 - n2);
                    case '*': return new IntValue(n1 * n2);
                    case '/': if(n2 == 0) throw new DivisionByZeroException("division by zero");
                            return new IntValue(n1 / n2);
                    default: throw new InvalidOperationException("invalid operation");
                }
            }
            else throw new TypeMismatchException("second operand is not an integer");
        }
        else throw new TypeMismatchException("first operand is not an integer");
    }

    @Override
    public String toString() {return this.e1.toString() + this.op + this.e2.toString();}

    @Override
    public IExp deepCopy() {
        return new ArithExp(this.op, this.e1.deepCopy(), this.e2.deepCopy());
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typ1, typ2;
        typ1 = this.e1.typeCheck(typeEnv);
        typ2 = this.e2.typeCheck(typeEnv);
        if(typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())){
                return new IntType();
            } else
                throw new TypeMismatchException("second operand is not an integer");
        } else
            throw new TypeMismatchException("first operand is not an integer");
    }
}
