package ru.gavrilov.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.gavrilov.common.Controller;

public class ErrorStartAppFormController implements Controller {

    public Button exitButton;
    private Stage stage;

    @FXML
    private void initialize() {
        exitButton.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
