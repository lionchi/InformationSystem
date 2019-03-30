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

    public TextField inventoryNumber;
    public TextField locationField;
    public Button okButton;

    @FXML
    private void initialize() {
        long start = System.currentTimeMillis();
        okButton.setOnAction(event -> {
            if ((!inventoryNumber.getText().equals("") && !locationField.getText().equals(""))
                    && (inventoryNumber.getText() != null && locationField.getText() != null)) {
                pk.setInventoryNumber(inventoryNumber.getText());
                pk.setLocation(locationField.getText().toUpperCase());
                fileManager.writeJson();
                long end = System.currentTimeMillis();
                System.out.println("Время создания результирующего файла в милисекундах равно " + (end - start));
                Platform.exit();
                System.exit(0);
            } else {
                new Alert(Alert.AlertType.ERROR, "Необходимо указать инвентарный номер и расположение")
                        .showAndWait();
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
