package Controller;

import Exceptions.MyException;
import Model.PrgState;
import Model.adts.*;
import Model.expressions.*;
import Model.statements.*;
import Model.types.BoolType;
import Model.types.IntType;
import Model.types.RefType;
import Model.types.StringType;
import Model.values.BoolValue;
import Model.values.IntValue;
import Model.values.StringValue;
import Repository.IRepo;
import Repository.Repo;
import View.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;


public class MainController {

    private ProgramController secondController;

    public MainController() {
        FXMLLoader fxmlLoader2 = new FXMLLoader(MainApplication.class.getResource("program-application.fxml"));
        try {
            Scene scene2 = new Scene(fxmlLoader2.load(), 800, 400);
            Stage stage2 = new Stage();
            stage2.setScene(scene2);
            stage2.show();
            this.secondController = fxmlLoader2.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ListView<MyIPair<String, Controller>> programList;

    @FXML
    protected void onChooseButtonPress() {
        this.secondController.setController(this.programList.getSelectionModel().getSelectedItem().getSecondItem());
    }

    private ObservableList<MyIPair<String, Controller>> getCommands() {
        ObservableList<MyIPair<String, Controller>> returnList = FXCollections.observableArrayList();
        IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));
        IStmt ex2 = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntValue(2)),
                                new ArithExp('*', new ValueExp(new IntValue(3)),
                                        new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b", new ArithExp('+', new VarExp("a"),
                                        new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));
        IStmt ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"), new AssignStmt("v", new ValueExp(
                                        new IntValue(2))), new AssignStmt("v", new ValueExp(
                                        new IntValue(3)))), new PrintStmt(new VarExp("v"))))));
        IStmt ex4 = new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new OpenRFileStmt(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFileStmt(new VarExp("varf"))))))))));
        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new HeapAllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new HeapAllocStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")),
                                                new PrintStmt(new VarExp("a")))))));
        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new HeapAllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new HeapAllocStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new HeapReadExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+',
                                                        new HeapReadExp(new HeapReadExp(new VarExp("a"))),
                                                        new ValueExp(new IntValue(5)))))))));
        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new HeapAllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new PrintStmt(new HeapReadExp(new VarExp("v"))),
                                new CompStmt(new HeapWriteStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp('+',
                                                new HeapReadExp(new VarExp("v")),
                                                new ValueExp(new IntValue(5))))))));
        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new HeapAllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new HeapAllocStmt("a", new VarExp("v")),
                                        new CompStmt(new HeapAllocStmt("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(new HeapReadExp(new HeapReadExp(new VarExp("a")))))))));
        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(
                                new RelationalExp(">", new VarExp("v"), new ValueExp(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v",
                                                new ArithExp('-',
                                                        new VarExp("v"),
                                                        new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));

        IStmt ex10 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(new HeapAllocStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(new ForkStmt(
                                                new CompStmt(new HeapWriteStmt("a",
                                                        new ValueExp(new IntValue(30))),
                                                        new CompStmt(new AssignStmt("v",
                                                                new ValueExp(new IntValue(32))),
                                                                new CompStmt(new PrintStmt(new VarExp("v")),
                                                                        new PrintStmt(new HeapReadExp(
                                                                                new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")),
                                                        new PrintStmt(new HeapReadExp(new VarExp("a")))))))));

        IStmt ex11 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(new VarDeclStmt("b", new RefType(new IntType())),
                        new CompStmt(new VarDeclStmt("v", new IntType()),
                                new CompStmt(new HeapAllocStmt("a", new ValueExp(new IntValue(0))),
                                        new CompStmt(new HeapAllocStmt("b", new ValueExp(new IntValue(0))),
                                                new CompStmt(new HeapWriteStmt("a", new ValueExp(new IntValue(1))),
                                                        new CompStmt(new HeapWriteStmt("b", new ValueExp(new IntValue(2))),
                                                                new CompStmt(new CondAssignmentStmt("v",
                                                                        new RelationalExp("<",
                                                                                new HeapReadExp(new VarExp("a")),
                                                                                new HeapReadExp(new VarExp("b"))),
                                                                        new ValueExp(new IntValue(100)),
                                                                        new ValueExp(new IntValue(200))),
                                                                        new CompStmt(new PrintStmt(new VarExp("v")),
                                                                                new CompStmt(new CondAssignmentStmt("v",
                                                                                        new RelationalExp(">",
                                                                                                new ArithExp('-',
                                                                                                        new HeapReadExp(new VarExp("b")),
                                                                                                        new ValueExp(new IntValue(2))),
                                                                                                new HeapReadExp(new VarExp("a"))),
                                                                                        new ValueExp(new IntValue(100)),
                                                                                        new ValueExp(new IntValue(200))),
                                                                                        new PrintStmt(new VarExp("v"))))))))))));

        IStmt ex12 = new CompStmt(new VarDeclStmt("v1", new RefType(new IntType())),
                new CompStmt(new VarDeclStmt("v2", new RefType(new IntType())),
                        new CompStmt(new VarDeclStmt("v3", new RefType(new IntType())),
                                new CompStmt(new VarDeclStmt("cnt", new IntType()),
                                        new CompStmt(new HeapAllocStmt("v1", new ValueExp(new IntValue(2))),
                                                new CompStmt(new HeapAllocStmt("v2", new ValueExp(new IntValue(3))),
                                                        new CompStmt(new HeapAllocStmt("v3", new ValueExp(new IntValue(4))),
                                                                new CompStmt(new NewLatchStmt("cnt", new HeapReadExp(new VarExp("v2"))),
                                                                        new CompStmt(new ForkStmt(new CompStmt(new HeapWriteStmt("v1", new ArithExp('*', new HeapReadExp(new VarExp("v1")), new ValueExp(new IntValue(10)))),
                                                                                new CompStmt(new PrintStmt(new HeapReadExp(new VarExp("v1"))),
                                                                                        new CompStmt(new CountDownStmt("cnt"),
                                                                                                new ForkStmt(new CompStmt(new HeapWriteStmt("v2", new ArithExp('*', new HeapReadExp(new VarExp("v2")), new ValueExp(new IntValue(10)))),
                                                                                                        new CompStmt(new PrintStmt(new HeapReadExp(new VarExp("v2"))),
                                                                                                                new CompStmt(new CountDownStmt("cnt"),
                                                                                                                        new ForkStmt(new CompStmt(new HeapWriteStmt("v3", new ArithExp('*', new HeapReadExp(new VarExp("v3")), new ValueExp(new IntValue(10)))),
                                                                                                                                new CompStmt(new PrintStmt(new HeapReadExp(new VarExp("v3"))),
                                                                                                                                        new CountDownStmt("cnt")))))))))))),
                                                                                new CompStmt(new AwaitStmt("cnt"),
                                                                                        new CompStmt(new PrintStmt(new ValueExp(new IntValue(100))),
                                                                                                new CompStmt(new CountDownStmt("cnt"),
                                                                                                        new PrintStmt(new ValueExp(new IntValue(100)))))))))))))));

        returnList.add(buildController(ex1, "1"));
        returnList.add(buildController(ex2, "2"));
        returnList.add(buildController(ex3, "3"));
        returnList.add(buildController(ex4, "4"));
        returnList.add(buildController(ex5, "5"));
        returnList.add(buildController(ex6, "6"));
        returnList.add(buildController(ex7, "7"));
        returnList.add(buildController(ex8, "8"));
        returnList.add(buildController(ex9, "9"));
        returnList.add(buildController(ex10, "10"));
        returnList.add(buildController(ex11, "11"));
        returnList.add(buildController(ex12, "12"));
        returnList.remove(null);

        return returnList;
    }

    @FXML
    public void initialize() {
        this.programList.setItems(getCommands());
        this.programList.getSelectionModel().selectFirst();
        this.programList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private MyPair<String, Controller> buildController(IStmt statement, String number){
        try{
            statement.typeCheck(new MyDictionary<>());
            PrgState pr = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), statement, new MyDictionary<>(),
                    new MyHeap<>(), new MyHeap<>());
            IRepo repo = new Repo(pr, "log"+number+".txt");
            Controller ct = new Controller(repo);
            return new MyPair<>(statement.toString(), ct);
        } catch (MyException exception) {
            Alert programDoneAlert = new Alert(Alert.AlertType.ERROR);
            programDoneAlert.setTitle("Error!");
            programDoneAlert.setContentText("Program " + number + " type error:" + exception.getMessage());
            programDoneAlert.showAndWait();
        }
        return null;
    }
}