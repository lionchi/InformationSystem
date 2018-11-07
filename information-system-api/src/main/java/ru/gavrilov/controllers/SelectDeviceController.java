package ru.gavrilov.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ru.gavrilov.Security;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.common.Controller;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.hardware.HardwareAbstractionLayer;

import java.io.IOException;

public class SelectDeviceController implements Controller {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();

    public ComboBox<HWDiskStore> devices;
    public Button selectButton;

    private Stage stage;
    private ObservableList<HWDiskStore> hwDiskStores = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        initDevice();
        selectButton.setOnAction(event -> {
            if (devices.getValue()!=null) {
                try {
                    Security.setLicenseForIS(devices.getValue());
                    Security.saveSecurityContext();
                    Platform.exit();
                    System.exit(0);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Выберите устройство-ключ").showAndWait();
            }
        });
    }

    private void initDevice() {
        this.hwDiskStores.addAll(hardwareAbstractionLayer.getDiskStores());
        devices.setItems(this.hwDiskStores);
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
