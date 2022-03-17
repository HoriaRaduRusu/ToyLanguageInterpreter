package Repository;

import Exceptions.InvalidFileException;
import Model.PrgState;
import Model.adts.MyIList;

import java.util.List;

public interface IRepo {
    void logPrgStateExec(PrgState state) throws InvalidFileException;
    int size();
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> new_list);
}
