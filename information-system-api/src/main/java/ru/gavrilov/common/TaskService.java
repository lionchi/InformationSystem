package ru.gavrilov.common;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ru.gavrilov.entrys.ProcessEntry;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

public class TaskService<T extends Task, C extends TextInputControl> {
    private T task;
    private C control;
    private Stage stage = new Stage(StageStyle.UNDECORATED);

    public TaskService(T task, C control) {
        this.task = task;
        this.control = control;
    }

    public void taskExecuter() {
        Executors.newCachedThreadPool().submit(task);
        this.initLoader();
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, eventTask -> {
            control.setText((String) task.getValue());
            this.loaderClosed();
        });
    }

    public void taskExecuter(TextInputControl... inputControls) {
        Executors.newCachedThreadPool().submit(task);
        this.initLoader();
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, eventTask -> {
            List<String> values = (List<String>) task.getValue();
            int i = 0;
            for (TextInputControl inputControl : inputControls) {
                inputControl.setText(values.get(i));
                i++;
            }
            this.loaderClosed();
        });
    }

    public void taskExecuterProcess(TableView<ProcessEntry> processesTable, TableColumn<ProcessEntry, String> pid,
                                    TableColumn<ProcessEntry, String> cpu, TableColumn<ProcessEntry, String> memory,
                                    TableColumn<ProcessEntry, String> vsz, TableColumn<ProcessEntry, String> name,
                                    TableColumn<ProcessEntry, String> rss, ObservableList<ProcessEntry> tableModels) {
        Executors.newCachedThreadPool().submit(task);
        this.initLoader();
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
            this.loaderClosed();
        });
    }

    private void initLoader (){
        GuiForm loader = new GuiForm("loader.fxml");
        this.stage.setScene(new Scene(loader.getParent()));
        this.stage.initModality(Modality.WINDOW_MODAL);
        Window window = control.getScene().getWindow();
        this.stage.initOwner(window);
        double centerXPosition = window.getX() + window.getWidth()/2d;
        double centerYPosition = window.getY() + window.getHeight()/2d;
        this.stage.setX(centerXPosition);
        this.stage.setY(centerYPosition);
        this.stage.show();
    }

    private void loaderClosed(){
        this.stage.close();
    }
}
