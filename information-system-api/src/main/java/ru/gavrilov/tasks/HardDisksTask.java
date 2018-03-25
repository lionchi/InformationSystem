package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.HWPartition;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.util.FormatUtil;

public class HardDisksTask extends Task<String> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(HardDisksTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking Disks...");
        HWDiskStore[] diskStores = hardwareAbstractionLayer.getDiskStores();
        StringBuilder stringBuilder = new StringBuilder();

        for (HWDiskStore disk : diskStores) {
            boolean readwrite = disk.getReads() > 0 || disk.getWrites() > 0;
            stringBuilder.append(String.format("%s: (model: %s - S/N: %s) size: %s, reads: %s (%s), writes: %s (%s), xfer: %s ms%n",
                    disk.getName(), disk.getModel(), disk.getSerial(),
                    disk.getSize() > 0 ? FormatUtil.formatBytesDecimal(disk.getSize()) : "?",
                    readwrite ? disk.getReads() : "?", readwrite ? FormatUtil.formatBytes(disk.getReadBytes()) : "?",
                    readwrite ? disk.getWrites() : "?", readwrite ? FormatUtil.formatBytes(disk.getWriteBytes()) : "?",
                    readwrite ? disk.getTransferTime() : "?"));

            HWPartition[] partitions = disk.getPartitions();
            if (partitions == null) {
                continue;
            }
            for (HWPartition part : partitions) {
                stringBuilder.append(String.format(" |-- %s: %s (%s) Maj:Min=%d:%d, size: %s%s%n", part.getIdentification(),
                        part.getName(), part.getType(), part.getMajor(), part.getMinor(),
                        FormatUtil.formatBytesDecimal(part.getSize()),
                        part.getMountPoint().isEmpty() ? "" : " @ " + part.getMountPoint()));
            }
        }

        return stringBuilder.toString();
    }
}
