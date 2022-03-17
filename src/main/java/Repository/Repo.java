package Repository;

import Exceptions.InvalidFileException;
import Model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repo implements IRepo{

    private List<PrgState> repo_list;
    private String logFilePath;

    public Repo(PrgState prg, String logFilePath){
        this.repo_list = new ArrayList<>();
        this.logFilePath = logFilePath;
        this.repo_list.add(prg);
    }

    @Override
    public void logPrgStateExec(PrgState state) throws InvalidFileException {
        try(PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)))){
            logFile.println(state);
        }
        catch (IOException ioe) {
            throw new InvalidFileException("Invalid file!");
        }
    }

    @Override
    public int size() {
        return this.repo_list.size();
    }

    @Override
    public List<PrgState> getPrgList() {
        return this.repo_list;
    }

    @Override
    public void setPrgList(List<PrgState> new_list) {
        this.repo_list = new_list;
    }
}
