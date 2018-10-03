package ru.gavrilov.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.gavrilov.common.Controller;
import ru.gavrilov.common.FileManager;
import ru.gavrilov.entrys.PK;

public class SerialNumberFormController implements Controller {
    private Stage stage;
    private FileManager fileManager = new FileManager();
    private static final PK pk = PK.INSTANCE;

    public TextField serialNumber;
    public Button okButton;

    @FXML
    private void initialize() {
        okButton.setOnAction(event -> {
            if(!serialNumber.getText().equals("") && serialNumber.getText()!=null) {
                pk.setSerialNumberPk(serialNumber.getText());
                fileManager.writeJson();
                Platform.exit();
                System.exit(0);
            } else {
                new Alert(Alert.AlertType.ERROR, "Необходимо указать серийный номер ПК").showAndWait();
            }
        });
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
}
