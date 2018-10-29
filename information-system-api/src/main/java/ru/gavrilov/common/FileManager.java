package ru.gavrilov.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import ru.gavrilov.HWPartition;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.hardware.HWDiskStore;

import java.io.*;
import java.util.concurrent.Executors;

public class FileManager {
    private String mountPoint;
    private String nameFolder;
    private File folder;
    private File file;
    private static final PK pk = PK.INSTANCE;

    public FileManager() {
    }

    public FileManager(String mountPoint, String nameFolder, File folder) {
        this.mountPoint = mountPoint;
        this.nameFolder = nameFolder;
        this.folder = folder;
    }

    public void writeJson() {
        File createFile = new File(folder, String.format("%s (Инв.№ %s)", pk.getLocation(), pk.getInventoryNumber()));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(createFile, pk);
            StegenographicLabelService.initLabel(createFile);
            System.out.println("Сериализация прошла успешна!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean formattingСheck(HWDiskStore diskStore) {
        HWPartition[] partitions = diskStore.getPartitions();
        long totalSpace = 0;
        long freeSpace = 0;
        if (partitions == null) {
            return false;
        }
        for (HWPartition part : partitions) {
            if (!part.getMountPoint().isEmpty()) {
                File file = new File(part.getMountPoint());
                totalSpace += file.getTotalSpace();
                freeSpace += file.getFreeSpace();
            }
        }
        if (totalSpace == freeSpace || (totalSpace - freeSpace) <= 17000) {
            return true;
        } else {
            return false;
        }
    }

    public static void createBatch(String drive, String speed, String type, String name, ProgressBar progressBar,
                                   Button okButton, Button startFormattedButton, TextField newName) {
        try {
            progressBar.setProgress(-1.0f);
            okButton.setDisable(true);
            startFormattedButton.setDisable(true);
            newName.setEditable(false);
            FileWriter fw = new FileWriter("Phoenix.bat");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Format /y" + " " + drive + speed + " " + type + "/v:" + name);
            bw.close();
            Executors.newCachedThreadPool().submit(() -> {
                try {
                    executeBatch(progressBar, okButton, startFormattedButton, newName);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
        }
    }

    private static void executeBatch(ProgressBar progressBar, Button okButton, Button startFormattedButton, TextField newName) throws InterruptedException {
        Process p;
        try {
            p = Runtime.getRuntime().exec("Phoenix.bat");
            p.waitFor();
            File f1 = new File("Phoenix.bat");
            f1.delete();
            Platform.runLater(() -> {
                okButton.setDisable(false);
                startFormattedButton.setDisable(false);
                newName.setEditable(true);
                progressBar.setProgress(0);
            });
        } catch (IOException ex) {
        }
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
