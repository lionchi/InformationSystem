package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.HWPartition;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;

public class HardDisksTask extends Task<TreeItem> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(HardDisksTask.class);

    @Override
    protected TreeItem call() throws Exception {
        LOG.info("Checking Disks...");
        TreeItem rootItem = new TreeItem("Hard Disks");
        List<TreeItem> contents = new ArrayList<>();
        HWDiskStore[] diskStores = hardwareAbstractionLayer.getDiskStores();

        for (HWDiskStore disk : diskStores) {
            TreeItem treeItem = new TreeItem(disk);

            HWPartition[] partitions = disk.getPartitions();
            if (partitions == null) {
                contents.add(treeItem);
                continue;
            }
            for (HWPartition part : partitions) {
                treeItem.getChildren().add(new TreeItem(String.format("%s: %s (%s) Maj:Min=%d:%d, size: %s%s", part.getIdentification(),
                        part.getName(), part.getType(), part.getMajor(), part.getMinor(),
                        FormatUtil.formatBytesDecimal(part.getSize()),
                        part.getMountPoint().isEmpty() ? "" : " @ " + part.getMountPoint())));
            }
            contents.add(treeItem);
        }

        rootItem.getChildren().addAll(contents);
        return rootItem;
    }
}
