package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.HWPartition;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.common.FileManager;
import ru.gavrilov.entrys.HDD;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;

public class HardDisksTask extends Task<TreeItem> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(HardDisksTask.class);
    private static final PK pk = PK.INSTANCE;

    @Override
    protected TreeItem call() throws Exception {
        LOG.info("Checking Disks...");
        long start = System.currentTimeMillis();
        TreeItem rootItem = new TreeItem("Hard Disks");
        List<TreeItem> contents = new ArrayList<>();
        HWDiskStore[] diskStores = hardwareAbstractionLayer.getDiskStores();
        ArrayList<HDD> disks = new ArrayList<>();

        for (HWDiskStore disk : diskStores) {
            TreeItem treeItem = new TreeItem(disk);
            boolean isFormatted = FileManager.formattingСheck(disk);
            disk.setFormatted(isFormatted);
            disks.add(new HDD(disk.getModel(), disk.getSerial(), FormatUtil.formatBytesDecimal(disk.getSize()), isFormatted));
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
        pk.setHardDisks(disks);
        long end = System.currentTimeMillis();
        System.out.println("Время сбора информации об информационных носителей  в милисекундах равно " + (end-start));
        return rootItem;
    }
}
