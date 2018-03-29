package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.software.OSFileStore;
import ru.gavrilov.software.OperatingSystem;
import ru.gavrilov.software.os.FileSystem;
import ru.gavrilov.util.FormatUtil;

public class FileSystemTask extends Task<TreeItem> {

    private static final OperatingSystem operatingSystem = SystemInfo.INSTANCE.getOperatingSystem();
    private static final Logger LOG = LoggerFactory.getLogger(FileSystemTask.class);

    @Override
    protected TreeItem call() throws Exception {
        LOG.info("Checking file system...");
        FileSystem fileSystem = operatingSystem.getFileSystem();
        TreeItem rootItem = new TreeItem("File System");

        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            rootItem.getChildren().add(new TreeItem(fs));
        }

        return rootItem;
    }
}
