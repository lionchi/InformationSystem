package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import ru.gavrilov.HWPartition;
import ru.gavrilov.MainApp;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.common.FileManager;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.hardware.HardwareAbstractionLayer;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class SearchUsbDeviceTask extends Task<FileManager> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();

    @Override
    protected FileManager call() throws Exception {
        HWDiskStore[] diskStores = hardwareAbstractionLayer.getDiskStores();
        String[] mas = getSerialNumberAndNameFolder().split(",");
        String nameFile = mas[0];
        String serialNumberSsd = mas[1];
        String nameFolder = mas[2];
        String mountPoint = "";
        boolean flag = true;
        for (HWDiskStore disk : diskStores) {
            if (disk.getModel().contains("SCSI")) {
                for (HWPartition hwPartition : disk.getPartitions()) {
                    if (FileManager.isFileFound(hwPartition.getMountPoint(), nameFile, serialNumberSsd)) {
                        mountPoint = hwPartition.getMountPoint();
                        flag = false;
                        break;
                    }
                }
            }
            if (!flag) break;
        }

        FileManager fileManager = null;
        if (!mountPoint.isEmpty()) {
            File folder = new File(mountPoint + nameFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }
            fileManager = new FileManager(mountPoint, nameFolder, folder);
        }
        return fileManager;
    }


    private String getSerialNumberAndNameFolder() {
        Properties prop = new Properties();
        String nameFolder = "", nameFile = "", serialNumberSsd = "";
        try {
            prop.load(MainApp.class.getClassLoader().getResourceAsStream("app.properties"));
            nameFile = prop.getProperty("name.file");
            serialNumberSsd = prop.getProperty("serial.number.ssd");
            nameFolder = prop.getProperty("name.folder");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return nameFile + "," + serialNumberSsd + "," + nameFolder;
    }
}
