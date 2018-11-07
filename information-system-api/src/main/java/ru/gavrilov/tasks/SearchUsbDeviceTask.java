package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import ru.gavrilov.MainApp;
import ru.gavrilov.Security;
import ru.gavrilov.common.FileManager;;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class SearchUsbDeviceTask extends Task<FileManager> {

    @Override
    protected FileManager call() throws Exception {
        String nameFolder = getNameFolder();
        if (!Security.checkLicenseForIS()) {
            return null;
        }

        String mountPoint = Security.getDir();

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


    private String getNameFolder() {
        Properties prop = new Properties();
        String nameFolder = "";
        try {
            prop.load(MainApp.class.getClassLoader().getResourceAsStream("app.properties"));
            nameFolder = prop.getProperty("name.folder");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return nameFolder;
    }
}
