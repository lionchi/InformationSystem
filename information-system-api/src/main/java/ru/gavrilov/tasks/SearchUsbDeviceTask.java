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
        String serialNumber = mas[0];
        String nameFolder = mas[1];
        String mountPoint = "";
        for (HWDiskStore disk : diskStores) {
            if (disk.getSerial().equals(serialNumber)) {
                HWPartition partitions = disk.getPartitions()[0];
                mountPoint = partitions.getMountPoint();
                break;
            }
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
        String nameFolder = "", serialNumber = "";
        try {
            prop.load(MainApp.class.getClassLoader().getResourceAsStream("app.properties"));
            serialNumber = prop.getProperty("serial.number");
            nameFolder = prop.getProperty("name.folder");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return serialNumber + "," + nameFolder;
    }
}
