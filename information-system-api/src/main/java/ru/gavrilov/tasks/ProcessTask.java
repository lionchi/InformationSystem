package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.entrys.ProcessEntry;
import ru.gavrilov.hardware.GlobalMemory;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.software.OSProcess;
import ru.gavrilov.software.OperatingSystem;
import ru.gavrilov.util.FormatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessTask extends Task<List<ProcessEntry>> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final OperatingSystem operatingSystem = SystemInfo.INSTANCE.getOperatingSystem();
    private static final Logger LOG = LoggerFactory.getLogger(ProcessTask.class);

    @Override
    protected List<ProcessEntry> call() throws Exception {
        LOG.info("Checking Processes...");
        List<ProcessEntry> processEntries = new ArrayList<>();
        GlobalMemory memory = hardwareAbstractionLayer.getMemory();
        // Sort by highest CPU
        List<OSProcess> procs = Arrays.asList(operatingSystem.getProcesses(5, OperatingSystem.ProcessSort.CPU));

        for (int i = 0; i < procs.size() && i < 5; i++) {
            OSProcess p = procs.get(i);
            String pid = String.valueOf(p.getProcessID());
            String cpu = String.format("%5.1f", 100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime());
            String mem = String.format("%4.1f", 100d * p.getResidentSetSize() / memory.getTotal());
            String vsz = FormatUtil.formatBytes(p.getVirtualSize());
            String rss = FormatUtil.formatBytes(p.getResidentSetSize());
            String name = p.getName();
            processEntries.add(new ProcessEntry(pid, cpu, mem, vsz, rss, name));
        }

        return processEntries;
    }
}
