package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.GlobalMemory;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.software.OperatingSystem;
import ru.gavrilov.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;

public class MemoryTask extends Task<List<String>> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final OperatingSystem operatingSystem = SystemInfo.INSTANCE.getOperatingSystem();
    private static final Logger LOG = LoggerFactory.getLogger(MemoryTask.class);

    @Override
    protected List<String> call() throws Exception {
        LOG.info("Checking memory...");
        GlobalMemory memory = hardwareAbstractionLayer.getMemory();
        List<String> strings = new ArrayList<>();
        strings.add(String.valueOf(operatingSystem.getProcessCount()));
        strings.add(String.valueOf(operatingSystem.getThreadCount()));

        strings.add(FormatUtil.formatBytes(memory.getAvailable()) + "/"
                + FormatUtil.formatBytes(memory.getTotal()));
        strings.add(FormatUtil.formatBytes(memory.getSwapUsed()) + "/"
                + FormatUtil.formatBytes(memory.getSwapTotal()));

        return strings;
    }
}
