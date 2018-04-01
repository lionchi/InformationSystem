package ru.gavrilov.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.gavrilov.common.Controller;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.software.OSFileStore;
import ru.gavrilov.util.FormatUtil;

import java.math.BigDecimal;

public class InformationController implements Controller {

    private Stage stage;
    private HWDiskStore hwDiskStore;
    private OSFileStore fileStore;

    public TextField name;
    public TextField model;
    public TextField serialNumber;
    public TextField size;
    public TextField reads;
    public TextField writes;
    public TextField transferTime;
    public Button okButton;

    public TextField description;
    public TextField type;
    public TextField usable;
    public TextField totalSpace;
    public TextField pr;
    public TextField volume;
    public TextField logicalVolume;
    public TextField mount;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public <C> void setHardDisksController(C hwDiskStore) {
        this.hwDiskStore = (HWDiskStore) hwDiskStore;
        initFieldForHardDisks();
    }

    public <C> void setFileStore(C fileStore) {
        this.fileStore = (OSFileStore) fileStore;
        initFieldForFileStore();
    }

    private void initFieldForHardDisks() {
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

    private void initFieldForFileStore() {
        name.setText(fileStore.getName());
        description.setText(fileStore.getDescription().isEmpty() ? "file system" : fileStore.getDescription());
        type.setText(fileStore.getType());
        usable.setText(FormatUtil.formatBytes(fileStore.getUsableSpace()));
        totalSpace.setText(FormatUtil.formatBytes(fileStore.getTotalSpace()));
        pr.setText(String.valueOf(BigDecimal.valueOf(100d * fileStore.getUsableSpace() / fileStore.getTotalSpace()).setScale(BigDecimal.ROUND_CEILING,2)));
        volume.setText(fileStore.getVolume());
        logicalVolume.setText(fileStore.getLogicalVolume());
        mount.setText(fileStore.getMount());
        okButton.setOnAction(event -> stage.close());
    }
}
