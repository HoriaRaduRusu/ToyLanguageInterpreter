package Controller;

import Exceptions.EmptyRepoException;
import Exceptions.EmptyStackException;
import Exceptions.InvalidPositionException;
import Model.PrgState;
import Model.adts.MyIList;
import Model.adts.MyIStack;
import Model.statements.IStmt;
import Model.values.IValue;
import Model.values.IntValue;
import Model.values.StringValue;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.Map;
import java.util.stream.IntStream;

public class ProgramController {

    private Controller controller = null;

    @FXML
    private TextField prgStatesCounter;

    @FXML
    private TableView<Map.Entry<Integer, IValue>> heapTable;

    @FXML
    private TableColumn<Map.Entry<Integer, IValue>, String> heapAddressColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, IValue>, String> heapValueColumn;

    @FXML
    private Button runOneStepButton;

    @FXML
    private ListView<IValue> outList;

    @FXML
    private ListView<StringValue> fileList;

    @FXML
    private ListView<PrgState> prgStateList;

    @FXML
    private TableView<Map.Entry<String, IValue>> symTable;

    @FXML
    private TableColumn<Map.Entry<String, IValue>, String> symNameColumn;

    @FXML
    private TableColumn<Map.Entry<String, IValue>, String> symValueColumn;

    @FXML
    private TableView<Map.Entry<Integer, Integer>> latchTable;

    @FXML
    private TableColumn<Map.Entry<Integer, Integer>, String> latchLocationColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, Integer>, String> latchValueColumn;


    @FXML
    private ListView<IStmt> exeStackList;

    public void setController(Controller new_controller){
        this.controller = new_controller;
        this.runOneStepButton.setDisable(false);
        this.updateAppearance();
        this.prgStateList.getSelectionModel().selectFirst();
    }

    @FXML
    protected void runOneStepHandler(){
        try {
            this.controller.oneStepForAllGUI();
            this.updateAppearance();
        } catch (EmptyRepoException e) {
            this.runOneStepButton.setDisable(true);
            this.updateCount();
            Alert programDoneAlert = new Alert(Alert.AlertType.INFORMATION);
            programDoneAlert.setTitle("Current program finished");
            programDoneAlert.setContentText("Program done successfully!");
            programDoneAlert.showAndWait();

        }
    }

    @FXML
    protected void onStateChangedHandler(){
        PrgState selected = this.prgStateList.getSelectionModel().getSelectedItem();
        if(selected != null) {
            updateSymTable(selected);
            updateExeStack(selected);
        }
        else{
            this.symTable.setItems(FXCollections.observableArrayList());
            this.exeStackList.setItems(FXCollections.observableArrayList());
        }
    }

    private void updateAppearance(){
        this.updateCount();
        this.updateHeapTable();
        this.updateOut();
        this.updateFileList();
        this.updatePrgStateList();
        this.updateLatchTable();
        onStateChangedHandler();
    }

    private void updateCount(){
        this.prgStatesCounter.setText(Integer.toString(this.controller.getRepoSize()));
    }

    private void updateHeapTable(){
        ObservableList<Map.Entry<Integer, IValue>> heapTableValues = FXCollections.observableArrayList();
        if(this.controller.getRepoSize() > 0)
            heapTableValues.setAll(this.controller.getHeapTable().getContent().entrySet());
        this.heapTable.setItems(heapTableValues);
    }

    private void updateOut(){
        ObservableList<IValue> outListValues = FXCollections.observableArrayList();
        if(this.controller.getRepoSize() > 0) {
            MyIList<IValue> out = this.controller.getOut();
            for (int i = 0; i < out.size(); i++) {
                try {
                    outListValues.add(out.getAt(i));
                } catch (InvalidPositionException e) {
                    e.printStackTrace();
                }
            }
        }
        this.outList.setItems(outListValues);
    }

    private void updateFileList(){
        ObservableList<StringValue> fileListValues = FXCollections.observableArrayList();
        if(this.controller.getRepoSize() > 0)
            fileListValues.setAll(this.controller.getFileTable().getKeys());
        this.fileList.setItems(fileListValues);
    }

    private void updatePrgStateList(){
        this.prgStateList.setItems(FXCollections.observableList(this.controller.getPrgList()));
        if(this.controller.getRepoSize() > 0  && this.prgStateList.getSelectionModel().getSelectedItem() == null)
            this.prgStateList.getSelectionModel().selectFirst();
    }

    private void updateLatchTable(){
        ObservableList<Map.Entry<Integer, Integer>> latchTableValues = FXCollections.observableArrayList();
        if(this.controller.getRepoSize() > 0){
            latchTableValues.setAll(this.controller.getLatchTable().getContent().entrySet());
        }
        this.latchTable.setItems(latchTableValues);
    }

    private void updateSymTable(PrgState currentState){
        this.symTable.setItems(FXCollections.observableArrayList(currentState.getSymTable().getContent().entrySet()));
    }

    private void updateExeStack(PrgState currentState){
        ObservableList<IStmt> exeStackValues = FXCollections.observableArrayList();
        MyIStack<IStmt> stack = currentState.getStk();
        while(!stack.isEmpty()){
            try {
                exeStackValues.add(stack.pop());
            } catch (EmptyStackException e) {
                e.printStackTrace();
            }
        }
        IntStream.range(0, exeStackValues.size())
                .mapToObj(i->exeStackValues.get(exeStackValues.size()-i-1))
                .forEach(stack::push);
        this.exeStackList.setItems(exeStackValues);
    }

    @FXML
    public void initialize() {
        this.heapAddressColumn.setCellValueFactory(
                cellData->new ReadOnlyStringWrapper(Integer.toString(cellData.getValue().getKey())));
        this.heapValueColumn.setCellValueFactory(
                cellData->new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));
        this.prgStateList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.prgStateList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<PrgState> call(ListView<PrgState> prgStateListView) {
                return new ListCell<>() {

                    @Override
                    protected void updateItem(PrgState item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(Integer.toString(item.getId()));
                        }
                        else
                            setText("");
                    }
                };
            }
        });
        this.symNameColumn.setCellValueFactory(cellData->new ReadOnlyStringWrapper(cellData.getValue().getKey()));
        this.symValueColumn.setCellValueFactory(
                cellData->new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));
        this.latchLocationColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey().toString()));
        this.latchValueColumn.setCellValueFactory(
                cellData-> new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));
    }
}
