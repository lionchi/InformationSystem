package ru.gavrilov.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.gavrilov.HWPartition;
import ru.gavrilov.common.Controller;
import ru.gavrilov.common.FileManager;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.software.OSFileStore;
import ru.gavrilov.util.FormatUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class InformationController implements Controller {

    private Stage stage;
    private HWDiskStore hwDiskStore;
    private OSFileStore fileStore;
    private static final PK pk = PK.INSTANCE;

    public TextField name;
    public TextField model;
    public TextField serialNumber;
    public TextField size;
    public TextField reads;
    public TextField writes;
    public TextField transferTime;
    public TextField isFormatted;
    public TextField newName;
    public ProgressBar progressBar = new ProgressBar();
    public RadioButton fat32Button;
    public RadioButton ntfsButton;
    public RadioButton quickButton;
    public RadioButton normalButton;
    public Button okButton;
    public Button startFormattedButton;

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
        AtomicReference<String> type = new AtomicReference<>("/fs:FAT32");
        AtomicReference<String> speed = new AtomicReference<>(" " + "/q");
        boolean readwrite = hwDiskStore.getReads() > 0 || hwDiskStore.getWrites() > 0;
        name.setText(hwDiskStore.getName());
        model.setText(hwDiskStore.getModel());
        serialNumber.setText(hwDiskStore.getSerial().trim());
        size.setText(hwDiskStore.getSize() > 0 ? FormatUtil.formatBytesDecimal(hwDiskStore.getSize()) : "?");
        String rd = readwrite ? String.valueOf(hwDiskStore.getReads()) : "?";
        String rdByte = readwrite ? FormatUtil.formatBytesDecimal(hwDiskStore.getReadBytes()) : "?";
        reads.setText(rd + "(" + rdByte + ")");
        String wr = readwrite ? String.valueOf(hwDiskStore.getWrites()) : "?";
        String wrByte = readwrite ? FormatUtil.formatBytesDecimal(hwDiskStore.getWriteBytes()) : "?";
        writes.setText(wr + "(" + wrByte + ")");
        transferTime.setText(readwrite ? String.valueOf(hwDiskStore.getTransferTime()) + " milliseconds" : "?");
        quickButton.setOnAction(event -> {
            quickButton.setSelected(true);
            speed.set(" " + "/q");
            normalButton.setSelected(false);
        });
        normalButton.setOnAction(event -> {
            normalButton.setSelected(true);
            speed.set("");
            quickButton.setSelected(false);
        });
        fat32Button.setOnAction(event -> {
            fat32Button.setSelected(true);
            type.set("/fs:FAT32");
            ntfsButton.setSelected(false);
        });
        ntfsButton.setOnAction(event -> {
            ntfsButton.setSelected(true);
            type.set("/fs:NTFS");
            fat32Button.setSelected(false);
        });
        isFormatted.setText(hwDiskStore.isFormatted() ? "да" : "нет");
        okButton.setOnAction(event -> stage.close());
        startFormattedButton.setOnAction(event -> {
            for (HWPartition hwPartition : hwDiskStore.getPartitions()) {
                if (!hwPartition.getMountPoint().isEmpty()) {
                    FileManager.createBatch(hwPartition.getMountPoint().replace("\\", ""), speed.get(),
                            type.get(), newName.getText().isEmpty() ? "BlankDisk" : newName.getText(), progressBar, okButton, startFormattedButton, newName);
                }
            }
            hwDiskStore.setFormatted(true);
            isFormatted.setText("да");
            pk.getHardDisks().forEach(hd -> {
                if (hd.getSerialNumber().equals(hwDiskStore.getSerial())) {
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    String currentDateAndTime = simpleDateFormat.format(date);
                    hd.setDateOfFormatting(currentDateAndTime);
                    hd.setFormatted(hwDiskStore.isFormatted());
                }
            });
        });
    }

    private void initFieldForFileStore() {
        name.setText(fileStore.getName());
        description.setText(fileStore.getDescription().isEmpty() ? "file system" : fileStore.getDescription());
        type.setText(fileStore.getType());
        usable.setText(FormatUtil.formatBytesDecimal(fileStore.getUsableSpace()));
        totalSpace.setText(FormatUtil.formatBytesDecimal(fileStore.getTotalSpace()));
        if (fileStore.getTotalSpace() != 0) {
            pr.setText(String.valueOf(BigDecimal.valueOf(100d * fileStore.getUsableSpace() / fileStore.getTotalSpace()).setScale(BigDecimal.ROUND_CEILING, 2)));
        }
        volume.setText(fileStore.getVolume());
        logicalVolume.setText(fileStore.getLogicalVolume());
        mount.setText(fileStore.getMount());
        okButton.setOnAction(event -> stage.close());
    }
}
