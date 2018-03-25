package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.software.OSFileStore;
import ru.gavrilov.software.OperatingSystem;
import ru.gavrilov.software.os.FileSystem;
import ru.gavrilov.util.FormatUtil;

public class FileSystemTask extends Task<String> {

    private static final OperatingSystem operatingSystem = SystemInfo.INSTANCE.getOperatingSystem();
    private static final Logger LOG = LoggerFactory.getLogger(FileSystemTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking file system...");
        FileSystem fileSystem = operatingSystem.getFileSystem();
        StringBuilder stringBuilder = new StringBuilder("File System:");

        stringBuilder.append(String.format(" File Descriptors: %d/%d%n", fileSystem.getOpenFileDescriptors(),
                fileSystem.getMaxFileDescriptors()));

        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long usable = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            stringBuilder.append(String.format("%s (%s) [%s] %s of %s free (%.1f%%) is %s " +
                            (fs.getLogicalVolume() != null && fs.getLogicalVolume().length() > 0 ? "[%s]" : "%s") +
                            " and is mounted at %s%n", fs.getName(),
                    fs.getDescription().isEmpty() ? "file system" : fs.getDescription(), fs.getType(),
                    FormatUtil.formatBytes(usable), FormatUtil.formatBytes(fs.getTotalSpace()), 100d * usable / total,
                    fs.getVolume(), fs.getLogicalVolume(), fs.getMount()));
        }

        return stringBuilder.toString();
    }
}
