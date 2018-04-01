package ru.gavrilov.common;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ru.gavrilov.entrys.ProcessEntry;
import ru.gavrilov.hardware.HWDiskStore;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

public class TaskService<T extends Task, P extends Pane> {
    private T task;
    private P pane;

    public TaskService(T task, P pane) {
        this.task = task;
        this.pane = pane;
    }

    public void taskExecuter(TextInputControl inputControl) {
        Executors.newCachedThreadPool().submit(task);
        ProgressIndicator pi = this.initLoader();
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, eventTask -> {
            inputControl.setText((String) task.getValue());
            this.loaderClosed(pi);
        });
    }

    public void taskExecuter(TextInputControl... inputControls) {
        Executors.newCachedThreadPool().submit(task);
        ProgressIndicator pi = this.initLoader();
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, eventTask -> {
            List<String> values = (List<String>) task.getValue();
            int i = 0;
            for (TextInputControl inputControl : inputControls) {
                inputControl.setText(values.get(i));
                i++;
            }
            this.loaderClosed(pi);
        });
    }

    public void taskExecuterProcess(TableView<ProcessEntry> processesTable, TableColumn<ProcessEntry, String> pid,
                                    TableColumn<ProcessEntry, String> cpu, TableColumn<ProcessEntry, String> memory,
                                    TableColumn<ProcessEntry, String> vsz, TableColumn<ProcessEntry, String> name,
                                    TableColumn<ProcessEntry, String> rss, ObservableList<ProcessEntry> tableModels) {
        Executors.newCachedThreadPool().submit(task);
        ProgressIndicator pi = this.initLoader();
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, eventTask -> {
            tableModels.addAll((Collection<? extends ProcessEntry>) task.getValue());
            pid.setCellValueFactory(cellData->cellData.getValue().pidProperty());
            cpu.setCellValueFactory(cellData->cellData.getValue().cpuProperty());
            memory.setCellValueFactory(cellData->cellData.getValue().memoryProperty());
            vsz.setCellValueFactory(cellData->cellData.getValue().vszProperty());
            rss.setCellValueFactory(cellData->cellData.getValue().rssProperty());
            name.setCellValueFactory(cellData->cellData.getValue().nameProperty());
            processesTable.setItems(tableModels);
            processesTable.setColumnResizePolicy(param -> false);
            this.loaderClosed(pi);
        });
    }

    public void taskExecuterTreeView(TreeView treeView) {
        Executors.newCachedThreadPool().submit(task);
        ProgressIndicator pi = this.initLoader();
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, eventTask -> {
            treeView.setRoot((TreeItem) task.getValue());
            this.loaderClosed(pi);
        });
    }

    private ProgressIndicator initLoader (){
        ProgressIndicator pi=new ProgressIndicator();
        Window window = this.pane.getScene().getWindow();
        double centerXPosition = window.getX() + window.getWidth()/2d;
        double centerYPosition = window.getY() + window.getHeight()/2d;
        pi.setLayoutX(window.getWidth()/2d-30d);
        pi.setLayoutY(window.getHeight()/2d-75d);
        pi.setStyle("-fx-accent: #ffee00;" +
                "-fx-control-inner-background: #ffffff;");
        pi.setPrefSize(35,35);
        pi.progressProperty();

        pane.getChildren().add(pi);

        this.pane.setDisable(true);
        return pi;
    }

    private void loaderClosed(ProgressIndicator pi){
        this.pane.setDisable(false);
        this.pane.getChildren().remove(pi);
    }

/*    private void initLoader (){
        GuiForm loader = new GuiForm("loader.fxml");
        this.stage.setScene(new Scene(loader.getParent()));
        this.stage.initModality(Modality.WINDOW_MODAL);
        Window window = this.inputControl != null ? inputControl.getScene().getWindow() : this.node.getScene().getWindow();
        this.stage.initOwner(window);
        double centerXPosition = window.getX() + window.getWidth()/2d;
        double centerYPosition = window.getY() + window.getHeight()/2d;
        this.stage.setX(centerXPosition);
        this.stage.setY(centerYPosition);
        this.stage.show();
    }

    private void loaderClosed(){
        this.stage.close();
    }*/
}
