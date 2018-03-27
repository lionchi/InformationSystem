package ru.gavrilov.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.gavrilov.common.Controller;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.util.FormatUtil;

public class HardDisksController implements Controller {

    private Stage stage;
    private HWDiskStore hwDiskStore;

    public TextField name;
    public TextField model;
    public TextField serialNumber;
    public TextField size;
    public TextField reads;
    public TextField writes;
    public TextField transferTime;
    public Button okButton;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setHardDisksController(HWDiskStore hwDiskStore) {
        this.hwDiskStore = hwDiskStore;
        initField();
    }

    private void initField() {
        boolean readwrite = hwDiskStore.getReads() > 0 || hwDiskStore.getWrites() > 0;
        name.setText(hwDiskStore.getName());
        model.setText(hwDiskStore.getModel());
        serialNumber.setText(hwDiskStore.getSerial().trim());
        size.setText(hwDiskStore.getSize() > 0 ? FormatUtil.formatBytesDecimal(hwDiskStore.getSize()) : "?");
        String rd = readwrite ? String.valueOf(hwDiskStore.getReads()) : "?";
        String rdByte = readwrite ? FormatUtil.formatBytes(hwDiskStore.getReadBytes()) : "?";
        reads.setText(rd + "(" + rdByte + ")");
        String wr = readwrite ? String.valueOf(hwDiskStore.getWrites()) : "?";
        String wrByte = readwrite ? FormatUtil.formatBytes(hwDiskStore.getWriteBytes()) : "?";
        writes.setText(wr + "(" + wrByte + ")");
        transferTime.setText(readwrite ? String.valueOf(hwDiskStore.getTransferTime()) : "?");
        okButton.setOnAction(event -> stage.close());
    }
}
