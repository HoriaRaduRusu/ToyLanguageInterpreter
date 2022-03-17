package Controller;

import Exceptions.*;
import Model.PrgState;
import Model.adts.MyIDictionary;
import Model.adts.MyIHeap;
import Model.adts.MyIList;
import Model.values.IValue;
import Model.values.IntValue;
import Model.values.RefValue;
import Model.values.StringValue;
import Repository.IRepo;

import java.io.BufferedReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private IRepo repo;
    private ExecutorService executor;

    public Controller(IRepo repo) { this.repo = repo; }

    public void allStep(boolean display_flag){
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(this.repo.getPrgList());
        while(prgList.size() > 0){
            List<Integer> symTableAddr = this.repo.getPrgList().stream()
                    .map(e->this.getAddrFromSymTable(e.getSymTable().getContent().values()))
                            .reduce(new ArrayList<>(), (e1, e2)-> Stream.concat(e1.stream(), e2.stream())
                                    .distinct().collect(Collectors.toList()));
            MyIHeap<Integer, IValue> heap = this.getHeapTable();
            List<Integer> heapAddr = this.getAddrFromHeap(heap.getContent().values());
            Map<Integer, IValue> newHeap = safeGarbageCollector(symTableAddr, heapAddr, heap.getContent());
            heap.setContent(newHeap);
            this.oneStepForAllPrg(prgList);
            if(display_flag)
                this.displayCurrentState();
            prgList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();
        repo.setPrgList(prgList);
    }

    public void oneStepForAllGUI() throws EmptyRepoException {
        List<PrgState> prgList = removeCompletedPrg(this.repo.getPrgList());
        if(repo.getPrgList().size() <= 0)
            throw new EmptyRepoException("The program is done!");
        executor = Executors.newFixedThreadPool(2);
        List<Integer> symTableAddr = this.repo.getPrgList().stream()
                .map(e->this.getAddrFromSymTable(e.getSymTable().getContent().values()))
                .reduce(new ArrayList<>(), (e1, e2)-> Stream.concat(e1.stream(), e2.stream())
                        .distinct().collect(Collectors.toList()));
        MyIHeap<Integer, IValue> heap = this.getHeapTable();
        List<Integer> heapAddr = this.getAddrFromHeap(heap.getContent().values());
        Map<Integer, IValue> newHeap = safeGarbageCollector(symTableAddr, heapAddr, heap.getContent());
        heap.setContent(newHeap);
        this.oneStepForAllPrg(prgList);
        executor.shutdownNow();
        if(repo.getPrgList().size() <= 0)
            throw new EmptyRepoException("The program is done!");
    }

    private void oneStepForAllPrg(List<PrgState> prgList){
        prgList.forEach(prg-> {
            try {
                repo.logPrgStateExec(prg);
            } catch (InvalidFileException e) {
                System.out.println(e.getMessage());
            }
        });
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(p::oneStep))
                .collect(Collectors.toList());
        try {
            List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (ExecutionException e) {
                            Throwable exc = e.getCause();
                            if (exc instanceof MyException) {
                                System.out.println(exc.getMessage());
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).filter(Objects::nonNull).toList();
            prgList.addAll(newPrgList);
            prgList.forEach(prg -> {
                try {
                    repo.logPrgStateExec(prg);
                } catch (InvalidFileException e) {
                    e.printStackTrace();
                }
            });
        }
        catch (InterruptedException ie){
            System.out.println("Execution interrupted");
        }
        repo.setPrgList(prgList);
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList){
        return inPrgList.stream().filter(PrgState::isNotCompleted).collect(Collectors.toList());
    }


    public Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr,
                                                     Map<Integer, IValue> heap){
        return heap.entrySet().stream()
                .filter(e->symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap){
        return heap.entrySet().stream()
                .filter(e->symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues){
        return symTableValues.stream()
                .filter(v-> v instanceof RefValue)
                .map(v-> {RefValue v1 = (RefValue)v; return v1.getAddr();})
                .collect(Collectors.toList());
    }

    public List<Integer> getAddrFromHeap(Collection<IValue> heapValues){
        return heapValues.stream()
                .filter(v->v instanceof RefValue).map(v -> ((RefValue)v).getAddr())
                .collect(Collectors.toList());
    }

    void displayCurrentState(){
        System.out.println("---------------------------------");
        this.repo.getPrgList().forEach(System.out::println);
    }

    public int getRepoSize(){
        return this.repo.size();
    }

    public MyIHeap<Integer, IValue> getHeapTable(){
        return this.repo.getPrgList().get(0).getHeap();
    }

    public MyIList<IValue> getOut(){
        return this.repo.getPrgList().get(0).getOut();
    }

    public MyIDictionary<StringValue, BufferedReader> getFileTable(){
        return this.repo.getPrgList().get(0).getFileTable();
    }

    public List<PrgState> getPrgList(){
        return this.repo.getPrgList();
    }

    public MyIHeap<Integer, Integer> getLatchTable(){
        return this.repo.getPrgList().get(0).getLatchTable();
    }
}
