package Model;

import Exceptions.*;
import Model.adts.*;
import Model.statements.IStmt;
import Model.values.IValue;
import Model.values.IntValue;
import Model.values.StringValue;

import java.io.BufferedReader;
import java.util.Map;
import java.util.stream.Collectors;


public class PrgState {
    private static int current_id = 1;
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, IValue> symTable;
    private MyIList<IValue> out;
    private MyIDictionary<StringValue, BufferedReader> fileTable;
    private MyIHeap<Integer, IValue> heap;
    private MyIHeap<Integer, Integer> latchTable;
    private IStmt originalProgram;
    private int id;

    private static synchronized int getID(){
        return current_id++;
    }

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, IValue> symtbl, MyIList<IValue> ot,
                    MyIDictionary<StringValue, BufferedReader> fileTable, MyIHeap<Integer, IValue> heap,
                    MyIHeap<Integer, Integer> latchTable){
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.originalProgram = null;
        this.fileTable = fileTable;
        this.heap = heap;
        this.latchTable = latchTable;
        this.id = getID();
    }

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, IValue> symtbl, MyIList<IValue> ot, IStmt prg,
                    MyIDictionary<StringValue, BufferedReader> fileTable,  MyIHeap<Integer, IValue> heap,
                    MyIHeap<Integer, Integer> latchTable){
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.originalProgram = prg.deepCopy();
        this.fileTable = fileTable;
        this.exeStack.push(prg);
        this.heap = heap;
        this.latchTable = latchTable;
        this.id = getID();
    }

    public MyIStack<IStmt> getStk() {
        return this.exeStack;
    }

    public MyIDictionary<String, IValue> getSymTable() {
        return this.symTable;
    }

    public MyIDictionary<String, IValue> getSymTableCopy(){
        return MyDictionary.cloneValue(this.symTable);
    }

    public MyIList<IValue> getOut() {
        return this.out;
    }

    public MyIDictionary<StringValue, BufferedReader> getFileTable() { return this.fileTable;}

    public MyIHeap<Integer, IValue> getHeap() {return this.heap;}

    public MyIHeap<Integer, Integer> getLatchTable() {return this.latchTable;}

    public int getId() {return this.id;}

    public void setStk(MyIStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(MyIDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public void setOut(MyIList<IValue> out) {
        this.out = out;
    }

    public void setFileTable(MyIDictionary<StringValue, BufferedReader> fileTable) { this.fileTable = fileTable;}

    public void setHeap(MyIHeap<Integer, IValue> heap) {this.heap = heap;}

    public void setLatchTable(MyIHeap<Integer, Integer> latchTable) {this.latchTable = latchTable;}

    @Override
    public String toString() {
        StringBuilder retString = new StringBuilder();
        retString.append("ID:").append(this.id).append("\n");
        retString.append("ExeStack:\n");
        retString.append(this.exeStack);
        retString.append("\nSymTable:\n");
        retString.append(this.symTable);
        retString.append("\nOut:\n");
        retString.append(this.out);
        retString.append("\nFileTable:\n");
        for (StringValue name : this.fileTable.getKeys())
            retString.append(name).append('\n');
        retString.append("\nHeap:\n");
        retString.append(this.heap);
        retString.append("\nLatch Table:\n");
        retString.append(this.latchTable);
        return retString.toString();
    }

    public Boolean isNotCompleted(){
        return !this.exeStack.isEmpty();
    }

    public PrgState oneStep() throws EmptyStackException, RedeclaredVariableException, TypeMismatchException,
            InvalidOperationException, InvalidIDException, DivisionByZeroException, UndeclaredVariableException,
            NullKeyException, FileAlreadyOpenException, InvalidFileException, FileNotOpenException, FileReadException {
        if(this.exeStack.isEmpty()) throw new EmptyStackException("prgstate stack is empty");
        IStmt crtStmt = this.exeStack.pop();
        return crtStmt.execute(this);
    }

}
