package Model.expressions;

import Exceptions.*;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.types.BoolType;
import Model.types.IType;
import Model.types.IntType;
import Model.values.BoolValue;
import Model.values.IValue;
import Model.values.IntValue;

public class RelationalExp implements IExp{

    private String operator;
    private IExp exp1;
    private IExp exp2;

    public RelationalExp(String operator, IExp exp1, IExp exp2){
        this.operator = operator;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap<Integer, IValue> heap) throws TypeMismatchException,
            InvalidOperationException, NullKeyException, InvalidIDException, DivisionByZeroException {
        IValue v1, v2;
        v1 = exp1.eval(tbl, heap);
        if(v1.getType().equals(new IntType())){
            v2 = exp2.eval(tbl, heap);
            if(v2.getType().equals(new IntType())){
                IntValue i1 = (IntValue) v1, i2 = (IntValue) v2;
                int op1 = i1.getValue(), op2 = i2.getValue();
                return switch (this.operator) {
                    case "<=" -> new BoolValue(op1 <= op2);
                    case "<" -> new BoolValue(op1 < op2);
                    case ">=" -> new BoolValue(op1 >= op2);
                    case ">" -> new BoolValue(op1 > op2);
                    case "==" -> new BoolValue(op1 == op2);
                    case "!=" -> new BoolValue(op1 != op2);
                    default -> throw new InvalidOperationException("Invalid operation!");
                };
            }
            else
                throw new TypeMismatchException("Right operator is not integer!");
        }
        else
            throw new TypeMismatchException("Left operator is not integer!");
    }

    @Override
    public String toString(){
        return this.exp1.toString() + " " + this.operator + " " + this.exp2.toString();
    }

    @Override
    public IExp deepCopy() {
        return new RelationalExp(this.operator, this.exp1.deepCopy(), this.exp2.deepCopy());
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typ1, typ2;
        typ1 = this.exp1.typeCheck(typeEnv);
        typ2 = this.exp2.typeCheck(typeEnv);
        if(typ1.equals(new IntType())){
            if(typ2.equals(new IntType())){
                return new BoolType();
            }
            else
                throw new TypeMismatchException("second operand is not an integer!");
        }
        else
            throw new TypeMismatchException("first operand is not an integer!");
    }
}
