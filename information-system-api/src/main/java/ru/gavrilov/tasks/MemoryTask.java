package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.GlobalMemory;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.software.OSProcess;
import ru.gavrilov.software.OperatingSystem;
import ru.gavrilov.util.FormatUtil;

import java.util.Arrays;
import java.util.List;

public class MemoryTask extends Task<String> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final OperatingSystem operatingSystem = SystemInfo.INSTANCE.getOperatingSystem();
    private static final Logger LOG = LoggerFactory.getLogger(MemoryTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking Processes...");
        GlobalMemory memory = hardwareAbstractionLayer.getMemory();
        StringBuilder stringBuilder = new StringBuilder("Processes: " + operatingSystem.getProcessCount() + ", Threads: " + operatingSystem.getThreadCount() + "\n");
        // Sort by highest CPU
        List<OSProcess> procs = Arrays.asList(operatingSystem.getProcesses(5, OperatingSystem.ProcessSort.CPU));

        stringBuilder.append("   PID  %CPU %MEM       VSZ       RSS Name" + "\n");
        for (int i = 0; i < procs.size() && i < 5; i++) {
            OSProcess p = procs.get(i);
            stringBuilder.append(String.format(" %5d %5.1f %4.1f %9s %9s %s%n", p.getProcessID(),
                    100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
                    100d * p.getResidentSetSize() / memory.getTotal(), FormatUtil.formatBytes(p.getVirtualSize()),
                    FormatUtil.formatBytes(p.getResidentSetSize()), p.getName()));
        }

        LOG.info("Checking memory...");
        stringBuilder.append("Memory: " + FormatUtil.formatBytes(memory.getAvailable()) + "/"
                + FormatUtil.formatBytes(memory.getTotal()) + "\n");
        stringBuilder.append("Swap used: " + FormatUtil.formatBytes(memory.getSwapUsed()) + "/"
                + FormatUtil.formatBytes(memory.getSwapTotal()) + "\n");

        return stringBuilder.toString();
    }
}
