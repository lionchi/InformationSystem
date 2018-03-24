package ru.gavrilov.common;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.Executors;

public class TaskService<T extends Task, C extends TextInputControl> {
    private T task;
    private C control;

    public TaskService(T task, C control) {
        this.task = task;
        this.control = control;
    }

    public void taskExecuter() {
        Executors.newCachedThreadPool().submit(task);
        Stage stage = new Stage(StageStyle.UNDECORATED);
        GuiForm loader = new GuiForm("loader.fxml");
        stage.setScene(new Scene(loader.getParent()));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(control.getScene().getWindow());
        stage.setX(950);
        stage.setY(400);
        stage.show();
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, eventTask -> {
            control.setText((String) task.getValue());
            stage.close();
        });
    }
}
